package com.technologysia.pool.demo;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.StringReader;

public class Demo {
    public static void main(String[] args) throws Exception {
        System.out.println(demo());
        System.in.read();

    }

    public static String demo() throws Exception {
        // 创建对象池配置
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        // 创建对象工厂
        PooledObjectFactory factory = new StringBufferFactory();
        // 创建对象池
        ObjectPool<StringBuffer> pool = new GenericObjectPool<>(factory, config);
        StringReader in = new StringReader("abcdefg");
        StringBuffer buf = null;
        try {
            // 从池中获取对象
            buf = pool.borrowObject();

            // 使用对象
            for (int c = in.read(); c != -1; c = in.read()) {
                buf.append((char) c);
            }
            return buf.toString();
        } catch (Exception e) {
            try {
                // 出现错误将对象置为失效
                pool.invalidateObject(buf);
                // 避免 invalidate 之后再 return 抛异常
                buf = null;
            } catch (Exception ex) {
                // ignored
            }

            throw e;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                // ignored
            }

            try {
                if (null != buf) {
                    // 使用完后必须 returnObject
                    pool.returnObject(buf);
                }
            } catch (Exception e) {
                // ignored
            }
            System.out.println(pool);
        }
    }
}
