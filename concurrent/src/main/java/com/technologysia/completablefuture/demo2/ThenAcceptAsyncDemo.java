package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 可以使用thenAcceptAsync方法， 串联的CompletableFuture可以异步地执行。
 */
public class ThenAcceptAsyncDemo {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAcceptAsync message")
                .thenAcceptAsync(s -> {
                    System.out.println("1-"+Thread.currentThread().getName());
                    result.append(s);
                });
        System.out.println("2-"+cf.join());
        System.out.println("3-"+(result.length() > 0));
    }
}
