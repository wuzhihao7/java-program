package com.keehoo.rule.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/28
 */
@Data
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {
    public static final int NO_LOGIN = -1;
    public static final int SUCCESS = 0;
    public static final int CHECK_FAIL = 1;
    public static final int NO_PERMISSION = 2;
    public static final int UNKNOWN_EXCEPTION = -99;
    /**
     * 返回的信息
     */
    private String msg = "success";
    /**
     * 接口返回码
     * 0: 成功
     * >0: 表示已知的异常(例如提示错误等，需要调用地方单独处理)
     * <0: 表示未知的异常(不需要单独处理，调用方统一处理)
     */
    private int code = SUCCESS;
    /**
     * 返回的数据
     */
    private T data;

    public ApiResult(){
        super();
    }

    public ApiResult(T data){
        super();
        this.data = data;
    }

    public ApiResult(Throwable e){
        super();
        this.msg = e.getMessage();
        this.code = UNKNOWN_EXCEPTION;
    }
}
