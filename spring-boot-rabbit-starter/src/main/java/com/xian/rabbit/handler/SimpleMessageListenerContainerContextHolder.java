package com.xian.rabbit.handler;

import com.xian.rabbit.config.RabbitAutoBeanDefinitionRegistryPostProcessor;
import com.xian.rabbit.constant.MqConstant;
import com.xian.rabbit.db.entity.RabbitConsumerEntity;
import com.xian.rabbit.db.entity.RabbitDeclareEntity;
import com.xian.rabbit.db.entity.RabbitQueueEntity;
import com.xian.rabbit.model.MessageQueueDatail;
import com.xian.rabbit.service.ConsumerHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.xian.rabbit.config.RabbitAutoBeanDefinitionRegistryPostProcessor.CONTAINER_SUFFIX;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-08 16:20
 **/
@Slf4j
@Component
public class SimpleMessageListenerContainerContextHolder {

    private static final String CONTAINER_NOT_EXISTS = "消息队列%s对应的监听容器不存在！";

    public static final String symbol = "_";

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
            String[] split = s.split(symbol);
            if(split[0].equals(queueName)){
                return ALL_QUEUE2_CONTAINER_MAP.get(s);
            }
        }
        Assert.notNull(null, String.format(CONTAINER_NOT_EXISTS, key));
        return null;
    }


    /**
     *
     * @param queue
     * @param exchange
     * @param routingKey
     * @return
     */
    public static Binding declareBinding(Queue queue,Exchange exchange,String routingKey){
        return BindingBuilder.bind( queue ).to( exchange ).with( routingKey ).noargs();
    }

    /**
     * 拼装 队列
     * @param queue
     * @return
     */
    public static Queue declareQueue(RabbitQueueEntity queue){
        RabbitDeclareEntity anEnum = queue.getDeclareEntity();
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isNotBlank( anEnum.getDeadRoutingKey() )){
            map.put(MqConstant.X_DEAD_LETTER_ROUTING_KEY, anEnum.getDeadRoutingKey());
        }
        if(StringUtils.isNotBlank( anEnum.getDeadExchange() )){
            map.put( MqConstant.X_DEAD_LETTER_EXCHANGE,anEnum.getDeadExchange());
        }
        if(null != anEnum.getPriority()) {
            map.put( MqConstant.X_MAX_PRIORITY,anEnum.getPriority() );
        }
        if(null != anEnum.getTtl()){
            map.put( MqConstant.X_MESSAGE_TTL,anEnum.getTtl() );
        }
        return  new Queue( queue.getQueueName(), anEnum.getDurable() == 1 ?false : true,false,anEnum.getAutoDelete()== 1 ? false : true,map );
    }


    /**
     *  拼装交换机
     * @param queue
     * @return
     */
    public static Exchange exchange(RabbitQueueEntity queue){
        RabbitQueueEntity declareEntity = queue.getDeclareEntity().getQueueEntity();

        if(queue.getDeclareEntity() != null && null != queue.getDeclareEntity().getQueueEntity()){
            return new ExchangeBuilder(queue.getExchange(),queue.getDeclareEntity().getExchangeType())
                    .alternate( declareEntity.getExchange() ) // 备用交互机
                    .durable( queue.getDeclareEntity().getExchangeDurable() == 1 ? false : true ).build();
        }else {
            return new ExchangeBuilder(queue.getExchange(),queue.getDeclareEntity().getExchangeType())
                    .durable( queue.getDeclareEntity().getExchangeDurable() == 1 ? false : true ).build();
        }
    }


    /**
     * 声明 SimpleMessageListenerContainer
     * @param queue
     * @param connectionFactory
     * @param consumerHandlerService
     * @return
     */
    public static SimpleMessageListenerContainer declareContainer(RabbitQueueEntity queue,ConnectionFactory connectionFactory,ConsumerHandlerService consumerHandlerService){
        RabbitConsumerEntity consumer = queue.getConsumerEntity();
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setQueueNames(queue.getQueueName());
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(consumerHandlerService);
        container.setPrefetchCount(consumer.getPrefetchSize());
        container.setConcurrentConsumers(consumer.getConcurrent());
        //设置确认模式为手工确认
        if(consumer.getAcknowledgeMode() == 1 ? false : true){
            container.setAcknowledgeMode( AcknowledgeMode.MANUAL);
        }else {
            container.setAcknowledgeMode( AcknowledgeMode.AUTO );
        }
        container.setExposeListenerChannel( true );
        ALL_QUEUE2_CONTAINER_MAP.put(queue.getQueueName()+ CONTAINER_SUFFIX,container  );
        return container;
    }

}
