package com.changingpay.daotest;

import com.changingpay.tspring.business.AuthInfoBusi;
import com.changingpay.tspring.business.MessageClient;
import com.changingpay.tspring.business.MessageServer;
import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by 公司 on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:spring-mybatis.xml"})
public class TransactionTest {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private static final Log log =
            LogFactory.getLog(TransactionTest.class);

    @Autowired
    private TAuthorityAuthInfoMapper authInfoMapper;

    @Autowired
    private AuthInfoBusi authInfoBusi;

    @Autowired
    private MessageServer socketServer;

    /**
     * 主要测试事务传播特性。
     */
    @Test
    public void saveAuthInfo(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                authInfoBusi.insert666();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主要测试事务隔离级别。
     */
    @Test
    public void getAuthInfo(){
        Runnable insertAuthInfo = new Runnable() {
            @Override
            public void run() {
                authInfoBusi.insert600();
            }
        };
        Thread t =  new Thread(insertAuthInfo);
        Runnable getAuthInfo = new Runnable() {
            @Override
            public void run() {
                authInfoBusi.getAuthInfo("600000");
            }
        };
        t.start();
        try {
            t.join();
            for(int i = 0 ; i < 5; i++){
                Thread t1 = new Thread(getAuthInfo,"get"+i);
                t1.start();
                t1.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试socket通讯。
     */
    @Test
    public void sendMess(){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                socketServer.acceptClient();
            }
        });

        for(int i = 0;i < 10;i++){
            taskExecutor.execute(new MessageClient());
        }

        try {
            MessageServer.count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
