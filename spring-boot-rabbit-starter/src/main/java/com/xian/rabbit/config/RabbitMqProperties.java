package com.xian.rabbit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author: xlr
 * @Date: Created in 7:12 PM 2020/12/12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xian.rabbitmq")
public class RabbitMqProperties {


    private String host ;
    private Integer port;
    private String username;
    private String password;
    private String virtualHost;
    private String addresses;

}
