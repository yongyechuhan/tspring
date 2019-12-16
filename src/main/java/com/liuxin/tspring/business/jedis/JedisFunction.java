package com.liuxin.tspring.business.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Service
public class JedisFunction {

    private Logger logger = LoggerFactory.getLogger(JedisFunction.class);

    @Autowired
    private JedisCluster jedisCluster;

    public void getVal(){
        for(int i = 0; i < 50; i++){
            try {
                Thread.sleep(2000);
                logger.info("获取缓存值{},当前请求次数{}", jedisCluster.get("abcuserId"), i);
            } catch (Exception e) {
                logger.error("获取值失败", e);
            }
        }
    }

    public void getVal3(){
        for(int i = 0; i < 100; i++){
            try {
                Thread.sleep(1000);
                logger.info("获取缓存值{},当前请求次数{}", jedisCluster.get("abcuserId"), i);
            } catch (Exception e) {
                logger.error("获取值失败", e);
            }
        }
    }
}
