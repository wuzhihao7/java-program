package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class DoubleNode {
    public int data;
    public DoubleNode previous;
    public DoubleNode next;

    public DoubleNode(int data){
        this.data = data;
    }

    public void display(){
        System.out.print("data: " + data);
    }
}
