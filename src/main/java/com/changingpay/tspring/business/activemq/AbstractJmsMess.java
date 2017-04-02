package com.changingpay.tspring.business.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.ConnectionFactory;

/**
 * Created by Liuxin on 2017/4/2.
 */
public class AbstractJmsMess {

    private static Log logger = LogFactory.getLog(AbstractJmsMess.class);

    private static String activemqUser;

    private static String activemqPwd;

    private static String activemqUrl;

    public static ConnectionFactory getConnFactory(){
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
