package com.changingpay.daotest;

import com.changingpay.tspring.business.BuyTicket;
import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 公司 on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:spring-mybatis.xml"})
public class TransactionTest {

    private static final Log log =
            LogFactory.getLog(TransactionTest.class);

    @Autowired
    private TAuthorityAuthInfoMapper authInfoMapper;

    @Autowired
    private BuyTicket buyTicket;

    @Test
    public void getAuthInfo(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                buyTicket.insert666();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
