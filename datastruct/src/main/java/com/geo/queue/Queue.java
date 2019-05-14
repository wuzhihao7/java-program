package com.geo.queue;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class Queue {
    private int[] queueArray;
    private int maxQueueSize;
    /**
     * 存储对头元素下标
     */
    private int front;
    /**
     * 存储队尾元素下标
     */
    private int rear;
    /**
     * 队列长度
     */
    private int length;

    public Queue(int maxQueueSize){
        this.maxQueueSize = maxQueueSize;
        queueArray = new int[maxQueueSize];
        front = 0;
        rear = -1;
        length = 0;
    }

    /**
     * 插入
     * @param elem 插入元素
     */
    public void insert(int elem){
        if(isFull()){
            throw new UnsupportedOperationException("queue is full");
        }
        //如果队尾指针已经到达数组的末端，插入到数组的第一个位置
        if(rear == maxQueueSize){
            rear = -1;
        }
        queueArray[++rear] = elem;
        length++;
    }

    /**
     * 删除
     * @return 删除元素
     */
    public int remove(){
        if(isEmpty()){
            throw new UnsupportedOperationException("queue is empty");
        }
        int elem = queueArray[front++];
        //如果队头指针已经到达数组末端，则移到数组第一个位置
        if(front == maxQueueSize){
            front = 0;
        }
        length--;
        return elem;
    }

    /**
     * 查看队头元素
     * @return 返回队头元素
     */
    public int peek(){
        if(isEmpty()){
            throw new UnsupportedOperationException("queue is empty");
        }
        return queueArray[front];
    }

    /**
     * 查看队列大小
     * @return 队列大小
     */
    public int size(){
        return length;
    }

    /**
     * 队列是否为空
     * @return 是为true，否为false
     */
    public boolean isEmpty(){
        return length == 0;
    }

    /**
     * 队列是否满了
     * @return 是为true，否为false
     */
    public boolean isFull(){
        return length == maxQueueSize;
    }
}
