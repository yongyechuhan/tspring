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
                jmsMessServer.init();
            }
        });
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                jmsMessClient.initClient();
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
}
