package com.technologysia.future;

import java.time.LocalDateTime;

public class FutureDemo {
    public static void main(String[] args) {
        Client client = new Client();
        Data test = client.request("test");
        System.out.println("请求完毕：" + LocalDateTime.now());
        System.out.println("获取结果：" + test.getResult());
        System.out.println("获取完毕：" + LocalDateTime.now());
    }
}
