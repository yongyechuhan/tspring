package com.changingpay.tspring.business;

import com.changingpay.tspring.dao.TAuthorityAuthInfoMapper;
import com.changingpay.tspring.model.TAuthorityAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BuyTicket{
	
	@Autowired 
	private TAuthorityAuthInfoMapper authInfoMapper;

	private static int i = 0;
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertRole(){
		TAuthorityAuthInfo authInfo = 
					new TAuthorityAuthInfo();
			
		authInfo.setAuthId("666666");
		authInfo.setAuthDesc("test");
		authInfo.setStatus("0");

		return authInfoMapper.insertSelective(authInfo);
	}

	public static int getI() {
		return i;
	}

	public static void setI(int i) {
		BuyTicket.i = i;
	}
}
