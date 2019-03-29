package com.keehoo.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/23
 */
public class FileCopyDemo {
    public static void main(String[] args) {
        try (
                FileInputStream fis = new FileInputStream("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\channel-demo.txt");
                FileOutputStream fos = new FileOutputStream("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\nio-data-output.txt")
        ){
            FileChannel fisChannel = fis.getChannel();
            FileChannel fosChannel = fos.getChannel();
            copy(fisChannel, fosChannel);
            System.out.println("done.");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static void copy(FileChannel fisChannel, FileChannel fosChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = -1;
        try {
            read = fisChannel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (read != -1){
            //the buffer is used to drained
            buffer.flip();
            //keep sure that buffer was fully drained
            while (buffer.hasRemaining()){
                try {
                    fosChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Now the buffer is empty, ready for the filling
            buffer.clear();
            try {
                read = fisChannel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
