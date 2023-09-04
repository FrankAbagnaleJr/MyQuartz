package com.example.demo.boot.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

//监听类
@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //新建一个triggerKey
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_name", "trigger_group");
        try {
            //根据triggerKey从scheduler调度器中取trigger触发器
            Trigger trigger = scheduler.getTrigger(triggerKey);
            //如果没取到，那么就新建一个trigger触发器
            if (trigger == null) {
                //创建trigger触发器
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        // cron 每5秒执行一次
                        .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                        .build();
                //创建任务类
                JobDetail jobDetail = JobBuilder.newJob(BootJob.class)
                        .withIdentity("jobDetail_name", "jobDetail_group")
                        .usingJobData("count",0)
                        .build();
                //准备调度器
                scheduler.scheduleJob(jobDetail, trigger);
                //启动调度器
                scheduler.start();
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
