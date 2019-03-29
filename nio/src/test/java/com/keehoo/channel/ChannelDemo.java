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
