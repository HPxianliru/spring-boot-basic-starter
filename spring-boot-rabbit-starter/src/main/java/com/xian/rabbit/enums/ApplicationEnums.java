package com.xian.rabbit.enums;

import lombok.Getter;

/**
 * @Description
 * @Author: xlr
 * @Date: Created in 9:27 PM 2020/12/12
 */
@Getter
public enum ApplicationEnums {

    /**
     *
     */
    MQ("mq","测试工程名称");
    private String application;

    private String destination;


    ApplicationEnums(String application, String destination) {
        this.application = application;
        this.destination = destination;
    }
}
