package com.technologysia.thread.interrupt;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * 通过java内定的机制响应中断：
 * 当线程调用sleep()，wait()方法后进入阻塞后，如果线程在阻塞的过程中被中断了，
 * 那么线程会捕获或抛出一个中断异常，我们可以根据这个中断异常去控制线程的停止。
 */
public class Demo2 implements Runnable {
    @Override
    public void run() {
        int num = 0;
        try{
            while (num < Integer.MAX_VALUE /2){
                if(num % 10000 == 0){
                    System.out.println(num);
                }
                num++;
                TimeUnit.MILLISECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            //捕获中断异常，在本地代码中，出现中断异常后退出循环
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Demo2());
        thread.start();
        TimeUnit.SECONDS.sleep(5);
        thread.interrupt();
    }
}
