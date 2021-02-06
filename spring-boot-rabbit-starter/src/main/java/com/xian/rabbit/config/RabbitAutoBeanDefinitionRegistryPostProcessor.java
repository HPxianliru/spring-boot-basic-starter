package com.xian.rabbit.config;

import com.xian.rabbit.db.dao.RabbitApplicationDao;
import com.xian.rabbit.db.dao.RabbitConsumerDao;
import com.xian.rabbit.db.dao.RabbitDeclareDao;
import com.xian.rabbit.db.dao.RabbitQueueDao;
import com.xian.rabbit.db.entity.RabbitApplicationEntity;
import com.xian.rabbit.db.entity.RabbitConsumerEntity;
import com.xian.rabbit.db.entity.RabbitDeclareEntity;
import com.xian.rabbit.db.entity.RabbitQueueEntity;
import com.xian.rabbit.handler.SimpleMessageListenerContainerContextHolder;
import com.xian.rabbit.service.ConsumerHandlerService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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
    // 已监听的消息队列
    public static List<RabbitQueueEntity> QUEUE_ENTITY_LISTENING = new ArrayList<>();
    //所有队列
    public static List<RabbitQueueEntity> QUEUE_LISTENING = new ArrayList<>();
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RabbitAutoBeanDefinitionRegistryPostProcessor.beanDefinitionRegistry = registry;
        APPLICATION = env.getProperty( "spring.application.name" );

        List<RabbitQueueEntity> queueEntities = queueEntities();
        QUEUE_LISTENING.addAll(queueEntities);
        // 队列创建
        for (RabbitQueueEntity queue : queueEntities) {
            if(queue.getApplicationEntity().getApplication().equals( APPLICATION )){
                // 队列创建
                beanDefinitionRegistry.registerBeanDefinition( queue.getQueueName(), BeanDefinitionBuilder.genericBeanDefinition( Queue.class, () -> {
                    return SimpleMessageListenerContainerContextHolder.declareQueue( queue );
                } ).getBeanDefinition());
                //交换机
                beanDefinitionRegistry.registerBeanDefinition( queue.getExchange(),
                        BeanDefinitionBuilder.genericBeanDefinition(
                                Exchange.class, () ->  SimpleMessageListenerContainerContextHolder.exchange(queue) ).getBeanDefinition() );
                // 绑定关系
                Binding binding = SimpleMessageListenerContainerContextHolder.declareBinding( (Queue)getBean(queue.getQueueName()),(Exchange)getBean(queue.getExchange()),queue.getRoutingKey());
                beanDefinitionRegistry.registerBeanDefinition( queue.getQueueName() + "-" + queue.getExchange()+BIND_SUFFIX,
                        BeanDefinitionBuilder.genericBeanDefinition( Binding.class, () -> binding ).getBeanDefinition() );
                ConsumerHandlerService consumerHandlerService =  getBean(ConsumerHandlerService.class);
                // 消费者
                List<ConsumerHandlerService> consumerHandlerServices = new ArrayList<>(applicationContext.getBeansOfType(ConsumerHandlerService.class).values());
                for (ConsumerHandlerService handlerService : consumerHandlerServices) {
                    if(handlerService.isMatch(queue.getQueueName())){
                        QUEUE_ENTITY_LISTENING.add(queue);
                        beanDefinitionRegistry.registerBeanDefinition(queue.getQueueName()+ CONTAINER_SUFFIX,
                                BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class, () -> {
                                    return SimpleMessageListenerContainerContextHolder.declareContainer( queue,applicationContext.getBean("connectionFactory", ConnectionFactory.class),consumerHandlerService );
                                }).getBeanDefinition());
                    }
                }
            }
        }
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


    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(){
        DataSource bean = new HikariDataSource(hikariConfig());
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public HikariConfig hikariConfig(){
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(env.getProperty("spring.datasource.url"));
        configuration.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        configuration.setUsername(env.getProperty("spring.datasource.username"));
        configuration.setPassword(env.getProperty("spring.datasource.password"));
        return configuration;
    }

    public static <T>T getBean(String bean){
        return (T) applicationContext.getBean(bean);
    }
    public static <T>T getBean(Class bean){
        return (T) applicationContext.getBean(bean);
    }

    /**
     *
     * @return
     */
    private List<RabbitQueueEntity> queueEntities(){

        RabbitQueueDao rabbitQueueDao = new RabbitQueueDao(jdbcTemplate());
        RabbitDeclareDao rabbitDeclareDao = new RabbitDeclareDao(jdbcTemplate());
        RabbitConsumerDao rabbitConsumerDao = new RabbitConsumerDao(jdbcTemplate());
        RabbitApplicationDao rabbitApplicationDao =new RabbitApplicationDao(jdbcTemplate());
        List<RabbitQueueEntity> queueEntities = rabbitQueueDao.selectUserList(new RabbitQueueEntity());
        List<RabbitDeclareEntity> rabbitDeclareEntities = rabbitDeclareDao.selectUserList(new RabbitDeclareEntity());
        List<RabbitConsumerEntity> rabbitConsumerEntities = rabbitConsumerDao.selectUserList(new RabbitConsumerEntity());
        List<RabbitApplicationEntity> rabbitApplicationEntities = rabbitApplicationDao.selectUserList(new RabbitApplicationEntity());
        for (RabbitQueueEntity queueEntity : queueEntities) {
            for (RabbitDeclareEntity declareEntity : rabbitDeclareEntities) {
                if(queueEntity.getDeclareId().equals(declareEntity.getId())){
                    queueEntity.setDeclareEntity(declareEntity);
                }
                if(declareEntity.getAlternateId().equals(queueEntity.getId())){
                    declareEntity.setQueueEntity(queueEntity);
                }
            }
            for (RabbitConsumerEntity consumerEntity : rabbitConsumerEntities) {
                if(queueEntity.getConsumerId().equals(consumerEntity.getId())){
                    queueEntity.setConsumerEntity(consumerEntity);
                }
                if(consumerEntity.getQueueId().equals(queueEntity.getId())){
                    consumerEntity.setQueueEntity(queueEntity);
                }
            }
            for (RabbitApplicationEntity applicationEntity : rabbitApplicationEntities) {
                if(queueEntity.getAppId().equals(applicationEntity.getId())){
                    queueEntity.setApplicationEntity(applicationEntity);
                }
            }
        }
        return queueEntities;
    }

}
