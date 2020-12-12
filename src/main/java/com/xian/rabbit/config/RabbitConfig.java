package com.xian.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.*;

/**
 *
 * @Auther: xlr
 * @Date: 2019/2/21 15:38
 * @Description:
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private RabbitTemplateConfig rabbitTemplateConfig;

    /**
     * 定制化amqp模版      可根据需要定制多个
     * 此处为模版类定义 Jackson消息转换器
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     *
     * @return the amqp template
     */
    @Bean
    @Primary
    public AmqpTemplate amqpTemplate() {
        // 使用jackson 消息转换器
        rabbitTemplate().setEncoding("UTF-8");
        // 消息发送失败返回到队列中，yml需要配置 publisher-returns: true
        rabbitTemplate().setMandatory(true);
        return rabbitTemplate();
    }

    /**
     * 消费者监听工厂
     * @param
     * @return
     */
    @Bean
    @RabbitListener
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        ConnectionFactory connectionFactory;
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setExposeListenerChannel(true);
        //设置确认模式为手工确认
        container.setAcknowledgeMode( AcknowledgeMode.MANUAL);
        return container;
    }
    /**
     * 用于创建/删除 Exchange、Queue、Binding 和初始化RabbitMQ
     * 主要针对动态创建
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnClass
    public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory){

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup( true );
        return rabbitAdmin;
    }

    @Bean
    @ConditionalOnClass
    public ConnectionFactory connectionFactory(){
        //创建连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //设置集群方式
        if(StringUtils.isNotBlank( rabbitMqProperties.getAddresses() )){
            connectionFactory.setAddresses(rabbitMqProperties.getAddresses());
        }
        //设置单节点方式
        connectionFactory.setHost(rabbitMqProperties.getHost());
        //设置端口
        connectionFactory.setPort(rabbitMqProperties.getPort());
        //设置用户名
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        //设置密码
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        //设置虚拟主机
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        //消息确认机制confirm-callback或return-callback,成功后confirm,失败后回调
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //exchange根据路由键匹配不到对应的queue时将会调用basic.return将消息返还给生产者
        rabbitTemplate.setMandatory(true);
        // json 序列化
        rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter());
        rabbitTemplate.setConfirmCallback(rabbitTemplateConfig);
        rabbitTemplate.setReturnCallback(rabbitTemplateConfig);
        return rabbitTemplate;
    }

}
