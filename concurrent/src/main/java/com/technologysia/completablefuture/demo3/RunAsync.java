package com.technologysia.completablefuture.demo3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RunAsync {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().isDaemon());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("done=" + cf.isDone());
        TimeUnit.SECONDS.sleep(4);
        System.out.println("done=" + cf.isDone());
    }
}
