package com.liuxin.tspring.thread;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by 公司 on 2017/1/13.
 */
@Component
public class ConcurrentLinkedQueueTest {
    private static ConcurrentLinkedQueue<Integer> linkedQueue =
            new ConcurrentLinkedQueue<Integer>();

    public static int count = 2;

    public static void offer(){
        for(int i = 0 ;i < 10000 ;i++){
            linkedQueue.add(i);
        }
    }

    public static class poll implements Runnable{
        @Override
        public void run() {
            while(!(linkedQueue.size() < 1)){
                System.out.println(Thread.currentThread().getName()+":"+linkedQueue.poll());
            }
        }
    }
}
