package com.technologysia.completablefuture.demo1;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class AsyncDemo {
    public static void main(String[] args) throws IOException {
        CompletableFuture<Integer> f = new CompletableFuture<>();

        new Thread(() -> {
            // 子线程A启动
            System.out.println("子线程A启动");
            try {
                System.out.println("子线程A沉睡5s");
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程A令future完成");
            f.complete(100);  // 当子线程A执行到f.complete的时候，会去看是否有注册好的f的then或者when（非async的），如果有的话，会顺次去执行。
            System.out.println("子线程A结束");
        }).start();


        // 当前线程（主线程）执行到这里的时候，如果子线程还没有执行到f.complete(100)，
        // 那么当前线程会把whenComplete事件注册起来，并且说好哪个线程执行了f.complete(100)，
        // 哪个线程就负责执行whenComplete的内容。
        // 如果当前线程（主线程）执行到这里的时候，f.complete(100)已经被其他线程执行完毕了。
        // 那么只有当前线程自己来执行whenComplete里面的内容了。
        f.whenComplete((i, ex) -> {
            // 这个场景下，whenComplete的回调的执行线程会是子线程A
            System.out.println("do something after complete begin");
            try {
                System.out.println("沉睡10s");
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("do something after complete end");
        });
        System.out.println("main over");
    }
}
