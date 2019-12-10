package com.technologysia.completablefuture.demo1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建CompletableFuture
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        //获取异步结果，get() 方法会一直阻塞直到 Future 完成。因此，以下的调用将被永远阻塞，因为该Future一直不会完成。
//        System.out.println("1-"+completableFuture.get());
        //可以使用 CompletableFuture.complete() 手工的完成一个 Future,
        //所有等待这个 Future 的客户端都将得到一个指定的结果，并且 completableFuture.complete() 之后的调用将被忽略。
        completableFuture.complete("complete");
        System.out.println("2-"+completableFuture.get());
    }
}
