package com.keehoo.path;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/25
 */
public class ClassLoaderUtils {
    /**
     * 加载Java类。 使用全限定类名
     *
     * @param className
     * @return
     */
    public static Class loadClass(String className) {
        try {
            return getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found '" + className + "'", e);
        }
    }

    /**
     * 得到类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return ClassLoaderUtils.class.getClassLoader();
    }

    /**
     * 提供相对于classpath的资源路径，返回文件的输入流
     *
     * @param relativePath
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getStream(String relativePath) throws MalformedURLException, IOException {
        if (!relativePath.contains("../")) {
            return getClassLoader().getResourceAsStream(relativePath);
        } else {
            return ClassLoaderUtils.getStreamByExtendResource(relativePath);
        }
    }

    public static InputStream getStream(URL url) throws IOException {
        if (url != null) {
            return url.openStream();
        } else {
            return null;
        }
    }

    public static InputStream getStreamByExtendResource(String relativePath) throws MalformedURLException, IOException {
        return ClassLoaderUtils.getStream(ClassLoaderUtils.getExtendResource(relativePath));
    }

    /**
     * 提供相对于classpath的资源路径，返回属性对象，它是一个散列表
     * @param resource
     * @return
     */
    public static Properties getProperties(String resource) {
        Properties properties = new Properties();
        try {
            properties.load(getStream(resource));
        } catch (IOException e) {
            throw new RuntimeException("couldn't load properties file '" + resource + "'", e);
        }
        return properties;
    }

    /**
     * 得到本Class所在的ClassLoader的Classpat的绝对路径。
     * @return
     */
    public static String getAbsolutePathOfClassLoaderClassPath() {
        System.out.println(ClassLoaderUtils.getClassLoader().getResource("").toString());
        return ClassLoaderUtils.getClassLoader().getResource("").toString();
    }

    public static URL getExtendResource(String relativePath) throws MalformedURLException {
        System.out.println("传入的相对路径：" + relativePath);
        if (!relativePath.contains("../")) {
            return ClassLoaderUtils.getResource(relativePath);
        }
        String classPathAbsolutePath = ClassLoaderUtils.getAbsolutePathOfClassLoaderClassPath();
        if ("/".equals(relativePath.substring(0, 1))) {
            relativePath = relativePath.substring(1);
        }
        String wildcardString = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
        relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);
        int containSum = ClassLoaderUtils.containSum(wildcardString, "../");
        classPathAbsolutePath = ClassLoaderUtils.cutLastString(classPathAbsolutePath, "/", containSum);
        String resourceAbsolutePath = classPathAbsolutePath + relativePath;
        System.out.println("绝对路径：" + resourceAbsolutePath);
        URL resourceAbsoluteURL = new URL(resourceAbsolutePath);
        return resourceAbsoluteURL;
    }

    private static int containSum(String source, String dest) {
        int containSum = 0;
        int destLength = dest.length();
        while (source.contains(dest)) {
            containSum = containSum + 1;
            source = source.substring(destLength);
        }
        return containSum;
    }

    private static String cutLastString(String source, String dest, int num) {
        for (int i = 0; i < num; i++) {
            source = source.substring(0, source.lastIndexOf(dest, source.length() - 2) + 1);
        }
        return source;
    }

    public static URL getResource(String resource) {
        System.out.println("传入的相对于classpath的路径：" + resource);
        return ClassLoaderUtils.getClassLoader().getResource(resource);
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println(ClassLoaderUtils.getExtendResource("../../"));;
    }
}
