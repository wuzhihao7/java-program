package com.house.demo;

import java.time.*;

/**
 * @author wuzhihao
 * @date 2019/1/21
 */
public class LocalDateTimeDemo {
    public static void main(String[] args){
        //当前时间日期
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current DateTime:" + now);
        LocalDateTime now2 = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        System.out.println("Current DateTime:" + now2);

        //指定日期时间
        LocalDateTime dateTime = LocalDateTime.of(2019, Month.JANUARY, 21, 12, 1, 2, 3);
        System.out.println("Create DateTime:" + dateTime);

        //指定时区
        LocalDateTime dateTime2 = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
        System.out.println("Los_Angeles:" + dateTime2);

        //Getting date from the base date i.e 01/01/1970
        LocalDateTime dateFromBase = LocalDateTime.ofEpochSecond(10000, 0, ZoneOffset.UTC);
        System.out.println("10000th second time from The Base Day:"+dateFromBase);
    }
}
