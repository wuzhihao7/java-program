package com.keehoo.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/18
 */
public class SimpleThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch end = new CountDownLatch(300);
        SimpleThreadPool executor = new SimpleThreadPool();
        IntStream.range(0, 1000).forEach(i -> {
                    try {
                        executor.submit(() -> {
                            System.out.printf("[线程] - [%s] 开始工作...\n", Thread.currentThread().getName());
                            System.out.printf("[线程] - [%s] 工作完毕...\n", Thread.currentThread().getName());
                        });
                    } catch (SimpleThreadPool.DiscardException e) {
                        System.err.println(e.getMessage());
                    }
                }
        );
        //如果放开注释即会执行完所有任务关闭线程池
        executor.shutdown();
    }
}