package com.keehoo.threadpool;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/18
 */
public class SimpleThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool executor = new SimpleThreadPool();
        IntStream.range(0, 10).forEach(i ->
                executor.submit(() -> {
                    System.out.printf("[线程] - [%s] 开始工作...\n", Thread.currentThread().getName());
                    try {
                        Thread.sleep(2_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("[线程] - [%s] 工作完毕...\n", Thread.currentThread().getName());
                })
        );
        //如果放开注释即会执行完所有任务关闭线程池
        executor.shutdown();
    }
}