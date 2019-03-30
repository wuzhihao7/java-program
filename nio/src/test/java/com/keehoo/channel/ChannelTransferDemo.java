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
