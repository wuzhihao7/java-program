package com.keehoo.thread.base;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class DaemonDemo {
    public static void main(String[] args) {
        daemon1();
        daemon2();
    }
    public static void daemon1() {
        Thread t = new Thread(() -> {
            while (true){
                System.out.println("I'm alive");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("finally block");
                }
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void daemon2(){
        DaemonDemo demo = new DaemonDemo();
        MyThread t1 = demo.new MyThread();
        MyThread t2 = demo.new MyThread();
        t1.setName("关羽");
        t2.setName("张飞");
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        Thread.currentThread().setName("刘备");
        IntStream.range(0,5).forEach(i -> System.out.println(Thread.currentThread().getName()+": "+i));
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            IntStream.range(0,100).forEach(i -> System.out.println(getName() + ": " + i));
        }
    }
}
