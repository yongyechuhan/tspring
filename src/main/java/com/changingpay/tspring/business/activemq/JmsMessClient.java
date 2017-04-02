package com.changingpay.tspring.business.activemq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by Liuxin on 2017/4/2.
 */
@Service
public class JmsMessClient extends AbstractJmsMess{

    private static Log logger = LogFactory.getLog(JmsMessClient.class);

    public void initClient(){

        Connection conn = null;

        // 发送或接收消息的线程
        Session session;
        // 消息的目的地
        Destination destination;
        // 消息发送者
        MessageConsumer consumer;

        ConnectionFactory connFactory = AbstractJmsMess.getConnFactory();

        try {
            conn = connFactory.createConnection();
            conn.start();
            session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("LG_MessQueue");
            consumer = session.createConsumer(destination);
            while(true){
                TextMessage message = (TextMessage) consumer.receive(5000);
                if(message != null){
                    logger.info("已接受 "+message.getText());
                } else {
                    break;
                }
            }
            logger.info("recive end");
        } catch (JMSException e) {
            logger.error("获取 ActiveMQ链接失败！", e);
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (JMSException e) {
                    logger.error("释放 ActiveMQ链接失败！", e);
                }
            }
        }
    }
}
