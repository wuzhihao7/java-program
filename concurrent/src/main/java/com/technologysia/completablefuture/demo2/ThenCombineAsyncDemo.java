package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 异步使用BiFunction处理两个阶段的结果
 * 类似上面的例子，但是有一点不同： 依赖的前两个阶段异步地执行，所以thenCombine()也异步地执行，即时它没有Async后缀。
 *
 * Javadoc中有注释：
 *
 * Actions supplied for dependent completions of non-async methods may be performed by the thread that completes the current CompletableFuture, or by any other caller of a completion method
 *
 * 所以我们需要join方法等待结果的完成。
 */
public class ThenCombineAsyncDemo {
    public static void main(String[] args) {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(String::toUpperCase)
                .thenCombine(CompletableFuture.completedFuture(original).thenApplyAsync(String::toLowerCase),
                        (s1, s2) -> s1 + s2);
        System.out.println(cf.join());
    }
}
