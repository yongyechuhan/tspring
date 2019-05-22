package com.liuxin.tspring.business.nio;

import com.liuxin.tspring.business.AbsBusi;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by 公司 on 2017/2/17.
 */
public class NIOBusi extends AbsBusi {

    private static final Log logging =
            AbsBusi.getLog(NIOBusi.class);

    public void readResources(String fileName){
        String filePath = getFilePath(fileName);
        if(StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("未能获取正确的文件路径。");
        }

        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(filePath, "rw");
        } catch (FileNotFoundException e) {
            logging.error(e.getMessage());
        }
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = 0;
        try {
            bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {
                System.out.println("Read " + bytesRead);
                buf.flip();

                while(buf.hasRemaining()){
                    System.out.print((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePath(String filename){
        if(StringUtils.isNotEmpty(filename)){
            return NIOBusi.class.getClassLoader().getResource(filename).getPath();
        }else{
            return null;
        }
    }
}
