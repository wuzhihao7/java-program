package com.technologysia.completablefuture.demo3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo {
    private static Executor executor = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {
        System.out.println("main begin");
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> test11(), executor);
        CompletableFuture<Void> t2 = CompletableFuture.runAsync(() -> test12(), executor);
        CompletableFuture.allOf(t1, t2).whenComplete((aVoid, throwable) -> {
            System.out.println("done");
        });
        System.out.println("main over");
    }

    public static void test11(){
        CompletableFuture.runAsync(() -> test111(), executor).whenComplete((aVoid, throwable) -> {});
        System.out.println("test11");
    }

    public static void test12(){
        CompletableFuture.runAsync(() -> test121(), executor).whenComplete((aVoid, throwable) -> {});
        System.out.println("test12");
    }

    public static void test111(){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test111");
    }

    public static void test121(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test121");
    }

}
