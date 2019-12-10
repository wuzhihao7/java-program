package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;

/**
 * 组合 CompletableFuture
 *
 * 我们可以使用thenCompose()完成上面两个例子。这个方法等待第一个阶段的完成(大写转换)， 它的结果传给一个指定的返回CompletableFuture函数，它的结果就是返回的CompletableFuture的结果。
 *
 * 有点拗口，但是我们看例子来理解。函数需要一个大写字符串做参数，然后返回一个CompletableFuture, 这个CompletableFuture会转换字符串变成小写然后连接在大写字符串的后面。
 */
public class ThenComposeDemo {
    public static void main(String[] args) {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApply(String::toLowerCase)
                        .thenApply(s -> upper + s));
        System.out.println(cf.join());
    }
}
