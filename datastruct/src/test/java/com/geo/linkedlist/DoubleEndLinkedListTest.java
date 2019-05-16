package com.geo.linkedlist;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class DoubleEndLinkedListTest {
    @Test
    public void test(){
        DoubleEndLinkedList doubleEndLinkedList = new DoubleEndLinkedList();
        try {
            doubleEndLinkedList.deleteFirst();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(doubleEndLinkedList.isEmpty());
        doubleEndLinkedList.insertFirst(new Node(1));
        doubleEndLinkedList.display();
        doubleEndLinkedList.insertLast(new Node(2));
        doubleEndLinkedList.display();
        System.out.println(doubleEndLinkedList.deleteFirst());
        doubleEndLinkedList.display();
    }
}