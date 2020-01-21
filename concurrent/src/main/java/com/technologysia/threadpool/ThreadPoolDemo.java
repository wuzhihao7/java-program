package com.technologysia.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 反面教材，线程池资源耗尽，线程全部阻塞
 */
public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, TimeoutException, ExecutionException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("consumer-queue-thread-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(2, 10, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(128),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        new Thread(() -> {
            ThreadPoolExecutor tpe = ((ThreadPoolExecutor) pool);
            while (true) {
                System.out.println();

                int queueSize = tpe.getQueue().size();
                System.out.println("当前排队线程数：" + queueSize);

                int activeCount = tpe.getActiveCount();
                System.out.println("当前活动线程数：" + activeCount);

                long completedTaskCount = tpe.getCompletedTaskCount();
                System.out.println("执行完成线程数：" + completedTaskCount);

                long taskCount = tpe.getTaskCount();
                System.out.println("总线程数：" + taskCount);

                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new ThreadStat("主方法", Thread.currentThread()).start();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> async(pool), pool);
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> async2(pool), pool);

        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);
        all.join();


//        while (true){
//            if(all.isDone()) break;
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("全部完成");

        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("等待...");
        }

    }

    private static void async(ExecutorService pool) {
        new ThreadStat("1-async", Thread.currentThread()).start();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            new ThreadStat("1-async,1",Thread.currentThread()).start();
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("1-模拟耗时1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool);
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            new ThreadStat("1-async,2",Thread.currentThread()).start();
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("1-模拟耗时2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool);

        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);
//        while (true){
//            if(all.isDone()) break;
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("1-异步完成");
    }


    private static void async2(ExecutorService pool) {
        new ThreadStat("2-async",Thread.currentThread()).start();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            new ThreadStat("2-async,1",Thread.currentThread()).start();
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("2-模拟耗时1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool);
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            new ThreadStat("2-async,2",Thread.currentThread()).start();
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("2-模拟耗时2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool);

        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);

//        while (true){
//            if(all.isDone()) break;
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("2-异步完成");
    }
}

class ThreadStat extends Thread{
    private String trace;
    private Thread thread;

    public ThreadStat(String trace, Thread thread){
        this.trace = trace;
        this.thread = thread;
    }

    @Override
    public void run() {
        while (true){
            System.err.println(trace+":"+thread.getName() +"---"+ thread.getState());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
