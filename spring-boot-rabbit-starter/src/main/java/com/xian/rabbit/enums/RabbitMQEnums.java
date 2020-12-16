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
    PRI_QUEUE("优先级队列","pri","pri_key","pri_exchange",ApplicationEnums.MQ,RabbitMqConsumerEnums.AUTO_CONSUMER,RabbitMqDeclareEnums.PRI_DECLARE),
    DEAD_QUEUE("死信队列","dead","dead_key","dead_exchange",ApplicationEnums.MQ,RabbitMqConsumerEnums.AUTO_CONSUMER,RabbitMqDeclareEnums.DEAD_DECLARE),
    DEFAULT_TEST_QUEUE("默认队列","test","test_key","test_exchange",ApplicationEnums.MQ,RabbitMqConsumerEnums.AUTO_CONSUMER,RabbitMqDeclareEnums.DEFAULT_DIRECT_DECLARE),
    DELAY_QUEUE("延迟队列","delay","delay_key","delay_exchange",ApplicationEnums.MQ,RabbitMqConsumerEnums.AUTO_CONSUMER,RabbitMqDeclareEnums.DELAY_DECLARE);

    /**
     * 描述
     */
    private String destination;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     *
     */
    private String routingKey;
    /**
     * 交换机名称
     */
    private String exchange;
    /**
     * 所属应用 OwnedApplicationConstant
     */
    private ApplicationEnums ownedApplication;
    /**
     * 消费者
     */
    RabbitMqConsumerEnums consumerEnums;
    /**
     * 高级特性
     */
    RabbitMqDeclareEnums  declareEnums;


    RabbitMQEnums(String destination, String queueName, String routingKey, String exchange, ApplicationEnums ownedApplication, RabbitMqConsumerEnums consumerEnums, RabbitMqDeclareEnums declareEnums) {
        this.destination = destination;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.exchange = exchange;
        this.ownedApplication = ownedApplication;
        this.consumerEnums = consumerEnums;
        this.declareEnums = declareEnums;
    }
}
