package com.geo.array;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/13
 */
public class ArrayTest {
    @Test
    public void test(){
        Array array = new Array(10);
        array.insert("a");
        int a = array.contains("a");
        System.out.println(a);
        array.display();
        boolean a1 = array.remove("a");
        System.out.println(a1);
        array.display();
    }
}
