package com.technologysia.completablefuture.demo3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Sync {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> {
            System.out.println(Thread.currentThread().isDaemon());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.toUpperCase();
        });
        System.out.println("main");
        // gotNow 如果成功就返回结果
//        System.out.println(cf.getNow(null));
        // 一直等待成功，然后返回结果
//        System.out.println(cf.join());
    }
}
