package com.xian.rabbit.config;

import com.xian.rabbit.enums.RabbitMQEnums;
import com.xian.rabbit.handler.SimpleMessageListenerContainerContextHolder;
import com.xian.rabbit.service.ConsumerHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.*;


/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 13:25
 **/
@Slf4j
@Conditional({RabbitCondition.class})
@Configuration
public class RabbitAutoBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware,EnvironmentAware {

    private static BeanDefinitionRegistry beanDefinitionRegistry;
    private static ApplicationContext applicationContext;
    private static Environment env;
    private static String APPLICATION;
    public static final String BIND_SUFFIX = "Binding";
    public static final String CONTAINER_SUFFIX = SimpleMessageListenerContainerContextHolder.symbol+"Container";
    public static List<RabbitMQEnums> QUEUE_NOT_LISTENING = new ArrayList<>(Arrays.asList( RabbitMQEnums.values() ));
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RabbitAutoBeanDefinitionRegistryPostProcessor.beanDefinitionRegistry = registry;
        APPLICATION = env.getProperty( "spring.application.name" );
        declareQueue();
        declareExchange();
        declareQueueAndBinding();
        declareTopicMessageListenerContainer();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitAutoBeanDefinitionRegistryPostProcessor.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        RabbitAutoBeanDefinitionRegistryPostProcessor.env = environment;
    }

    /**
     * 声明队列 拼装
     */
    void declareQueue(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if(anEnum.getOwnedApplication().getApplication().equals( APPLICATION )){
                beanDefinitionRegistry.registerBeanDefinition( anEnum.getQueueName(), BeanDefinitionBuilder.genericBeanDefinition( Queue.class, () -> {
                    return SimpleMessageListenerContainerContextHolder.declareQueue( anEnum );
                } ).getBeanDefinition());

            }
        }
    }

    /**
     * 声明交换机 拼装
     */
    void declareExchange(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if(anEnum.getOwnedApplication().getApplication().equals( APPLICATION )) {
               beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(),
                       BeanDefinitionBuilder.genericBeanDefinition(
                               Exchange.class, () ->  SimpleMessageListenerContainerContextHolder.exchange(anEnum) ).getBeanDefinition() );
            }
        }
    }
    /**
     * 绑定 交换机和队列
     */
    void declareQueueAndBinding(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if (anEnum.getOwnedApplication().getApplication().equals( APPLICATION )) {
                Binding binding = SimpleMessageListenerContainerContextHolder.declareBinding( (Queue)getBean(anEnum.getQueueName()),(Exchange)getBean(anEnum.getExchange()),anEnum.getRoutingKey());
                beanDefinitionRegistry.registerBeanDefinition( anEnum.getQueueName() + "-" + anEnum.getExchange()+BIND_SUFFIX,
                        BeanDefinitionBuilder.genericBeanDefinition( Binding.class, () -> binding ).getBeanDefinition() );
            }
        }
    }

    /**
     * 声明Topic消息监听容器
     */
    private void declareTopicMessageListenerContainer() {
        List<ConsumerHandlerService> consumerHandlerServices = new ArrayList<>(applicationContext.getBeansOfType(ConsumerHandlerService.class).values());
        for (ConsumerHandlerService consumerHandlerService : consumerHandlerServices) {
            for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
                if(APPLICATION.equals( anEnum.getOwnedApplication().getApplication() )){
                    if (consumerHandlerService.isMatch( anEnum )) {
                        beanDefinitionRegistry.registerBeanDefinition(anEnum.getQueueName()+ CONTAINER_SUFFIX,
                                BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class, () -> {
                                    return SimpleMessageListenerContainerContextHolder.declareContainer( anEnum,applicationContext.getBean("connectionFactory", ConnectionFactory.class),consumerHandlerService );
                                }).getBeanDefinition());
                    }
                }else {
                    QUEUE_NOT_LISTENING.remove( anEnum );
                }
            }
        }
        if(!QUEUE_NOT_LISTENING.isEmpty()){
            log.warn("No consumers in the current queues {}", QUEUE_NOT_LISTENING );
        }
    }

    public static <T>T getBean(String bean){
        return (T) applicationContext.getBean(bean);
    }

}
