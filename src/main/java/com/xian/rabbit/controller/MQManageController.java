package com.xian.rabbit.controller;

import java.util.List;

import com.xian.rabbit.handler.SimpleMessageListenerContainerContextHolder;
import com.xian.rabbit.model.MessageQueueDatail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 16:29
 **/
@RestController
@RequestMapping("/mqManage")
public class MQManageController {

    /**
     * 重置指定队列消费者数量
     * @param queueName
     * @param concurrentConsumers
     * @return
     */
    @GetMapping("resetConcurrentConsumers")
    public boolean resetConcurrentConsumers(String queueName, int concurrentConsumers) {
        return SimpleMessageListenerContainerContextHolder.resetQueueConcurrentConsumers(queueName, concurrentConsumers);
    }

    /**
     * 重启对消息队列的监听
     * @param queueName
     * @return
     */
    @GetMapping("restartMessageListener")
    public boolean restartMessageListener(String queueName) {
        return SimpleMessageListenerContainerContextHolder.restartMessageListener(queueName);
    }

    /**
     * 停止对消息队列的监听
     * @param queueName
     * @return
     */
    @GetMapping("stopMessageListener")
    public boolean stopMessageListener(String queueName) {
        return SimpleMessageListenerContainerContextHolder.stopMessageListener(queueName);
    }

    /**
     * 获取所有消息队列对应的消费者
     * @return
     */
    @GetMapping("statAllMessageQueueDetail")
    public List<MessageQueueDatail> statAllMessageQueueDetail() {
        return SimpleMessageListenerContainerContextHolder.statAllMessageQueueDetail();
    }
}
