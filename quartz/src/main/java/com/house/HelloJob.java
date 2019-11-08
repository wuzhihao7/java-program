package com.house;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(HelloJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("hello, quartz!");
    }
}
