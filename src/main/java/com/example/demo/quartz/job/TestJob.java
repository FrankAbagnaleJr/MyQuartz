package com.example.demo.quartz.job;

import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @auther: jijin
 * @date: 2023/9/3 21:53 周日
 * @project_name: demo
 * @version: 1.0
 * @description TODO
 */

public class TestJob {
    public static void main(String[] args) throws SchedulerException {

        //创建任务类
        JobDetail jobDetail = JobBuilder
                .newJob(Myjob.class)
                .withIdentity("job1", "job_group1")   //把任务逻辑的.class文件传入
                .usingJobData("job_order","625103471")  //设置任务的id，可根据id来防止重复执行，逻辑里面都可以获取到
                .usingJobData("count",0)     //计数器，可以用来统计任务执行了几次
                .build();

        //创建触发类
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger1", "trigger_group1")
                .usingJobData("trigger_order","625103471")
                .startNow()
                //触发器的类型，有简单、日历、cron等，这里用的是简单
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
                //cron表达式触发，这里是每5秒执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();

        //创建定时器类
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //将任务类和触发器类传入
        scheduler.scheduleJob(jobDetail, trigger);
        //启动任务
        scheduler.start();
    }
}
