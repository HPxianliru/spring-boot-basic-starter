package com.xian.basis.quartz.controller;


import com.xian.basis.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program:
 * @description:
 * @author: liru.xian
 * @create: 2020-12-04 16:55
 **/
@RestController
@RequestMapping
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    /**
     * 创建房间
     *
     * @param room
     * @return
     */
    @PostMapping("/create")
    public Object createRoom(@RequestBody QuartzForm room) {
        quartzService.addJob(room.getJobName(),room.getJobGroupName(),room.getJobTime(),room.getJobTimes(),room.getJobData());
        return "success";
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param
     * @return
     */
    @PostMapping("/update")
    public Object updateBatchByIds(QuartzForm quartz) {
        quartzService.updateJob(quartz.getJobName(),quartz.getJobGroupName(),quartz.getJobTime()+"");
        return "success";
    }

    /**
     * 修改房间
     *
     * @param
     * @return
     */
    @PostMapping("/del")
    public Object del(QuartzForm quartz) {
        quartzService.deleteJob(quartz.getJobName(),quartz.getJobGroupName());
        return "success";
    }

    /**
     * 修改房间
     *
     * @param
     * @return
     */
    @PostMapping("/find")
    public Object find() {
        List<Map<String, Object>> maps = quartzService.queryAllJob();
        return maps;
    }


}

