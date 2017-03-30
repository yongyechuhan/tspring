package com.changingpay.nettytest;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.changingpay.base.LoadTestConfig;
import com.changingpay.tspring.netty.TimeClient;
import com.changingpay.tspring.netty.TimeServer;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by think on 2017/3/19.
 */
public class NettyTest{

    private Log logger = LogFactory.getLog(NettyTest.class);

    @Autowired
    private TimeServer timeServer;

    @Autowired
    private TimeClient timeClient;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private static final CountDownLatch downLatch =
            new CountDownLatch(2);

    @Test
    public void startNettyServer(){
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        timeServer.bind();
                        downLatch.countDown();
                    } catch (Exception e) {
                        logger.error("nettyServer通讯异常", e);
                    }
                }
            });
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        timeClient.connect();
                        downLatch.countDown();
                    } catch (Exception e) {
                        logger.error("nettyClient通讯异常", e);
                    }
                }
            });
            downLatch.await();
        } catch (Exception e) {
            logger.error("netty通讯异常" , e);
        }
    }

    @Test
    public void testQuickSort(){
        int[] array = {1 , 64, 13, 22, 17, 12, 31, 24, 66, 5, 21, 83};
        String sortRes = cycleSort(sortArray(array));
        System.out.println(sortRes);
    }

    private String cycleSort(List list){
        int[] sortArray = (int[])list.get(0);
        int splitIndex = (int)list.get(1);
        if(sortArray.length > 2){
            int[] left = new int[splitIndex];
            for(int i = 0; i < splitIndex; i++){
                left[i] = sortArray[i];
            }
            System.out.print(new Gson().toJson(left)+"-----------");
            int[] right = new int[sortArray.length - splitIndex - 1];
            for(int i = splitIndex + 1,j = 0; i < sortArray.length; i++,j++){
                right[j] = sortArray[i];
            }
            System.out.print(new Gson().toJson(right));
            System.out.println();
            if(splitIndex == 0){
                return sortArray[0] + " " + cycleSort(sortArray(right));
            }else if(splitIndex == sortArray.length - 1){
                return cycleSort(sortArray(left)) + " " + sortArray[sortArray.length - 1];
            }
            return cycleSort(sortArray(left)) + " " + sortArray[splitIndex] + " " + cycleSort(sortArray(right));
        }
        if(sortArray.length == 1) return  String.valueOf(sortArray[0]);
        int[] a = (int[])sortArray(sortArray).get(0);
        return String.valueOf(a[0]+" "+a[1]);
    }

    private List sortArray(int[] sortArray){
        int i = 0;
        int j = sortArray.length - 1;
        int max_idx = 0;
        int min_idx = 0;
        int local = sortArray[i];
        while(i != j){
            if(sortArray[j--] < local){
                min_idx = ++j;
            }else{
                min_idx = 0;
            }
            if(min_idx > 0){
                while(i != j){
                    if(sortArray[++i] > local){
                        max_idx = i;
                        break;
                    }else{
                        max_idx = 0;
                    }
                }
                if(max_idx > 0){
                    int temp = sortArray[min_idx];
                    sortArray[min_idx] = sortArray[max_idx];
                    sortArray[max_idx] = temp;
                }
            }
        }

            sortArray[0] = sortArray[i];
            sortArray[i] = local;

            List list = new ArrayList();
        list.add(sortArray);
            list.add(i);
        return list;
    }

    @Test
    public void test(){
        int[] array = {4, 13, 5, 17, 19};
        System.out.println(new Gson().toJson(sortArray(array)));
    }
}
