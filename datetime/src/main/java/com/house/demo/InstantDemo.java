package com.house.demo;

import java.time.Duration;
import java.time.Instant;

/**
 * @author wuzhihao
 * @date 2019/1/21
 */
public class InstantDemo {
    public static void main(String[] args){
        //当前时间戳
        Instant now = Instant.now();
        System.out.println("Current Timestamp:" + now);

        //指定时间戳
        Instant timestamp = Instant.ofEpochMilli(now.toEpochMilli());
        System.out.println("Create Timestamp:" + timestamp);

        //Duration example
        Duration thirtyDay = Duration.ofDays(1);
        System.out.println("Duration of Days:"+thirtyDay);
        Duration hours = Duration.ofHours(1);
        System.out.println("Duration of Hours:" + hours);
        Duration seconds = Duration.ofSeconds(1);
        System.out.println("Duration of Seconds:" + seconds);
    }
}
