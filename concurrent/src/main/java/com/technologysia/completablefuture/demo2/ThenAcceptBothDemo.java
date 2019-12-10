package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 使用BiConsumer处理两个阶段的结果
 *
 */
public class ThenAcceptBothDemo {
    public static void main(String[] args) {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).thenAcceptBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                (s1, s2) -> result.append(s1 + s2));
        System.out.println(result);
    }
}
