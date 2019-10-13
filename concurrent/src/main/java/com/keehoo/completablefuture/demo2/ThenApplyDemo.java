package com.keehoo.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 注意thenApply方法名称代表的行为。
 *
 * then意味着这个阶段的动作发生当前的阶段正常完成之后。本例中，当前节点完成，返回字符串message。
 *
 * Apply意味着返回的阶段将会对结果前一阶段的结果应用一个函数。
 *
 * 函数的执行会被阻塞，这意味着getNow()只有打斜操作被完成后才返回。
 */
public class ThenApplyDemo {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        System.out.println(cf.getNow(null));
    }
}
