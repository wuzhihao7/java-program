package com.house.sequence;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列工厂
 * @author wuzhihao
 * @date 2019/1/11
 */
public class SequenceFactory {
    /**
     * 存放序列生成器的容器。key为序列名，value为序列生成器
     */
    private final static ConcurrentHashMap<String, Generator> map = new ConcurrentHashMap<>();

    /**
     * 构造器私有化，确保只能通过方法获取序列生成器
     */
    private SequenceFactory() {}

    /**
     * 从容器中获取一个序列生成器
     * @param seqName 序列名
     * @return 序列号生成器
     */
    public static Generator getSeq(String seqName) {
        seqName = seqName.toLowerCase();
        if(map.get(seqName) == null) {
            synchronized (SequenceFactory.class) {
                if(map.get(seqName) == null) {
                    map.put(seqName, new Generator(seqName));
                }
            }
        }
        return map.get(seqName);
    }
}
