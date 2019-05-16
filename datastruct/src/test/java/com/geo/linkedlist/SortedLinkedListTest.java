package com.geo.linkedlist;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class SortedLinkedListTest {
    @Test
    public void test(){
        SortedLinkedList sortedLinkedList = new SortedLinkedList();
        System.out.println(sortedLinkedList.isEmpty());
        sortedLinkedList.insert(new Node(1));
        sortedLinkedList.insert(new Node(3));
        sortedLinkedList.insert(new Node(4));
        sortedLinkedList.display();
    }
}