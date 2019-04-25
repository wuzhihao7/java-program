package com.keehoo.path;

import java.io.File;
import java.io.IOException;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/25
 */
public class PathDemo {
    public static void main(String[] args) throws IOException {
        //当前类文件的URI目录。不包括自己！
        System.out.println("PathDemo.class.getResource(\"\"): "+PathDemo.class.getResource(""));
        System.out.println("PathDemo.class.getResource(\"\").getFile(): "+PathDemo.class.getResource("").getFile());
        //当前的classpath的绝对URI路径。
        System.out.println("PathDemo.class.getClassLoader().getResource(\"\"): "+PathDemo.class.getClassLoader().getResource(""));
        System.out.println("PathDemo.class.getClassLoader().getResource(\"/\"): "+PathDemo.class.getClassLoader().getResource("/"));
        System.out.println("PathDemo.class.getResource(\"/\"): "+PathDemo.class.getResource("/"));
        //推荐
        System.out.println("Thread.currentThread().getContextClassLoader().getResource(\"\"): "+Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println("Thread.currentThread().getContextClassLoader().getResource(\"/\"): "+Thread.currentThread().getContextClassLoader().getResource("/"));
        System.out.println("PathDemo.class.getClassLoader().getResource(\"\"): "+PathDemo.class.getClassLoader().getResource(""));
        System.out.println("ClassLoader.getSystemResource(\"\"): "+ClassLoader.getSystemResource(""));
        //用户工作目录
        System.out.println("System.getProperty(\"user.dir\"): "+System.getProperty("user.dir"));

        System.out.println(new File(PathDemo.class.getResource("").getFile(), "PathDemo.class").getAbsolutePath());
        System.out.println(new File(PathDemo.class.getResource("").getFile(), "PathDemo.class").getCanonicalPath());

        File file = new File("");
        System.out.println(file.getAbsolutePath()+": "+file.getAbsoluteFile().exists());
        System.out.println(file.getPath()+": "+file.exists());
        System.out.println(file.getCanonicalPath()+": "+file.getCanonicalFile().exists());

        file = new File("/");
        System.out.println(file.getAbsolutePath()+": "+file.getAbsoluteFile().exists());
        System.out.println(file.getPath()+": "+file.exists());
        System.out.println(file.getCanonicalPath()+": "+file.getCanonicalFile().exists());

        file = new File(".");
        System.out.println(file.getAbsolutePath()+": "+file.getAbsoluteFile().exists());
        System.out.println(file.getPath()+": "+file.exists());
        System.out.println(file.getCanonicalPath()+": "+file.getCanonicalFile().exists());
    }
}
