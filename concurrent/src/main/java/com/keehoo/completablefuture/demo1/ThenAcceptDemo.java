package com.keehoo.completablefuture.demo1;

import java.util.concurrent.CompletableFuture;

/**
 *  thenAccept() 和 thenRun()
 * 如果你不想从你的回调函数中返回任何东西，仅仅想在Future完成后运行一些代码片段，你可以使用thenAccept()和 thenRun()方法，这些方法经常在调用链的最末端的最后一个回调函数中使用。
 * CompletableFuture.thenAccept()持有一个Consumer<T>，返回一个CompletableFuture<Void>。它可以访问CompletableFuture的结果：
 *
 */
public class ThenAcceptDemo {
    public static void main(String[] args) {
        // thenAccept() example
        CompletableFuture.supplyAsync(() -> {
            return "product";
        }).thenAccept(product -> {
            System.out.println("Got product detail from remote service: " + product);
        });

    }
}
