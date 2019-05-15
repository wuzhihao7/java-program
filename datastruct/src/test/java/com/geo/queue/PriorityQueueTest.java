package com.geo.queue;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class PriorityQueueTest {
    @Test
    public void test(){
        PriorityQueue priorityQueue = new PriorityQueue(10, 5);
        priorityQueue.insert(1);
        priorityQueue.insert(4);
        priorityQueue.insert(5);
        priorityQueue.insert(6);
        priorityQueue.insert(7);
        priorityQueue.display();
        System.out.println(priorityQueue.peek());
        System.out.println(priorityQueue.size());
        System.out.println(priorityQueue.remove());
    }
}