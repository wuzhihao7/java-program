package com.keehoo.completablefuture.demo2;

import javax.print.DocFlavor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

/**
 * 首先我们创建了一个CompletableFuture, 完成后返回一个字符串message,接着我们调用thenApplyAsync方法，它返回一个CompletableFuture。这个方法在第一个函数完成后，异步地应用转大写字母函数。
 *
 * 这个例子还演示了如何通过delayedExecutor(timeout, timeUnit)延迟执行一个异步任务。
 *
 * 我们创建了一个分离的handler阶段： exceptionHandler， 它处理异常异常，在异常情况下返回message upon cancel。
 *
 * 下一步我们显式地用异常完成第二个阶段。 在阶段上调用join方法，它会执行大写转换，然后抛出CompletionException（正常的join会等待1秒，然后得到大写的字符串。不过我们的例子还没等它执行就完成了异常）， 然后它触发了handler阶段。
 */
public class ExceptionallyDemo {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase,
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
        CompletableFuture exceptionHandler = cf.handle((s, th) -> { return (th != null) ? "message upon cancel" : ""; });
        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        System.out.println("1-"+cf.isCompletedExceptionally());
        try {
            System.out.println("2-"+cf.join());
        } catch(CompletionException ex) { // just for testing
            System.out.println("3-"+ex.getCause().getMessage());
        }

        System.out.println("4-"+exceptionHandler.join());

    }
}
