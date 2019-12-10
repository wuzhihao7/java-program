package com.technologysia.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/22
 */
public class CountDownLatchDemo {
    private static int count;
    public static void main(String[] args) {
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch end = new CountDownLatch(300);
        ExecutorService executorService = Executors.newFixedThreadPool(300);
        IntStream.range(0, 300).forEach(i -> {
            Runnable run = () -> {
                try {
                    start.await();
                    Thread.sleep(100);
                    synchronized (CountDownLatchDemo.class){
                        count++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    end.countDown();
                }
            };
            executorService.execute(run);
        });
        start.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        System.out.println(count);
    }
}
