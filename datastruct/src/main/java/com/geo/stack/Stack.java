package com.geo.stack;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class Stack {
    private int size;
    private int top;
    private char[] stackArray;

    public Stack(int size){
        this.stackArray = new char[size];
        this.top = -1;
        this.size = size;
    }

    /**
     * 入栈
     * @param elem 入栈元素
     */
    public void push(char elem){
        if(isFull()){
            throw new StackOverflowError();
        }
        stackArray[++top] = elem;
    }

    /**
     * 出栈
     * @return 出栈元素
     */
    public char pop(){
        if(isEmpty()){
            throw new NullPointerException();
        }
        return stackArray[top--];
    }

    /**
     * 查看栈顶元素
     * @return 栈顶元素
     */
    public int peek(){
        if(isEmpty()){
            throw new NullPointerException();
        }
        return stackArray[top];
    }

    /**
     * 是否为空
     * @return true为空，false为非空
     */
    public boolean isEmpty(){
        return top == -1;
    }

    /**
     * 是否满了
     * @return 是为true，否为false
     */
    public boolean isFull(){
        return top == size -1;
    }
}
