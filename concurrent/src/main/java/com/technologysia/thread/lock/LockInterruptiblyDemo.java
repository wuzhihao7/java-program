package com.technologysia.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class LockInterruptiblyDemo {
    public static volatile boolean flag;
    private Lock lock = new ReentrantLock();

    public void insert() throws InterruptedException {
        ////注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        lock.lockInterruptibly();
        try {
            System.out.println(Thread.currentThread().getName()+"得到了锁");
            while (!flag){

            }
        }finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+"释放锁");
        }
    }

    public static void main(String[] args) {
        LockInterruptiblyDemo demo = new LockInterruptiblyDemo();
        MyThreadDemo t1 = new MyThreadDemo(demo);
        MyThreadDemo t2 = new MyThreadDemo(demo);
        t1.start();
        t2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        t2.interrupt();
    }
}

class MyThreadDemo extends Thread{
    private LockInterruptiblyDemo lockInterruptiblyDemo;
    public MyThreadDemo(LockInterruptiblyDemo lockInterruptiblyDemo){
        this.lockInterruptiblyDemo = lockInterruptiblyDemo;
    }

    @Override
    public void run() {
        try {
            lockInterruptiblyDemo.insert();
        } catch (InterruptedException e) {
            LockInterruptiblyDemo.flag = true;
            System.out.println(getName()+"被中断");
        }
    }
}
