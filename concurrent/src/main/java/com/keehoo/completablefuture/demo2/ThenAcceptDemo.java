package com.keehoo.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)， 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept:
 * 本例中消费者同步地执行，所以我们不需要在CompletableFuture调用join方法。
 */
public class ThenAcceptDemo {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message")
                .thenAccept(s -> {
                    System.out.println(Thread.currentThread().getName());
                    result.append(s);
                });
        System.out.println(result.length() > 0);
    }
}
