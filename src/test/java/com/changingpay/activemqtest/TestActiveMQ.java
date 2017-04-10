package com.changingpay.activemqtest;

import com.changingpay.base.LoadTestConfig;
import com.changingpay.tspring.business.activemq.JmsMessClient;
import com.changingpay.tspring.business.activemq.JmsMessServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Liuxin on 2017/4/2.
 */
public class TestActiveMQ extends LoadTestConfig {
    @Autowired
    private JmsMessServer jmsMessServer;

    @Autowired
    private JmsMessClient jmsMessClient;

    @Test
    public void testActiveMess(){
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                jmsMessServer.initP2PServer();
            }
        });
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                jmsMessClient.initP2PClient();
            }
        });
        serverThread.start();
        clientThread.start();
        try {
            serverThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPublishSubMess(){
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                jmsMessServer.initPublisher();
            }
        });
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                jmsMessClient.initSubscribe();
            }
        });
        clientThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverThread.start();
        try {
            serverThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
