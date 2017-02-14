package com.changingpay.daotest;

import com.changingpay.tspring.business.BuyTicket;
import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import com.changingpay.tspring.model.TAuthorityAuthInfo;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 公司 on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:spring-mybatis.xml"})
public class TransactionTest {
    @Autowired
    private TAuthorityAuthInfoMapper authInfoMapper;

    @Autowired
    private BuyTicket buyTicket;

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    public void getAuthInfo(){
        buyTicket.insertRole();

            TAuthorityAuthInfo authInfo =
                    new TAuthorityAuthInfo();
            authInfo.setAuthId("100000");
            authInfo.setStatus("1");
            authInfo.setAuthDesc("测试事务");
            authInfoMapper.insert(authInfo);
            System.out.println(authInfo.getAuthDesc());
            //buyTicket.deleteRole();

    }
}
