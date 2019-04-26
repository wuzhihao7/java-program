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

# volatile

## volatile实现原理

在生成汇编代码时会在volatile修饰的共享变量进行写操作的时候会多出**Lock前缀的指令**。主要有这两个方面的影响：

1. 将当前处理器缓存行的数据写回系统内存；
2. 这个写回内存的操作会使得其他CPU里缓存了该内存地址的数据无效

为了提高处理速度，处理器不直接和内存进行通信，而是先将系统内存的数据读到内部缓存（L1，L2或其他）后再进行操作，但操作完不知道何时会写到内存。如果对声明了volatile的变量进行写操作，JVM就会向处理器发送一条Lock前缀的指令，将这个变量所在缓存行的数据写回到系统内存。但是，就算写回到内存，如果其他处理器缓存的值还是旧的，再执行计算操作就会有问题。所以，在多处理器下，为了保证各个处理器的缓存是一致的，就会实现**缓存一致性**协议，**每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期**了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器对这个数据进行修改操作的时候，会重新从系统内存中把数据读到处理器缓存里。因此，经过分析我们可以得出如下结论：

1. Lock前缀的指令会引起处理器缓存写回内存；
2. 一个处理器的缓存回写到内存会导致其他处理器的缓存失效；
3. 当处理器发现本地缓存失效后，就会从内存中重读该变量数据，即可以获取当前最新值。

这样针对volatile变量通过这样的机制就使得每个线程都能获得该变量的最新值。

## volatile的happens-before关系

**volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。**

## volatile的内存语义实现

> 内存屏障

![Ã¥ÂÂÃ¥Â­ÂÃ¥Â±ÂÃ©ÂÂÃ¥ÂÂÃ§Â±Â"Ã¨Â¡Â¨](https://www.javazhiyin.com/wp-content/uploads/2018/07/a5b9ac1845dc93a864aff3d8226b5b68.png)

java编译器会在生成指令系列时在适当的位置会插入内存屏障指令来禁止特定类型的处理器重排序。为了实现volatile的内存语义，JMM会限制特定类型的编译器和处理器重排序，JMM会针对编译器制定volatile重排序规则表：

![volatileÃ©ÂÂÃ¦ÂÂÃ¥ÂºÂÃ¨Â§ÂÃ¥ÂÂÃ¨Â¡Â¨](https://www.javazhiyin.com/wp-content/uploads/2018/07/2696ae90e7b02072063a35492d956201.png)

"NO"表示禁止重排序。为了实现volatile内存语义时，编译器在生成字节码时，会在指令序列中插入内存屏障来禁止特定类型的**处理器重排序**。对于编译器来说，发现一个最优布置来最小化插入屏障的总数几乎是不可能的，为此，JMM采取了保守策略：

1. 在每个volatile写操作的前面插入一个StoreStore屏障；
2. 在每个volatile写操作的后面插入一个StoreLoad屏障；
3. 在每个volatile读操作的后面插入一个LoadLoad屏障；
4. 在每个volatile读操作的后面插入一个LoadStore屏障。

需要注意的是：volatile写是在前面和后面**分别插入内存屏障**，而volatile读操作是在**后面插入两个内存屏障**

**StoreStore屏障**：禁止上面的普通写和下面的volatile写重排序；

**StoreLoad屏障**：防止上面的volatile写与下面可能有的volatile读/写重排序

**LoadLoad屏障**：禁止下面所有的普通读操作和上面的volatile读重排序

**LoadStore屏障**：禁止下面所有的普通写操作和上面的volatile读重排序

![volatileÃ¥ÂÂÃ¦ÂÂÃ¥ÂÂ¥Ã¥ÂÂÃ¥Â­ÂÃ¥Â±ÂÃ©ÂÂÃ§Â¤ÂºÃ¦ÂÂÃ¥ÂÂ¾](https://www.javazhiyin.com/wp-content/uploads/2018/07/498744fe0c99a6a4cd451cbf1cbe3980.png)

# final

## 变量

### final成员变量

通常每个类中的成员变量可以分为**类变量（static修饰的变量）以及实例变量**。针对这两种类型的变量赋初值的时机是不同的，类变量可以在声明变量的时候直接赋初值或者在静态代码块中给类变量赋初值。而实例变量可以在声明变量的时候给实例变量赋初值，在非静态初始化块中以及构造器中赋初值。类变量有**两个时机赋初值**，而实例变量则可以有**三个时机赋初值**。当final变量未初始化时系统不会进行隐式初始化，会出现报错。

1. **类变量**：必须要在**静态初始化块**中指定初始值或者**声明该类变量时**指定初始值，而且只能在这**两个地方**之一进行指定；
2. **实例变量**：必要要在**非静态初始化块**，**声明该实例变量**或者在**构造器中**指定初始值，而且只能在这**三个地方**进行指定。

### final局部变量

final局部变量由程序员进行显式初始化，如果final局部变量已经进行了初始化则后面就不能再次进行更改，如果final变量未进行初始化，可以进行赋值，**当且仅有一次**赋值，一旦赋值之后再次赋值就会出错。

>  **final基本数据类型 VS final引用数据类型**

**当final修饰基本数据类型变量时，不能对基本数据类型变量重新赋值，因此基本数据类型变量不能被改变。而对于引用类型变量而言，它仅仅保存的是一个引用，final只保证这个引用类型变量所引用的地址不会发生改变，即一直引用这个对象，但这个对象属性是可以改变的**

> **宏变量**

利用final变量的不可更改性，在满足一下三个条件时，该变量就会成为一个“宏变量”，即是一个常量。

1. 使用final修饰符修饰；
2. 在定义该final变量时就指定了初始值；
3. 该初始值在编译时就能够唯一指定。

注意：当程序中其他地方使用该宏变量的地方，编译器会直接替换成该变量的值

## 方法

### 重写

当父类的方法被final修饰的时候，子类不能重写父类的该方法，比如在Object中，getClass()方法就是final的，我们就不能重写该方法，但是hashCode()方法就不是被final所修饰的，我们就可以重写hashCode()方法。

### 重载

可以看出被final修饰的方法是可以重载的。经过我们的分析可以得出如下结论：

**1. 父类的final方法是不能够被子类重写的**

**2. final方法是可以被重载的**

## 类

**当一个类被final修饰时，表名该类是不能被子类继承的**。子类继承往往可以重写父类的方法和改变父类属性，会带来一定的安全隐患，因此，当一个类不希望被继承时就可以使用final修饰。

## 例子

final经常会被用作不变类上，利用final的不可更改性。

> 不可变类

不变类的意思是创建该类的实例后，该实例的实例变量是不可改变的。满足以下条件则可以成为不可变类：

1. 使用private和final修饰符来修饰该类的成员变量
2. 提供带参的构造器用于初始化类的成员变量
3. 仅为该类的成员变量提供getter方法，不提供setter方法，因为普通方法无法修改fina修饰的成员变量
4. 如果有必要就重写Object类 的hashCode()和equals()方法，应该保证用equals()判断相同的两个对象其hashCode值也是相等的。

## final域重排序规则

### final为基本类型

```java
package com.keehoo.thread;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class FinalDemo {
    private int a;
    private final int b;
    private static FinalDemo finalDemo;
    
    public FinalDemo(){
        a = 1;
        b = 2;
    }

    public static void write(){
        finalDemo = new FinalDemo();
    }

    public static void read(){
        FinalDemo demo = finalDemo;
        int a = demo.a;
        int b = demo.b;
    }
}

```



> **写final域重排序规则**

写final域的重排序规则**禁止对final域的写重排序到构造函数之外**，这个规则的实现主要包含了两个方面：

1. JMM禁止编译器把final域的写重排序到构造函数之外；
2. 编译器会在final域写之后，构造函数return之前，插入一个storestore屏障，这个屏障可以禁止处理器把final域的写重排序到构造函数之外。

由于a,b之间没有数据依赖性，普通域（普通变量）a可能会被重排序到构造函数之外，线程B就有可能读到的是普通变量a初始化之前的值（零值），这样就可能出现错误。而final域变量b，根据重排序规则，会禁止final修饰的变量b重排序到构造函数之外，从而b能够正确赋值，线程B就能够读到final变量初始化后的值。

写final域的重排序规则可以确保：**在对象引用为任意线程可见之前，对象的final域已经被正确初始化过了，而普通域就不具有这个保障**。

> **读final域重排序规则**

读final域重排序规则为：**在一个线程中，初次读对象引用和初次读该对象包含的final域，JMM会禁止这两个操作的重排序。**（注意，这个规则仅仅是针对处理器），处理器会在读final域操作的前面插入一个LoadLoad屏障。实际上，读对象的引用和读该对象的final域存在间接依赖性，一般处理器不会重排序这两个操作。但是有一些处理器会重排序，因此，这条禁止重排序规则就是针对这些处理器而设定的。

read()方法主要包含了三个操作：

1. 初次读引用变量finalDemo;
2. 初次读引用变量finalDemo的普通域a;
3. 初次读引用变量finalDemo的final与b;

读对象的普通域被重排序到了读对象引用的前面就会出现线程B还未读到对象引用就在读取该对象的普通域变量，这显然是错误的操作。而final域的读操作就“限定”了在读final域变量前已经读到了该对象的引用，从而就可以避免这种情况。

读final域的重排序规则可以确保：**在读一个对象的final域之前，一定会先读这个包含这个final域的对象的引用。**

### final为引用类型

> **对final修饰的对象的成员域写操作**

针对引用数据类型，final域写针对编译器和处理器重排序**增加了这样的约束**：在构造函数内对**一个final修饰的对象的成员域的写入，与随后在构造函数之外把这个被构造的对象的引用赋给一个引用变量**，这两个操作是不能被重排序的。注意这里的是“增加”也就说前面对final基本数据类型的重排序规则在这里还是使用。

```java
public class FinalReferenceDemo {
    final int[] arrays;
    private FinalReferenceDemo finalReferenceDemo;
 
    public FinalReferenceDemo() {
        arrays = new int[1];  //1
        arrays[0] = 1;        //2
    }
 
    public void writerOne() {
        finalReferenceDemo = new FinalReferenceDemo(); //3
    }
 
    public void writerTwo() {
        arrays[0] = 2;  //4
    }
 
    public void reader() {
        if (finalReferenceDemo != null) {  //5
            int temp = finalReferenceDemo.arrays[0];  //6
        }
    }
}
```

针对上面的实例程序，线程线程A执行wirterOne方法，执行完后线程B执行writerTwo方法，然后线程C执行reader方法。下图就以这种执行时序出现的一种情况来讨论（耐心看完才有收获）。

![Ã¥ÂÂfinalÃ¤Â¿Â®Ã©Â¥Â°Ã¥Â¼ÂÃ§ÂÂ¨Ã§Â±Â"Ã¥ÂÂÃ¦ÂÂ°Ã¦ÂÂ®Ã¥ÂÂ¯Ã¨ÂÂ½Ã§ÂÂÃ¦ÂÂ§Ã¨Â¡ÂÃ¦ÂÂ¶Ã¥ÂºÂ](https://www.javazhiyin.com/wp-content/uploads/2018/07/2749e7ea0c328162d632eb9c2d69c023.png)

由于对final域的写禁止重排序到构造方法外，因此1和3不能被重排序。由于一个final域的引用对象的成员域写入不能与随后将这个被构造出来的对象赋给引用变量重排序，因此2和3不能重排序。

> **对final修饰的对象的成员域读操作**

JMM可以确保线程C至少能看到写线程A对final引用的对象的成员域的写入，即能看下arrays[0] = 1，而写线程B对数组元素的写入可能看到可能看不到。JMM不保证线程B的写入对线程C可见，线程B和线程C之间存在数据竞争，此时的结果是不可预知的。如果可见的，可使用锁或者volatile。

> 为什么final引用不能从构造函数中“溢出” 

上面对final域写重排序规则可以确保我们在使用一个对象引用的时候该对象的final域已经在构造函数被初始化过了。但是这里其实是有一个前提条件的，也就是：**在构造函数，不能让这个被构造的对象被其他线程可见，也就是说该对象引用不能在构造函数中“逸出”**。以下面的例子来说：

```java
public class FinalReferenceEscapeDemo {
    private final int a;
    private FinalReferenceEscapeDemo referenceDemo;
 
    public FinalReferenceEscapeDemo() {
        a = 1;  //1
        referenceDemo = this; //2
    }
 
    public void writer() {
        new FinalReferenceEscapeDemo();
    }
 
    public void reader() {
        if (referenceDemo != null) {  //3
            int temp = referenceDemo.a; //4
        }
    }
}
```

可能执行的时序图：

![finalÃ¥ÂÂÃ¥Â¼ÂÃ§ÂÂ¨Ã¥ÂÂ¯Ã¨ÂÂ½Ã§ÂÂÃ¦ÂÂ§Ã¨Â¡ÂÃ¦ÂÂ¶Ã¥ÂºÂ](https://www.javazhiyin.com/wp-content/uploads/2018/07/0ee61e8b9f29ca568b81038782c842ba.png)

假设一个线程A执行writer方法另一个线程执行reader方法。因为构造函数中操作1和2之间没有数据依赖性，1和2可以重排序，先执行了2，这个时候引用对象referenceDemo是个没有完全初始化的对象，而当线程B去读取该对象时就会出错。尽管依然满足了final域写重排序规则：在引用对象对所有线程可见时，其final域已经完全初始化成功。但是，引用对象“this”逸出，该代码依然存在线程安全的问题。

# 三大性质总结：原子性、可见性以及有序性

## 原子性

原子性是指**一个操作是不可中断的，要么全部执行成功要么全部执行失败，有着“同生共死”的感觉**。及时在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程所干扰。

Java内存模型中定义了8种操作都是原子的，不可再分的。

1. lock(锁定)：作用于主内存中的变量，它把一个变量标识为一个线程独占的状态；
2. unlock(解锁):作用于主内存中的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定
3. read（读取）：作用于主内存的变量，它把一个变量的值从主内存传输到线程的工作内存中，以便后面的load动作使用；
4. load（载入）：作用于工作内存中的变量，它把read操作从主内存中得到的变量值放入工作内存中的变量副本
5. use（使用）：作用于工作内存中的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用到变量的值的字节码指令时将会执行这个操作；
6. assign（赋值）：作用于工作内存中的变量，它把一个从执行引擎接收到的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作；
7. store（存储）：作用于工作内存的变量，它把工作内存中一个变量的值传送给主内存中以便随后的write操作使用；
8. write（操作）：作用于主内存的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中。

把一个变量从主内存中复制到工作内存中就需要执行read,load操作，将工作内存同步到主内存中就需要执行store,write操作。注意的是：**java内存模型只是要求上述两个操作是顺序执行的并不是连续执行的**。也就是说read和load之间可以插入其他指令，store和writer可以插入其他指令。比如对主内存中的a,b进行访问就可以出现这样的操作顺序：**read a,read b, load b,load a**。

由原子性变量操作read,load,use,assign,store,write，可以**大致认为基本数据类型的访问读写具备原子性**（例外就是long和double的非原子性协定）

> synchronized

上面一共有八条原子操作，其中六条可以满足基本数据类型的访问读写具备原子性，还剩下lock和unlock两条原子操作。如果我们需要更大范围的原子性操作就可以使用lock和unlock原子操作。尽管jvm没有把lock和unlock开放给我们使用，但jvm以更高层次的指令monitorenter和monitorexit指令开放给我们使用，反应到java代码中就是`synchronized`关键字，也就是说**synchronized满足原子性**。

>volatile

如果让volatile保证原子性，必须符合以下两条规则：

1. **运算结果并不依赖于变量的当前值，或者能够确保只有一个线程修改变量的值；**
2. **变量不需要与其他的状态变量共同参与不变约束**

## 有序性

> synchronized

synchronized语义表示锁在同一时刻只能由一个线程进行获取，当锁被占用后，其他线程只能等待。因此，synchronized语义就要求线程在访问读写共享变量时只能“串行”执行，因此**synchronized具有有序性**。

> volatile

在java内存模型中说过，为了性能优化，编译器和处理器会进行指令重排序；也就是说java程序天然的有序性可以总结为：**如果在本线程内观察，所有的操作都是有序的；如果在一个线程观察另一个线程，所有的操作都是无序的**。在单例模式的实现上有一种双重检验锁定的方式（Double-checked Locking）

```java
public class Singleton {
    private Singleton() { }
    private volatile static Singleton instance;
    public Singleton getInstance(){
        if(instance==null){
            synchronized (Singleton.class){
                if(instance==null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

这里为什么要加volatile了？我们先来分析一下不加volatile的情况，有问题的语句是这条：

> instance = new Singleton();

这条语句实际上包含了三个操作：1.分配对象的内存空间；2.初始化对象；3.设置instance指向刚分配的内存地址。但由于存在重排序的问题，可能有以下的执行顺序：

![Ã¤Â¸ÂÃ¥ÂÂ volatileÃ¥ÂÂ¯Ã¨ÂÂ½Ã§ÂÂÃ¦ÂÂ§Ã¨Â¡ÂÃ¦ÂÂ¶Ã¥ÂºÂ](https://www.javazhiyin.com/wp-content/uploads/2018/07/7f526ab858a0c2fc7e2ff342607f8885.png)

如果2和3进行了重排序的话，线程B进行判断if(instance==null)时就会为true，而实际上这个instance并没有初始化成功，显而易见对线程B来说之后的操作就会是错得。而**用volatile修饰**的话就可以禁止2和3操作重排序，从而避免这种情况。**volatile包含禁止指令重排序的语义，其具有有序性**。

## 可见性

可见性是指当一个线程修改了共享变量后，其他线程能够立即得知这个修改。

当线程获取锁时会从主内存中获取共享变量的最新值，释放锁的时候会将共享变量同步到主内存中。从而，**synchronized具有可见性**

# 初识Lock与AbstractQueuedSynchronizer(AQS)

## lock简介

锁是用来控制多个线程访问共享资源的方式，一般来说，一个锁能够防止多个线程同时访问共享资源。在Lock接口出现之前，java程序主要是靠synchronized关键字实现锁功能的，而java SE5之后，并发包中增加了lock接口，它提供了与synchronized一样的锁功能。**虽然它失去了像synchronize关键字隐式加锁解锁的便捷性，但是却拥有了锁获取和释放的可操作性，可中断的获取锁以及超时获取锁等多种synchronized关键字所不具备的同步特性。**

需要注意的是**synchronized同步块执行完成或者遇到异常是锁会自动释放，而lock必须调用unlock()方法释放锁，因此在finally块中释放锁**。

### synchronized的缺陷

如果一个代码块被synchronized修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况：

1. 获取锁的线程执行完了该代码块，然后线程释放对锁的占有；
2. 线程执行发生异常，此时JVM会让线程自动释放锁。

那么如果这个获取锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，其他线程便只能干巴巴地等待，试想一下，这多么影响程序执行效率。

因此就需要有一种机制可以不让等待的线程一直无期限地等待下去（比如只等待一定的时间或者能够响应中断），通过Lock就可以办到。

再举个例子：当有多个线程读写文件时，读操作和写操作会发生冲突现象，写操作和写操作会发生冲突现象，但是读操作和读操作不会发生冲突现象。

但是采用synchronized关键字来实现同步的话，就会导致一个问题：

　　如果多个线程都只是进行读操作，所以当一个线程在进行读操作时，其他线程只能等待无法进行读操作。

因此就需要一种机制来使得多个线程都只是进行读操作时，线程之间不会发生冲突，通过Lock就可以办到。

　　另外，通过Lock可以知道线程有没有成功获取到锁。这个是synchronized无法办到的。

总结一下，也就是说Lock提供了比synchronized更多的功能。但是要注意以下几点：

1. Lock不是Java语言内置的，synchronized是Java语言的关键字，因此是内置特性。Lock是一个类，通过这个类可以实现同步访问；
2. Lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用；而Lock则必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象。

### java.util.concurrent.locks包下常用的类

#### Lock

```java
public interface Lock {
     //获取锁
    void lock();
    //获取锁的过程能够响应中断
    void lockInterruptibly() throws InterruptedException;
    //非阻塞式响应中断能立即返回，获取锁放回true反之返回fasle
    boolean tryLock();
    //超时获取锁，在超时内或者未中断的情况下能够获取锁
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    //释放锁
    void unlock();
    //获取与lock绑定的等待通知组件，当前线程必须获得了锁才能进行等待，进行等待时会先释放锁，当再次获取锁时才能从等待中返回
    Condition newCondition();
}
```

##### ReentrantLock

ReentrantLock，意思是“可重入锁”。ReentrantLock是唯一实现了Lock接口的类。

> lock()

用来获取锁。如果锁已被其他线程获取，则进行等待

如果采用Lock，必须主动去释放锁，并且在发生异常时，不会自动释放锁。因此一般来说，使用Lock必须在try{}catch{}块中进行，并且将释放锁的操作放在finally块中进行，以保证锁一定被被释放，防止死锁的发生。通常使用Lock来进行同步的话，是以下面这种形式去使用的：

```java
package com.keehoo.thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class ReentrantLockDemo {
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public void insert(Thread t){
        lock.lock();
        try {
            System.out.println(t.getName() + " 得到了锁");
            IntStream.range(0,5).forEach(i -> arrayList.add(i));
        }finally {
            System.out.println(t.getName() + " 释放了锁");
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.insert(Thread.currentThread());
            }
        }.start();
    }
}

```

> tryLock()

它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。

> tryLock(long time, TimeUnit unit)

和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。

所以，一般情况下通过tryLock来获取锁时是这样使用的：

```java
package com.keehoo.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class TryLockDemo extends Thread {
    private static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        if(lock.tryLock()){
            try {
                System.out.println(getName()+"得到了锁");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                System.out.println(getName()+"释放了锁");
                lock.unlock();
            }
        }else{
            System.out.println(getName()+"获取锁失败");
        }
    }

    public static void main(String[] args) {
        TryLockDemo t1 = new TryLockDemo();
        TryLockDemo t2 = new TryLockDemo();
        t1.start();
        t2.start();
    }
}

```

> lockInterruptibly()

当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，而线程B只有在等待，那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程。

由于lockInterruptibly()的声明中抛出了异常，所以lock.lockInterruptibly()必须放在try块中或者在调用lockInterruptibly()的方法外声明抛出InterruptedException。因此lockInterruptibly()一般的使用形式如下：

```java
package com.keehoo.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class LockInterruptiblyDemo {
    public static volatile boolean flag;
    private Lock lock = new ReentrantLock();

    public void insert() throws InterruptedException {
        ////注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        lock.lockInterruptibly();
        try {
            System.out.println(Thread.currentThread().getName()+"得到了锁");
            while (!flag){

            }
        }finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+"释放锁");
        }
    }

    public static void main(String[] args) {
        LockInterruptiblyDemo demo = new LockInterruptiblyDemo();
        MyThreadDemo t1 = new MyThreadDemo(demo);
        MyThreadDemo t2 = new MyThreadDemo(demo);
        t1.start();
        t2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        t2.interrupt();
    }
}

class MyThreadDemo extends Thread{
    private LockInterruptiblyDemo lockInterruptiblyDemo;
    public MyThreadDemo(LockInterruptiblyDemo lockInterruptiblyDemo){
        this.lockInterruptiblyDemo = lockInterruptiblyDemo;
    }

    @Override
    public void run() {
        try {
            lockInterruptiblyDemo.insert();
        } catch (InterruptedException e) {
            LockInterruptiblyDemo.flag = true;
            System.out.println(getName()+"被中断");
        }
    }
}

```



注意，当一个线程获取了锁之后，是不会被interrupt()方法中断的。因为单独调用interrupt()方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程。

因此当通过lockInterruptibly()方法获取某个锁时，如果不能获取到，只有进行等待的情况下，是可以响应中断的。

而用synchronized修饰的话，当一个线程处于等待某个锁的状态，是无法被中断的，只有一直等待下去。

#### ReadWriteLock

ReadWriteLock也是一个接口，在它里面只定义了两个方法：

```java
public interface ReadWriteLock {
    /**
     * Returns the lock used for reading.
     */
    Lock readLock();
 
    /**
     * Returns the lock used for writing.
     */
    Lock writeLock();
}
```

一个用来获取读锁，一个用来获取写锁。也就是说将文件的读写操作分开，分成2个锁来分配给线程，从而使得多个线程可以同时进行读操作。下面的ReentrantReadWriteLock实现了ReadWriteLock接口。

##### **ReentrantReadWriteLock**

ReentrantReadWriteLock里面提供了很多丰富的方法，不过最主要的有两个方法：readLock()和writeLock()用来获取读锁和写锁。

**如果有一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁。**

**如果有一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁。**

```java
package com.keehoo.thread.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class ReentrantReadWriteLockDemo {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public synchronized void get(){
        IntStream.range(0, 20).forEach(i -> System.out.println(Thread.currentThread().getName()+" 正在进行读操作"));
        System.out.println(Thread.currentThread().getName()+" 读操作进行完毕");
    }

    public void get2(){
        lock.readLock().lock();
        try {
            IntStream.range(0, 50).forEach(i -> System.out.println(Thread.currentThread().getName()+" 正在进行读操作"));
            System.out.println(Thread.currentThread().getName()+" 读操作进行完毕");
        }finally {
            lock.readLock().unlock();
        }
    }
    public static void main(String[] args) {
//        synchronizedDemo();
        readWriteLock();

    }

    private static void synchronizedDemo() {
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.get();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.get();
            }
        }.start();
    }

    private static void readWriteLock(){
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                demo.get2();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                demo.get2();
            }
        }.start();
    }
}

```

#### Lock和synchronized的选择

总结来说，Lock和synchronized有以下几点不同：

　　1）Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；

　　2）synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；

　　3）Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；

　　4）通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。

　　5）Lock可以提高多个线程进行读操作的效率。

　　在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。

### 锁的相关概念介绍

#### 可重入锁

如果锁具备可重入性，则称作为可重入锁。像synchronized和ReentrantLock都是可重入锁，可重入性在我看来实际上表明了锁的分配机制：基于线程的分配，而不是基于方法调用的分配。举个简单的例子，当一个线程执行到某个synchronized方法时，比如说method1，而在method1中会调用另外一个synchronized方法method2，此时线程不必重新去申请锁，而是可以直接执行方法method2。

```java
class MyClass {
    public synchronized void method1() {
        method2();
    }
     
    public synchronized void method2() {
    }
}
```

上述代码中的两个方法method1和method2都用synchronized修饰了，假如某一时刻，线程A执行到了method1，此时线程A获取了这个对象的锁，而由于method2也是synchronized方法，假如synchronized不具备可重入性，此时线程A需要重新申请锁。但是这就会造成一个问题，因为线程A已经持有了该对象的锁，而又在申请获取该对象的锁，这样就会线程A一直等待永远不会获取到的锁。而由于synchronized和Lock都具备可重入性，所以不会发生上述现象。

#### 可中断锁

可中断锁：顾名思义，就是可以相应中断的锁。

在Java中，synchronized就不是可中断锁，而Lock是可中断锁。

如果某一线程A正在执行锁中的代码，另一线程B正在等待获取该锁，可能由于等待时间过长，线程B不想等待了，想先处理其他事情，我们可以让它中断自己或者在别的线程中中断它，这种就是可中断锁。

#### 公平锁

公平锁即尽量以请求锁的顺序来获取锁。比如同是有多个线程在等待一个锁，当这个锁被释放时，等待时间最久的线程（最先请求的线程）会获得该所，这种就是公平锁。

非公平锁即无法保证锁的获取是按照请求锁的顺序进行的。这样就可能导致某个或者一些线程永远获取不到锁。

在Java中，synchronized就是非公平锁，它无法保证等待的线程获取锁的顺序。

而对于ReentrantLock和ReentrantReadWriteLock，它默认情况下是非公平锁，但是可以设置为公平锁。

我们可以在创建ReentrantLock对象时，通过以下方式来设置锁的公平性：

如果参数为true表示为公平锁，为fasle为非公平锁。默认情况下，如果使用无参构造器，则是非公平锁。

另外在ReentrantLock类中定义了很多方法，比如：

- isFair()        //判断锁是否是公平锁

- isLocked()    //判断锁是否被任何线程获取了

- isHeldByCurrentThread()  //判断锁是否被当前线程获取了

- hasQueuedThreads()   //判断是否有线程在等待该锁，在ReentrantReadWriteLock中也有类似的方法，同样也可以设置为公平锁和非公平锁。不过要记住，ReentrantReadWriteLock并未实现Lock接口，它实现的是ReadWriteLock接口。

#### 读写锁

读写锁将对一个资源（比如文件）的访问分成了2个锁，一个读锁和一个写锁。

正因为有了读写锁，才使得多个线程之间的读操作不会发生冲突。

ReadWriteLock就是读写锁，它是一个接口，ReentrantReadWriteLock实现了这个接口。

可以通过readLock()获取读锁，通过writeLock()获取写锁。

## AQS

同步器是用来构建锁和其他同步组件的基础框架，它的实现主要依赖一个int成员变量来表示同步状态以及通过一个FIFO队列构成等待队列。它的**子类必须重写AQS的几个protected修饰的用来改变同步状态的方法**，其他方法主要是实现了排队和阻塞机制。**状态的更新使用getState,setState以及compareAndSetState这三个方法**。

子类被**推荐定义为自定义同步组件的静态内部类**，同步器自身没有实现任何同步接口，它仅仅是定义了若干同步状态的获取和释放方法来供自定义同步组件的使用，同步器既支持独占式获取同步状态，也可以支持共享式获取同步状态，这样就可以方便的实现不同类型的同步组件。

同步器是实现锁（也可以是任意同步组件）的关键，在锁的实现中聚合同步器，利用同步器实现锁的语义。可以这样理解二者的关系：**锁是面向使用者，它定义了使用者与锁交互的接口，隐藏了实现细节；同步器是面向锁的实现者，它简化了锁的实现方式，屏蔽了同步状态的管理，线程的排队，等待和唤醒等底层操作**。锁和同步器很好的隔离了使用者和实现者所需关注的领域。

### AQS的模板方法设计模式

AQS的设计是使用模板方法设计模式，它将**一些方法开放给子类进行重写，而同步器给同步组件所提供模板方法又会重新调用被子类所重写的方法**。举个例子，AQS中需要重写的方法tryAcquire：

```java
protected boolean tryAcquire(int arg) {
	throw new UnsupportedOperationException();
}
```

ReentrantLock中NonfairSync（继承AQS）会重写该方法为：

```java
protected final boolean tryAcquire(int acquires) {
	return nonfairTryAcquire(acquires);
}
```

而AQS中的模板方法acquire():

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)){
        selfInterrupt();
    }
}
```

会调用tryAcquire方法，而此时当继承AQS的NonfairSync调用模板方法acquire时就会调用已经被NonfairSync重写的tryAcquire方法。这就是使用AQS的方式，在弄懂这点后会lock的实现理解有很大的提升。可以归纳总结为这么几点：

1. 同步组件（这里不仅仅值锁，还包括CountDownLatch等）的实现依赖于同步器AQS，在同步组件实现中，使用AQS的方式被推荐定义继承AQS的静态内存类；
2. AQS采用模板方法进行设计，AQS的protected修饰的方法需要由继承AQS的子类进行重写实现，当调用AQS的子类的方法时就会调用被重写的方法；
3. AQS负责同步状态的管理，线程的排队，等待和唤醒这些底层操作，而Lock等同步组件主要专注于实现同步语义；
4. 在重写AQS的方式时，使用AQS提供的`getState(),setState(),compareAndSetState()`方法进行修改同步状态

AQS可重写的方法如下图

![AQSÃ¥ÂÂ¯Ã©ÂÂÃ¥ÂÂÃ§ÂÂÃ¦ÂÂ¹Ã¦Â³Â.png](https://www.javazhiyin.com/wp-content/uploads/2018/07/a86795cdfb34f2b28176f9eedbd28da8.png)

在实现同步组件时AQS提供的模板方法如下图：

![AQSÃ¦ÂÂÃ¤Â¾ÂÃ§ÂÂÃ¦Â¨Â¡Ã¦ÂÂ¿Ã¦ÂÂ¹Ã¦Â³Â.png](https://www.javazhiyin.com/wp-content/uploads/2018/07/9ab0cd2ba2e92e8b08c69a90e3e08461.png)

AQS提供的模板方法可以分为3类：

1. 独占式获取与释放同步状态；
2. 共享式获取与释放同步状态；
3. 查询同步队列中等待线程情况；

同步组件通过AQS提供的模板方法实现自己的同步语义。
新建同步组件需要把握两点：
1. 实现同步组件时推荐定义继承AQS的静态内存类，并重写需要的protected修饰的方法；
2. 同步组件语义的实现依赖于AQS的模板方法，而AQS模板方法又依赖于被AQS的子类所重写的方法。

### 同步队列

当共享进程被某个线程占有，其他请求资源的线程将会阻塞，从而进入同步队列。AQS中的同步队列是通过链式方式进行实现。

在AQS有一个静态内部类Node，其中有这样一些属性：

```java
volatile int waitStatus;//节点状态
volatile Node prev;//当前节点/线程的前驱节点
volatile Node next;//当前节点/线程的后继节点
volatile Thread thread;//加入同步队列的线程引用
Node nextWaiter;//等待队列中的下一个节点
```

节点状态有：

- int CANCELLED = 1;//节点从同步队列中取消
- int SIGNAL = -1;//后继节点的线程处于等待状态，如果当前节点释放同步状态会通知后继节点，使得后记节点的线程能够运行；
- int CONDITION = -2;//当前节点进入等待队列中
- int PROPAGATE = -3;//表示下一次共享式同步状态获取将会无条件传播下去
- int INITIAL



## 线程池

## Java中的ThreadPoolExecutor类

java.uitl.concurrent.ThreadPoolExecutor类是线程池中最核心的一个类，在ThreadPoolExecutor类中提供了四个构造方法：

```java
public class ThreadPoolExecutor extends AbstractExecutorService {
	public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
	        BlockingQueue<Runnable> workQueue);

	public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
	        BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory);

	public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
	        BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler);

	public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
		BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler);
}
```

参数:

- **corePoolSize**：核心池大小。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
- **maximumPoolSize**：线程池最大线程数，它表示在线程池中最多能创建多少个线程
- **keepAliveTime**：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
- **unit**：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：

```java
TimeUnit.DAYS;               //天
TimeUnit.HOURS;             //小时
TimeUnit.MINUTES;           //分钟
TimeUnit.SECONDS;           //秒
TimeUnit.MILLISECONDS;      //毫秒
TimeUnit.MICROSECONDS;      //微妙
TimeUnit.NANOSECONDS;       //纳秒
```

- **workQueue**：一个阻塞队列，用来存储等待执行的任务。`ArrayBlockingQueue`和`PriorityBlockingQueue`使用较少，一般使用`LinkedBlockingQueue`和`SynchronousQueue`。线程池的排队策略与BlockingQueue有关。
- **threadFactory**：线程工厂，主要用来创建线程；
- **handler**：表示当拒绝处理任务时的策略，有以下四种取值：

```java
ThreadPoolExecutor.AbortPolicy:直接拒绝所提交的任务，并抛出RejectedExecutionException异常； 
ThreadPoolExecutor.DiscardPolicy:不处理直接丢弃掉任务；
ThreadPoolExecutor.DiscardOldestPolicy:丢弃掉阻塞队列中存放时间最久的任务，执行当前任务
ThreadPoolExecutor.CallerRunsPolicy:只用调用者所在的线程来执行任务；
```

在ThreadPoolExecutor类中有几个非常重要的方法：

- `execute()`方法实际上是Executor中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，通过这个方法可以向线程池提交一个任务，交由线程池去执行。
- `submit()`方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，在ThreadPoolExecutor中并没有对其进行重写，这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，只不过它利用了Future来获取任务执行结果。
- `shutdown()`和`shutdownNow()`是用来关闭线程池的。

### 线程池状态

线程池主要控制的状态是ctl，它是一个原子的整数，AtomicInteger类型的ctl代表了ThreadPoolExecutor中的控制状态，它是一个复核类型的成员变量，是一个原子整数，借助高低位包装了两个概念：

- workerCount：有效的线程数量，占据ctl的低29位；
- runState：线程池的状态，占据ctl的高3位

runStated的值有下面几种：

- **RUNNING**：接受新的任务，并处理队列中的任务
- **SHUTDOWN**：不接受新的任务，继续处理队列中的任务
- **STOP**：不接受新的任务，也不处理队列中的任务，并且中断正在处理的任务
- **TIDYING**：所有任务都结束了，workerCount是0，通过调用terminated()方法转换状态
- **TERMINATED**：terminated()方法已经完成

状态转换：

- RUNNING -> SHUTDOWN

调用`shutdown()`方法，或者隐式地调用`finalize()`方法

- (RUNNING or SHUTDOWN) -> STOP

调用`shutdownNow()`方法

- SHUTDOWN -> TIDYING

当池是空的时候

- TIDYING -> TERMINATED

当`terminated()`方法调用完成时

### 任务的执行

> ThreadPoolExecutor类中其他一些比较重要的成员变量：

```java
private final BlockingQueue<Runnable> workQueue;              //任务缓存队列，用来存放等待执行的任务
private final ReentrantLock mainLock = new ReentrantLock();   //线程池的主要状态锁，对线程池状态（比如线程池大小、runState等）的改变都要使用这个锁
private final HashSet<Worker> workers = new HashSet<Worker>();  //用来存放工作集
private volatile long  keepAliveTime;    //线程存活时间    
private volatile boolean allowCoreThreadTimeOut;   //是否允许为核心线程设置存活时间
private volatile int   corePoolSize;     //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
private volatile int   maximumPoolSize;   //线程池最大能容忍的线程数
private volatile int   poolSize;       //线程池中当前的线程数
private volatile RejectedExecutionHandler handler; //任务拒绝策略
private volatile ThreadFactory threadFactory;   //线程工厂，用来创建线程
private int largestPoolSize;   //用来记录线程池中曾经出现过的最大线程数
private long completedTaskCount;   //用来记录已经执行完毕的任务个数
```

> execute()方法

```java
    public void execute(Runnable var1) {
        if (var1 == null) {
            throw new NullPointerException();
        } else {
            int var2 = this.ctl.get();
            if (workerCountOf(var2) < this.corePoolSize) {
                if (this.addWorker(var1, true)) {
                    return;
                }

                var2 = this.ctl.get();
            }

            if (isRunning(var2) && this.workQueue.offer(var1)) {
                int var3 = this.ctl.get();
                if (!isRunning(var3) && this.remove(var1)) {
                    this.reject(var1);
                } else if (workerCountOf(var3) == 0) {
                    this.addWorker((Runnable)null, false);
                }
            } else if (!this.addWorker(var1, false)) {
                this.reject(var1);
            }

        }
    }

```

