package com.liuxin.tspring.business.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Created by Liuxin on 2017/4/2.
 */
public class AbstractJmsMess {

    private static Log logger = LogFactory.getLog(AbstractJmsMess.class);

    private static String activemqUser;

    private static String activemqPwd;

    private static String activemqUrl;

    private static ConnectionFactory getConnFactory(){
        return NestClass.connFactory;
    }

    private static class NestClass{
        private static ConnectionFactory connFactory;
        static{
            try{
                connFactory = new ActiveMQConnectionFactory(activemqUser,
                        activemqPwd, activemqUrl);
            }catch (Exception e){
                logger.error("获取ActiveMQ工厂失败！", e);
            }
        }
    }

    protected static Connection getConnection(){
        ConnectionFactory connectionFactory = getConnFactory();
        if(connectionFactory != null){
            try {
                Connection connection = connectionFactory.createConnection();
                return connection;
            } catch (JMSException e) {
                logger.error("创建ActiveMQ连接失败！", e);
            }
        }
        return null;
    }

    public void setActivemqUser(String activemqUser) {
        this.activemqUser = activemqUser;
    }

    public void setActivemqPwd(String activemqPwd) {
        this.activemqPwd = activemqPwd;
    }

    public void setActivemqUrl(String activemqUrl) {
        this.activemqUrl = activemqUrl;
    }
}
