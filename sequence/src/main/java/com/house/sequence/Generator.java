package com.house.sequence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 序列生成器
 * @author wuzhihao
 * @date 2019/1/11
 */
public class Generator {
    /**
     * 当前放大倍数后的序列号
     */
    private long currentId = 0L;
    /**
     * 放大倍数后的最大序列号
     */
    private long maxId = 0L;
    /**
     * 序列号放大倍数
     */
    private final int MULTIPLE = 10000;

    /**
     * 锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 序列名
     */
    private String seqName;

    protected Generator(String seqName) {
        this.seqName = seqName;
    }

    /**
     * 获取序列号 判断当前放大倍数后的序列号是否等于放大倍数后的最大序列号： 是，从数据库中获取序列号，设置到step中，计算当前放大倍数后的序列号（step
     * * 放大倍数）， 计算放大倍数后的最大序列号（(step + 1) * 放大倍数） 否，直接返回当前放大倍数后的序列号，并自增
     *
     * @return 返回一个序列号
     * @throws Exception 异常
     */
    public Long getId() throws Exception {
        lock.lock();
        try {
            if (currentId == maxId) {
                getSeqFromDb();
            }
            return currentId++;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从数据库获取一个序列号
     *
     * @throws Exception 异常
     */
    private void getSeqFromDb() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.179.130:1521:XE", "oracle",
                "oracle");
        String sql = "select " + this.seqName + ".nextval from dual";
        PreparedStatement pstat = conn.prepareStatement(sql);
        boolean execute = pstat.execute();
        long step;
        if (execute) {
            ResultSet resultSet = pstat.getResultSet();
            if (resultSet.next()) {
                step = resultSet.getLong(1);
            } else {
                throw new RuntimeException("返回结果集为空！");
            }
            resultSet.close();
        } else {
            throw new RuntimeException("数据库执行失败！");
        }
        conn.close();
        currentId = (step * MULTIPLE);
        maxId = ((step + 1) * MULTIPLE);
    }
}
