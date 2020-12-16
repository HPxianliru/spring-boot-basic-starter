package com.xian.rabbit.model;

import com.xian.rabbit.handler.SimpleMessageListenerContainerContextHolder;
import lombok.Data;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.ObjectUtils;

/**
 *
 * @Description 消息队列详情
 * @Author: xlr
 * @Date: Created in 6:59 PM 2020/12/12
 */
@Data
public class MessageQueueDatail {

    /**
     * 队列名称
     */
    private String queueName;

    /**
     *  实现类
     */
    private String clazz;

    /**
     * 监听容器标识
     */
    private String containerIdentity;

    /**
     * 监听是否有效
     */
    private boolean activeContainer;

    /**
     * 是否正在监听
     */
    private boolean running;

    /**
     * 活动消费者数量
     */
    private int activeConsumerCount;

    public MessageQueueDatail(String queueName, SimpleMessageListenerContainer container) {
        String[] split = queueName.split( SimpleMessageListenerContainerContextHolder.symbol);
        this.queueName = split[0];
        this.clazz = split[1];
        this.running = container.isRunning();
        this.activeContainer = container.isActive();
        this.activeConsumerCount = container.getActiveConsumerCount();
        this.containerIdentity = "Container@" + ObjectUtils.getIdentityHexString(container);
    }

}
