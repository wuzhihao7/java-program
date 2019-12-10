package com.technologysia.connection;

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
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "root");
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
