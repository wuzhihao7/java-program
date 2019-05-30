package com.geo.hellojob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 触发任务
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/30
 */
public class HelloSchedule {
    public static void main(String[] args) throws SchedulerException {
        //创建JobDetail实例，与HelloJob.class绑定
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("myJob").build();
        //创建一个Trigger触发器，定义该job立即执行，并且每隔2秒执行一次，一直执行
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger").startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();
        //创建schedule实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

    }
}
