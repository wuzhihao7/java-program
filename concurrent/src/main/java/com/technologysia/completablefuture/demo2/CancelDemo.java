package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 和完成异常类似，我们可以调用cancel(boolean mayInterruptIfRunning)取消计算。
 * 对于CompletableFuture类，布尔参数并没有被使用，这是因为它并没有使用中断去取消操作，相反，cancel等价于completeExceptionally(new CancellationException())。
 */
public class CancelDemo {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase,
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
        CompletableFuture<String> cf2 = cf.exceptionally(throwable -> "canceled message");
        System.out.println(cf.cancel(true));
        System.out.println(cf.isCompletedExceptionally());
        System.out.println(cf2.join());
    }
}
