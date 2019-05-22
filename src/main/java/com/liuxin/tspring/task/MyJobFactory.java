/**
 * Project Name:pay-system-webapp
 * File Name:MyJobFactory.java
 * Package Name:com.unionpay.paysystem.jobtask
 * Date:2016年6月14日上午11:56:26
 * Copyright (c) 2016, Guizhou UnionPay All Rights Reserved.
 *
 */
package com.liuxin.tspring.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * ClassName: MyJobFactory <br/>
 * Description:扩展JobFactory 自动注入服务接口 <br/>
 * Date: 2016年6月14日 上午11:56:26 <br/>
 * @author Calvin Wu <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 */
public class MyJobFactory extends AdaptableJobFactory {
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
