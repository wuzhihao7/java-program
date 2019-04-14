package com.keehoo.thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class ReentrantLockDemo {
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public void insert(Thread t){
        lock.lock();
        try {
            System.out.println(t.getName() + " 得到了锁");
            IntStream.range(0,5).forEach(i -> arrayList.add(i));
        }finally {
            System.out.println(t.getName() + " 释放了锁");
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            }
        }.start();
    }
}
