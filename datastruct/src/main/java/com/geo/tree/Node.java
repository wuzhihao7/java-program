package com.geo.tree;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class Node {
    public int data;
    public Node leftChild;
    public Node rightChild;

    public Node(int data){
        this.data = data;
    }

    public void display(){
        System.out.print("data: " + data);
    }
}
