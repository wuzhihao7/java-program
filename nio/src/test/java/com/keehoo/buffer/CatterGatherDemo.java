package com.technologysia.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/24
 */
public class CatterGatherDemo {
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
        ScatteringByteChannel scatter = createChannelInstance("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\gather-out.txt", false);
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
     *
     * @param data
     */
    private static void getherBytes(String data) {
        //the first buffer is used for holding a random number
        ByteBuffer buf1 = ByteBuffer.allocate(8);
        //the second buffer is used for holding a data that we want to write
        ByteBuffer buf2 = ByteBuffer.allocate(400);
        buf1.asIntBuffer().put(97);
        buf2.asCharBuffer().put(data);
        GatheringByteChannel gatherer = createChannelInstance("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\test\\resources\\gather-out.txt", true);
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
            if (isOutput) {
                fileChannel = new FileOutputStream(file).getChannel();
            } else {
                fileChannel = new FileInputStream(file).getChannel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileChannel;
    }
}
