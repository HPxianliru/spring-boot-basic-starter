package com.xian.rabbit.template;

import com.google.gson.Gson;
import com.xian.rabbit.enums.RabbitMQEnums;
import com.xian.rabbit.exception.BussinesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program:
 * @description: mq 模板工具类
 * @author: xlr
 * @create: 2019-03-22 14:53
 **/
@Component
@Slf4j
public class AmqTemplateUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Gson GSON = new Gson();

    public static AmqTemplateUtil amqTemplateUtil;


    @PostConstruct
    public void init(){
        amqTemplateUtil = this;
        amqTemplateUtil.rabbitTemplate = rabbitTemplate;
    }

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
            amqTemplateUtil.rabbitTemplate.convertAndSend(exchange,routingKey,  context,messagePostProcessor,correlationData);
        }catch (Exception e){
            log.error("队列发送失败：queue key:"+routingKey+"json 内容："+context +" 错误信息{}",e.getMessage());
            throw new BussinesException("rabbitTemplate 发送消息失败");
        }
    }

    /**
     *   死信队列
     * @param json
     */
    public static void sendDeadLetterMes(RabbitMQEnums rabbitMQEnums, String json){
        try{
            RabbitMQEnums delayQueue = RabbitMQEnums.DELAY_QUEUE;
            amqTemplateUtil.rabbitTemplate.convertAndSend(delayQueue.getExchange(),delayQueue.getRoutingKey(),  json);
        }catch (Exception e){
            log.error("延迟队列发送失败 "+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }
    /**
     *   延迟
     * @param json
     * @param expiration 毫秒 millisecond
     */
    public static void sendDelayMes(RabbitMQEnums rabbitMQEnums,String json,String expiration){
        try{
            amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),  json,message -> {
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
    public static void sendMes(RabbitMQEnums rabbitMQEnums, String json, CorrelationData correlationData){
        try{
            amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),  json,correlationData);
        }catch (Exception e){
            log.error("队列发送失败：queue key:"+rabbitMQEnums.getRoutingKey()+"json 内容："+json +" 错误信息{}",e.getMessage());
        }
    }

    /**
     *
     * @param rabbitMQEnums
     * @param json
     */
    public static void sendDirectMes(RabbitMQEnums rabbitMQEnums, String json){
        try{
            amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getQueueName(),json);
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
    public void convertAndSend(RabbitMQEnums rabbitMQEnums, final Object message, final MessagePostProcessor messagePostProcessor){
        amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(), rabbitMQEnums.getRoutingKey(), message, messagePostProcessor,null);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param object
     * @param correlationData
     */
    public static void sendMes(RabbitMQEnums rabbitMQEnums, Object object, CorrelationData correlationData){

        amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),object,correlationData);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param object
     */
    public static void sendMes(RabbitMQEnums rabbitMQEnums, Object object){
        amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),object);
    }

    /**
     *
     * @param rabbitMQEnums
     * @param str
     */
    public static void sendMes(RabbitMQEnums rabbitMQEnums, String str){
        amqTemplateUtil.rabbitTemplate.convertAndSend(rabbitMQEnums.getExchange(),rabbitMQEnums.getRoutingKey(),str);
    }
}
