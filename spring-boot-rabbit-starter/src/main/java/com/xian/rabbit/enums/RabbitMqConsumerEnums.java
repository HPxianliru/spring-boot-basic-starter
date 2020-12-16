package com.xian.rabbit.enums;

import lombok.Getter;

/**
 * @Description
 * @Author: xlr
 * @Date: Created in 3:27 PM 2020/12/13
 */
@Getter
public enum RabbitMqConsumerEnums {
    /**
     *
     */
    AUTO_CONSUMER(1,5,true),
    MANUAL_CONSUMER(1,1,true);

    /**
     * 每次获取数量
     */
    private Integer prefetchCount;
    /**
     * 消费者数量
     */
    private Integer concurrentConsumers;
    /**
     *  默认手动确认
     */
    private Boolean acknowledgeMode = true;

    RabbitMqConsumerEnums(Integer prefetchCount, Integer concurrentConsumers, Boolean acknowledgeMode) {
        this.prefetchCount = prefetchCount;
        this.concurrentConsumers = concurrentConsumers;
        this.acknowledgeMode = acknowledgeMode;
    }
}
