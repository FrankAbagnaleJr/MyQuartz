package com.example.demo.quartz.job;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther: jijin
 * @date: 2023/9/3 21:51 周日
 * @project_name: demo
 * @version: 1.0
 * @description TODO
 */
@DisallowConcurrentExecution   //禁止任务并发执行
@PersistJobDataAfterExecution  //持久化JobDetail （对trigger中的JobDataMap无效）
public class Myjob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

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
            Thread.sleep(3000); //任务执行超时也不用担心重复执行
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
