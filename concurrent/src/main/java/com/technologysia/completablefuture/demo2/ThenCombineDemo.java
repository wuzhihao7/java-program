package com.technologysia.completablefuture.demo2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 使用BiFunction处理两个阶段的结果
 * <p>
 * 如果CompletableFuture依赖两个前面阶段的结果， 它复合两个阶段的结果再返回一个结果，我们就可以使用thenCombine()函数。整个流水线是同步的，所以getNow()会得到最终的结果，它把大写和小写字符串连接起来。
 */
public class ThenCombineDemo {
    public static void main(String[] args) {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(s -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return s.toUpperCase();
        })
                .thenCombine(CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                        (s1, s2) -> {
                            System.out.println("s1=" + s1);
                            System.out.println("s2=" + s2);
                            return s1 + s2;
                        });
        System.out.println(cf.getNow(null));
    }
}
