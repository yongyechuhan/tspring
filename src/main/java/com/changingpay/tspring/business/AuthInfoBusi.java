package com.changingpay.tspring.business;

import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import com.changingpay.tspring.model.TAuthorityAuthInfo;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 公司 on 2017/2/15.
 */
@Service
public class AuthInfoBusi {

    private static volatile boolean flag = true;

    @Autowired
    private TAuthorityAuthInfoMapper authInfoMapper;

    @Transactional(propagation=Propagation.REQUIRED)
    public int insert666() {
        TAuthorityAuthInfo authInfo =
                new TAuthorityAuthInfo();
        authInfo.setAuthId("666666");
        authInfo.setAuthDesc("test");
        authInfo.setStatus("0");
        System.out.println("------插入666------");
        authInfoMapper.insertSelective(authInfo);
        insert100();
        System.out.println("------插入666结束------");
        return 0;
    }

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
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

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_UNCOMMITTED)
    public void getAuthInfo(String authId){
        TAuthorityAuthInfo authInfo =
                authInfoMapper.selectByPrimaryKey(authId);
        LogFactory.getLog(AuthInfoBusi.class).info("读取600 "+authInfo == null);
        while(flag){
            if(authInfo != null) {
                synchronized (AuthInfoBusi.class) {
                    LogFactory.getLog(AuthInfoBusi.class).info("线程" + Thread.currentThread().getName() + "准备删除600");
                    authInfoMapper.deleteByPrimaryKey(authId);
                    flag = false;
                    LogFactory.getLog(AuthInfoBusi.class).info("线程" + Thread.currentThread().getName() + "已删除600");
                }
            }
        }
    }

    /**
     * 无论该方式事务是否属于独立事务，当方法发生异常后。
     * 上层事务均会回滚。
     * 独立事务不受外部事务影响。如果独立事务内部不会发生异常。
     * 事务一定会得到执行。
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public int insert100(){
        TAuthorityAuthInfo authInfo =
                new TAuthorityAuthInfo();
        authInfo.setAuthId("100000");
        authInfo.setStatus("1");
        authInfo.setAuthDesc("测试独立事务F");
        try {
            System.out.println("------插入100------");
            authInfoMapper.insertSelective(authInfo);
        }catch (Exception e){
            throw new RuntimeException("保存数据100失败");
        }finally {
            System.out.println("------插入100结束------");
        }
        return 0;
    }
}
