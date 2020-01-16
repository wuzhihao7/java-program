package com.technologysia.threadpool.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author wuzhihao
 */
public class MonitorableThreadPoolExecutor extends ThreadPoolExecutor {
    /**
     * 监控处理器
     */
    private Map<String, MonitorHandler> handlerMap = new HashMap<String, MonitorHandler>();

    private final Object lock = new Object();

    public MonitorableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MonitorableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MonitorableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MonitorableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        // 依次调用处理器
        for (MonitorHandler handler : handlerMap.values()) {
            if (handler.usable()) {
                handler.before(t, r);
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        // 依次调用处理器
        for (MonitorHandler handler : handlerMap.values()) {
            if (handler.usable()) {
                handler.after(r, t);
            }
        }
    }

    @Override
    protected void terminated() {
        super.terminated();
        for (MonitorHandler handler : handlerMap.values()) {
            if (handler.usable()) {
                handler.terminated(getLargestPoolSize(), getCompletedTaskCount());
            }
        }
    }

    public MonitorHandler addMonitorTask(String key, MonitorHandler task, boolean overrideIfExist) {
        if (overrideIfExist) {
            synchronized (lock) {
                return handlerMap.put(key, task);
            }
        } else {
            synchronized (lock) {
                return handlerMap.putIfAbsent(key, task);
            }
        }
    }

    public MonitorHandler addMonitorTask(String key, MonitorHandler task) {
        return addMonitorTask(key, task, true);
    }

    public MonitorHandler removeMonitorTask(String key) {
        synchronized (lock) {
            return handlerMap.remove(key);
        }
    }
}
