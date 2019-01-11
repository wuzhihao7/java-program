package com.house.exception;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 错误码接口，使用泛型，错误码可能为数字或者字符串
 * @author wuzhihao
 * @date 2019/1/10
 */
public interface ErrorCode {
    /**
     * 获取错误码
     * @return 错误码
     */
    Object getCode();

    /**
     * 获取错误信息，i18n标准
     * @return 错误信息
     */
    default String getMessage(){
        return ResourceBundle.getBundle(this.getClass().getName(), Optional.of(new Locale("en_US")).orElse(Locale.getDefault())).getString(this.getCode().toString());
    }
}
