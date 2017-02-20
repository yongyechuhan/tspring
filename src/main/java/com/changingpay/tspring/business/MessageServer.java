package com.changingpay.tspring.business;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@Service
public class MessageServer{
	@Autowired
	private TAuthorityAuthInfoMapper authInfoMapper;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private static final BlockingQueue<Socket> queue =
            new ArrayBlockingQueue<Socket>(10);

	public static CountDownLatch count = new CountDownLatch(10);

	private static ServerSocket socketServer;

	private static final Log logging = LogFactory.getLog(MessageServer.class);

	static{
		try {
			socketServer = new ServerSocket(9000);
			logging.info("初始化服务端成功");
		} catch (IOException e) {
			logging.error("初始化服务端出错"+e.getMessage());
		}
	}

    private static class NestProcess implements Runnable{
		private Socket socket;
		private InputStream input;
		private OutputStream output;

		NestProcess(Socket socket){
			this.socket = socket;
			try {
				input = socket.getInputStream();
				output = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        @Override
        public void run() {
            byte[] mess = new byte[2048];
            StringBuilder s = new StringBuilder("");
            int rc;
            try {
                input.read(mess);
                s.append(new String(mess));
                logging.info("接收到信息："+s);
                synchronized (MessageServer.class) {
                    count.countDown();
                    System.out.println("当前剩余 "+count.getCount());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收线程后，将socket放入线程安全队列中。
     */
    private static class NestAccept implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    Socket socket = socketServer.accept();
                    queue.put(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public void acceptClient(){
	    //提交SocketServer接收线程。
        taskExecutor.execute(new NestAccept());

        while(count.getCount() > 0){
            Socket socket = queue.poll();
            if(socket != null) {
                taskExecutor.execute(new NestProcess(socket));
            }
        }
	}
}
