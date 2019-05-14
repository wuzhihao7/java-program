package com.geo.stack;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class StackTest {
    @Test
    public void test(){
        String str = "[{abc)";
        BrecketChecker checker = new BrecketChecker(str);
        checker.check();
    }
}
