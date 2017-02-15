package com.changingpay.tspring.business;

import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import com.changingpay.tspring.model.TAuthorityAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;


@Service
public class BuyTicket{
	
	@Autowired 
	private TAuthorityAuthInfoMapper authInfoMapper;

	@Autowired
	private AuthInfoBusi authInfoBusi;

	private static int i = 0;

	static CountDownLatch start6 = new CountDownLatch(1);
	static CountDownLatch start1 = new CountDownLatch(1);
	static CountDownLatch[] downLatches = {start1,start6};

	private static final Object lockA = new Object();
	private static final Object lockB = new Object();

	@Transactional(propagation=Propagation.REQUIRED)
	public int insert666(){
		TAuthorityAuthInfo authInfo = 
					new TAuthorityAuthInfo();
		authInfo.setAuthId("666666");
		authInfo.setAuthDesc("test");
		authInfo.setStatus("0");
		System.out.println("------插入666------");
		authInfoMapper.insertSelective(authInfo);
		start6.countDown();
		authInfoBusi.insert600();
		authInfoBusi.insert100(downLatches);
		try {
			start1.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------插入666结束------");
		return 0;
	}

	public void getAuthInfo(String authId){
		TAuthorityAuthInfo authInfo =
				authInfoMapper.selectByPrimaryKey(authId);
	}

	public int deleteRole(){
		return authInfoMapper.deleteByPrimaryKey("666666");
	}

	public static int getI() {
		return i;
	}

	public static void setI(int i) {
		BuyTicket.i = i;
	}
}
