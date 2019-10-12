package com.keehoo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 虽然thenAccept()可以访问CompletableFuture的结果，但thenRun()不能访Future的结果，它持有一个Runnable返回CompletableFuture：
 */
public class ThenRunDemo {
    public static void main(String[] args) {
        // thenRun() example
        CompletableFuture.supplyAsync(() -> {
            // Run some computation
            return "future";
        }).thenRun(() -> {
            // Computation Finished.
            System.out.println("thenRun");
        });

    }
}
