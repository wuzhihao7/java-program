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
        try (RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\channel-demo.txt", "r")){
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
