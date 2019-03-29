package com.keehoo.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/11
 */
public class ChannelDemo {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\channel-demo.txt", "r")) {
            //利用channel中的FileChannel来实现文件读取
            FileChannel channel = file.getChannel();
            //设置缓冲区容量
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);
            //从通道中读取数据到缓冲区，返回读取的字节数量
            int read = channel.read(byteBuffer);
            //数量为-1表示读取完毕
            while (read != -1) {
                System.out.print("read:" + read + ":");
                //切换模式为读模式，其实就是把position的位置设置为0，可以从0开始读取
                byteBuffer.flip();
                //如果缓冲区还有数据
                while (byteBuffer.hasRemaining()) {
                    //输出缓冲区内容
                    System.out.print(((char) byteBuffer.get()));
                }
                System.out.println();
                //数据读取完毕后清空缓冲区,（直接把position复位为0，可以直接覆盖内容）
                byteBuffer.clear();
                //继续把通道内剩余数据写入缓冲区
                read = channel.read(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
