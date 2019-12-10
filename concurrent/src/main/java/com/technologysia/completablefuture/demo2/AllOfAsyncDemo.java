package com.technologysia.completablefuture.demo2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 当所有的阶段都完成后异步地创建一个阶段
 * 使用thenApplyAsync()替换那些单个的CompletableFutures的方法，allOf()会在通用池中的线程中异步地执行。所以我们需要调用join方法等待它完成。
 */
public class AllOfAsyncDemo {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(String::toUpperCase))
                .collect(Collectors.toList());
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> System.out.println("1-"+cf.getNow(null)));
                    result.append("done");
                });
        allOf.join();
        System.out.println(result);
    }
}
