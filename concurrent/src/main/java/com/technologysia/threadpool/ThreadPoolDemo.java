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
                new ArrayBlockingQueue<>(128),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(IntStream.range(0, 20).mapToObj(i -> CompletableFuture.runAsync(() -> {
//            System.out.println("1_" + i + ":" + Thread.currentThread().getName());
            //统计交易方发生额
            CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
//                System.out.println("2_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, pool);
            //统计账务方发生额
            CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
//                System.out.println("3_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, pool);
            //交易方和账务方统计完成后，进行合并
            cf1.thenCombineAsync(cf2, (tradeAccountDayMap, accountingAccountDayMap) -> {
//                System.out.println("4_" + i + ":" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }, pool).whenComplete((o, throwable) -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
//            System.out.println("5_" + i + ":" + Thread.currentThread().getName());
        }, pool)).toArray(CompletableFuture[]::new));
        voidCompletableFuture.whenCompleteAsync((aVoid, throwable) -> System.out.println("外部计算完成")).join();
        System.out.println(Thread.currentThread().getName());
        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
        }

    }
}
