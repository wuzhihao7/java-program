package com.technologysia.pool.demo;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class StringBufferFactory extends BasePooledObjectFactory<StringBuffer> {
    /**
     * 创建一个新对象
     * @return 返回新对象
     * @throws Exception
     */
    @Override
    public StringBuffer create() throws Exception {
        return new StringBuffer();
    }

    /**
     * 封装为池化对象
     * @param stringBuffer
     * @return
     */
    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer stringBuffer) {
        return new DefaultPooledObject<>(stringBuffer);
    }

    /**
     * 钝化对象，向池中返还对象时会调用此方法，将stringbuffer清空
     * @param p
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<StringBuffer> p) throws Exception {
        p.getObject().setLength(0);
    }
}
