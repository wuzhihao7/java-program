package com.geo.array;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class OrderArrayTest {
    @Test
    public void test(){
        OrderArray orderArray = new OrderArray(4);
        orderArray.insert(1);
        orderArray.insert(2);
        orderArray.insert(3);
        orderArray.insert(4);
        System.out.println(orderArray.find(8));
        System.out.println(orderArray.find(1));
        orderArray.display();
        orderArray.delete(3);
        orderArray.display();
    }
}
