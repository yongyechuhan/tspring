package com.changingpay.springtest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Liuxin on 2017/3/31.
 */
public class TestBeanLoad {

    private Log logger = LogFactory.getLog(TestBeanLoad.class);

    @Test
    public void testFileSystemXmlApplication(){
        ApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ThreadPoolTaskExecutor taskExecutor = ac.getBean(ThreadPoolTaskExecutor.class);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("测试JVM");
                while(true){
                    logger.info("-----------线程池执行中------------");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        logger.error("线程睡眠失败!", e);
                    }
                }
            }
        });
        while(true);
    }
}
