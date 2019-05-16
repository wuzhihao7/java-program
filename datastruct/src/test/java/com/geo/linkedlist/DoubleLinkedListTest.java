package com.geo.linkedlist;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class DoubleLinkedListTest {
    @Test
    public void test(){
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
        doubleLinkedList.insertLast(new DoubleNode(1));
        doubleLinkedList.display();
        doubleLinkedList.insertFirst(new DoubleNode(2));
        doubleLinkedList.display();
        doubleLinkedList.insertAfter(new DoubleNode(2));
        doubleLinkedList.display();
        doubleLinkedList.deleteLast();
        doubleLinkedList.display();
        doubleLinkedList.insertFirst(new DoubleNode(1));
        doubleLinkedList.display();
        doubleLinkedList.deleteFirst();
        doubleLinkedList.display();
    }
}