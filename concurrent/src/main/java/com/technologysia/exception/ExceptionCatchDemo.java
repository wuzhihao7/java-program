package com.technologysia.exception;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 多线程运行不能按照顺序执行过程中捕获异常的方式来处理异常，异常会被直接抛出到控制台（由于线程的本质，使得你不能捕获从线程中逃逸的异常。一旦异常逃逸出任务的run方法，它就会向外传播到控制台，除非你采用特殊的形式捕获这种异常。）
 */
public class ExceptionCatchDemo implements Runnable{
    public static void main(String[] args) throws InterruptedException {
//        ExecutorService executorService = Executors.newSingleThreadExecutor(new HanlderThreadFactory());
        //如果你知道将要在代码中处处使用相同的异常处理器，那么更简单的方式是在Thread类中设置一个静态域，并将这个处理器设置为默认的未捕获处理器。
        //这个处理器只有在不存在线程专有的未捕获异常处理器的情况下才会被调用。
        Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.execute(new ExceptionCatchDemo());
        }catch (Exception e){
            //多线程环境无法捕获异常
            System.out.println(MessageFormat.format("in main: 捕获异常-[{0}]", e));
        }
        executorService.shutdown();
        //每隔一秒钟检查一次是否执行完毕（状态为 TERMINATED），当从 while 循环退出时就表明线程池已经完全终止了。
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("线程还在执行。。。");
        }
        System.out.println("done！");

    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("run() by "+t);
        System.out.println("eh = "+t.getUncaughtExceptionHandler());
        throw new RuntimeException("线程中抛出异常");
    }
}

/**
 * 定义符合线程异常处理器规范的异常处理器
 */
class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 在线程因未捕获的异常而临近死亡时被调用
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(t.getName());
        System.err.println(MessageFormat.format("in handler: 捕获异常-[{0}]", e));
        throw new RuntimeException(e);
    }
}

/**
 * 线程工厂用来将任务附着给线程，并给该线程绑定一个异常处理器
 */
class HanlderThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        System.out.println(this + "creating new Thread");
        Thread t = new Thread(r);
        System.out.println("created " + t);
        //设定线程工厂的异常处理器
        t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
        System.out.println("eh=" + t.getUncaughtExceptionHandler());
        return t;
    }
}