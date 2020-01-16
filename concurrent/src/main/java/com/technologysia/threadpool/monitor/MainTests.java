package com.technologysia.threadpool.monitor;

import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author wuzhihao
 */
public class MainTests {
    static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        // fixed size 5
        final MonitorableThreadPoolExecutor pool = new MonitorableThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        pool.addMonitorTask("TimeMonitorTask", newTimeMonitorHandler());
        // 起一个线程不断地往线程池丢任务
        Thread t = new Thread(() -> startAddTask(pool));
        t.start();

        // 丢任务丢20 ms
        Thread.sleep(50);
        stop = true;
        t.join();
        pool.shutdown();
        // 等线程池任务跑完
        pool.awaitTermination(100, TimeUnit.SECONDS);
    }

    private static MonitorHandler newTimeMonitorHandler() {

        return new MonitorHandler() {
            // 任务开始时间记录map, 多线程增删, 需用ConcurrentHashMap
            Map<Runnable, Long> timeRecords = new ConcurrentHashMap<Runnable, Long>();

            @Override
            public boolean usable() {
                return true;
            }

            @Override
            public void terminated(int largestPoolSize, long completedTaskCount) {
                System.out.println(String.format("%s:largestPoolSize=%d, completedTaskCount=%s", time(), largestPoolSize, completedTaskCount));
            }

            @Override
            public void before(Thread thread, Runnable runnable) {
                System.out.println(String.format("%s: before[%s -> %s]", time(), thread, runnable));
                timeRecords.put(runnable, System.currentTimeMillis());
            }

            @Override
            public void after(Runnable runnable, Throwable throwable) {
                long end = System.currentTimeMillis();
                Long start = timeRecords.remove(runnable);

                Object result = null;
                if (throwable == null && runnable instanceof FutureTask<?>) { // 有返回值的异步任务，不一定是Callable<?>，也有可能是Runnable
                    try {
                        result = ((Future<?>) runnable).get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // reset
                    } catch (ExecutionException | CancellationException e) {
                        throwable = e;
                    }
                }

                if (throwable == null) { // 任务正常结束
                    if (result != null) { // 有返回值的异步任务
                        System.out.println(String.format("%s: after[%s -> %s], costs %d millisecond, result: %s", time(), Thread.currentThread(), runnable, end - start, result));
                    } else {
                        System.out.println(String.format("%s: after[%s -> %s], costs %d millisecond", time(), Thread.currentThread(), runnable, end - start));
                    }
                } else {
                    System.err.println(String.format("%s: after[%s -> %s], costs %d millisecond, exception: %s", time(), Thread.currentThread(), runnable, end - start, throwable));
                }
            }

        };
    }

    // 随机runnable或者callable<?>, 任务随机抛异常
    private static void startAddTask(MonitorableThreadPoolExecutor pool) {
        int count = 0;
        while (!stop) {
            if (RandomUtils.nextBoolean()) {// 丢Callable<?>任务
                pool.submit(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        // 随机抛异常
                        boolean bool = RandomUtils.nextBoolean();
                        // 随机耗时 0~100 ms
                        Thread.sleep(RandomUtils.nextInt(0,100));
                        if (bool) {
                            throw new RuntimeException("thrown randomly");
                        }
                        return bool;
                    }

                });
            } else { // 丢Runnable
                pool.submit(new Runnable() {

                    @Override
                    public void run() {
                        // 随机耗时 0~100 ms
                        try {
                            Thread.sleep(RandomUtils.nextInt(0, 100));
                        } catch (InterruptedException e) {}
                        // 随机抛异常
                        if (RandomUtils.nextBoolean()) {
                            throw new RuntimeException("thrown randomly");
                        }
                    };

                });
            }
            System.out.println(String.format("%s:submitted %d task", time(), ++count));
        }
    }

    private static String time() {
        return String.valueOf(System.currentTimeMillis());
    }

}
