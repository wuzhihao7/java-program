package com.keehoo.completablefuture.demo1;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("consumer-queue-thread-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(5, 20, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture[] completableFutures = IntStream.range(0, 200000).mapToObj(operand -> CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName()), pool))
                .collect(Collectors.toList()).toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(completableFutures).join();
        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("线程还在执行。。。");
        }

    }
}
