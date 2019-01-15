# 舍入法

***RoundingMode.UP：***绝对值向上进位 ，若当前精度位后一位数字不为0 , 则绝对值向上进位，正数更大 , 负数更小  

***RoundingMode.DOWN：*** 绝对值向下舍位，若当前精度位后一位数字不为0，则绝对值向下舍位，正数更小，负数更大  	

***RoundingMode.CEILING：*** 向上进位，若当前精度位后一位数字不为0，则向上进位，数字更大  

***RoundingMode.FLOOR：*** 向下舍位，若当前精度位后一位数字不为0，则向下舍位，数字更小  

***RoundingMode.HALF_UP：*** 4舍5入，正数4舍5进，负数4舍5退  

***RoundingMode.HALF_DOWN：*** 5舍6入，正数5舍6进，负数5舍6退  

***RoundingMode.HALF_EVEN：*** 银行家算法:正数4舍6进,5看前一位数字,若为奇数则进位,若为偶数则舍位,负数5舍6退  

***RoundingMode.UNNECESSARY：*** 不舍位,精度位数小于小数位数会抛出异常

```java
        BigDecimal bigDecimal = new BigDecimal("11.556");
        //绝对值向上进位, 11.56
        System.out.println(bigDecimal.setScale(2, RoundingMode.UP));
        //绝对值向上进位, 11.55
        System.out.println(bigDecimal.setScale(2, RoundingMode.DOWN));
        //四舍五入， 11，56
        System.out.println(bigDecimal.setScale(2, RoundingMode.HALF_UP));
        //五舍六入， 11.56
        System.out.println(bigDecimal.setScale(2, RoundingMode.HALF_DOWN));
        //四舍六进，五看前一位，奇数进位偶数舍位， 11.56
        System.out.println(bigDecimal.setScale(2, RoundingMode.HALF_EVEN));
        //向上进位， 11.56
        System.out.println(bigDecimal.setScale(2, RoundingMode.CEILING));
        //向下舍位， 11.55
        System.out.println(bigDecimal.setScale(2, RoundingMode.FLOOR));
        //精度小于小数位，java.lang.ArithmeticException: Rounding necessary
        System.out.println(bigDecimal.setScale(2, RoundingMode.UNNECESSARY));
```

# 保留位

```java
        //四舍五入
        BigDecimal bigDecimal = new BigDecimal("1.234");
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        System.out.println(bigDecimal.toString());
        //#.00 保留两位小数
        String num = new DecimalFormat("#.00").format(1.234);
        System.out.println(num);
        //
        String num2 = String.format("%.2f", 1.234);
        System.out.println(num2);
```

