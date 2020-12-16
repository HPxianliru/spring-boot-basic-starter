package com.xian.basis.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-04 16:53
 **/
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class QuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        log.info("---> Quartz job {}, {} <----", new Date(), jobDetail);
    }
}
