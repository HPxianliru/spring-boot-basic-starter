package com.xian.basis.quartz.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @program: xian
 * @description:
 * @author: liru.xian
 * @create: 2020-08-14 15:26
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.quartz")
public class QuartzConfigProperties implements Serializable {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}