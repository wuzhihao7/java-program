package com.keehoo.completablefuture.demo2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 当所有的阶段都完成后创建一个阶段
 *
 * 上一个例子是当任意一个阶段完成后接着处理，接下来的两个例子演示当所有的阶段完成后才继续处理, 同步地方式和异步地方式两种。
 */
public class AllOfDemo {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(String::toUpperCase))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).whenComplete((v, th) -> {
            futures.forEach(cf -> System.out.println("1-"+cf.getNow(null)));
            result.append("done");
        });
        System.out.println("2-"+result);
    }
}
