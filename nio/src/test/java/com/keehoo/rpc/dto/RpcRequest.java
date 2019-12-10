package com.technologysia.rpc.dto;

import lombok.Data;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/2
 */
@Data
public class RpcRequest {
    /**请求id*/
    private String requestId;
    /**请求接口名*/
    private String interfaceName;
    /**服务版本**/
    private String serviceVersion;
    /**方法名*/
    private String methodName;
    /**参数类型*/
    private Class<?>[] parameterTypes;
    /**参数*/
    private Object[] parameters;
}
