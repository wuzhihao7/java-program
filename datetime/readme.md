# 时间日期API

## 相关包介绍

Java日期/时间API包含以下相应的包：  

1. java.time包：这是新的Java日期/时间API的基础包，所有的主要基础类都是这个包的一部分，如：LocalDate, LocalTime, LocalDateTime, Instant, Period, Duration等等。所有这些类都是不可变的和线程安全的，在绝大多数情况下，这些类能够有效地处理一些公共的需求。  
2. java.time.chrono包：这个包为非ISO的日历系统定义了一些泛化的API，我们可以扩展AbstractChronology类来创建自己的日历系统。  
3. java.time.format包：这个包包含能够格式化和解析日期时间对象的类，在绝大多数情况下，我们不应该直接使用它们，因为java.time包中相应的类已经提供了格式化和解析的方法。  
4. java.time.temporal包：这个包包含一些时态对象，我们可以用其找出关于日期/时间对象的某个特定日期或时间，比如说，可以找到某月的第一天或最后一天。你可以非常容易地认出这些方法，因为它们都具有“withXXX”的格式。  
5. java.time.zone包：这个包包含支持不同时区以及相关规则的类。  

## API示例

`java.time.LocalDate`：LocalDate是一个不可变的类，它表示默认格式(yyyy-MM-dd)的日期，我们可以使用now()方法得到当前时间，也可以提供输入年份、月份和日期的输入参数来创建一个LocalDate实例。该类为now()方法提供了重载方法，我们可以传入ZoneId来获得指定时区的日期。

```java
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
```

`java.time.LocalTime`：LocalTime是一个不可变的类，它的实例代表一个符合人类可读格式的时间，默认格式是hh:mm:ss.zzz。像LocalDate一样，该类也提供了时区支持，同时也可以传入小时、分钟和秒等输入参数创建实例

```java
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
```

`java.time.LocalDateTime`：LocalDateTime是一个不可变的日期-时间对象，它表示一组日期-时间，默认格式是yyyy-MM-dd-HH-mm-ss.zzz。它提供了一个工厂方法，接收LocalDate和LocalTime输入参数，创建LocalDateTime实例。

```java
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
```

`java.time.Instant`：Instant类是用在机器可读的时间格式上的，它以Unix时间戳的形式存储日期时间

```java
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
```

日期API工具：我们早些时候提到过，大多数日期/时间API类都实现了一系列工具方法，如：加/减天数、周数、月份数，等等。还有其他的工具方法能够使用TemporalAdjuster调整日期，并计算两个日期间的周期。

```java
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
```

解析和格式化：将一个日期格式转换为不同的格式，之后再解析一个字符串，得到日期时间对象

```java
        //Format examples
        LocalDate date = LocalDate.now();
        //default format
        System.out.println("Default format of LocalDate="+date);
        //specific format
        System.out.println(date.format(DateTimeFormatter.ofPattern("d::MMM::uuuu")));
        System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));

        LocalDateTime dateTime = LocalDateTime.now();
        //default format
        System.out.println("Default format of LocalDateTime="+dateTime);
        //specific format
        System.out.println(dateTime.format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")));
        System.out.println(dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));

        Instant timestamp = Instant.now();
        //default format
        System.out.println("Default format of Instant="+timestamp);

        //Parse examples
        LocalDateTime dt = LocalDateTime.parse("21::1月::2019 17::32::30",
                DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
        System.out.println("Default format after parsing = "+dt);
```

旧的日期时间支持：旧的日期/时间类已经在几乎所有的应用程序中使用，因此做到向下兼容是必须的。

```java
        //Date to Instant
        Instant timestamp = new Date().toInstant();
        //Now we can convert Instant to LocalDateTime or other similar classes
        LocalDateTime date = LocalDateTime.ofInstant(timestamp,
                ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
        System.out.println("Date = "+date);

        //Calendar to Instant
        Instant time = Calendar.getInstance().toInstant();
        System.out.println(time);
        //TimeZone to ZoneId
        ZoneId defaultZone = TimeZone.getDefault().toZoneId();
        System.out.println(defaultZone);

        //ZonedDateTime from specific Calendar
        ZonedDateTime gregorianCalendarDateTime = new GregorianCalendar().toZonedDateTime();
        System.out.println(gregorianCalendarDateTime);

        //Date API to Legacy classes
        Date dt = Date.from(Instant.now());
        System.out.println(dt);

        TimeZone tz = TimeZone.getTimeZone(defaultZone);
        System.out.println(tz);

        GregorianCalendar gc = GregorianCalendar.from(gregorianCalendarDateTime);
        System.out.println(gc);
```

