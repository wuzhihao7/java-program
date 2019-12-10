package com.technologysia.rpc;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/3
 */

public class DemoImpl implements Idemo{
    @Override
    public Integer add(Integer i, Integer j) {
        return i+j;
    }
}
