package com.keehoo.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class CustomThreadPoolDemo {
    public static void main(String[] args) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("wallet-statistic-pool-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(4), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        pool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("before");
        pool.shutdown();
        System.out.println("after");
        try {
            while (!pool.awaitTermination(1, TimeUnit.SECONDS)){
                System.out.println("wait shundown");
            }
            System.out.println("end");
        } catch (InterruptedException e) {


        }
    }
}
