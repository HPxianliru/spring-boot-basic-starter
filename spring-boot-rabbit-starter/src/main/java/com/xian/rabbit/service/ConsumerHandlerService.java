package com.xian.rabbit.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * @program: 消费者匹配实现接口
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 13:57
 **/
public interface ConsumerHandlerService  extends ChannelAwareMessageListener {

    /**
     * 默认制定队列的消费者
     * @param queueName
     * @return
     */
    boolean isMatch(String queueName);

    void onMessage(Message message, Channel channel) throws Exception ;
}
