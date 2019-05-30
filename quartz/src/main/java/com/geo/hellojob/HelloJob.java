package com.geo.hellojob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;

/**
 * 编写具体要实现的任务
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/30
 */
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now: " + now);
        System.out.println("Hello Quartz");
        try {
            System.out.println(context.getScheduler().getCurrentlyExecutingJobs());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println(context.getJobDetail());
        System.out.println(context.getTrigger());
    }
}
