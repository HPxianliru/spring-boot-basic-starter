package com.xian.basis.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Map;

/**
 * @program: 定时任务对象
 * @description:
 * @author: liru.xian
 * @create: 2020-12-04 16:55
 **/
@Data
public class QuartzForm implements Serializable {

    @ApiModelProperty("任务实现类")
    private Class<? extends QuartzJobBean> jobClass;
    @ApiModelProperty("任务名称")
    private String jobName;
    @ApiModelProperty("任务组名")
    private String jobGroupName;
    @ApiModelProperty("时间表达式 (这是每隔多少秒为一次任务)")
    private Integer jobTime;
    @ApiModelProperty("运行的次数 （<0:表示不限次数）   ")
    private Integer jobTimes;
    @ApiModelProperty("参数 map用于传参")
    private Map jobData;
}
