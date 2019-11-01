package com.keehoo.completablefuture.demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用 exceptionally() 回调处理异常 exceptionally()回调给你一个从原始Future中生成的错误恢复的机会。你可以在这里记录这个异常并返回一个默认值。
 */
public class ExceptionallyDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        exceptionally();
        CompletableFuture<Integer> one = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1执行");
            return 1;
        });
        CompletableFuture<Integer> two = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("2执行");
//            throw new RuntimeException("2");
            return 2;
        });
        CompletableFuture<Integer> three = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("3执行");
            throw new RuntimeException("3");
//            return 3;
        });
//        CompletableFuture<Void> four = CompletableFuture.allOf(one, two, three).thenRunAsync(() -> {
//            try {
//                TimeUnit.MILLISECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("4执行");
//        }).exceptionally(e -> {
//            System.out.println(e);
//            return null;
//        });
//        System.out.println("main");
//        four.join();
        CompletableFuture<Void> voidCompletableFuture = two.thenRunAsync(() -> System.out.println("123123"));
        CompletableFuture.allOf(one, two, three,voidCompletableFuture).exceptionally(e-> null).join();
        System.out.println(one.isCompletedExceptionally());
        System.out.println(two.isCompletedExceptionally());
        System.out.println(three.isCompletedExceptionally());
        two.exceptionally(e -> {
            System.out.println(e);
            return null;
        });
        three.exceptionally(e -> {
            System.out.println(e);
            return null;
        });
//        System.out.println(one.getNow(null));
//        System.out.println(two.getNow(null));
//        System.out.println(three.getNow(null));

    }

    private static void exceptionally() throws InterruptedException, ExecutionException {
        Integer age = -1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if(age < 0) {
                throw new IllegalArgumentException("Age can not be negative");
            }
            if(age > 18) {
                return "Adult";
            } else {
                return "Child";
            }
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return "Unknown!";
        });

        System.out.println("Maturity : " + maturityFuture.get());
    }
}
