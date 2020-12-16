package com.xian.rabbit.service.impl;

import com.rabbitmq.client.Channel;
import com.xian.rabbit.enums.RabbitMQEnums;
import com.xian.rabbit.service.ConsumerHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

/**
 * @program: 默认测试 消费
 * @description:
 * @author: liru.xian
 * @create: 2020-12-09 09:44
 **/
@Slf4j
@Component
public class DefaultConsumerHandlerServiceImpl implements ConsumerHandlerService {


    /**
     * @param rabbitMQEnums
     * @return
     */
    @Override
    public boolean isMatch(RabbitMQEnums rabbitMQEnums) {
        return RabbitMQEnums.DEFAULT_TEST_QUEUE.getQueueName().equals(rabbitMQEnums.getQueueName())?Boolean.TRUE:Boolean.FALSE;
    }


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        log.info(new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
