package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class DoubleLinkedList {
    private DoubleNode first;
    private DoubleNode last;

    public void insertFirst(DoubleNode node){
        if(isEmpty()){
            last = node;
        }else{
            first.previous = node;
        }
        node.next = first;
        first = node;
    }

    public void insertLast(DoubleNode node){
        if(isEmpty()){
            first = node;
        }else{
            last.next = node;
            node.previous = last;
        }
        last = node;
    }

    public DoubleNode deleteFirst(){
        if(isEmpty()){
            throw new UnsupportedOperationException("DoubleLinkedList is empty");
        }
        DoubleNode temp = first;
        if(first.next == null){
            last = null;
        }else{
            first.next.previous = null;
        }
        first = first.next;
        return temp;
    }

    public DoubleNode deleteLast(){
        if(isEmpty()){
            throw new UnsupportedOperationException("DoubleLinkedList is empty");
        }
        DoubleNode temp = last;
        if(last.previous == null){
            first = null;
        }else{
            last.previous.next = null;
        }
        last = last.previous;
        return temp;
    }

    public DoubleNode find(int key){
        DoubleNode cur = first;
        while (cur != null && cur.data != key){
            cur = cur.next;
            if(cur == null){
                return null;
            }
        }
        return cur;
    }

    public boolean insertAfter(DoubleNode node){
        DoubleNode target = find(node.data);
        boolean flag = true;
        if(target == null){
            flag = false;
        }else{
            if(target.next == null){
                insertLast(node);
            }else{
                target.next.previous = node;
                node.next = target.next;
                target.next = node;
                node.previous = target;
            }
        }
        return flag;
    }

    public void display(){
        DoubleNode node = first;
        while (node != null){
            node.display();
            node = node.next;
            if(node != null){
                System.out.print(", ");
            }else{
                System.out.println();
            }
        }
    }

    public boolean isEmpty(){
        return first == null;
    }
}
