package com.changingpay.tspring.thread;

import com.changingpay.tspring.util.SleepUtils;

/**
 * Created by think on 2017/1/25.
 */
public class ThreadState {
    public static class TimeWaiting implements Runnable{
        @Override
        public void run() {
            while(true){
                SleepUtils.second(100);
            }
        }
    }

    public static class Wating implements Runnable{
        @Override
        public void run() {
            while(true){
                synchronized(Wating.class){
                    try {
                        Wating.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class Blocked implements Runnable{
        @Override
        public void run() {
            synchronized (Blocked.class){
                while(true){
                    SleepUtils.second(100);
                }
            }
        }
    }
}
