package com.xian.rabbit.handler;

import com.xian.rabbit.model.MessageQueueDatail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 16:20
 **/
@Component
public class SimpleMessageListenerContainerContextHolder {

    private static final String CONTAINER_NOT_EXISTS = "消息队列%s对应的监听容器不存在！";


    @Autowired
    private RabbitListenerEndpointRegistry registry;

    /**
     * 所有的队列监听容器MAP
     */
    private static final Map<String, SimpleMessageListenerContainer> ALL_QUEUE2_CONTAINER_MAP = new ConcurrentHashMap<>();

    /**
     *  获取所有的 监听队列
     * @return
     */
    public static Map<String, SimpleMessageListenerContainer> getAllQueue2ContainerMap(){
        return ALL_QUEUE2_CONTAINER_MAP;
    }

    /**
     * 重置消息队列并发消费者数量
     * @param queueName
     * @param concurrentConsumers must greater than zero
     * @return
     */
    public static boolean resetQueueConcurrentConsumers(String queueName, int concurrentConsumers) {
        Assert.state(concurrentConsumers > 0, "参数 'concurrentConsumers' 必须大于0.");
        SimpleMessageListenerContainer container = findContainerByQueueName(queueName);
        if (container.isActive() && container.isRunning()) {
            container.setConcurrentConsumers(concurrentConsumers);
            return true;
        }
        return false;
    }


    /**
     * 重启对消息队列的监听
     * @param queueName
     * @return
     */
    public static boolean restartMessageListener(String queueName) {
        SimpleMessageListenerContainer container = findContainerByQueueName(queueName);
        Assert.state(!container.isRunning(), String.format("消息队列%s对应的监听容器正在运行！", queueName));
        container.start();
        return true;
    }

    /**
     * 停止对消息队列的监听
     * @param queueName
     * @return
     */
    public static boolean stopMessageListener(String queueName) {
        SimpleMessageListenerContainer container = findContainerByQueueName(queueName);
        Assert.state(container.isRunning(), String.format("消息队列%s对应的监听容器未运行！", queueName));
        container.stop();
        return true;
    }

    private Map<String, SimpleMessageListenerContainer> getQueue2ContainerAllMap() {
        registry.getListenerContainers().forEach(container -> {
            SimpleMessageListenerContainer simpleContainer = (SimpleMessageListenerContainer) container;
            Arrays.stream(simpleContainer.getQueueNames()).forEach(queueName ->
                    ALL_QUEUE2_CONTAINER_MAP.putIfAbsent(StringUtils.trim(queueName), simpleContainer));
        });
        return ALL_QUEUE2_CONTAINER_MAP;
    }
    /**
     * 统计所有消息队列详情
     * @return
     */
    public  static List<MessageQueueDatail> statAllMessageQueueDetail() {
        List<MessageQueueDatail> queueDetailList = new ArrayList<>();
        ALL_QUEUE2_CONTAINER_MAP.entrySet().forEach(entry -> {
            String queueName = entry.getKey();
            SimpleMessageListenerContainer container = entry.getValue();
            MessageQueueDatail deatil = new MessageQueueDatail(queueName, container);
            queueDetailList.add(deatil);
        });

        return queueDetailList;
    }

    /**
     * 根据队列名查找消息监听容器
     * @param queueName
     * @return
     */
    private static SimpleMessageListenerContainer findContainerByQueueName(String queueName) {
        String key = StringUtils.trim(queueName);
        for (String s : ALL_QUEUE2_CONTAINER_MAP.keySet()) {
            String[] split = s.split("-");
            if(split[0].equals(queueName)){
                return ALL_QUEUE2_CONTAINER_MAP.get(s);
            }
        }
        Assert.notNull(null, String.format(CONTAINER_NOT_EXISTS, key));
        return null;
    }

}
