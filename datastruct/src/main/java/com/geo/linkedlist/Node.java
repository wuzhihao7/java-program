package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class Node {
    public int data;
    public Node next;

    public Node(int data, Node next){
        this.data = data;
        this.next = next;
    }

    public void display(){
        System.out.print("data: "+data);
    }
}
