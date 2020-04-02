package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

public class Demo6 {
    public static void main(String[] args) throws InterruptedException {
        //sleepThread睡眠1000ms
        final Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getId() + " sleep: " + Thread.currentThread().isInterrupted());
                    System.out.println(Thread.currentThread().getId() + " sleep: " + Thread.currentThread().isInterrupted());
                }
                while (true){}
            }
        };
        //busyThread一直执行死循环
        Thread busyThread = new Thread() {
            @Override
            public void run() {
                boolean flag = true;
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        if(flag){
                            System.out.println(Thread.currentThread().getId() + " busy: " + Thread.currentThread().isInterrupted());
                            System.out.println(Thread.currentThread().getId() + " busy: " + Thread.currentThread().isInterrupted());
                            flag = false;
                        }
                    }
                }
            }
        };
        sleepThread.start();
        busyThread.start();
        sleepThread.interrupt();
        busyThread.interrupt();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("sleepThread isInterrupted: " + sleepThread.isInterrupted()+"---"+sleepThread.getId());
        System.out.println("busyThread isInterrupted: " + busyThread.isInterrupted()+"---"+sleepThread.getId());
    }
}
