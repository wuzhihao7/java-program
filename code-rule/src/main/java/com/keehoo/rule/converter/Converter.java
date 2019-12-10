package com.technologysia.rule.converter;

/**
 * 对象转换
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/20
 */
public interface Converter<S, T> {
    T doForward(S s);
    S doBackward(T t);
}
