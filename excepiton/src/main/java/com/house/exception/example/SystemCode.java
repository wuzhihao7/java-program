package com.house.exception.example;

import com.house.exception.ErrorCode;

/**
 * @author wuzhihao
 * @date 2019/1/10
 */
public enum SystemCode implements ErrorCode {

    /**
     * 系统错误
     */
    SYSTEM_ERROR(9999),
    /**
     * 系统错误2
     */
    SYSTEM_ERROR2(8888);

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 构造方法
     * @param code 错误码
     */
    SystemCode(Integer code){
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
