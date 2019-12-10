package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 运行一个简单的异步阶段
 * CompletableFuture的方法如果以Async结尾，它会异步的执行(没有指定executor的情况下)， 异步执行通过ForkJoinPool实现， 它使用守护线程去执行任务。
 * 注意这是CompletableFuture的特性， 其它CompletionStage可以override这个默认的行为。
 */
public class RunAsyncDemo {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println("1-"+Thread.currentThread().isDaemon());
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("2-"+cf.isDone());
        TimeUnit.SECONDS.sleep(2);
        System.out.println("3-"+cf.isDone());
    }
}
