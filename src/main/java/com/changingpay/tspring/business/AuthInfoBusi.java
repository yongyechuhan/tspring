package com.changingpay.tspring.business;

import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import com.changingpay.tspring.model.TAuthorityAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 公司 on 2017/2/15.
 */
@Service
public class AuthInfoBusi {

    @Autowired
    private TAuthorityAuthInfoMapper authInfoMapper;

    @Transactional(propagation= Propagation.REQUIRED)
    public int insert600(){
        TAuthorityAuthInfo authInfo =
                new TAuthorityAuthInfo();
        authInfo.setAuthId("600000");
        authInfo.setStatus("1");
        authInfo.setAuthDesc("测试独立事务S");
        try {
            System.out.println("------插入600------");
            authInfoMapper.insertSelective(authInfo);
        }catch (Exception e){
            throw new RuntimeException("保存数据600失败");
        }finally {
            System.out.println("------插入600结束------");
        }
        return 0;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public int insert100(CountDownLatch[] downLatches){
        TAuthorityAuthInfo authInfo =
                new TAuthorityAuthInfo();
        authInfo.setAuthId("100000");
        authInfo.setStatus("1");
        authInfo.setAuthDesc("测试独立事务F");
        try {
            downLatches[1].await();
            System.out.println("------插入100------");
            authInfoMapper.insertSelective(authInfo);
        }catch (Exception e){

        }finally {
            System.out.println("------插入100结束------");
            downLatches[0].countDown();
        }
        return 0;
    }
}
