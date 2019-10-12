package com.keehoo.completablefuture;

import java.util.concurrent.*;

public class ThenApplyAsyncDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        thenApply();
        System.out.println("----------------");
        thenApplyAsync();
        System.out.println("------------------");
        thenApplyAsyncExecutor();
    }

    /**
     * 如果你传入一个Executor到thenApplyAsync()回调中，，任务将从Executor线程池获取一个线程执行。
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void thenApplyAsyncExecutor() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            return "Some result";
        }).thenApplyAsync(result -> {
            // Executed in a thread obtained from the executor
            System.out.println(Thread.currentThread().getName());
            return "Processed Result: "+result;
        }, executorService);
        System.out.println(completableFuture.get());
        executorService.shutdown();
    }

    /**
     * 为了控制执行回调任务的线程，你可以使用异步回调。如果你使用thenApplyAsync()回调，将从ForkJoinPool.commonPool()获取不同的线程执行。
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void thenApplyAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Some Result";
        }).thenApplyAsync(result -> {
            // Executed in a different thread from ForkJoinPool.commonPool()
            System.out.println(Thread.currentThread().getName());
            return "Processed Result:" + result;
        });
        System.out.println(completableFuture.get());
    }

    /**
     * 异步回调方法的笔记
     * CompletableFuture提供的所有回调方法都有两个变体：
     * // thenApply() variants :
     * <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
     * <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
     * <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     * 这些异步回调变体通过在独立的线程中执行回调任务帮助你进一步执行并行计算。
     * 以下示例：
     * 在以下示例中，在thenApply()中的任务和在supplyAsync()中的任务执行在相同的线程中。任何supplyAsync()立即执行完成,那就是执行在主线程中（尝试删除sleep测试下）。
     *
     *
     */
    private static void thenApply() throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println(Thread.currentThread().getName());
            return "Some Result";
        }).thenApply(result -> {
    /*
      Executed in the same thread where the supplyAsync() task is executed
      or in the main thread If the supplyAsync() task completes immediately (Remove sleep() call to verify)
    */
            System.out.println(Thread.currentThread().getName());
            return "Processed Result: " + result;
        });
        System.out.println(completableFuture.get());
    }
}
