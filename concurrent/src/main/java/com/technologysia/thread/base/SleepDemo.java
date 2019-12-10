package com.technologysia.thread.base;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class SleepDemo {
    private Object obj = new Object();
    private int i = 10;
    public static void main(String[] args) {
        SleepDemo demo = new SleepDemo();
        MyThread t1 = demo.new MyThread();
        MyThread t2 = demo.new MyThread();
        t1.start();
        t2.start();
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            synchronized (obj){
                i++;
                System.out.println("i = " + i);
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "进入睡眠状态");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+Thread.currentThread().getName()+"睡眠结束");
                i++;
                System.out.println("i = " + i);
            }
        }
    }
}