package com.technologysia.threadpool.monitor;

/**
 * @author wuzhihao
 */
public interface MonitorHandler {
    /**
     * 监控是否可用
     * @return true - 可用，false -不可用
     */
    boolean usable();

    /**
     * 任务执行前回调
     * @param thread 即将执行该任务的线程
     * @param runnable 即将执行的任务
     */
    void before(Thread thread, Runnable runnable);

    /**
     * 任务执行后回调
     * 注意:
     *      1.当你往线程池提交的是{@link Runnable} 对象时, 参数runnable就是一个{@link Runnable}对象
     *      2.当你往线程池提交的是{@link java.util.concurrent.Callable<?>} 对象时,
     *      参数runnable实际上就是一个{@link java.util.concurrent.FutureTask<?>}对象
     *      这时你可以通过把参数runnable downcast为FutureTask<?>或者Future来获取任务执行结果
     * @param runnable 执行完成后的任务
     * @param throwable 异常信息
     */
    void after(Runnable runnable, Throwable throwable);

    /**
     * 线程池关闭后回调
     * @param largestPoolSize 最大线程数
     * @param completedTaskCount 完成任务数
     */
    void terminated(int largestPoolSize, long completedTaskCount);
}
