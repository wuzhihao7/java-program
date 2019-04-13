# 线程安全
## 避免死锁的情况

1. 避免一个线程同时获得多个锁
2. 避免一个线程在锁内部占有多个资源，尽量保证每个锁只占用一个资源
3. 尝试使用定时锁，使用Lock.tryLock(timeout)，当超时等待时当前线程不会阻塞
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

创建线程之后，调用start()方法开始运行，当调用wait()，join()， LockSupport.lock()方法线程会进入到**WAITING**状态，而同样的wait(timeout)，sleep(long)，join()，LockSupport.parkNanos()，LockSupport.parkUtil()增加了超时等待功能，也就是调用这些方法后线程会进入**TIMED_WAITING**状态，当超时等待时间到达后，线程会切换到**RUNNABLE**状态，另外当**WAITING**和**TIMED_WAITING**状态时可以通过Object.notify()，Object.notifyAll()方法使线程转换到**RUNNABLE**状态。当线程出现资源竞争时，即等待获取锁的时候，线程会进入到**BLOCKED**阻塞状态，当线程获取锁时，线程进入到**RUNNABLE**状态。线程运行结束后，线程进入到**TERMINATED**状态。

当线程进入到synchronized方法或代码块时线程切换到的是**BLOCKED**状态，而使用java.util.concurrent.locks下Lock进行加锁的时候线程切换到的是**WAITING**或**TIMED_WAITING**状态，因为Lock会调用LockSupport的方法。

|   状态名称    |                             说明                             |
| :-----------: | :----------------------------------------------------------: |
|      NEW      |       初始状态，线程被构建，但是还没有调用start()方法        |
|   RUNNABLE    | 运行状态，Java线程将操作系统中的就绪和运行两种状态笼统地称作“运行中” |
|    BLOCKED    |                  阻塞状态，表示线程阻塞于锁                  |
|    WAITING    | 等待状态，表示线程进入等等待状态，进入该状态表示当前线程需要等待其他线程做出一些特定动作(通知或者中断) |
| TIMED_WAITING | 超时等待状态，该状态不同于WAITING，它是可以在指定的时间自行返回的 |
|  TERMINATED   |              终止状态，表示当前线程已经执行完毕              |

### 线程状态的基本操作

#### interrupted

中断表示一个运行中的线程是否被其他线程进行了中断操作。

中断操作可以看做线程间一种简便的交互方式。一般在**结束线程时通过中断标志位方式可以有机会去清理资源，相对于武断而直接的结束线程，这种方式要优雅和安全。**

当抛出InterruptedException的时候，会清除中断标志位，也就是说isInterrupted()方法会返回false。

| 方法名                              | 详细解释                                           | 备注                                                         |
| :---------------------------------- | :------------------------------------------------- | :----------------------------------------------------------- |
| public void interrupt()             | 中断一个正处于阻塞状态的线程，将中断标志位置为true | 如果该线程被调用了Object.wait()/Object.wait(long)，或者被调用Sleep(long),join(long)方法时会抛出InterruptedException并且中断标志位会被清除 |
| public boolean isInterrupted()      | 该线程是否被中断                                   | 中断标志位不会被清除                                         |
| public static boolean interrupted() | 当前线程是否被中断                                 | 中断标志位会被清除                                           |

