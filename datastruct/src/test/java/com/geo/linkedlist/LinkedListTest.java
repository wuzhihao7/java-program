package com.geo.linkedlist;

import org.junit.Test;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class LinkedListTest {
    @Test
    public void test(){
        LinkedList linkedList = new LinkedList();
        linkedList.insertFirst(new Node(1, null));
        linkedList.display();
        linkedList.find(1).display();
        linkedList.delete(1).display();
        System.out.println(linkedList.isEmpty());
    }
}