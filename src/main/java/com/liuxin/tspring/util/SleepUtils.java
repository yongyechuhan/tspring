package com.liuxin.tspring.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by think on 2017/1/25.
 */
public class SleepUtils {
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
