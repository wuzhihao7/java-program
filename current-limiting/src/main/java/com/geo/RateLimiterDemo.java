package com.geo;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/20
 */
public class RateLimiterDemo {
    private static final RateLimiter LIMITER = RateLimiter.create(10);

    public void doRequest(){
        boolean acquire = LIMITER.tryAcquire(1, 1, TimeUnit.SECONDS);
        if(acquire){
            System.out.println(Thread.currentThread().getName()+": 调用成功！");
        }else{
            System.out.println(Thread.currentThread().getName()+": 当前调用人数过多，请稍后重试");
        }
    }

    public static void main(String[] args) {
        RateLimiterDemo rateLimiterDemo = new RateLimiterDemo();
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Random random = new Random(10);
        IntStream.range(0, 100).forEach(i -> {
            executorService.submit(() -> {
                try {
                    countDownLatch.await();
                    int sleepTime = random.nextInt(1000);
                    Thread.sleep(sleepTime);
                    rateLimiterDemo.doRequest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            countDownLatch.countDown();
        });
        executorService.shutdown();
    }
}
