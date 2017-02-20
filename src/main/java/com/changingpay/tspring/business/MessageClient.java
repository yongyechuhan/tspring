package com.changingpay.tspring.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by 公司 on 2017/2/16.
 */
public class MessageClient implements Runnable{

    private static final Log log = LogFactory.getLog(MessageClient.class);

    private static final String ip = "127.0.0.1";

    private static final int port = 9000;

    public Socket getSocket(){
        try {
            return new Socket(ip,port);
        } catch (IOException e) {
            log.error("初始化连接服务端失败。"+e.getMessage());
        }
        return null;
    }

    @Override
    public void run() {
        Socket socket = this.getSocket();
        if(socket != null) new SendMess(socket).sendMessage();
    }

    public static class SendMess{
        private Socket socket;
        private InputStream in;
        private OutputStream out;
        private String threadName;
        public SendMess(Socket socket){
            this.socket = socket;
            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                threadName = Thread.currentThread().getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(){
            OutputStreamWriter writer =
                    new OutputStreamWriter(out);
            try {
                writer.write("线程"+threadName+"签到");
                writer.flush();
            } catch (IOException e) {
                log.error("发送信息失败"+e.getMessage());
            }
        }
    }
}
