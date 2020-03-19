package com.technologysia.promise;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 模式角色：Promise.Promisor、Promise.Result
 */
public class FTPClientUtil {
    private FTPClientUtil() {

    }

    //模式角色：Promise.Promisor.compute
    public static Future<FTPClientUtil> newInstance(final String ftpServer, final String ftpUserName, final String ftpPassword){
        Callable<FTPClientUtil> callable = () -> {
            FTPClientUtil self = new FTPClientUtil();
            self.init(ftpServer, ftpUserName, ftpPassword);
            return self;
        };

        //task相当于模式角色:Promise.Promise
        final FutureTask<FTPClientUtil> task = new FutureTask<>(callable);

        //创建的线程相当于模式角色：Promise.TaskExecutor
        new Thread(task).start();

        return task;
    }

    private void init(String ftpServer, String ftpUserName, String ftpPassword) {
        System.out.println("初始化ftp客户端");
    }

    public void update(File file){
        System.out.println("上传文件");
    }

    public void close(){
        System.out.println("关闭连接");
    }
}
