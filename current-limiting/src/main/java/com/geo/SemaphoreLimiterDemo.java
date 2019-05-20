package com.geo;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/20
 */
public class SemaphoreLimiterDemo {
    private static final Semaphore SEMAPHORE = new Semaphore(3);

    private void semaphoreLimiter() {
        // 队列中允许存活的任务个数不能超过 5 个
        if (SEMAPHORE.getQueueLength() > 5) {
            System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - 拒絕...");
        } else {
            try {
                SEMAPHORE.acquire();
                System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - 通过...");
                //处理核心逻辑
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                SEMAPHORE.release();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreLimiterDemo semaphoreLimiterDemo = new SemaphoreLimiterDemo();
        final ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            service.execute(semaphoreLimiterDemo::semaphoreLimiter);
        }
        TimeUnit.SECONDS.sleep(5);
    }
}
