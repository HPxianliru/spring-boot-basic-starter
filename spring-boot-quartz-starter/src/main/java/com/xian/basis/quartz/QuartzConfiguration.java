package com.xian.basis.quartz;

import com.xian.basis.quartz.config.QuartzCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;

/**
 * @program: xian
 * @description:
 * @author: liru.xian
 * @create: 2020-10-16 16:34
 **/
@Slf4j
@Conditional(value = QuartzCondition.class)
@ComponentScan(basePackages={QuartzConfiguration.BASE_PACKAGE})
public class QuartzConfiguration extends CachingConfigurerSupport {

    public static final String BASE_PACKAGE = "com.xian.basis.quartz";
    static {
        log.info("quartz starter init........");
    }
}
