package com.keehoo.thread.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class ReentrantReadWriteLockDemo {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public synchronized void get(){
        IntStream.range(0, 20).forEach(i -> System.out.println(Thread.currentThread().getName()+" 正在进行读操作"));
        System.out.println(Thread.currentThread().getName()+" 读操作进行完毕");
    }

    public void get2(){
        lock.readLock().lock();
        try {
            IntStream.range(0, 50).forEach(i -> System.out.println(Thread.currentThread().getName()+" 正在进行读操作"));
            System.out.println(Thread.currentThread().getName()+" 读操作进行完毕");
        }finally {
            lock.readLock().unlock();
        }
    }
    public static void main(String[] args) {
//        synchronizedDemo();
        readWriteLock();

    }

    private static void synchronizedDemo() {
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.get();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.get();
            }
        }.start();
    }

    private static void readWriteLock(){
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.get2();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.get2();
            }
        }.start();
    }
}
