Java路径

Java中使用的路径，分为两种：绝对路径和相对路径。具体而言，又分为四种：

一、URI形式的绝对资源路径

如：file:/D:/java/eclipse32/workspace/jbpmtest3/bin/aaa.b

URL是URI的特例。URL的前缀/协议，必须是Java认识的。URL可以打开资源，而URI则不行。

URL和URI对象可以互相转换，使用各自的toURI(),toURL()方法即可！

 

二、本地系统的绝对路径

D:/java/eclipse32/workspace/jbpmtest3/bin/aaa.b

Java.io包中的类，需要使用这种形式的参数。

但是，它们一般也提供了URI类型的参数，而URI类型的参数，接受的是URI样式的String。因此，通过URI转换，还是可以把URI样式的绝对路径用在java.io包中的类中。

 

三、相对于classpath的相对路径

如：相对于

file:/D:/java/eclipse32/workspace/jbpmtest3/bin/这个路径的相对路径。其中，bin是本项目的classpath。所有的Java源文件编译后的.class文件复制到这个目录中。

 

 

四、相对于当前用户目录的相对路径

就是相对于System.getProperty("user.dir")返回的路径。

对于一般项目，这是项目的根路径。对于JavaEE服务器，这可能是服务器的某个路径。这个并没有统一的规范！

所以，绝对不要使用“相对于当前用户目录的相对路径”。然而：

默认情况下，java.io 包中的类总是根据当前用户目录来分析相对路径名。此目录由系统属性 user.dir 指定，通常是 Java 虚拟机的调用目录。

这就是说，在使用java.io包中的类时，最好不要使用相对路径。否则，虽然在J2SE应用程序中可能还算正常，但是到了J2EE程序中，一定会出问题！而且这个路径，在不同的服务器中都是不同的！


getPath():

基本语法：public String getPath()，直接返回此抽象路径名的路径名字符串。

注意：返回的是定义时的路径，可能是相对路径，也可能是绝对路径，这个取决于定义时用的是相对路径还是绝对路径。如果定义时用的是绝对路径，那么使用getPath()返回的结果跟用getAbsolutePath()返回的结果一样

getAbsolutePath():

基本语法：public String getAbsolutePath()，返回此抽象路径名的绝对路径名字符串。

注意：①如果这个抽象路径名已经是绝对的，那么路径名字符串就像getPath()方法一样简单地返回。

          ②如果此抽象路径名为空抽象路径名，则返回由系统属性user.dir命名的当前用户目录的路径名字符串。

          ③否则，该路径名以系统相关的方式解决。

          ④返回的是定义时的路径对应的相对路径，但不会处理“.”和“..”的情况

getCanonicalPath()：

基本语法：public String getCanonicalPath() throws IOException，返回此抽象路径名规范化后的路径名字符串。

注意：此方法首先将此路径名转换为绝对形式，就像调用getAbsolutePath()方法一样，然后以系统相关的方式将其映射到其唯一的形式。 这通常包括从路径名中删除冗余名称，例如"."和".." 
