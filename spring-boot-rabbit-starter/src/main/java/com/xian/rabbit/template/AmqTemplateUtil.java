package com.xian.rabbit.template;

import com.google.gson.Gson;
import com.xian.rabbit.db.entity.RabbitQueueEntity;
import com.xian.rabbit.exception.BussinesException;
import com.xian.rabbit.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @program:
 * @description: mq 模板工具类
 * @author: xlr
 * @create: 2019-03-22 14:53
 **/
@Slf4j
public class AmqTemplateUtil {

    private static final Gson GSON = new Gson();
    
    /**
     *
     * @param exchange
     * @param routingKey
     * @param object
     * @param correlationData
     */
    private static void sendMes(String exchange, String routingKey, Object object, final MessagePostProcessor messagePostProcessor,CorrelationData correlationData){
        String context = "";
        if(object instanceof String){
           context = object.toString();
        }else {
            context = GSON.toJson(object);
        }
        try{
            rabbitTemplate().convertAndSend(exchange,routingKey,  context,messagePostProcessor,correlationData);
        }catch (Exception e){
            log.error("队列发送失败：queue key:"+routingKey+"json 内容："+context +" 错误信息{}",e.getMessage());
            throw new BussinesException("rabbitTemplate 发送消息失败");
        }
    }

    /**
     *   死信队列
     * @param json
     */
    public static void sendDeadLetterMes(RabbitQueueEntity rabbitMQEnums, String json){
        try{
            rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),  json);
        }catch (Exception e){
            log.error("延迟队列发送失败 "+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }
    /**
     *   延迟
     * @param json
     * @param expiration 毫秒 millisecond
     */
    public static void sendDelayMes(RabbitQueueEntity rabbitMQEnums,String json,String expiration){
        try{
            rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),  json,message -> {
                //注意这里时间要是字符串形式
                message.getMessageProperties().setExpiration(expiration);
                return message;
            });
        }catch (Exception e){
            log.error("延迟队列发送失败 "+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }

    /**
     *
     * @param rabbitMQEnums
     * @param json
     * @param correlationData
     */
    public static void sendMes(RabbitQueueEntity rabbitMQEnums, String json, CorrelationData correlationData){
        try{
            rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),  json,correlationData);
        }catch (Exception e){
            log.error("队列发送失败：queue key:"+rabbitMQEnums.getRoutingKey()+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }

    /**
     *
     * @param rabbitMQEnums
     * @param json
     */
    public static void sendDirectMes(RabbitQueueEntity rabbitMQEnums, String json){
        try{
            rabbitTemplate().convertAndSend(rabbitMQEnums.getQueueName(),json);
        }catch (Exception e){
            log.error("Direct队列发送失败：queue :"+rabbitMQEnums.getQueueName()+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }

    /**
     *
     * @param rabbitMQEnums
     * @param message
     * @param messagePostProcessor 自定义 请求头
     */
    public void convertAndSend(RabbitQueueEntity rabbitMQEnums, final Object message, final MessagePostProcessor messagePostProcessor){
        rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(), rabbitMQEnums.getRoutingKey(), message, messagePostProcessor,null);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param object
     * @param correlationData
     */
    public static void sendMes(RabbitQueueEntity rabbitMQEnums, Object object, CorrelationData correlationData){

        rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),object,correlationData);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param object
     */
    public static void sendMes(RabbitQueueEntity rabbitMQEnums, Object object){
        rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),object);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param str
     */
    public static void sendMes(RabbitQueueEntity rabbitMQEnums, String str){
        rabbitTemplate().convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),str);
    }

    /**
     * 获取rabbitTemplate 模板 @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
     * @return
     */
    public static RabbitTemplate rabbitTemplate(){
        return SpringContextHolder.getBean( RabbitTemplate.class );
    }
}
