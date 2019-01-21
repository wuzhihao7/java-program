package com.house.demo;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author wuzhihao
 * @date 2019/1/21
 */
public class LocalTimeDemo {
    public static void main(String[] args){
        //当前时间
        LocalTime now = LocalTime.now();
        System.out.println("Current Time:" + now);

        LocalTime time = LocalTime.of(23, 1, 2, 3);
        //java.time.DateTimeException: Invalid value for HourOfDay (valid values 0 - 23): 24
//        LocalTime time = LocalTime.of(24, 1, 2, 3);
        System.out.println("Create Time:" + time);

        //指定时区时间
        LocalTime now2 = LocalTime.now(ZoneId.of("America/Los_Angeles"));
        System.out.println("Los_Angeles Time:" + now2);

        //Getting date from the base Time i.e 01/01/1970 00:00:00
        LocalTime time2 = LocalTime.ofSecondOfDay(1);
        System.out.println("Get Time From The Base Time:" + time2);
    }
}
