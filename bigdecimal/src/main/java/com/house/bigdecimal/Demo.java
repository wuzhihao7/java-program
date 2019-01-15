package com.house.bigdecimal;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author wuzhihao
 * @date 2019/1/15
 */
public class Demo {
    @Test
    public void test1(){
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
    }

    @Test
    public void test2(){
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
    }
}
