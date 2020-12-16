package com.xian.basis.quartz.config;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * @program: xian
 * @description:
 * @author: liru.xian
 * @create: 2020-09-17 17:38
 **/
public class QuartzCondition implements Condition {

    private static final String MARK = "spring.datasource.quartz.enabled";

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //获取环境配置
        Environment environment = conditionContext.getEnvironment();
        String property = environment.getProperty(MARK);
        return (!StringUtils.isEmpty(property)) && Boolean.parseBoolean(property);
    }
}
