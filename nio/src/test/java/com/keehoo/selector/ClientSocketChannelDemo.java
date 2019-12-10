package com.technologysia.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/28
 */
public class ClientSocketChannelDemo {
    public static void main(String[] args) {
        try(
                //打开socket连接，连接服务端
                SocketChannel socketChannel = SocketChannel.open()){
            socketChannel.connect(new InetSocketAddress("localhost",8090));
            //请求服务端，发送请求
            ByteBuffer buf1 = ByteBuffer.allocate(1024);
            buf1.put("来自客户端的请求".getBytes());
            buf1.flip();
            if(buf1.hasRemaining()){
                socketChannel.write(buf1);
            }
            buf1.clear();
            //接收服务端返回数据
            ByteBuffer header = ByteBuffer.allocate(6);
            ByteBuffer body = ByteBuffer.allocate(1024);
            ByteBuffer[] bufArray = {header, body};
            socketChannel.read(bufArray);

            header.flip();
            body.flip();
            if(header.hasRemaining()){
                System.out.println(">>>客户端接收head数据：" + new String(header.array()));
            }
            if(body.hasRemaining()){
                System.out.println(">>>客户端接收body数据：" + new String(body.array()));
            }
            header.clear();
            body.clear();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
