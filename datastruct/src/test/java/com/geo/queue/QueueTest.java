package com.geo.queue;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class QueueTest {
    @Test
    public void test(){
        Queue queue = new Queue(10);
        queue.insert(1);
        queue.insert(2);
        queue.insert(3);
        System.out.println(queue.size());
        System.out.println(queue.peek());
        System.out.println(queue.remove());
    }
}
