package com.technologysia.promise;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 某系统的一个数据同步模块需要将一批本地文件上传到指定的目标 FTP 服务器上。这些文件是根据页面中的输入条件查询数据库的相应记录生成的。在将文件上传到目标服务器之前，需要对 FTP 客户端实例进行初始化（包括与对端服务器建立网络连接、向服务器发送登录用户和向服务器发送登录密码）。而 FTP 客户端实例初始化这个操作比较耗时间，我们希望它尽可能地在本地文件上传之前准备就绪。因此我们可以引入异步编程，使得 FTP 客户端实例初始化和本地文件上传这两个任务能够并发执行，减少不必要的等待。另一方面，我们不希望这种异步编程增加了代码编写的复杂性。这时，Promise 模式就可以派上用场了：先开始 FTP 客户端实例的初始化，并得到一个获取 FTP 客户端实例的凭据对象。在不必等待 FTP 客户端实例初始化完毕的情况下，每生成一个本地文件，就通过凭据对象获取 FTP 客户端实例，再通过该 FTP 客户端实例将文件上传到目标服务器上
 * DataSyncTask 类的 run 方法先开始 FTP 客户端实例的初始化，并得到获取相应 FTP 客户端实例的凭据对象 ftpClientUtilPromise。接着，它直接开始查询数据库并生成本地文件。而此时，FTP 客户端实例的初始化可能尚未完成。在本地文件生成之后，run 方法通过调用 ftpClientUtilPromise 的 get 方法来获取相应的 FTP 客户端实例。此时，如果相应的 FTP 客户端实例的初始化仍未完成，则该调用会阻塞，直到相应的 FTP 客户端实例的初始化完成或者失败。run 方法获取到 FTP 客户端实例后，调用其 upload 方法将文件上传到指定的 FTP 服务器。
 */
public class DataSyncTask implements Runnable {
    private  final Map<String, String> taskParameters;

    public DataSyncTask(Map<String, String> taskParameters) {
        this.taskParameters = taskParameters;
    }


    @Override
    public void run() {
        String ftpServer = taskParameters.get("server");
        String ftpUserName = taskParameters.get("userName");
        String ftpPassword = taskParameters.get("password");

        //先开始初始化ftp客户端实例
        Future<FTPClientUtil> ftpClientUtilPromise = FTPClientUtil.newInstance(ftpServer, ftpUserName, ftpPassword);
        //查询数据库。。。
        FTPClientUtil ftpClientUtil = null;
        try {
            //获取初始化完毕的ftp客户端实例
            ftpClientUtil = ftpClientUtilPromise.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //上传文件。。。

    }
}
