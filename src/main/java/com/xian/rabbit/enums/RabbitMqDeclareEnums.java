package com.xian.rabbit.enums;

import lombok.Getter;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * @Description
 * @Author: xlr
 * @Date: Created in 3:26 PM 2020/12/13
 */
@Getter
public enum  RabbitMqDeclareEnums {
    /**
     *
     */
    DEAD_DECLARE( ExchangeTypes.DIRECT,true,null,null,true,null,null,null,false),
    PRI_DECLARE(ExchangeTypes.DIRECT,true,100,null,true,RabbitMQEnums.PRI_QUEUE,null,null,false),
    DEFAULT_DIRECT_DECLARE(ExchangeTypes.DIRECT,true,true,false),
    DELAY_DECLARE(ExchangeTypes.DIRECT,true,null,6000,true,null,"dead_key","dead_exchange",false);
    /**
     *  交换机类型 fan-out、topic、headers、system、direct
     */
    private String exchangeType;
    /**
     * 持久化
     */
    private Boolean durable;
    /**
     * 设置队列最大优先级
     */
    private Integer priority;
    /**
     * 设置延迟时间 单位毫秒
     */
    private Integer ttl;
    /**
     * 交换机持久
     */
    private Boolean exchangeDurable = Boolean.TRUE;
    /**
     * 备份交换机
     */
    private RabbitMQEnums alternate;
    /**
     * 死信队列 key
     */
    private String deadRoutingKey;
    /**
     * 死信交换机
     */
    private String deadExchange;
    /**
     * 是否自动删除
     */
    private Boolean autoDelete;


    RabbitMqDeclareEnums(String exchangeType, Boolean durable, Integer priority, Integer ttl, Boolean exchangeDurable, RabbitMQEnums alternate, String deadRoutingKey,String deadExchange, Boolean autoDelete) {
        this.exchangeType = exchangeType;
        this.durable = durable;
        this.priority = priority;
        this.ttl = ttl;
        this.exchangeDurable = exchangeDurable;
        this.alternate = alternate;
        this.deadRoutingKey = deadRoutingKey;
        this.deadExchange = deadExchange;
        this.autoDelete = autoDelete;
    }

    /**
     * 普通队列
     * @param exchangeType
     * @param durable
     * @param exchangeDurable
     * @param autoDelete
     */
    RabbitMqDeclareEnums(String exchangeType, Boolean durable, Boolean exchangeDurable, Boolean autoDelete) {
        this.exchangeType = exchangeType;
        this.durable = durable;
        this.exchangeDurable = exchangeDurable;
        this.autoDelete = autoDelete;
    }
}
