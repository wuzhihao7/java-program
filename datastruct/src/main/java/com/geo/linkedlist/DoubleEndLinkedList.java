package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class DoubleEndLinkedList {
    //指向链表中的第一个节点
    private Node first;
    //指向链表中的最后一个节点
    private Node last;

    public void insertFirst(Node node){
        if(isEmpty()){
            last = node;
        }
        node.next = first;
        first = node;
    }

    public void insertLast(Node node){
        if(isEmpty()){
            first = node;
        }else{
            last.next = node;
        }
        last = node;
    }

    public Node deleteFirst(){
        if(isEmpty()){
            throw new UnsupportedOperationException("DoubleEndLinkedList is empty");
        }
        Node temp = first;
        if(first == null){
            last = null;
        }
        first = first.next;
        return temp;
    }

    public void display(){
        Node cur = first;
        while (cur != null){
            cur.display();
            if(cur.next != null){
                System.out.print(", ");
            }
            cur = cur.next;
        }
        System.out.println();
    }

    public boolean isEmpty(){
        return first == null;
    }
}
