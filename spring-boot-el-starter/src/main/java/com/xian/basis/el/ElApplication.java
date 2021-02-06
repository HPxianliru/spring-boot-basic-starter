package com.xian.basis.el;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: xian
 * @description:
 * @author: liru.xian
 * @create: 2020-10-16 16:34
 **/
@Slf4j
@SpringBootApplication
public class ElApplication {

    public static final String BASE_PACKAGE = "com.xian.basis.quartz";
    static {
        log.info("quartz starter init........");
    }

    public static void main(String[] args) {
        SpringApplication.run( ElApplication.class,args );
    }
}
