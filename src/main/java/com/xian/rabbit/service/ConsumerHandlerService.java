package com.xian.rabbit.service;

import com.xian.rabbit.enums.RabbitMQEnums;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * @program: 消费者匹配实现接口
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 13:57
 **/
public interface ConsumerHandlerService  extends ChannelAwareMessageListener {

    /**
     *
     * @param rabbitMQEnums
     * @return
     */
    boolean isMatch(RabbitMQEnums rabbitMQEnums);
}
