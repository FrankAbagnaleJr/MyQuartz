package com.example.demo.boot.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
public class SchedulerConfig {
    @Autowired
    private DataSource dataSource;
    //创建Properties类，就是把配置文件的属性导入进来
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("/spring-quartz.properties"); //传入quartz的配置文件
        propertiesFactoryBean.setLocation(classPathResource);
        propertiesFactoryBean.afterPropertiesSet(); //调用这个方法才会读取配置文件
        return propertiesFactoryBean.getObject();
    }
    //创建线程池
    @Bean
    public Executor schedulerThreadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int coreNum = Runtime.getRuntime().availableProcessors();//获取cpu的核心数
        executor.setCorePoolSize(coreNum);
        executor.setMaxPoolSize(coreNum);
        executor.setQueueCapacity(coreNum);
        return executor;
    }
    // 把Properties类传进来创建SchedulerFactoryBean
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        //创建一个SchedulerFactoryBean对象，然后去set它的属性
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setSchedulerName("cluster_scheduler");//不设置也无所谓
        factoryBean.setDataSource(dataSource);
        factoryBean.setQuartzProperties(quartzProperties()); //导入properties配置文件
        factoryBean.setTaskExecutor(schedulerThreadPool()); //设置执行任务线程池
        factoryBean.setStartupDelay(10); //设置延迟10秒执行，不设置是立即执行
        return factoryBean;
    }
    //创建调度器
    @Bean
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }
}
