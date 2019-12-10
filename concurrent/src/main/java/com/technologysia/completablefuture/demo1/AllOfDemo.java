package com.technologysia.completablefuture.demo1;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * CompletableFuture.allOf的使用场景是当你一个列表的独立future，并且你想在它们都完成后并行的做一些事情。
 * 假设你想下载一个网站的100个不同的页面。你可以串行的做这个操作，但是这非常消耗时间。因此你想写一个函数，传入一个页面链接，返回一个CompletableFuture，异步的下载页面内容。
 */
public class AllOfDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // A list of 100 web page links
        List<String> webPageLinks = Arrays.asList("1", "2", "3");
        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(AllOfDemo::downloadWebPage)
                .collect(Collectors.toList());

        // Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                pageContentFutures.toArray(new CompletableFuture[0])
        );

        System.out.println("--------------");
        // When all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
        //当所有future完成的时候，我们调用了future.join()，因此我们不会在任何地方阻塞。
        //join()方法和get()方法非常类似，这唯一不同的地方是如果最顶层的CompletableFuture完成的时候发生了异常，它会抛出一个未经检查的异常。
        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> pageContentFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));

        System.out.println(allPageContentsFuture.get());

        System.out.println("---------------");
        CompletableFuture<Long> countFuture = allPageContentsFuture.thenApply(pageContents -> {
            return pageContents.stream()
                    .count();
        });

        System.out.println("Number of Web Pages having CompletableFuture keyword - " +
                countFuture.get());



    }

    static CompletableFuture<String> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("download...");
            // Code to download and return the web page's content
            return "page";
        });
    }
}
