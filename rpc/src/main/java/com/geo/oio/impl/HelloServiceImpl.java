package com.geo.oio.impl;

import com.geo.oio.HelloService;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/21
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
