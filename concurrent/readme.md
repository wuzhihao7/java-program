# 线程安全
## 避免死锁的情况

1. 避免一个线程同时获得多个锁
2. 避免一个线程在锁内部占有多个资源，尽量保证每个锁只占用一个资源
3. 尝试使用定时锁，使用Lock.tryLock(timeOut)，当超时等待时当前线程不会阻塞
4. 对于数据库锁，加锁和解锁必须在同一个数据库连接里，否则会出现解锁失败的情况

## 线程的状态转换和基本操作

### 创建线程

1. 继承Thread，重写run()方法
2. 实现Runnable接口
3. 实现Callable接口

```java
package com.keehoo.thread;

import java.util.concurrent.*;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/13
 */
public class CreateThreadDemo {
    public static void main(String[] args) {
//        thread();
//        runnable();
//        callable();
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

```

### 线程的状态转换

![Ã§ÂºÂ¿Ã§Â¨ÂÃ§ÂÂ¶Ã¦ÂÂÃ¨Â½Â¬Ã¦ÂÂ¢Ã¥ÂÂ¾](https://www.javazhiyin.com/wp-content/uploads/2018/07/ac415236728725ea554a9f1721a87822.png)