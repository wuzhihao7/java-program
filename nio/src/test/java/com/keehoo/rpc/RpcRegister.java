package com.keehoo.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/2
 */
public class RpcRegister {
    /**存储注册的服务提供实现类*/
    private Map<String, Object> registerMap = new HashMap<>();
    private  static  RpcRegister register=new RpcRegister();
    public static  RpcRegister buildRegister(){
        return register;
    }
    public RpcRegister register(String interfaceName,Object obj){
        registerMap.put(interfaceName,obj);
        return this;
    }
    public Object findServer(String interfaceName){
        return  registerMap.get(interfaceName);
    }
}
