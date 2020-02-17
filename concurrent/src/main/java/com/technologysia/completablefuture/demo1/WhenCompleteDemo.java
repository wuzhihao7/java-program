package com.technologysia.completablefuture.demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WhenCompleteDemo {
    public static void main(String[] args) {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        });
        System.out.println("main1");
        CompletableFuture<Integer> f2 = f1.whenCompleteAsync((integer, throwable) -> {
            System.out.println(integer);
        });
        System.out.println("main2");
        f2.join();
    }
}
