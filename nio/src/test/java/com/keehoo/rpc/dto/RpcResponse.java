package com.keehoo.rpc.dto;

import lombok.Data;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/2
 */
@Data
public class RpcResponse {
    /**请求流水号*/
    private String requestId;
    /**异常*/
    private Exception exception;
    /**返回结果**/
    private Object result;
}
