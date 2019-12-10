package com.technologysia.thread.base;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class SynchronizedDemo implements Runnable {
    private static int count;
    @Override
    public void run() {
        synchronized (SynchronizedDemo.class){
            IntStream.range(0, 10).forEach(i -> count++);
        }
    }

    public static void main(String[] args) {
        IntStream.range(0,10).forEach(i -> {
            Thread t = new Thread(new SynchronizedDemo());
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("result: " + count);
    }
}
