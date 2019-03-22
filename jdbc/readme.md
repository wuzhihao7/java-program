# 注册驱动

```java
        try {
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
/** 驱动底层自动注册
        static{ 
           try{ 
        java.sql.DriverManager.registerDriver(new Driver()); 
        }catch(SQLException e){ 
           throw new RuntimeException("can't register driver!"); 
        } 
        }
*/
```

# 获取连接

```java
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "root");
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
```

# 事务操作

```java
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(false);
            //不开启事务的话，手工提交会报错，java.sql.SQLException: Can''t call commit when autocommit=true
//            connection.setAutoCommit(true);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
```

