package com.geo.queue;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class PriorityQueue {
    private int[] queueArray;
    private int maxSize;
    private int length;
    private int referencePoint;

    public PriorityQueue(int maxSize, int referencePoint){
        this.maxSize = maxSize;
        this.referencePoint = referencePoint;
        this.queueArray = new int[maxSize];
        this.length = 0;
    }

    public void insert(int elem){
        if(isFull()){
            throw new UnsupportedOperationException("queue is full");
        }
        if(isEmpty()){
            queueArray[length++] = elem;
        }else{
            int i;
            for (i=length;i>0;i--){
                //带插入元素的距离
                int dis = Math.abs(elem - referencePoint);
                //当前元素的距离
                int curDis = Math.abs(queueArray[i-1] - referencePoint);
                //将比插入元素优先级高的元素后移一位
                if(dis >= curDis){
                    queueArray[i] = queueArray[i-1];
                }else{
                    break;
                }
            }
            queueArray[i] = elem;
            length++;
        }
    }

    public int remove(){
        if(isEmpty()){
            throw new UnsupportedOperationException("queue is empty");
        }
        int elem = queueArray[--length];
        return elem;
    }

    public int peek(){
        if(isEmpty()){
            throw new UnsupportedOperationException("queue is empty");
        }
        return queueArray[length-1];
    }

    public int size(){
        return length;
    }

    public boolean isEmpty(){
        return length == 0;
    }

    public boolean isFull(){
        return length == maxSize;
    }

    public void display() {
        if(!isEmpty()){
            for(int i=0;i<length;i++){
                System.out.print(queueArray[i] + "\t");
            }
            System.out.println();
        }
    }
}
