package com.keeoo.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/22
 */
public class Demo {
    public static void main(String[] args) {
        try {
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(false);
            //不开启事务的话，手工提交会报错，java.sql.SQLException: Can''t call commit when autocommit=true
//            connection.setAutoCommit(true);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
