package com.house.demo;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

/**
 * @author wuzhihao
 * @date 2019/1/21
 */
public class DateTimeUtils {
    public static void main(String[] args){

        //获取日期年份，判断是否是闰年
        LocalDate today = LocalDate.now();
        System.out.println("Year:" + today.getYear() + ", is Leap Year? " + today.isLeapYear());
        //判断
        System.out.println("Today is before 01/01/2015? "+today.isBefore(LocalDate.of(2015,1,1)));
        System.out.println("Today is after 01/01/2015? "+today.isAfter(LocalDate.of(2015,1,1)));
        System.out.println("Today is equals 21/01/2019? "+today.isEqual(LocalDate.of(2019,1,21)));
        //计算
        System.out.println("10 days after today will be "+today.plusDays(10));
        System.out.println("3 weeks after today will be "+today.plusWeeks(3));
        System.out.println("20 months after today will be "+today.plusMonths(20));

        System.out.println("10 days before today will be "+today.minusDays(10));
        System.out.println("3 weeks before today will be "+today.minusWeeks(3));
        System.out.println("20 months before today will be "+today.minusMonths(20));


        //Temporal adjusters for adjusting the dates
        System.out.println("First date of this month= "+today.with(TemporalAdjusters.firstDayOfMonth()));
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        System.out.println("Last date of this year= "+lastDayOfYear);

        Period period = today.until(lastDayOfYear);
        System.out.println("Period Format= "+period);
        System.out.println("Months remaining in the year= "+period.getMonths());
    }
}
