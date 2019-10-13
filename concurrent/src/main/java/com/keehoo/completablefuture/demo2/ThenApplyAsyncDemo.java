package com.keehoo.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 通过调用异步方法(方法后边加Async后缀)，串联起来的CompletableFuture可以异步地执行（使用ForkJoinPool.commonPool()）。
 */
public class ThenApplyAsyncDemo {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println("1-"+Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        System.out.println("2-"+cf.getNow(null));
        System.out.println("3-"+cf.join());
    }
}
