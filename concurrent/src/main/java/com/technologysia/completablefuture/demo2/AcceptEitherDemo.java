package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 在两个完成的阶段其中之一上调用消费函数
 * 和前一个例子很类似了，只不过我们调用的是消费者函数 (Function变成Consumer):
 */
public class AcceptEitherDemo {
    public static void main(String[] args) {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return s.toUpperCase();
                })
                .acceptEither(CompletableFuture.completedFuture(original).thenApplyAsync(String::toLowerCase),
                        s -> result.append(s).append(" acceptEither"));
        System.out.println(cf.join());
        System.out.println(result.toString());
    }
}
