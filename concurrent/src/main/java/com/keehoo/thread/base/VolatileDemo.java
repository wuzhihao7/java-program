package com.keehoo.thread.base;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class VolatileDemo {
    private static volatile boolean flag;

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!flag){
                    System.out.println("loop");
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
    }
}
