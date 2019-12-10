package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 在两个阶段都执行完后运行一个 Runnable
 * 这个例子演示了依赖的CompletableFuture如果等待两个阶段完成后执行了一个Runnable。 注意下面所有的阶段都是同步执行的，第一个阶段执行大写转换，第二个阶段执行小写转换。
 */
public class RunAfterBothDemo {
    public static void main(String[] args) {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).runAfterBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                () -> result.append("done"));
        System.out.println(result);
    }
}
