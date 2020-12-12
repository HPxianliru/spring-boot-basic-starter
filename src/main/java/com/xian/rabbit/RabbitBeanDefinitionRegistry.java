package com.xian.rabbit;

import lombok.extern.slf4j.Slf4j;

/**
 * @program:
 * @description: Rabbit加载类
 * @author: liru.xian
 * @create: 2020-11-12 14:31
 **/
@Slf4j
//@Configuration
//@Conditional({RabbitCondition.class})
//@ComponentScan(basePackages={RabbitBeanDefinitionRegistry.BASE_PACKAGE})
public class RabbitBeanDefinitionRegistry {
    public static final String BASE_PACKAGE = "com.xian.basis.rabbit";
    static {
        log.info("RabbitMQ 组件启动");
    }
}

