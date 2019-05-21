package com.geo.oio;

import com.geo.oio.impl.HelloServiceImpl;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/21
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }
}
