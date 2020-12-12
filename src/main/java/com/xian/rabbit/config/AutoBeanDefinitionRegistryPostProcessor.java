package com.xian.rabbit.config;

import com.xian.rabbit.constant.MqConstant;
import com.xian.rabbit.enums.RabbitMQEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 13:25
 **/
@Slf4j
@Conditional({RabbitCondition.class})
@Configuration
public class AutoBeanDefinitionRegistryPostProcessor  implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static BeanDefinitionRegistry beanDefinitionRegistry;
    private static ApplicationContext applicationContext;
    private static String APPLICATION;
    public static final String BIND_SUFFIX = "Binding";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        AutoBeanDefinitionRegistryPostProcessor.beanDefinitionRegistry = registry;
        declareQueue();
        declareExchange();
        declareQueueAndBinding();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AutoBeanDefinitionRegistryPostProcessor.applicationContext = applicationContext;
    }

    public static <T>T getBean(String bean){
        return (T) applicationContext.getBean(bean);
    }

    public static <T>T getBean(Class clazz){
        return (T) applicationContext.getBean(clazz);
    }


    void declareQueue(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if(anEnum.getOwnedApplication().equals( APPLICATION )){
                beanDefinitionRegistry.registerBeanDefinition( anEnum.getQueueName(), BeanDefinitionBuilder.genericBeanDefinition( Queue.class, () -> {
                    Map<String,Object> map = new HashMap<>();
                    if(null  != anEnum.getDeadQueue()){
                        map.put( MqConstant.X_DEAD_LETTER_EXCHANGE,anEnum.getDeadQueue().getExchange());
                        map.put(MqConstant.X_DEAD_LETTER_ROUTING_KEY, anEnum.getDeadQueue().getRoutingKey());
                    }
                    if(null != anEnum.getPriority()) {
                        map.put( MqConstant.X_MAX_PRIORITY,anEnum.getPriority() );
                    }
                    if(null != anEnum.getTtl()){
                        map.put( MqConstant.X_MESSAGE_TTL,anEnum.getTtl() );
                    }
                    return  new Queue( anEnum.getQueueName(), anEnum.getDurable(),false,anEnum.getAutoDelete(),map );
                } ).getBeanDefinition());

            }
        }
    }
    //声明交换机
    void declareExchange(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if(anEnum.getOwnedApplication().equals( APPLICATION )) {

                switch (anEnum.getExchangeType()) {
                    case ExchangeTypes.DIRECT:
                        beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(), BeanDefinitionBuilder.genericBeanDefinition( DirectExchange.class, () -> (DirectExchange) exchange(anEnum) ).getBeanDefinition() );
                        break;
                    case ExchangeTypes.FANOUT:
                        beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(), BeanDefinitionBuilder.genericBeanDefinition( FanoutExchange.class, () -> (FanoutExchange) exchange(anEnum)).getBeanDefinition() );
                        break;
                    case ExchangeTypes.HEADERS:
                        beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(), BeanDefinitionBuilder.genericBeanDefinition( HeadersExchange.class, () -> (HeadersExchange) exchange(anEnum) ).getBeanDefinition() );
                        break;
                    case ExchangeTypes.TOPIC:
                        beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(), BeanDefinitionBuilder.genericBeanDefinition( TopicExchange.class, () -> (TopicExchange) exchange(anEnum) ).getBeanDefinition() );
                        break;
                    default:
                        beanDefinitionRegistry.registerBeanDefinition( anEnum.getExchange(), BeanDefinitionBuilder.genericBeanDefinition( DirectExchange.class, () -> (DirectExchange) exchange(anEnum) ).getBeanDefinition() );
                }
            }
        }
    }
    //声明队列和绑定
    void declareQueueAndBinding(){
        for (RabbitMQEnums anEnum : RabbitMQEnums.values()) {
            if (anEnum.getOwnedApplication().equals( APPLICATION )) {
                Binding binding = BindingBuilder.bind( (Queue) getBean( anEnum.getQueueName() ) ).to( (Exchange) getBean( anEnum.getExchange() ) ).with( anEnum.getRoutingKey() ).noargs();
                beanDefinitionRegistry.registerBeanDefinition( anEnum.getQueueName() + "-" + anEnum.getExchange()+BIND_SUFFIX, BeanDefinitionBuilder.genericBeanDefinition( Binding.class, () -> binding ).getBeanDefinition() );
            }
        }
    }

    public static BeanDefinitionRegistry getBeanDefinitionRegistry(){
        return AutoBeanDefinitionRegistryPostProcessor.beanDefinitionRegistry;
    }

    public Exchange exchange(RabbitMQEnums anEnum){
        if(null != anEnum.getAlternate()){
            return new ExchangeBuilder(anEnum.getExchange(),anEnum.getExchangeType())
                    .alternate( anEnum.getAlternate().getExchange() )
                    .durable( anEnum.getExchangeDurable() ).build();
        }else {
            return new ExchangeBuilder(anEnum.getExchange(),anEnum.getExchangeType())
                    .durable( anEnum.getExchangeDurable() ).build();
        }
    }
}
