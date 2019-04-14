# 线程安全

线程安全：当多个线程访问同一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替运行，也不需要进行额外的同步，或者在调用方进行任何其他的协调操作，调用这个对象的行为都可以获取正确的结果，那这个对象是线程安全的。

## 避免死锁的情况

1. 避免一个线程同时获得多个锁
2. 避免一个线程在锁内部占有多个资源，尽量保证每个锁只占用一个资源
3. 尝试使用定时锁，使用Lock.tryLock(timeout)，当超时等待时当前线程不会阻塞
4. 对于数据库锁，加锁和解锁必须在同一个数据库连接里，否则会出现解锁失败的情况

# 线程的状态转换和基本操作

## 创建线程

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

## 线程的状态转换

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

## 线程状态的基本操作

### interrupted

中断表示一个运行中的线程是否被其他线程进行了中断操作。

中断操作可以看做线程间一种简便的交互方式。一般在**结束线程时通过中断标志位方式可以有机会去清理资源，相对于武断而直接的结束线程，这种方式要优雅和安全。**

当抛出InterruptedException的时候，会清除中断标志位，也就是说isInterrupted()方法会返回false。

| 方法名                              | 详细解释                                           | 备注                                                         |
| :---------------------------------- | :------------------------------------------------- | :----------------------------------------------------------- |
| public void interrupt()             | 中断一个正处于阻塞状态的线程，将中断标志位置为true | 如果该线程被调用了Object.wait()/Object.wait(long)，或者被调用Sleep(long),join(long)方法时会抛出InterruptedException并且中断标志位会被清除 |
| public boolean isInterrupted()      | 该线程是否被中断                                   | 中断标志位不会被清除                                         |
| public static boolean interrupted() | 当前线程是否被中断                                 | 中断标志位会被清除                                           |

```java
package com.keehoo.thread;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/13
 */
public class InterruptDemo {
    public static void main(String[] args) {
        interrupt1();
        interrupt2();
        intettupt3();
        interrupt4();
    }

    /**
     * 开启了两个线程分别为sleepThread和BusyThread, sleepThread睡眠1s，BusyThread执行死循环。
     * 然后分别对着两个线程进行中断操作，可以看出sleepThread抛出InterruptedException后清除标志位，
     * 而busyThread就不会清除标志位
     */
    public static void interrupt1() {
        final Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        Thread busyThread = new Thread() {
            @Override
            public void run() {
                while (true) {

                }
            }
        };

        sleepThread.start();
        busyThread.start();
        sleepThread.interrupt();
        busyThread.interrupt();
        while (sleepThread.isInterrupted()) {
        }

        System.out.println("sleepThread isInterrupted: " + sleepThread.isInterrupted());
        System.out.println("busyThread isInterrupted: " + busyThread.isInterrupted());
    }

    /**
     * 中断阻塞状态的线程
     */
    public static void interrupt2() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("进入睡眠状态");
                    Thread.sleep(10000);
                    System.out.println("睡眠完毕");
                } catch (InterruptedException e) {
                    System.out.println("得到中断异常");
                }
                System.out.println("run方法执行完毕");
            }
        };
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public static void intettupt3() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                IntStream.range(0, Integer.MAX_VALUE).forEach(i -> {
                    if (!isInterrupted()) {
                        System.out.println(i);
                    } else {
                        System.out.println("线程被中断了");
                        System.exit(1);
                    }
                });
            }
        };
        thread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public static void interrupt4() {
        MyThread thread = new MyThread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.isStop(true);

    }
}

class MyThread extends Thread {
    private volatile boolean stop;

    @Override
    public void run() {
        IntStream.range(0, Integer.MAX_VALUE).forEach(i -> {
            if (!stop) {
                System.out.println(i);
            } else {
                System.out.println("线程被设置了停止");
                System.exit(1);
            }
        });
    }

    public void isStop(boolean stop) {
        this.stop = stop;
    }
}

```

### join

如果一个线程实例A执行了threadB.join(),其含义是：当前线程A会等待threadB线程终止后threadA才会继续执行。

方法如下：

```java
public final synchronized void join(long millis)
public final synchronized void join(long millis, int nanos)
public final void join() throws InterruptedException
```

```java
package com.keehoo.thread;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class JoinDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                System.out.println("进入线程" + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");
            }
        };
        System.out.println("进入线程" + Thread.currentThread().getName());
        thread.start();
        System.out.println("线程" + Thread.currentThread().getName()+ "等待");
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程" + Thread.currentThread().getName() + "继续执行");
    }
}

```



### sleep vs wait

两者区别：

1. sleep()方法是Thread静态方法，而wait()是Object实例方法
2. wait()方法必须要在同步方法或者同步块中调用，也就是必须已经获得对象锁。而sleep()方法没有这个限制可以在任何地方种使用。另外，wait()方法会释放占有的对象锁，使得该线程进入等待池中，等待下一次获取资源。而sleep()方法只是会让出CPU并不会释放掉对象锁；
3. sleep()方法在休眠时间达到后如果再次获得CPU时间片就会继续执行，而wait()方法必须等待Object.notift/Object.notifyAll通知后，才会离开等待池，并且再次获得CPU时间片才会继续执行。

```java
package com.keehoo.thread;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class SleepDemo {
    private Object obj = new Object();
    private int i = 10;
    public static void main(String[] args) {
        SleepDemo demo = new SleepDemo();
        MyThread t1 = demo.new MyThread();
        MyThread t2 = demo.new MyThread();
        t1.start();
        t2.start();
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            synchronized (obj){
                i++;
                System.out.println("i = " + i);
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "进入睡眠状态");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+Thread.currentThread().getName()+"睡眠结束");
                i++;
                System.out.println("i = " + i);
            }
        }
    }
}
```



### yield

调用yield方法会让当前线程交出CPU权限，让CPU去执行其他的线程。它跟sleep方法类似，同样不会释放锁。但是yield不能控制具体的交出CPU的时间，另外，yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。

注意，调用yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，这一点是和sleep方法不一样的。

```java
package com.keehoo.thread;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class YieldDemo {
    class Producer extends Thread {
        @Override
        public void run() {
            IntStream.range(0, 5).forEach(i -> {
                System.out.println("I am Producer: produced item " + i);
            });
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            IntStream.range(0, 5).forEach(i -> {
                System.out.println("I am Consumer: consumed item " + i);
            });
        }
    }

    public static void main(String[] args) {
        YieldDemo demo = new YieldDemo();
        Producer producer = demo.new Producer();
        Consumer consumer = demo.new Consumer();
        producer.setPriority(Thread.MIN_PRIORITY);
        consumer.setPriority(Thread.MAX_PRIORITY);
        producer.start();
        consumer.start();
    }
}

```

### daemon

守护线程是一种特殊的线程，就和它的名字一样，它是系统的守护者，在后台默默地守护一些系统服务，比如垃圾回收线程，JIT线程就可以理解守护线程。与之对应的就是用户线程，用户线程就可以认为是系统的工作线程，它会完成整个系统的业务操作。用户线程完全结束后就意味着整个系统的业务任务全部结束了，因此系统就没有对象需要守护的了，守护线程自然而然就会退。当一个Java应用，只有守护线程的时候，虚拟机就会自然退出。

守护线程和用户线程的区别在于：守护线程依赖于创建它的线程，而用户线程则不依赖。举个简单的例子：如果在main线程中创建了一个守护线程，当main方法运行完毕之后，守护线程也会随着消亡。而用户线程则不会，用户线程会一直运行直到其运行完毕。

**守护线程在退出的时候并不会执行finnaly块中的代码，所以将释放资源等操作不要放在finnaly块中执行，这种操作是不安全的**

线程可以通过setDaemon(true)的方法将线程设置为守护线程。并且需要注意的是设置守护线程要先于start()方法，否则会报

```java
> Exception in thread "main" java.lang.IllegalThreadStateException
> at java.lang.Thread.setDaemon(Thread.java:1365)
> at learn.DaemonDemo.main(DaemonDemo.java:19)
```

这样的异常，但是该线程还是会执行，只不过会当做正常的用户线程执行。

```java
package com.keehoo.thread;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class DaemonDemo {
    public static void main(String[] args) {
        daemon1();
        daemon2();
    }
    public static void daemon1() {
        Thread t = new Thread(() -> {
            while (true){
                System.out.println("I'm alive");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("finally block");
                }
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void daemon2(){
        DaemonDemo demo = new DaemonDemo();
        MyThread t1 = demo.new MyThread();
        MyThread t2 = demo.new MyThread();
        t1.setName("关羽");
        t2.setName("张飞");
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        Thread.currentThread().setName("刘备");
        IntStream.range(0,5).forEach(i -> System.out.println(Thread.currentThread().getName()+": "+i));
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            IntStream.range(0,100).forEach(i -> System.out.println(getName() + ": " + i));
        }
    }
}

```

# Java内存模型以及happens-before规则

## 内存模型抽象结构

并发编程中主要需要解决两个问题：**1. 线程之间如何通信；2.线程之间如何完成同步**（这里的线程指的是并发执行的活动实体）。通信是指线程之间以何种机制来交换信息，主要有两种：共享内存和消息传递。这里，可以分别类比上面的两个举例。java内存模型是**共享内存的并发模型**，线程之间主要通过读-写共享变量来完成隐式通信。如果程序员不能理解Java的共享内存模型在编写并发程序时一定会遇到各种各样关于内存可见性的问题。

> 1.哪些是共享变量

在java程序中所有**实例域，静态域和数组元素**都是放在堆内存中（所有线程均可访问到，是可以共享的），而局部变量，方法定义参数和异常处理器参数不会在线程间共享。共享数据会出现线程安全的问题，而非共享数据不会出现线程安全的问题。

> 2.JMM抽象结构模型

CPU的处理速度和主存的读写速度不是一个量级的，为了平衡这种巨大的差距，每个CPU都会有缓存。因此，共享变量会先放在主存中，每个线程都有属于自己的工作内存，并且会把位于主存中的共享变量拷贝到自己的工作内存，之后的读写操作均使用位于工作内存的变量副本，并在某个时刻将工作内存的变量副本写回到主存中去。JMM就从抽象层次定义了这种方式，并且JMM决定了一个线程对共享变量的写入何时对其他线程是可见的。

> 2.JMM抽象结构模型

我们知道CPU的处理速度和主存的读写速度不是一个量级的，为了平衡这种巨大的差距，每个CPU都会有缓存。因此，共享变量会先放在主存中，每个线程都有属于自己的工作内存，并且会把位于主存中的共享变量拷贝到自己的工作内存，之后的读写操作均使用位于工作内存的变量副本，并在某个时刻将工作内存的变量副本写回到主存中去。JMM就从抽象层次定义了这种方式，并且JMM决定了一个线程对共享变量的写入何时对其他线程是可见的。

> 2.JMM抽象结构模型

我们知道CPU的处理速度和主存的读写速度不是一个量级的，为了平衡这种巨大的差距，每个CPU都会有缓存。因此，共享变量会先放在主存中，每个线程都有属于自己的工作内存，并且会把位于主存中的共享变量拷贝到自己的工作内存，之后的读写操作均使用位于工作内存的变量副本，并在某个时刻将工作内存的变量副本写回到主存中去。JMM就从抽象层次定义了这种方式，并且JMM决定了一个线程对共享变量的写入何时对其他线程是可见的。

![JMMÃ¥ÂÂÃ¥Â­ÂÃ¦Â¨Â¡Ã¥ÂÂÃ§ÂÂÃ¦ÂÂ½Ã¨Â±Â¡Ã§Â"ÂÃ¦ÂÂÃ§Â¤ÂºÃ¦ÂÂÃ¥ÂÂ¾](https://www.javazhiyin.com/wp-content/uploads/2018/07/b5f6a55fb85932180d3ba47bb807b96b.png)

如图为JMM抽象示意图，线程A和线程B之间要完成通信的话，要经历如下两步：
1. 线程A从主内存中将共享变量读入线程A的工作内存后并进行操作，之后将数据重新写回到主内存中；
2. 线程B从主存中读取最新的共享变量

从横向去看看，线程A和线程B就好像通过共享变量在进行隐式通信。这其中有很有意思的问题，如果线程A更新后数据并没有及时写回到主存，而此时线程B读到的是过期的数据，这就出现了“脏读”现象。可以通过同步机制（控制不同线程间操作发生的相对顺序）来解决或者通过volatile关键字使得每次volatile变量都能够强制刷新到主存，从而对每个线程都是可见的。

## 重排序

**为了提高性能，编译器和处理器常常会对指令进行重排序**。一般重排序可以分为如下三种：

![Ã¤Â"ÂÃ¦ÂºÂÃ§Â ÂÃ¥ÂÂ°Ã¦ÂÂÃ§Â"ÂÃ¦ÂÂ§Ã¨Â¡ÂÃ§ÂÂÃ¦ÂÂÃ¤Â"Â¤Ã¥ÂºÂÃ¥ÂÂÃ§ÂÂÃ§Â¤ÂºÃ¦ÂÂÃ¥ÂÂ¾](https://www.javazhiyin.com/wp-content/uploads/2018/07/aed4130ded077aa6782f49dd3bbd0ffd.png)

1. 编译器优化的重排序。编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序；
2. 指令级并行的重排序。现代处理器采用了指令级并行技术来将多条指令重叠执行。如果**不存在数据依赖性**，处理器可以改变语句对应机器指令的执行顺序；
3. 内存系统的重排序。由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上去可能是在乱序执行的。

如图，1属于编译器重排序，而2和3统称为处理器重排序。

**针对编译器重排序**，JMM的编译器重排序规则会禁止一些**特定类型的编译器重排序**；**针对处理器重排序**，编译器在生成指令序列的时候会通过**插入内存屏障指令来禁止某些特殊的处理器重排序**。

**如果两个操作访问同一个变量，且这两个操作有一个为写操作，此时这两个操作就存在数据依赖性**这里就存在三种情况：1. 读后写；2.写后写；3. 写后读，者三种操作都是存在数据依赖性的，如果重排序会对最终执行结果会存在影响。**编译器和处理器在重排序时，会遵守数据依赖性，编译器和处理器不会改变存在数据依赖性关系的两个操作的执行顺序**

**as-if-serial**语义的意思是：不管怎么重排序（编译器和处理器为了提供并行度），（单线程）程序的执行结果不能被改变。编译器，runtime和处理器都必须遵守as-if-serial语义。as-if-serial语义把单线程程序保护了起来，**遵守as-if-serial语义的编译器，runtime和处理器共同为编写单线程程序的程序员创建了一个幻觉：单线程程序是按程序的顺序来执行的**。

## happens-before规则

### happens-before定义

**JMM可以通过happens-before关系向程序员提供跨线程的内存可见性保证**（如果A线程的写操作a与B线程的读操作b之间存在happens-before关系，尽管a操作和b操作在不同的线程中执行，但JMM向程序员保证a操作将对b操作可见）。具体的定义为：

1. 如果一个操作happens-before另一个操作，那么第一个操作的执行结果将对第二个操作可见，而且第一个操作的执行顺序排在第二个操作之前。

2. 两个操作之间存在happens-before关系，并不意味着Java平台的具体实现必须要按照happens-before关系指定的顺序来执行。如果重排序之后的执行结果，与按happens-before关系来执行的结果一致，那么这种重排序并不非法（也就是说，JMM允许这种重排序）。

上面的**1是JMM对程序员的承诺**。从程序员的角度来说，可以这样理解happens-before关系：如果A happens-before B，那么Java内存模型将向程序员保证——A操作的结果将对B可见，且A的执行顺序排在B之前。注意，这只是Java内存模型向程序员做出的保证！

上面的**2是JMM对编译器和处理器重排序的约束原则**。正如前面所言，JMM其实是在遵循一个基本原则：只要不改变程序的执行结果（指的是单线程程序和正确同步的多线程程序），编译器和处理器怎么优化都行。JMM这么做的原因是：程序员对于这两个操作是否真的被重排序并不关心，程序员关心的是程序执行时的语义不能被改变（即执行结果不能被改变）。因此，happens-before关系本质上和as-if-serial语义是一回事。

### **as-if-serial VS happens-before**

1. as-if-serial语义保证单线程内程序的执行结果不被改变，happens-before关系保证正确同步的多线程程序的执行结果不被改变。
2. as-if-serial语义给编写单线程程序的程序员创造了一个幻境：单线程程序是按程序的顺序来执行的。happens-before关系给编写正确同步的多线程程序的程序员创造了一个幻境：正确同步的多线程程序是按happens-before指定的顺序来执行的。
3. as-if-serial语义和happens-before这么做的目的，都是为了在不改变程序执行结果的前提下，尽可能地提高程序执行的并行度。

### 具体规则

1. 程序顺序规则：一个线程中的每个操作，happens-before于该线程中的任意后续操作。
2. 监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。
3. volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。
4. 传递性：如果A happens-before B，且B happens-before C，那么A happens-before C。
5. start()规则：如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作。
6. join()规则：如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回。
7. 程序中断规则：对线程interrupted()方法的调用先行于被中断线程的代码检测到中断时间的发生。
8. 对象finalize规则：一个对象的初始化完成（构造函数执行结束）先行于发生它的finalize()方法的开始。

# 彻底理解synchronized

## synchronized实现原理

| 分类   | 具体分类     | 被锁的对象   | 伪代码                                     |
| ------ | ------------ | ------------ | ------------------------------------------ |
| 方法   | 实例方法     | 类的实例对象 | public synchronized void method(){}        |
|        | 静态方法     | 类对象       | public static synchronized void method(){} |
| 代码块 | 实例对象     | 类的实例对象 | synchronized(this){}                       |
|        | Class对象    | 类对象       | Synchronized(Object.class){}               |
|        | 任意实例对象 | 实例对象     | String lock = ""; synchronized(lock){}     |

## 对象锁(monitor)机制

执行同步代码块后首先要先执行**monitorenter**指令，退出的时候**monitorexit**指令。通过分析之后可以看出，使用Synchronized进行同步，其关键就是必须要对对象的监视器monitor进行获取，当线程获取monitor后才能继续往下执行，否则就只能等待。而这个获取的过程是**互斥**的，即同一时刻只有一个线程能够获取到monitor。

**锁的重入性**，即在同一锁程中，线程不需要再次获取同一把锁。Synchronized先天具有重入性。**每个对象拥有一个计数器，当线程获取该对象锁后，计数器就会加一，释放锁后就会将计数器减一**。

任意一个对象都拥有自己的监视器，当这个对象由同步块或者这个对象的同步方法调用时，执行方法的线程必须先获取该对象的监视器才能进入同步块和同步方法，如果没有获取到监视器的线程将会被阻塞在同步块和同步方法的入口处，进入到BLOCKED状态。

下图表现了对象，对象监视器，同步队列以及执行线程状态之间的关系：

![Ã¥Â¯Â¹Ã¨Â±Â¡Ã¯Â¼ÂÃ¥Â¯Â¹Ã¨Â±Â¡Ã§ÂÂÃ¨Â§ÂÃ¥ÂÂ¨Ã¯Â¼ÂÃ¥ÂÂÃ¦Â­Â¥Ã©ÂÂÃ¥ÂÂÃ¥ÂÂÃ§ÂºÂ¿Ã§Â¨ÂÃ§ÂÂ¶Ã¦ÂÂÃ§ÂÂÃ¥ÂÂ³Ã§Â³Â"](https://www.javazhiyin.com/wp-content/uploads/2018/07/e1798acc527d146e408520ea31d4e3df.png)

## synchronized优化

同一时刻只有一个线程能够获得对象的监视器（monitor），从而进入到同步代码块或者同步方法之中，即表现为**互斥性（排它性）**。这种方式肯定效率低下，每次只能通过一个线程

### CAS操作

使用锁时，线程获取锁是一种**悲观锁策略**，即假设每一次执行临界区代码都会产生冲突，所以当前线程获取到锁的时候同时也会阻塞其他线程获取该锁。而CAS操作（又称为无锁操作）是一种**乐观锁策略**，它假设所有线程访问共享资源的时候不会出现冲突，既然不会出现冲突自然而然就不会阻塞其他线程的操作。因此，线程就不会出现阻塞停顿的状态。

无锁操作是使用**CAS(compare and swap)**又叫做比较交换来鉴别线程是否出现冲突，出现冲突就重试当前操作直到没有冲突为止。

#### CAS的操作过程

CAS比较交换的过程可以通俗的理解为CAS(V,O,N)，包含三个值分别为：**V 内存地址存放的实际值；O 预期的值（旧值）；N 更新的新值**。当V和O相同时，也就是说旧值和内存中实际的值相同表明该值没有被其他线程更改过，即该旧值O就是目前来说最新的值了，自然而然可以将新值N赋值给V。反之，V和O不相同，表明该值已经被其他线程改过了则该旧值O不是最新版本的值了，所以不能将新值N赋给V，返回V即可。当多个线程使用CAS操作一个变量时，只有一个线程会成功，并成功更新，其余会失败。失败的线程会重新尝试，当然也可以选择挂起线程

>  Synchronized VS CAS

元老级的Synchronized(未优化前)最主要的问题是：在存在线程竞争的情况下会出现线程阻塞和唤醒锁带来的性能问题，因为这是一种互斥同步（阻塞同步）。而CAS并不是武断的间线程挂起，当CAS操作失败后会进行一定的尝试，而非进行耗时的挂起唤醒的操作，因此也叫做非阻塞同步。这是两者主要的区别

#### CAS的问题

1. ABA问题

因为CAS会检查旧值有没有变化，这里存在这样一个有意思的问题。比如一个旧值A变为了成B，然后再变成A，刚好在做CAS时检查发现旧值并没有变化依然为A，但是实际上的确发生了变化。解决方案可以沿袭数据库中常用的乐观锁方式，添加一个版本号可以解决。原来的变化路径A->B->A就变成了1A->2B->3C。java这么优秀的语言，当然在java 1.5后的atomic包中提供了AtomicStampedReference来解决ABA问题，解决思路就是这样的。

2. 自旋时间过长

使用CAS时非阻塞同步，也就是说不会将线程挂起，会自旋（无非就是一个死循环）进行下一次尝试，如果这里自旋时间过长对性能是很大的消耗。如果JVM能支持处理器提供的pause指令，那么在效率上会有一定的提升。

3. 只能保证一个共享变量的原子操作

当对一个共享变量执行操作时CAS能保证其原子性，如果对多个共享变量进行操作,CAS就不能保证其原子性。有一个解决方案是利用对象整合多个共享变量，即一个类中的成员变量就是这几个共享变量。然后将这个对象做CAS操作就可以保证其原子性。atomic中提供了AtomicReference来保证引用对象之间的原子性。

### Java对象头

在同步的时候是获取对象的monitor,即获取到对象的锁。那么对象的锁怎么理解？无非就是类似对对象的一个标志，那么这个标志就是存放在Java对象的对象头。Java对象头里的Mark Word里默认的存放的对象的Hashcode,分代年龄和锁标记位。32为JVM Mark Word默认存储结构为：

![Mark Wordå­å¨ç»æ](https://www.javazhiyin.com/wp-content/uploads/2018/07/42bdfa7e349f12ae09138abeae3a4187.png)

如图在Mark Word会默认存放hasdcode，年龄值以及锁标志位等信息。

Java SE 1.6中，锁一共有4种状态，级别从低到高依次是：**无锁状态、偏向锁状态、轻量级锁状态和重量级锁状态**，这几个状态会随着竞争情况逐渐升级。**锁可以升级但不能降级**，意味着偏向锁升级成轻量级锁后不能降级成偏向锁。这种锁升级却不能降级的策略，目的是为了提高获得锁和释放锁的效率。对象的Mark Word变化为下图：

![Mark WordÃ§ÂÂ¶Ã¦ÂÂÃ¥ÂÂÃ¥ÂÂ](https://www.javazhiyin.com/wp-content/uploads/2018/07/50fa30186610c7f775b7bfe06c0df2bf.png)

#### 偏向锁

HotSpot的作者经过研究发现，大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，为了让线程获得锁的代价更低而引入了偏向锁。

> 获取

当一个线程访问同步块并获取锁时，会在**对象头**和**栈帧中的锁记录**里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的Mark Word里是否存储着指向当前线程的偏向锁。如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1（表示当前是偏向锁）：如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程

> 撤销

偏向锁使用了一种**等到竞争出现才释放锁**的机制，所以当其他线程尝试竞争偏向锁时，持有偏向锁的线程才会释放锁。

![Ã¥ÂÂÃ¥ÂÂÃ©ÂÂÃ¦ÂÂ¤Ã©ÂÂÃ¦ÂµÂÃ§Â¨Â](https://www.javazhiyin.com/wp-content/uploads/2018/07/7923465aa2e3419e545edf87f777e0a8.png)

如图，偏向锁的撤销，需要等待**全局安全点**（在这个时间点上没有正在执行的字节码）。它会首先暂停拥有偏向锁的线程，然后检查持有偏向锁的线程是否活着，如果线程不处于活动状态，则将对象头设置成无锁状态；如果线程仍然活着，拥有偏向锁的栈会被执行，遍历偏向对象的锁记录，栈中的锁记录和对象头的Mark Word要么重新偏向于其他线程，要么恢复到无锁或者标记对象不适合作为偏向锁，最后唤醒暂停的线程。

下图线程1展示了偏向锁获取的过程，线程2展示了偏向锁撤销的过程。

![ååéè·ååæ¤éæµç¨](https://www.javazhiyin.com/wp-content/uploads/2018/07/0e0cbb109c7abc98b25d76d22bf4d6bc.png)

> 如何关闭偏向锁

偏向锁在Java 6和Java 7里是默认启用的，但是它在应用程序启动几秒钟之后才激活，如有必要可以使用JVM参数来关闭延迟：**-XX:BiasedLockingStartupDelay=0**。如果你确定应用程序里所有的锁通常情况下处于竞争状态，可以通过JVM参数关闭偏向锁：**-XX:-UseBiasedLocking=false**，那么程序默认会进入轻量级锁状态

#### 轻量级锁

> 加锁

线程在执行同步块之前，JVM会先在当前线程的栈桢中**创建用于存储锁记录的空间**，并将对象头中的Mark Word复制到锁记录中，官方称为**Displaced Mark Word**。然后线程尝试使用CAS将对象头中的Mark Word替换为指向锁记录的指针。如果成功，当前线程获得锁，如果失败，表示其他线程竞争锁，当前线程便尝试使用自旋来获取锁。

> 解锁

轻量级解锁时，会使用原子的CAS操作将Displaced Mark Word替换回到对象头，如果成功，则表示没有竞争发生。如果失败，表示当前锁存在竞争，锁就会膨胀成重量级锁。下图是两个线程同时争夺锁，导致锁膨胀的流程图。

![Ã¨Â½Â"Ã©ÂÂÃ§ÂºÂ§Ã©ÂÂÃ¥ÂÂ Ã©ÂÂÃ¨Â§Â£Ã©ÂÂÃ¤Â"Â¥Ã¥ÂÂÃ©ÂÂÃ¨ÂÂ¨Ã¨ÂÂ](https://www.javazhiyin.com/wp-content/uploads/2018/07/a5cc5af77ebe2a37a84d08698cd856cc.png)

因为自旋会消耗CPU，为了避免无用的自旋（比如获得锁的线程被阻塞住了），一旦锁升级成重量级锁，就不会再恢复到轻量级锁状态。当锁处于这个状态下，其他线程试图获取锁时，都会被阻塞住，当持有锁的线程释放锁之后会唤醒这些线程，被唤醒的线程就会进行新一轮的夺锁之争。

#### 各种锁的比较

![Ã¥ÂÂÃ§Â§ÂÃ©ÂÂÃ§ÂÂÃ¥Â¯Â¹Ã¦Â¯Â](https://www.javazhiyin.com/wp-content/uploads/2018/07/9197438ffe1ae0cd7a56192f42864448.png)

