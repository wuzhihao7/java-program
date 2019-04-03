package com.keehoo.rpc;

import lombok.Data;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/3
 */
@Data
public class RpcInitFactory {
    /**
     * 客户端连接远程ip地址
     **/
    private String ip;
    /***远程端口*/
    private int port;


    public RpcInitFactory(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        RpcInitFactory initFactory=  new RpcInitFactory("127.0.0.1",8090);
        Idemo demo = new DemoRemoteImpl(new CommonClient(initFactory));
        System.out.println(demo.add(2, 1));
    }

}

