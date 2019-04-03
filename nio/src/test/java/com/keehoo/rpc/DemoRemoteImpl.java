package com.keehoo.rpc;

import com.keehoo.rpc.dto.RpcRequest;
import com.keehoo.rpc.dto.RpcResponse;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/3
 */
public class DemoRemoteImpl implements Idemo {


    private  CommonClient client;

    public DemoRemoteImpl(CommonClient client) {
        this.client = client;
    }

    @Override
    public Integer add(Integer i, Integer j) {
        //构造rpc请求实体类
        RpcRequest rpcRquest=new RpcRequest();
        //设置版本号
        rpcRquest.setServiceVersion("123");
        //设置调用的接口名称
        rpcRquest.setInterfaceName(Idemo.class.getName());
        //设置调用方法名称
        rpcRquest.setMethodName("add");
        //设置参数
        rpcRquest.setParameters(new Integer[] {i,j});
        //设置参数类型
        rpcRquest.setParameterTypes(new Class[] {Integer.class,Integer.class});
        //进行远程调用
        RpcResponse response=  client.invoke(rpcRquest);
        if (null!=response){
            return Integer.parseInt(response.getResult().toString());
        }
        return null;
    }

}
