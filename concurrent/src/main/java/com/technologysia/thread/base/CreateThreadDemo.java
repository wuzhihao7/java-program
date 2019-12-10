package com.technologysia.thread.base;

import java.util.concurrent.*;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/13
 */
public class CreateThreadDemo {
    public static void main(String[] args) {
        thread();
        runnable();
        callable();
        futureTask();
    }

    /**
     * 继承Thread
     */
    public static void thread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                System.out.println("继承Thread");
                super.run();
            }
        };
        thread.start();
    }

    /**
     * 实现Runnable接口
     */
    public static void runnable(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("实现Runnable接口");
            }
        });
        thread.start();
    }

    /**
     * 实现Callable接口
     */
    public static void callable(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() {
                return "实现Callable接口";
            }
        });
        executorService.shutdown();
        try {
            String s = future.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void futureTask(){
        FutureTask<String> futureTask = new FutureTask<>(() -> "FutureTask");
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            String s = futureTask.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
