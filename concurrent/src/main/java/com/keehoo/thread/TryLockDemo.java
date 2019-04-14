package com.keehoo.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class TryLockDemo extends Thread {
    private static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        if(lock.tryLock()){
            try {
                System.out.println(getName()+"得到了锁");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                System.out.println(getName()+"释放了锁");
                lock.unlock();
            }
        }else{
            System.out.println(getName()+"获取锁失败");
        }
    }

    public static void main(String[] args) {
        TryLockDemo t1 = new TryLockDemo();
        TryLockDemo t2 = new TryLockDemo();
        t1.start();
        t2.start();
    }
}
