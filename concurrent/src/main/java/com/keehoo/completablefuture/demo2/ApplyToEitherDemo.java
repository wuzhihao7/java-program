package com.keehoo.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 在两个完成的阶段其中之一上应用函数
 * 下面的例子创建了CompletableFuture, applyToEither处理两个阶段， 在其中之一上应用函数(包保证哪一个被执行)。 本例中的两个阶段一个是应用大写转换在原始的字符串上， 另一个阶段是应用小些转换。
 */
public class ApplyToEitherDemo {
    public static void main(String[] args) {
        String original = "Message";
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return s.toUpperCase();
                });
        CompletableFuture<String> cf2 = cf1.applyToEither(
                CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return s.toLowerCase();
                }),
                s -> s + " from applyToEither");
        System.out.println(cf2.join());
    }
}
