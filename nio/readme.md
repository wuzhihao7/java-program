# 概念

## IO

一次完整的IO操作，是指文件从硬盘拷贝到用户空间的过程。

## 同步与异步

> **同步**

如果有多个任务或者时间发生，这些任务或事件必须逐个地执行，一个事件或者任务的执行会导致整个流程的暂时等待，这些事件没有办法并发的执行。

A调用B，在处理完之前它不会通知A，只有处理完之后才会明确的通知A。

> **异步**

如果有多个任务或者事件发生，这些任务或事件可以并发地执行，一个事件或者任务的执行不会导致整个流程的暂时等待。

A调用B，B在接到请求后先告诉A我已经收到请求了，然后异步去处理，处理完之后通过回调等方式通知A。

## 阻塞与非阻塞

> **阻塞**

当某个任务或者事件在执行过程中，它发出一个请求操作，但是由于该请求操作需要的条件不满足，那么就会一直在那等待，直至条件满足。

A调用B，A一直等着B的返回，别的事情什么也不干。

> 非阻塞

当某个任务或者事件在执行过程中，它发出一个请求操作，如果该请求操作需要的条件不满足，会立即返回一个标识信息告知条件不满足，不会一直在那等待。

A调用B，A不用一直等着B的返回，先去忙别的事情了。

***同步和异步着重点在于多个任务的执行过程中，一个任务的执行是否会导致整个流程的暂时等待；***

***阻塞和非阻塞着重点在于发出一个请求操作时，如果进行操作的条件不满足是否会返会一个标志信息告知条件不满足。***

## 阻塞IO与非阻塞IO

通常来说，IO操作包括：对硬盘的读写，对socket的读写以及外设的读写。

当用户线程发起一个IO请求操作（已读请求操作为例），内核会去查看要读取的数据是否就绪，对于阻塞IO来说，如果数据没有就绪，则会一直在那等待，知道数据就绪；对于非阻塞IO来说，如果数据没有就绪，则会返回一个标识信息告知用户线程当前要读的数据没有就绪。当数据就绪之后，便将数据拷贝到用户线程，这样才完成了一个完整的IO读请求操作，也就是说一个完整的IO读请求操作包括两个阶段：

- 查看数据是否就绪
- 进行数据拷贝（内核将数据拷贝到用户线程）

那么阻塞IO和非阻塞IO的区别就在于第一个阶段，如果数据没有就绪，就查看数据是否就绪的过程中是一直等待，还是直接返回一个标识信息。

java中传统的IO都是阻塞IO，比如通过socket来读数据，调用read()方法后，如果数据没有就绪，当前线程会一直阻塞在read()方法调用那里，直到有数据才返回；而如果是非阻塞IO的话，当数据没有就绪，read()方法应该返回一个标识信息，告知当前线程数据没有就绪，而不是一直在那里等待。

## 同步IO与异步IO

同步IO和异步IO模型是针对用户线程和内核的交互来说的

> **同步IO**

如果一个线程请求进行IO操作，在IO操作完成之前，该线程会被阻塞。

当用户发出IO请求操作之后，如果数据没有就绪，需要通过用户线程或者内核不断地去轮询数据是否就绪，当数据就绪时，再将数据拷贝到用户线程。

> **异步IO**

如果一个线程请求进行IO操作，IO操作不会导致请求线程被阻塞。

只有IO请求操作的发出是由用户线程来进行的，IO操作的两个阶段都是由内核自动完成，然后发送通知告知用户线程IO操作已经完成。

***同步IO和异步IO的关键区别反映在数据拷贝阶段是由用户线程完成还是内核完成。所以异步IO必须要有操作系统的底层支持。***

***阻塞IO和非阻塞IO是反映在当用户请求IO操作时，如果数据没有就绪，是用户线程一直等待数据就绪，还是会收到一个标识信息这一点上面的。也就是说，阻塞IO和非阻塞IO是反映在IO操作的第一个阶段，在查看数据是否就绪时是如何处理的。***

# Java中的三种IO模型

## BIO(Blocking I/O)

同步阻塞I/O模式，数据的读写必须阻塞在一个线程内等待其完成。

有一排水壶烧开水，叫一个线程停留在水壶那，知道这个水壶烧开，才去处理下一个水壶。但实际上线程在等待水壶烧开的时间段什么都没做。

## NIO(New I/O)

同事支持阻塞与非阻塞模式，但主要是使用同步非阻塞IO。

叫一个线程不断的轮询每个水壶的状态，看看是否有水壶的状态发生了改变，从而进行下一步的操作。

## AIO（Asynchronous I/O）

异步非阻塞IO。

为每个水壶上面装了一个开关，水烧开后，水壶会自动通知我水烧开了。

# 五种IO模型

## 阻塞IO模型

最传统的一种IO模型，即在读写数据过程中会发生阻塞现象。

当用户线程发出IO请求之后，内核回去查看数据是否就绪，如果没有就绪就会等待数据就绪，而用户线程机会存在阻塞状态，用户线程交出CPU。当数据就绪之后，内核会将数据拷贝到用户线程，并返回结果给用户线程，用户线程才解除`block`状态。

典型的阻塞IO模型的例子为：

```java
data = socker.read();
```

如果数据没有就绪，就会一直阻塞在read()方法。

应用进程通过系统调用`recvfrom`接收数据，但由于内核还未准备好数据报，应用进程就会阻塞住，直到内核准备好数据报，`recvfrom`完成数据报复制工作，应用进程才能结束阻塞状态。

## 非阻塞IO模型

当用户线程发起一个read操作后，并不需要等待，而是马上就得到了一个结果。如果结果是一个error时，它就知道数据还没有准备好，于是它可以再次发送read操作。一旦内核中的数据准备好了，并且又再次收到了用户线程的请求，那么它马上就将数据拷贝到了用户线程，然后返回。

所以事实上，非阻塞IO模型中，用户线程需要不断地询问内核数据是否就绪，也就是非阻塞IO不会交出CPU，而会一直暂用CPU。

典型的非阻塞IO模型一般如下：

```java
while(true){
    data = socker.read();
    if(data != error){
        ...
        break;
    }
}
```

但是对于非阻塞IO就有一个非常严重的问题，在while循环中需要不断地去询问内核数据是否就绪，这样就会导致CPU占用率非常高，因此一般情况下很少使用while循环这种方式来读取数据。

应用进程通过`recvfrom`调用不停地去和内核交互，直到内核准备好数据。如果没有准备好，内核会返回`error`，应用进程在得到`error`后，过一段时间再发送`recvfrom`请求。在两次发送请求的时间段，进程可以先做别的事情。

## 多路复用IO模型

Java NIO实际上就是多路复用IO。

在多路复用IO模型中，会有一个线程不断去轮询多个socket的状态，只有当socket真正有读写事件时，才真正调用实际的IO读写操作。因为在多路复用IO模型中，只需要使用一个线程就可以管理多个socket，系统不需要建立新的进程或者线程，也不必维护这些进程或者线程，并且只有在真正有socket读写事件进行时，才会使用IO资源，所以它大大减少了资源占用。

在Java NIO中，是通过`selector.select()`去查询每个通道是否有到达事件，如果没有事件，则一直阻塞在那里，因此这种方式会导致用户线程的阻塞。

多路复用IO模型适合连接数比较多的情况。

多路复用IO比非阻塞IO效率高是因为在非阻塞IO中，不断轮询socket状态是通过用户线程去进行的，而在多路复用IO中，轮询每个socket状态是在内核中进行的，这个效率要比用户线程高得多。

多路复用IO模型是通过轮询的方式来检测是否有事件到达，并且到到达的事件逐一进行响应。因此对于多路复用IO模型来说，一旦事件响应体很大，那么就会导致后续的事件迟迟得不到处理，并且会影响新的事件轮询。

IO多路转接是多了一个`select`函数，多个进程的IO可以注册到同一个`select`上，当用户进程调用该`select`，`select`会监听所有注册好的IO，如果所有监听的IO需要的数据都没有准备好时，`select`调用进程会阻塞。当任意一个IO所需的数据准备好之后，`select`调用就会返回，然后进程在通过`recvfrom`来进行数据拷贝。

这里的IO复用模型，并没有向内核注册信号处理函数，所以，它并不是非阻塞的。进程发出`select`后，要等到`select`监听的所有IO操作中至少有一个需要的数据准备好，才会有返回，并且也需要再次发送请求去进行文件的拷贝。

## 信号驱动IO模型

当用户线程发起一个IO请求操作，会给对应的socket注册一个信号函数，然后用户线程会继续执行，当内核数据就绪时会发送一个信号给用户线程，用户线程接收到信号后，便在信号函数中调用IO读写操作来进行实际的IO请求操作。

应用进程预先向内核注册一个信号处理函数，然后用户进程返回，并且不阻塞，当内核数据准备就绪时会发送一个信号给进程，用户进程便在信号处理函数中开始把数据拷贝到用户空间中。

信号驱动，内核是在数据准备好之后通知进程，然后进程再通过`recvfrom`操作进行数据拷贝。我们可以认为数据准备阶段是异步的，但是，数据拷贝操作是同步的。所以，整个IO过程也不能认为是异步的。

## 异步IO模型

异步IO模型才是最理想的IO模型，在异步IO模型中，当用户线程发起read操作之后，立刻就可以开始去做其它的事。而另一方面，从内核的角度，当它受到一个asynchronous read之后，它会立刻返回，说明read请求已经成功发起了，因此不会对用户线程产生任何block。然后，内核会等待数据准备完成，然后将数据拷贝到用户线程，当这一切都完成之后，内核会给用户线程发送一个信号，告诉它read操作完成了。也就说用户线程完全不需要实际的整个IO操作是如何进行的，只需要先发起一个请求，当接收内核返回的成功信号时表示IO操作已经完成，可以直接去使用数据了。

也就说在异步IO模型中，IO操作的两个阶段都不会阻塞用户线程，这两个阶段都是由内核自动完成，然后发送一个信号告知用户线程操作已完成。用户线程中不需要再次调用IO函数进行具体的读写。这点是和信号驱动模型有所不同的，在信号驱动模型中，当用户线程接收到信号表示数据已经就绪，然后需要用户线程调用IO函数进行实际的读写操作；而在异步IO模型中，收到信号表示IO操作已经完成，不需要再在用户线程中调用iO函数进行实际的读写操作。

注意，异步IO是需要操作系统的底层支持，在Java 7中，提供了Asynchronous IO。

前面四种IO模型实际上都属于同步IO，只有最后一种是真正的异步IO，因为无论是多路复用IO还是信号驱动模型，IO操作的第2个阶段都会引起用户线程阻塞，也就是内核进行数据拷贝的过程都会让用户线程阻塞。

用户进程发起`aio_read`操作之后，给内核传递描述符、缓冲区指针、缓冲区大小等，告诉内核当整个操作完成时，如何通知进程，然后就立刻去做其他事情了。当内核收到`aio_read`后，会立刻返回，然后内核开始等待数据准备，数据准备好以后，直接把数据拷贝到用户空间，然后再通知进程本次IO完成。

# 两种高性能IO设计模式

对于多线程模式，也就说来了client，服务器就会新建一个线程来处理该client的读写事件。

这种模式虽然处理起来简单方便，但是由于服务器为每个client的连接都采用一个线程去处理，使得资源占用非常大。因此，当连接数量达到上限时，再有用户请求连接，直接会导致资源瓶颈，严重的可能会直接导致服务器崩溃。

因此，为了解决这种一个线程对应一个客户端模式带来的问题，提出了采用线程池的方式，也就说创建一个固定大小的线程池，来一个客户端，就从线程池取一个空闲线程来处理，当客户端处理完读写操作之后，就交出对线程的占用。因此这样就避免为每一个客户端都要创建线程带来的资源浪费，使得线程可以重用。

但是线程池也有它的弊端，如果连接大多是长连接，因此可能会导致在一段时间内，线程池中的线程都被占用，那么当再有用户请求连接时，由于没有可用的空闲线程来处理，就会导致客户端连接失败，从而影响用户体验。因此，线程池比较适合大量的短连接应用。

因此便出现了下面的两种高性能IO设计模式：**Reactor**和**Proactor**。

在Reactor模式中，会先对每个client注册感兴趣的事件，然后有一个线程专门去轮询每个client是否有事件发生，当有事件发生时，便顺序处理每个事件，当所有事件处理完之后，便再转去继续轮询。

从这里可以看出，上面的五种IO模型中的多路复用IO就是采用Reactor模式。

在Proactor模式中，当检测到有事件发生时，会新起一个异步操作，然后交由内核线程去处理，当内核线程完成IO操作之后，发送一个通知告知操作已完成，可以得知，异步IO模型采用的就是Proactor模式。

# Java NIO

NIO主要有三大核心部分：Channel(通道)，Buffer(缓冲区), Selector。传统IO基于字节流和字符流进行操作，而NIO基于Channel和Buffer(缓冲区)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(选择区)用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个线程可以监听多个数据通道。

NIO和传统IO（一下简称IO）之间第一个最大的区别是，IO是面向流的，NIO是面向缓冲区的。 Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据。

IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。 NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变得可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。

## Channel

Java NIO的通道类似流，但又有些不同：

- 既可以从通道读取数据，又可以写数据到到通道。但流的读写通常是单向的。
- 通道可以异步地读写。
- 通道中的数据总是要读到一个Buffer，或者总是从一个Buffer中写入。

![img](https://oscimg.oschina.net/oscnet/a794435a095b75a9c5f86321bbbc1e9a273.jpg)

在Java NIO中，通道是用于在实体和字节缓冲区之间有效传输数据的介质。它从一个实体读取数据，并将其放在缓冲区块中以供消费。

通道作为Java NIO提供的网关来访问I/O机制。 通常，通道与操作系统文件描述符具有一对一关系，用于提供平台独立操作功能。

### NIO通道基础

通道实现是使用本地代码执行实际操作。通道接口允许我们以便捷和受控的方式访问低级IO服务。

在层次结构的顶部，通道接口如下所示：

```java
package java.nio.channels;  
 public interface Channel{  
    public boolean isclose();  
    public void Open() throws IOException;  
}
```

所有通道只有两个常用操作：

- 检查通道是否关闭(isclose())
- 关闭通道(close())

### 通道实现

在Java NIO中，主要使用的通道如下：

- `FileChannel`：文件通道用于从文件读取数据。它只能通过调用`getChannel()`方式来创建对象。不能直接创建`FileChannel`对象。

```java
FileInputStream fis = new FileInputStream(filename);
FileChannel channel = fis.getChannel();
```

- `DatagramChannel`：数据报通道可以通过UDP(用户数据报协议)通过网络读取和写入数据。它使用工厂方法来创建对象。

```java
DatagramChannel ch = DatagramChannel.open();
DatagramChannel ch = DatagramChannel.close();
```

- `SocketChannel`：数据报通道可以通过TCP(传输控制协议)通过网络读取和写入数据。它使用工厂方法来创建对象。

```java
SocketChannel ch = SocketChannel.open();  
ch.connect(new InetSocketAddress("somehost", someport));

SocketChannel ch = SocketChannel.close();
ch.connect(new InetSocketAddress("somehost", someport));
```

- `ServerSocketChannel`：ServerSocketChannel允许用户监听传入的TCP连接，与WEB服务器相同。对于每个传入连接，都会为连接创建一个SocketChannel。

```java
ServerSocketChannel ch = ServerSocketChannel.open();
ch.socket().bind(new InetSocketAddress(port));

ServerSocketChannel ch = ServerSocketChannel.open();
ch.socket().bind(new InetSocketAddress(port));
```

### 创建

无法直接打开一个FileChannel，需要通过InputStream、OutputStream或RandomAccessFile的getChannel方法来获取一个FileChannel实例

FileChannel常用方法：

- **read(ByteBuffer)**：从文件通道读取数据到Buffer，-1表示已经到达输入的末尾

- **write(ByteBuffer)**：从字节缓冲区写到文件通道

- **close()**：关闭

- **transferFrom()**：将数据从源通道传输到FileChannel中

  ```java
  toChannel.transferFrom(position, count, fromChannel);
  ```

- **transferTo()**：将数据从FileChannel传输到其他的channel中

  ```java
  fromChannel.transferTo(position, count, toChannel);
  ```

- **position()**：获取当前位置

- **position(int)**：设置当前位置

- **size()**：返回该实例所关联的文件的大小

- **truncate(int)**：截取文件的前int个字节

- **force(boolean)**：将通道里尚未写入磁盘的内存缓存数据强制写到磁盘上，boolean指明是否同时将文件元数据（权限信息等）写到磁盘上

### 示例

> **FileChannel**

```java
            try(RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\nio-data.txt", "r")){
            //利用channel中的FileChannel来实现文件读取
            FileChannel channel = file.getChannel();
            //设置缓冲区容量
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            //从通道中读取数据到缓冲区，返回读取的字节数量
            int read = channel.read(byteBuffer);
            //数量为-1表示读取完毕
            while (read != -1) {
                //切换模式为读模式，其实就是把position的位置设置为0，可以从0开始读取
                byteBuffer.flip();
                //如果缓冲区还有数据
                while (byteBuffer.hasRemaining()) {
                    //输出一个字符
                    System.out.print((char) byteBuffer.get());
                }
                //数据读取完毕后清空缓冲区
                byteBuffer.clear();
                //继续把通道内剩余数据写入缓冲区
                read = channel.read(byteBuffer);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
```

> 文件拷贝

```java
package com.keehoo.channel;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/11
 */
public class ChannelDemo {
    @Test
    public void testChannelToBuffer() {
        try (RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\channel-read.txt", "r");
             //利用channel中的FileChannel来实现文件读取
             FileChannel channel = file.getChannel();) {

            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            //设置缓冲区容量
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            //从通道中读取数据到缓冲区，返回读取的字节数量
            int read = channel.read(byteBuffer);
            //数量为-1表示读取完毕
            while (read != -1) {
                System.out.print("read:" + read + ":");
                //切换模式为读模式，其实就是把position的位置设置为0，可以从0开始读取
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();
                //如果缓冲区还有数据
                while (charBuffer.hasRemaining()) {
                    //输出缓冲区内容
//                    System.out.print(((char) byteBuffer.get()));
                    System.out.print(charBuffer.get());
                }
                System.out.println();
                //数据读取完毕后清空缓冲区,（直接把position复位为0，可以直接覆盖内容）
                byteBuffer.clear();
                charBuffer.clear();
                //继续把通道内剩余数据写入缓冲区
                read = channel.read(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBufferToChannel(){
        try(RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\channel-write.txt", "rw")){
            FileChannel writeChannel = file.getChannel();

            //分配缓冲区大小
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello world, nio write!".getBytes());
            //重置position的值
            buffer.flip();
            writeChannel.write(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Buffer

缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。

它定义了所有缓冲区通用的核心功能：限制，容量和当前位置。

Java NIO缓冲区用于与NIO通道进行交互。这是写入数据的内存块，以便在稍后再次进行读取。

### Buffer的主要属性

| 属性     |                  ##功能                  |
| -------- | :--------------------------------------: |
| capacity |                   容量                   |
| position | 缓冲区当前位置指针，最大可为capacity – 1 |
| limit    |         缓冲区最大读取和写入限制         |

### Buffer属性示意图

![img](https://oscimg.oschina.net/oscnet/3223ca81b3f2d27e527caa8f672b4953e41.jpg)

上图展示了写模式和读模式下，以上属性的示意图。写模式下，limit和capacity是一样的，这表示你能写入的最大容量数据。在读模式下，limit会和position一样，表示你能读到写入的全部数据。

### 缓冲区类型

对于每个原始类型，都有一个缓冲区类型，所有缓冲区都可以实现缓冲区接口。大多数使用的缓冲区类型是ByteBuffer。

在Java NIO中使用的核心缓冲区如下:

- CharBuffer

- DoubleBuffer

- IntBuffer

- LongBuffer

- ByteBuffer

- ShortBuffer

- FloatBuffer

  上述缓冲区覆盖了我们可以通过IO发送的基本数据类型：`characters`、`double`、`int`、`long`、`byte`、`short`和`float`。

  在NIO中，使用`java.nio.Buffer`类中实现的缓冲区进行数据传输。它与数组类似，具有固定的容量大小。

![img](http://www.yiibai.com/uploads/images/201709/2809/124090952_19645.png)

### 分配缓冲区

为了获得缓冲区对象，我们必须首先分配一个缓冲区。在每个`Buffer`类中，`allocate()`方法用于分配缓冲区。

```java
//ByteBuffer分配容量为28字节
ByteBuffer buf = ByteBuffer.allocate(28);
//CharBuffer分配空间为2048个字符
CharBuffer buf = CharBuffer.allocate(2048);
```

### 从缓冲区读取数据

从缓冲区读取数据有两种方法：

- 通过使用`get()`方法之一读取`Buffer`中的数据。

  ```java
  byte aByte = buf.get();
  ```

- 将数据从缓冲区读入通道

  ```java
  int bytesWritten = inChannel.write(buf);
  ```

###  将数据写入缓冲区

将数据写入缓冲区有两种方法：

- 使用`put()`方法将数据写入缓冲区
- 将数据从通道写入缓冲区

### 使用步骤

1. 写入数据到Buffer
2. 调用`flip()`方法调整指针位置准备读取
3. 从Buffer中读取数据
4. 调用`clear()`方法或者`compact`方法清理缓存

### 主要属性值

- **capacity**：Buffer缓冲区大小，只能写入capacity个原始类型。不会改变。
- **position**：下一个要被读写的数据索引。
- **limit**：第一个不被读写的数据的索引位置，通常就是缓冲区中实际数据的字节数

### 主要方法

- **flip()**：反转缓冲区，将limit的值设为position的值，然后position的值设为0。为从缓冲区读取字节做准备
- **rewind()**：从头再读或再写，limit不变，position的设置为0。
- **mark()**：标记当前的position的值，和reset()配合使用
- **reset()**：将当前position设为mark标记的值
- **hasRemaining()**：position和limit之间是否还有元素。
- **clear()**：清空整个缓冲区（没有擦除）。未读数据复制到缓冲区的起始处，position设到最后一个未读数据后。
- **compact()**：将所有未读数据拷贝到缓冲区起始处，然后将position设置到最后一个未读元素的正后面。limit依然像clear()方法一样，设置成capacity。

- **equals()**

当满足下列条件时，表示两个Buffer相等：

1. 有相同的类型(byte、char、int等)
2. Buffer中剩余的byte、char等的个数相等
3. Buffer中所有剩余的byte、char等都相等

- **compareTo()**

比较两个同类型Buffer的剩余元数，如果满足下列条件，则认为一个Buffer小于另一个Buffer：

1. 第一个不相等的元素小于另一个Buffer中对应的元素
2. 所有元素都相等，但第一个Buffer比另一个Buffer先耗尽（第一个Buffer的元素个数比另一个少）

### ByteBuffer

ByteBuffer是唯一直接与通道交换的缓冲器。

静态方法：

- **ByteBuffer wrap(Byte[] array)**：使用”支持“数组初始化一个ByteBuffer，数组和缓存的数据相互关联。

实例方法：

- **asXXBuffer系列方法**：可以获得特定基本数据类型的视图。ByteBuffer依然是实际存储数据的地方。
- **slice()**：创建新的ByteBuffer对象，和原来对象的字节数据相互关联，它们的区别是指针对象相互独立。

### 示例

```java
package com.keehoo.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/29
 */
public class BufferDemo {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\channel-read.txt", "r")){
            FileChannel readChannel = file.getChannel();
            //分配buffer
            ByteBuffer buffer = ByteBuffer.allocate(48);
            int read = readChannel.read(buffer);
            //buffer已经读满
            while (read != -1){
                buffer.flip();
                //buffer有内容
                while (buffer.hasRemaining()){
                    System.out.print((char)buffer.get());
                }
                //清空buffer
                buffer.clear();
                read = readChannel.read(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

## Charset

把字符集包装成对象，提供字符集的编码器和解码器

```java
		Charset.forName("UTF-8").encode("Hello World!")
    
       	Charset cs1=Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce=cs1.newEncoder();
        //获取解码器
        CharsetDecoder cd=cs1.newDecoder();
		//申请1024字节的空间地址
        CharBuffer  cbuf=CharBuffer.allocate(1024);
        cbuf.put("松鼠君");
		//转化为读模式
        cbuf.flip();
        //编码
        ByteBuffer bBuf=ce.encode(cbuf);
        for (int i=0;i<6;i++){
            System.out.println(bBuf.get());
        }
        bBuf.flip();//转化为读模式
		System.out.println(cd.decode(bBuf));
```

## Java NIO分散/聚集或向量

在Java NIO中，通道提供了称为分散/聚集或向量IO的重要功能。这是一种简单但功能强大的技术，通过这种技术，使用单个`write()`函数将字节从一组缓冲区写入流，并且可以使用单个`read()`函数将字节从流读取到一组缓冲区中。

Java NIO已经内置了分散/聚集的支持。它可以用于从通道读取和写入通道。

### 分散读取

用于将数据从单个通道读取多个缓冲区中的数据![img](http://www.yiibai.com/uploads/images/201709/2809/705100927_11990.png)

```java
public interface ScatteringByteChannel extends ReadableByteChannel{
    public long read(ByteBuffer[] args) throws IOException;
    public long read(ByteBuffer[] args, int length, int offset) throws IOException;
}
```

### 聚集写入

用于将数据从多个缓冲区写入单个通道![img](http://www.yiibai.com/uploads/images/201709/2809/184100931_96335.png)

```java
public interface GatheringByteChannel extends WritableByteChannel{
    public long write(ByteBuffer[] args) throws IOException;
    public long write(ByteBuffer[] args, int length, int offset) throws IOException;
}
```

### 基本散点/聚集示例

第一个缓冲区保存随机数，第二个缓冲区使用分散/聚集机制保存写入的数据

```java
package com.keehoo.buffer.cattergatherio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/24
 */
public class Demo {
    public static void main(String[] args) {
        String data = "hello world";
        getherBytes(data);
        scatterBytes();
    }

    /**
     * used for reading the bytes from a file channel into a set of buffers
     */
    private static void scatterBytes() {
        //the first buf is used for holding a random number
        ByteBuffer buf1 = ByteBuffer.allocate(8);
        //the second buf is used for holding a data that we want to write
        ByteBuffer buf2 = ByteBuffer.allocate(400);
        ScatteringByteChannel scatter = createChannelInstance("buffer-out.txt", false);
        //reading a data from the channel
        try {
            scatter.read(new ByteBuffer[]{buf1, buf2});
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read the two buffers seperately
        buf1.rewind();
        buf2.rewind();

        int bufferOne = buf1.asIntBuffer().get();
        String bufferTwo = buf2.asCharBuffer().toString();
        //verification of content
        System.out.println(bufferOne);
        System.out.println(bufferTwo);
    }

    /**
     * used for reading the bytes from the buffers and write it to a file channel
     * @param data
     */
    private static void getherBytes(String data) {
        //the first buffer is used for holding a random number
        ByteBuffer buf1 = ByteBuffer.allocate(8);
        Charset charset = Charset.forName("UTF-8");
        //the second buffer is used for holding a data that we want to write
        ByteBuffer buf2 = ByteBuffer.allocate(400);
        buf1.asIntBuffer().put(97);
        buf2.asCharBuffer().put(data);
        GatheringByteChannel gatherer = createChannelInstance("buffer-out.txt", true);
        //write the data into file
        try {
            gatherer.write(new ByteBuffer[]{buf1, buf2});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileChannel createChannelInstance(String file, boolean isOutput) {
        FileChannel fileChannel = null;
        try {
            if(isOutput){
                fileChannel = new FileOutputStream(file).getChannel();
            }else{
                fileChannel = new FileInputStream(file).getChannel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileChannel;
    }
}

```

## Java NIO通道之间的数据传输

在Java NIO中，可以非常频繁地将数据从一个通道传输到另一个通道。批量传输文件数据是非常普遍的，因为几个优化方法已经添加到`FileChannel`类中，使其更有效率。

通道之间的数据传输在`FileChannel`类中的两种方法是：

- **FileChannel.transferTo()**：用来从FileChannel到其他通道的数据传输

  ```java
  public abstract class Channel extends AbstractChannel {
      public abstract long transferTo(long position, long count, WritableByteChannel target);
  }
  ```

- **FileChannel.transferFrom()**：从源通道到FileChannel的数据传输

  ```java
  public abstract class Channel extends AbstractChannel{
      public abstract long transferFrom(ReadableByteChannel src, long position, long count);
  }
  ```

### 基本通道到通道数据的传输示例

从4个不同文件读取文件内容的简单示例，并将它们的组合输出写入到第五个文件：

```java
package com.keehoo.channel;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/24
 */
public class ChannelTransferDemo {
    private String filePath = "C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\";
    private RandomAccessFile fromFile;
    private RandomAccessFile toFile;
    private FileChannel fromChannel;
    private FileChannel toChannel;

    @Before
    public void setUp() throws FileNotFoundException {
        RandomAccessFile fromFile = new RandomAccessFile(filePath + "fromFile.txt", "rw");
        RandomAccessFile toFile = new RandomAccessFile(filePath + "toFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        FileChannel toChannel = toFile.getChannel();
        this.toFile = toFile;
        this.fromFile = fromFile;
        this.toChannel = toChannel;
        this.fromChannel = fromChannel;
    }

    /**
     * 将数据从源通道传输到fileChannel中
     */
    @Test
    public void testTransferFrom() throws IOException {
        long position = 0;
        long count = fromChannel.size();
        toChannel.transferFrom(fromChannel, position, count);
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        toChannel.read(buffer);
        System.out.print(new String(buffer.array()));
    }

    @Test
    public void testTransferTo() throws IOException {
        long position = 0;
        long count = fromChannel.size();
        fromChannel.transferTo(position, count, toChannel);
    }
}

```

## Selector

选择器是Java NIO中能够检测多个NIO通道，并能够知道通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接，提高效率。

使用了选择器就可以用一个线程管理多个channel，如果多个channel有多个线程管理，线程之前的切换是消耗资源的，而单个线程就避免了线程之间的消耗。

### 选择器常用方法

- **register(Selector selector, int ops)**：向选择器注册通道，并且可以选择注册指定的事件，目前事件分为4种：1.Connect，2.Accept，3.Read， 4.Write，一个通道可以注册多个事件
- **select()**：阻塞到至少有一个通道在你注册的事件上就绪了
- **selectNow()**：不会阻塞，不管什么通道就绪都立刻返回
- **select(long timeout)**：和select()一样，除了最长会阻塞timeout毫秒
- **selectedKeys()**：一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，然后可以通过调用selector的selectedKeys()方法，访问"以选择键集(selected key set)"中的就绪通道
- **wakeUp()**：可以使调用select()阻塞的对象返回，不阻塞。
- **close()**：用完selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有selectionKey实例无效。通道本身不会关闭
- **open()**：创建一个Selector

