package com.xian.rabbit.config;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @program: xian-live-backend
 * @description:
 * @author: liru.xian
 * @create: 2020-09-17 17:38
 **/
public class RabbitCondition implements Condition {

    private static final String MARK = "xian.rabbitmq.enabled";

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //获取环境配置
        Environment environment = conditionContext.getEnvironment();
        String property = environment.getProperty(MARK);
        return StringUtils.isNotBlank(property) && Boolean.parseBoolean(property);
    }
}
