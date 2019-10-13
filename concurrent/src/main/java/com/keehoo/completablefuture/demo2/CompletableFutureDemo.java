package com.keehoo.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 创建一个完成的CompletableFuture
 */
public class CompletableFutureDemo {
    public static void main(String[] args) {
        //最简单的例子就是使用一个预定义的结果创建一个完成的CompletableFuture,通常我们会在计算的开始阶段使用它。
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
        System.out.println(cf.isDone());
        //getNow(null)方法在future完成的情况下会返回结果，就比如上面这个例子，否则返回null (传入的参数)。
        System.out.println(cf.getNow(null));
    }
}
