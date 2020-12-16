package com.xian.rabbit;

import com.xian.rabbit.config.RabbitCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @program:
 * @description: Rabbit加载类
 * @author: liru.xian
 * @create: 2020-11-12 14:31
 **/
@Slf4j
@Configuration
@Conditional({RabbitCondition.class})
@ComponentScan(basePackages={RabbitBeanDefinitionRegistry.BASE_PACKAGE})
public class RabbitBeanDefinitionRegistry {
    public static final String BASE_PACKAGE = "com.xian.rabbit";
    static {
        log.info("RabbitMQ component Startup");
    }
}

