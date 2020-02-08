package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

public class Demo5 implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("run1："+Thread.currentThread().isInterrupted());
        }
        System.out.println("执行完");
        System.out.println("run2："+Thread.currentThread().isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Demo5());
        thread.start();
        System.out.println(thread.isInterrupted());
        thread.interrupt();
        System.out.println(thread.isInterrupted());
        TimeUnit.SECONDS.sleep(2);
        System.out.println(thread.isInterrupted());
    }
}
