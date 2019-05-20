package com.geo;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/20
 */
public class AtomicLimiterTest {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    public void atomicLimiter(){
        // 最大支持 3 個
        if (ATOMIC_INTEGER.get() >= 3) {
            System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - " + "拒絕...");
        } else {
            try {
                ATOMIC_INTEGER.incrementAndGet();
                //处理核心逻辑
                System.out.println(LocalDateTime.now() + " - " + Thread.currentThread().getName() + " - " + "通过...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ATOMIC_INTEGER.decrementAndGet();
            }
        }
    }

    @Test
    public void testAtomic() throws InterruptedException {
        final ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            service.execute(this::atomicLimiter);
        }
        TimeUnit.SECONDS.sleep(5);
        service.shutdown();
    }
}
