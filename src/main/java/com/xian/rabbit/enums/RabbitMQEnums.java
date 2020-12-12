package com.xian.rabbit.enums;

import lombok.Getter;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-09 09:15
 **/
@Getter
public enum RabbitMQEnums {


    /**
     *
     */
    PRI_QUEUE("pri","pri_key",true,"死信队列","priExchange", ExchangeTypes.DIRECT,Boolean.TRUE,6,Boolean.FALSE,1,1,ApplicationEnums.MQ.getApplication()),
    DEAD_QUEUE("dead","dead_key",true,"死信队列","deadExchange", ExchangeTypes.DIRECT,Boolean.TRUE,Boolean.FALSE,1,1,ApplicationEnums.MQ.getApplication()),
    DEFAULT_TEST_QUEUE("test","test_key",true,"测试","testExchange",ExchangeTypes.DIRECT,Boolean.TRUE,Boolean.FALSE,1,5,ApplicationEnums.MQ.getApplication()),
    DELAY_QUEUE("delay","delay_key",true,"延迟队列","delayExchange",ExchangeTypes.DIRECT,Boolean.FALSE,6000,RabbitMQEnums.DEAD_QUEUE,ApplicationEnums.MQ.getApplication());

    // 队列名称
    private String queueName;
    // 是否持久化
    private Boolean durable;
    private String routingKey;
    // 设置队列最大优先级
    private Integer priority;
    // 设置延迟时间 毫秒
    private Integer ttl;

    private String destination;
    //交换机
    private String exchange;
    /**
     *  ExchangeTypes
     */
    private String exchangeType;
    // 交换机持久
    private Boolean exchangeDurable = Boolean.TRUE;
    // 是否自动删除
    private Boolean autoDelete;
    // 每次获取数量
    private Integer prefetchCount;
    // 消费者数量
    private Integer concurrentConsumers;
    //死信队列
    private RabbitMQEnums deadQueue;
    // 所属应用  如果不指定  公用。 OwnedApplicationConstant
    private String ownedApplication;
    //备份交换机
    private RabbitMQEnums alternate;

    /**
     * 普通队列
     * @param queueName
     * @param routingKey
     * @param queueDurable
     * @param destination
     * @param exchange
     * @param exchangeType
     * @param exchangeDurable
     * @param autoDelete
     * @param prefetchCount
     * @param concurrentConsumers
     * @param ownedApplication
     */
    RabbitMQEnums(String queueName,  String routingKey,Boolean queueDurable, String destination, String exchange,String exchangeType, Boolean exchangeDurable,Boolean autoDelete, Integer prefetchCount, Integer concurrentConsumers, String ownedApplication) {
        this.queueName = queueName;
        this.durable = queueDurable;
        this.routingKey = routingKey;
        this.destination = destination;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.autoDelete = autoDelete;
        this.prefetchCount = prefetchCount;
        this.concurrentConsumers = concurrentConsumers;
        this.ownedApplication = ownedApplication;
        this.exchangeDurable = exchangeDurable;
    }

    /**
     * 普通队列
     * @param queueName
     * @param routingKey
     * @param queueDurable
     * @param destination
     * @param exchange
     * @param exchangeType
     * @param exchangeDurable
     * @param priority
     * @param autoDelete
     * @param prefetchCount
     * @param concurrentConsumers
     * @param ownedApplication
     */
    RabbitMQEnums(String queueName,  String routingKey,Boolean queueDurable, String destination, String exchange,String exchangeType, Boolean exchangeDurable,Integer priority,Boolean autoDelete, Integer prefetchCount, Integer concurrentConsumers, String ownedApplication) {
        this.queueName = queueName;
        this.durable = queueDurable;
        this.routingKey = routingKey;
        this.destination = destination;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.autoDelete = autoDelete;
        this.prefetchCount = prefetchCount;
        this.concurrentConsumers = concurrentConsumers;
        this.ownedApplication = ownedApplication;
        this.exchangeDurable = exchangeDurable;
        this.priority = priority;
    }

    /**
     * 延迟队列
     * @param queueName
     * @param queueDurable
     * @param routingKey
     * @param destination
     * @param autoDelete
     * @param ttl
     * @param deadQueue
     * @param ownedApplication
     */
    RabbitMQEnums(String queueName, String routingKey,Boolean queueDurable, String destination,String exchange, String exchangeType,Boolean autoDelete, Integer ttl, RabbitMQEnums deadQueue, String ownedApplication) {
        this.queueName = queueName;
        this.durable = queueDurable;
        this.routingKey = routingKey;
        this.destination = destination;
        this.autoDelete = autoDelete;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.ttl = ttl;
        this.deadQueue = deadQueue;
        this.ownedApplication = ownedApplication;
    }

    RabbitMQEnums(String queueName,  String routingKey, Boolean queueDurable,Integer priority, Integer ttl, String destination, String exchange, String exchangeType, Boolean autoDelete, Integer prefetchCount, Integer concurrentConsumers, RabbitMQEnums deadQueue, String ownedApplication, RabbitMQEnums alternate) {
        this.queueName = queueName;
        this.durable = queueDurable;
        this.routingKey = routingKey;
        this.priority = priority;
        this.ttl = ttl;
        this.destination = destination;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.autoDelete = autoDelete;
        this.prefetchCount = prefetchCount;
        this.concurrentConsumers = concurrentConsumers;
        this.deadQueue = deadQueue;
        this.ownedApplication = ownedApplication;
        this.alternate = alternate;
    }
}
