package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class SortedLinkedList {
    private Node first;

    public void insert(Node node){
        Node previous = null;
        Node cur = first;
        //链表由小到大排列
        while (cur != null && cur.data < node.data){
            previous = cur;
            cur = cur.next;
        }
        if(previous == null){
            first = node;
        }else{
            previous.next = node;
        }
        node.next = cur;
    }

    public Node deleteFirst(){
        if(isEmpty()){
            throw new UnsupportedOperationException("SortedLinkedList is empty");
        }
        Node temp = first;
        first = first.next;
        return temp;
    }

    public void display(){
        Node cur = first;
        while (cur != null){
            cur.display();
            cur = cur.next;
            if(cur != null){
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public boolean isEmpty(){
        return first == null;
    }
}
