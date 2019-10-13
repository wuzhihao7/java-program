package com.keehoo.completablefuture.demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用 supplyAsync() 运行一个异步任务并且返回结果
 * 当任务不需要返回任何东西的时候， CompletableFuture.runAsync() 非常有用。但是如果你的后台任务需要返回一些结果应该要怎么样？
 * CompletableFuture.supplyAsync() 就是你的选择。它持有supplier<T> 并且返回CompletableFuture<T>，T 是通过调用 传入的supplier取得的值的类型。
 *
 * 一个关于Executor 和Thread Pool笔记
 * 你可能想知道，我们知道runAsync()和supplyAsync()方法在单独的线程中执行他们的任务。但是我们不会永远只创建一个线程。
 * CompletableFuture可以从全局的 ForkJoinPool.commonPool()获得一个线程中执行这些任务。
 * 但是你也可以创建一个线程池并传给runAsync()和supplyAsync()方法来让他们从线程池中获取一个线程执行它们的任务。
 * CompletableFuture API 的所有方法都有两个变体-一个接受Executor作为参数，另一个不这样：
 *
 * // Variations of runAsync() and supplyAsync() methods
 * static CompletableFuture<Void>  runAsync(Runnable runnable)
 * static CompletableFuture<Void>  runAsync(Runnable runnable, Executor executor)
 * static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
 * static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
 *
 */
public class SupplyAsyncDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of the asynchronous computation";
        });
        System.out.println("main thread");
        System.out.println(completableFuture.get());
    }
}
