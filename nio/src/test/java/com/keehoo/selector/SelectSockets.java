package com.technologysia.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 简单的回显服务器，它监听传入的流连接和回复它所读的任何内容。
 * 单个Selector对象用于监听服务器套接字（接受新连接）和所有活动套接字频道。
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/12
 */
public class SelectSockets {
    public static int PORT = 8090;

    /**
     * use the same byte buffer for all channels.
     * A single thread is servicing all the channels, so no danger of concurrent access.
     * 直接内存不能用Buffer.array()方法
     */
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    public static void main(String[] args) throws IOException {
        new SelectSockets().go(args);
    }

    public void go(String[] args) throws IOException {
        int port = PORT;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }

        System.out.println("Listening on port "+port);
        //打开一个未绑定的ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个ServerSocket去和它绑定
        ServerSocket socket = serverSocketChannel.socket();
        //设置ServerSocketChannel将会监听的端口
        socket.bind(new InetSocketAddress(port));
        //创建选择器
        Selector selector = Selector.open();
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //将serverSocketChannel注册到selector
        SelectionKey register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //this may block for a long time. Upon returning, the selected set contains keys of the ready channels.
            int select = selector.select();
            if(select == 0){
                //nothing to do
                continue;
            }
            //get an iterator over the set of selected keys
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //在被选择的set中遍历全部的key
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //判断是否是一个连接到来
                if(selectionKey.isAcceptable()){
                    //只有ServerSocketChannel 支持 OP_ACCEPT 操作
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    //注册读事件
                    registerChannel(selector, client, SelectionKey.OP_READ);
                    //对连接进行处理
                    sayHello(client);
                }
                //判断这个channel是否有数据要读
                if(selectionKey.isReadable()){
                    readDataFromSocket(selectionKey);
                }
                //从selected set中移除这个key，因为它已经被处理过了
                iterator.remove();
            }
        }
    }

    /**
     * 使用给定的选择器为给定的感兴趣的操作注册给定的通道
     * @param selector
     * @param channel
     * @param ops
     */
    protected void registerChannel(Selector selector, SelectableChannel channel, int ops) throws IOException {
        if(channel == null){
            //可能会发生
            return;
        }
        //设置通道为非阻塞
        channel.configureBlocking(false);
        //将通道注册到选择器上
        channel.register(selector, ops);
    }

    /**
     * 向传入的客户端连接发出问候语。
     * 新连接的SocketChannel也会打招呼。
     * @param channel
     */
    private void sayHello(SocketChannel channel) throws IOException {
        buffer.clear();
        buffer.put(("Hello World!" + System.lineSeparator()).getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    /**
     * 对于一个准备读入数据的通道的简单的数据处理方法
     * 一个选择器决定了和通道关联的SelectionKey object是准备读状态。如果通道返回EOF，通道将被关闭。
     * 并且会自动使相关的key失效，选择器然后会在下一次的select call时取消掉通道的注册
     * @param selectionKey
     */
    protected void readDataFromSocket(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        int count;
        //清空buffer
        buffer.clear();
        //可以读到数据时一直循环，通道为非阻塞
        while ((count = socketChannel.read(buffer)) != -1){
            //将缓冲区设置为可读
            buffer.flip();
            //发送数据，不要期望能一次将数据发送完
            while (buffer.hasRemaining()){
                //此代码可能会在繁忙的循环中旋转
                socketChannel.write(buffer);
            }
            buffer.clear();
        }
        //读取结束后关闭通道，使key失效
        System.out.println("关闭通道");
        socketChannel.close();
    }
}
