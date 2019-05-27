package com.keehoo.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/27
 */
public class Demo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(3, true);
        IntStream.range(0, 10).forEach(i -> executorService.submit(() -> {
            try {
                //获取信号灯许可
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 进入，当前并发数为：" + (3 - semaphore.availablePermits()));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 即将离开");
            //释放信号灯
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + "已经离开，当前并发数为：" + (3 - semaphore.availablePermits()));
        }));
        executorService.shutdown();
    }
}
