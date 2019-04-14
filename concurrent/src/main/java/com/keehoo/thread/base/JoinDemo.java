package com.keehoo.thread.base;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class JoinDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                System.out.println("进入线程" + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");
            }
        };
        System.out.println("进入线程" + Thread.currentThread().getName());
        thread.start();
        System.out.println("线程" + Thread.currentThread().getName()+ "等待");
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程" + Thread.currentThread().getName() + "继续执行");
    }
}
