package com.keehoo.completablefuture;

import java.util.SortedMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 使用 thenCompose()组合两个独立的future
 * 假设你想从一个远程API中获取一个用户的详细信息，一旦用户信息可用，你想从另外一个服务中获取他的贷方。
 * 考虑下以下两个方法getUserDetail()和getCreditRating()的实现：
 *
 */
public class ThenComposeDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        thenApply();
        System.out.println("------------");
        thenCompose();
    }

    /**
     * 因此，规则就是-如果你的回调函数返回一个CompletableFuture，但是你想从CompletableFuture链中获取一个直接合并后的结果，这时候你可以使用thenCompose()。
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void thenCompose() throws InterruptedException, ExecutionException {
        CompletableFuture<Double> result = getUsersDetail("userId")
                .thenCompose(user -> getCreditRating(user));
        System.out.println(result.get());
    }

    /**
     * 在更早的示例中，Supplier函数传入thenApply将返回一个简单的值，但是在本例中，将返回一个CompletableFuture。以上示例的最终结果是一个嵌套的CompletableFuture。
     * 如果你想获取最终的结果给最顶层future，使用 thenCompose()方法代替-
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void thenApply() throws InterruptedException, ExecutionException {
        CompletableFuture<CompletableFuture<Double>> userId = getUsersDetail("userId")
                .thenApply(ThenComposeDemo::getCreditRating);
        System.out.println(userId.get().get());
        System.out.println(userId.get());
    }

    static CompletableFuture<String> getUsersDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("user");
            System.out.println(Thread.currentThread().getName());
            return "user";
        });
    }

    static CompletableFuture<Double> getCreditRating(String user) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("rating");
            System.out.println(Thread.currentThread().getName());
            return 1.0d;
        });
    }
}
