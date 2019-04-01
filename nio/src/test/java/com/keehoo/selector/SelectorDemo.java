package com.keehoo.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/26
 */
public class SelectorDemo {
    public static void main(String[] args) {
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
            //打开选择器
            Selector selector = Selector.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(8090));
            //与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字可以。
            serverSocketChannel.configureBlocking(false);
            //向通道注册选择器，并注册接受事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端启动成功...");
            while (true){
                //获取已经准备好的通道数量
                int readyChannels = selector.selectNow();
                //如果没有准备好，重试
                if(readyChannels == 0){
                    continue;
                }
                //获取准备好的通道中的事件集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if(key.isAcceptable()){
                        //在自己注册的事件中写业务逻辑
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        ByteBuffer buf1 = ByteBuffer.allocate(1024);
                        socketChannel.read(buf1);
                        buf1.flip();
                        if(buf1.hasRemaining()){
                            System.out.println(">>>服务端收到数据：" + new String(buf1.array()));
                        }
                        buf1.clear();

                        ByteBuffer header = ByteBuffer.allocate(6);
                        header.put("[head]".getBytes());
                        ByteBuffer body = ByteBuffer.allocate(1024);
                        body.put("this is body".getBytes());
                        header.flip();
                        body.flip();
                        ByteBuffer[] bufArray = {header, body};
                        socketChannel.write(bufArray);
                    }else if (key.isConnectable()) {
                    } else if (key.isReadable()) {
                    } else if (key.isWritable()) {
                    }
                    //注意每次迭代末尾的keyIterator.remove()调用。
                    //Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。
                    //下次该通道变成就绪时，Selector会再次将其放入已选择键集中
                    keyIterator.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
