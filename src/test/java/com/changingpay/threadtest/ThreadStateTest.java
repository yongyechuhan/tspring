package com.changingpay.threadtest;

import com.changingpay.tspring.thread.ThreadState;
import org.junit.Test;

/**
 * Created by think on 2017/1/25.
 */
public class ThreadStateTest {
    @Test
    public void testThreadTest(){
        Thread t = new Thread(new ThreadState.Wating(),"TimeWaitingThread");
        Thread t1 = new Thread(new ThreadState.TimeWaiting(),"WaitingThread");
        Thread t2 = new Thread(new ThreadState.Blocked(),"BlockedThread-1");
        Thread t3 = new Thread(new ThreadState.Blocked(),"BlockedThread-2");

        t.start();t1.start();t2.start();t3.start();
        try {
            t.join();t1.join();t2.join();t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
