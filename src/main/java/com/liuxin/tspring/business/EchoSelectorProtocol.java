package com.liuxin.tspring.business;

import com.liuxin.tspring.business.iface.TCPProtocol;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by 公司 on 2017/2/20.
 */
public class EchoSelectorProtocol extends AbsBusi implements TCPProtocol{

    private Log logging = getLog(EchoSelectorProtocol.class);

    private int bufSize; // 缓冲区的长度
    public EchoSelectorProtocol(int bufSize) {
        this.bufSize = bufSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        logging.info("Accept");
        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel clntChan = (SocketChannel) key.channel();
        //获取该信道所关联的附件，这里为缓冲区
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.clear();
        long bytesRead;
        //如果read（）方法返回-1，说明客户端关闭了连接，那么客户端已经接收到了与自己发送字节数相等的数据，可以安全地关闭
        while((bytesRead = clntChan.read(buf)) > 0){
            //如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写
            StringBuffer sb = new StringBuffer();
            buf.flip();
            sb.append(new String(buf.array(), 0, buf.limit()));
            logging.info("服务器端接受消息：" + sb);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        if (bytesRead == -1){
            clntChan.close();
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buffer= ByteBuffer.allocate(256);
        buffer.put("Is Readying for Process Data".getBytes());
        buffer.flip();
        SocketChannel clntChan = (SocketChannel) key.channel();
        //将数据写入到信道中
        System.out.println(buffer.position()+"   "+buffer.limit());
        while(buffer.hasRemaining()) {
            System.out.println(buffer.position()+"   "+buffer.limit());
            clntChan.write(buffer);
        }

        if (!buffer.hasRemaining()){
            //如果缓冲区中的数据已经全部写入了信道，则将该信道感兴趣的操作设置为可读
            key.interestOps(SelectionKey.OP_READ);
        }
        //为读入更多的数据腾出空间
        buffer.compact();
    }
}
