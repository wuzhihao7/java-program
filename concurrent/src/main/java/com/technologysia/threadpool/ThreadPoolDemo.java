package com.technologysia.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 反面教材，线程池资源耗尽，线程全部阻塞
 */
public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, TimeoutException, ExecutionException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("consumer-queue-thread-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(128),namedThreadFactory,new ThreadPoolExecutor.CallerRunsPolicy());
        System.out.println(Thread.currentThread().getName());

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(IntStream.range(0, 20).mapToObj(i -> CompletableFuture.runAsync(() -> {
            System.out.println("1_" + i + ":" + Thread.currentThread().getName());
            //统计交易方发生额
            CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
                System.out.println("2_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, pool);
            //统计账务方发生额
            CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
                System.out.println("3_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, pool);
            //交易方和账务方统计完成后，进行合并
            cf1.thenCombineAsync(cf2, (tradeAccountDayMap, accountingAccountDayMap) -> {
                System.out.println("4_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }, pool).join();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("5_" + i + ":" + Thread.currentThread().getName());
        }, pool)).toArray(CompletableFuture[]::new));
        voidCompletableFuture.get(120, TimeUnit.SECONDS);
        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("线程还在执行。。。");
        }

    }
}
