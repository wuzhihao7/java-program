package com.geo.hellojob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/30
 */
public class CronScheduler {
    public static void main(String[] args) throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();

        JobDetail cronJob = JobBuilder.newJob(HelloJob.class).withIdentity("cronJob").build();
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ? *")).build();
        scheduler.scheduleJob(cronJob, cronTrigger);
    }
}
