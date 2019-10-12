package com.keehoo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 使用 handle() 方法处理异常 API提供了一个更通用的方法 - handle()从异常恢复，无论一个异常是否发生它都会被调用。
 * 如果异常发生，res参数将是 null，否则，ex将是 null。
 */
public class HandleDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Integer age = 1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if(age < 0) {
                throw new IllegalArgumentException("Age can not be negative");
            }
            if(age > 18) {
                return "Adult";
            } else {
                return "Child";
            }
        }).handle((res, ex) -> {
            if(ex != null) {
                System.out.println("Oops! We have an exception - " + ex.getMessage());
                return "Unknown!";
            }
            System.out.println(res);
            return res;
        });

        System.out.println("Maturity : " + maturityFuture.get());

    }
}
