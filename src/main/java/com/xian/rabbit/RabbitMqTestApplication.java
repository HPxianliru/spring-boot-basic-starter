package com.xian.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:
 * @Date: Created in 2020/5/31 10:45
 * @Description: 启动类
 */
@Slf4j
@SpringBootApplication
public class RabbitMqTestApplication implements SpringApplicationRunListener {

    public static void main(String[] args) {
        log.info("RabbitMQ component Startup");
        SpringApplication.run( RabbitMqTestApplication.class, args);
    }

}
