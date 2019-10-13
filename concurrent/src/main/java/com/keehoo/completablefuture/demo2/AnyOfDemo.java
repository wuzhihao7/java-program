package com.keehoo.completablefuture.demo2;

import java.security.interfaces.RSAKey;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 当几个阶段中的一个完成，创建一个完成的阶段
 *
 * 下面的例子演示了当任意一个CompletableFuture完成后， 创建一个完成的CompletableFuture.
 *
 * 待处理的阶段首先创建， 每个阶段都是转换一个字符串为大写。因为本例中这些阶段都是同步地执行(thenApply),
 * 从anyOf中创建的CompletableFuture会立即完成，这样所有的阶段都已完成，我们使用whenComplete(BiConsumer<? super Object, ? super Throwable> action)处理完成的结果。
 */
public class AnyOfDemo {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");

        CompletableFuture.anyOf(messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(String::toUpperCase)).toArray(CompletableFuture[]::new)).whenComplete((res, th) -> {
            if(th == null) {
                System.out.println("1-"+res);
                result.append(res);
            }
        });
        System.out.println("2-"+result);
    }
}
