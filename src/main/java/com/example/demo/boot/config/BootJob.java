package com.example.demo.boot.config;

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class BootJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //执行的逻辑

        System.out.println("开始执行任务的时间："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobDataMap triggerMqp = context.getTrigger().getJobDataMap();
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap(); //可以获取到所有的JobDataMap键值对

        JobDataMap jobDataMap2 = context.getJobDetail().getJobDataMap();
        jobDataMap2.put("count",jobDataMap2.getInt("count")+1);
        System.out.println(jobDataMap2.get("count"));

        //如果该任务执行发生了异常，任务再次启动时候，会再次执行该任务，此时这个值为true，可以用户来判断是否重复执行
        boolean recovering = context.isRecovering();
        System.out.println(recovering);

        try {
            Thread.sleep(5000);  //任务执行超时也不用担心重复执行 @DisallowConcurrentExecution
            //当前的调度器
            System.out.println(context.getScheduler().getSchedulerInstanceId());
            //输出任务名字
            System.out.println("taskname="+context.getJobDetail().getKey().getName());
            //输出组的名字
            System.out.println("groupname="+context.getJobDetail().getKey().getGroup());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
