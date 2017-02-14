package com.changingpay.tspring.thread;

import com.changingpay.tspring.util.ConnectionDriver;
import com.mysql.jdbc.Connection;

import java.util.LinkedList;

/**
 * Created by think on 2017/1/27.
 */
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    public ConnectionPool(int intialSize){
        if(intialSize > 0){
            for(int i = 0; i < intialSize; i++){
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection){
        if(connection != null){
            synchronized(pool){
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    //当mills内无法获取连接，将会返回null
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            if(mills <= 0){
                while(pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else{
                long future = System.currentTimeMillis() + mills;
                long remainging = mills;
                while(pool.isEmpty() && remainging > 0){
                    pool.wait(remainging);
                    remainging = future - System.currentTimeMillis();
                }
                Connection result = null;
                if(!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
