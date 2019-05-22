package com.changingpay.threadtest;

import com.liuxin.tspring.thread.ConcurrentLinkedQueueTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by 公司 on 2017/1/13.
 */
@ContextConfiguration("classpath:applicationContext.xml")
public class ThreadTest {
    @Test
    public void startThread(){
        ConcurrentLinkedQueueTest.offer();
        ConcurrentLinkedQueueTest.poll p = new ConcurrentLinkedQueueTest.poll();
        for(int i = 0; i < ConcurrentLinkedQueueTest.count; i++){
            Thread t = new Thread(p);
            t.setName(String.valueOf(i));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
