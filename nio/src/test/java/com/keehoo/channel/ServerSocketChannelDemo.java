package com.technologysia.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/26
 */
public class ServerSocketChannelDemo {
    public static void main(String[] args) {
        try {
            //创建一个服务端socket并打开
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //绑定8090监听端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8090);
            serverSocketChannel.socket().bind(inetSocketAddress);
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            while (true){
                //获取请求连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(socketChannel != null){
                    ByteBuffer buf1 = ByteBuffer.allocate(1024);
                    socketChannel.read(buf1);
                    buf1.flip();
                    if(buf1.hasRemaining()){
                        System.out.println(">>>服务端收到的数据：" + new String(buf1.array()));
                    }
                    buf1.clear();
                    //构造返回的报文，分为头部和主体
                    ByteBuffer header = ByteBuffer.allocate(6);
                    header.put("[head]".getBytes());
                    ByteBuffer body = ByteBuffer.allocate(1024);
                    body.put("keehoo".getBytes());
                    header.flip();
                    body.flip();
                    ByteBuffer[] bufferArray = {header, body};
                    socketChannel.write(bufferArray);
                    socketChannel.close();
                }else{
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
