package com.house.demo;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

/**
 * @author wuzhihao
 * @date 2019/1/21
 */
public class LocalDateDemo {
    public static void main(String[] args){
        //获取当前日期
        LocalDate today = LocalDate.now();
        System.out.println("Current Date:" + today);

        //创建一个日期
        LocalDate date = LocalDate.of(2019, Month.JANUARY, 1);
        //java.time.DateTimeException: Invalid date 'February 29' as '2014' is not a leap year
        //LocalDate feb29_2014 = LocalDate.of(2014, Month.FEBRUARY, 29);
        System.out.println("Create a Date:" + date);

        /**
         * 指定时区
         * zoneId从{@link ZoneId}中找
         */
        LocalDate today_Los_Angeles = LocalDate.now(ZoneId.of("America/Los_Angeles"));
        //java.time.zone.ZoneRulesException: Unknown time-zone ID: PST
        //LocalDate today_Los_Angeles = LocalDate.now(ZoneId.of("PST"));
        System.out.println("Los_Angeles Date:" + today_Los_Angeles);

        //从01/01/1970 获取一个日期
        LocalDate dateFromBase = LocalDate.ofEpochDay(365);
        System.out.println("Get a Date from Base:" + dateFromBase);

        //2019年的第100天
        LocalDate hundredDayOf2019 = LocalDate.ofYearDay(2019, 100);
        System.out.println("Hundred day of 2019:" + hundredDayOf2019);

    }
}
