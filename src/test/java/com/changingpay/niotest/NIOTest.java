package com.changingpay.niotest;

import com.changingpay.tspring.business.nio.NIOClient;
import com.changingpay.tspring.business.nio.NIOServer;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by 公司 on 2017/2/20.
 */
public class NIOTest {
    @Test
    public void testSendByNIO(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NIOServer serv = new NIOServer();
                try {
                    serv.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                NIOClient client = new NIOClient();
                try {
                    client.readyForSend();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        t1.start();
        try {
            t.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
