package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 通过循环不断判断自身是否产生了中断
 */
public class Demo1 implements Runnable {

    @Override
    public void run() {
        int num = 0;
        while (num <= Integer.MAX_VALUE /2){
            if(Thread.currentThread().isInterrupted()){
                System.out.println("响应中断");
                break;
            }
            if(num % 10000 == 0){
                System.out.println(num);
            }
            num++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Demo1());
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
