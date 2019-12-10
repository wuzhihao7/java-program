package com.technologysia.completablefuture.demo2;

import java.util.concurrent.*;

/**
 * 异步方法一个非常有用的特性就是能够提供一个Executor来异步地执行CompletableFuture。这个例子演示了如何使用一个固定大小的线程池来应用大写函数。
 */
public class ThenApplyAsyncWithExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
            int count = 1;
            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "custom-executor-" + count++);
            }
        });

        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("1-"+Thread.currentThread().getName().startsWith("custom-executor-"));
            System.out.println("2-"+Thread.currentThread().isDaemon());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return s.toUpperCase();
        }, executor);

        System.out.println("3-"+cf.getNow(null));
        System.out.println("4-"+cf.join());
    }
}
