package com.changingpay.jedistest;

import com.liuxin.tspring.business.jedis.JedisFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml",
        "classpath*:spring-jedis.xml"})
public class TestJedisFunction {
    @Autowired
    private JedisFunction jedisFunction;
}
