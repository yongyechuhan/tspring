package com.changingpay.tspring.business.activemq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Liuxin on 2017/4/2.
 */
@Service
public class JmsMessServer extends AbstractJmsMess{

    private static Log logger = LogFactory.getLog(JmsMessServer.class);

    private static final int SEND_NUMBER = 5;

    private static final CountDownLatch countDownLatch =
            new CountDownLatch(SEND_NUMBER);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public JmsMessServer(){
        super();
    }

    public void init(){
        // 发送或接收消息的线程
        Session session;
        // 消息的目的地
        Destination destination;
        // 消息发送者
        MessageProducer producer;

        ConnectionFactory connFactory = AbstractJmsMess.getConnFactory();
        try {
            Connection connection = connFactory.createConnection();
            connection.start();
            session =
                    connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("LG_MessQueue");
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            sendMessage(session, producer);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("主线程执行失败！", e);
            }
        } catch (JMSException e) {
            logger.error("创建ActiveMQ连接失败！", e);
        }
    }

    private void sendMessage(Session session, MessageProducer messageProducer){
        for(int i = 0; i < SEND_NUMBER; i++){
            taskExecutor.execute(new SendMessage(session, messageProducer));
        }
    }

    private class SendMessage implements Runnable{
        private Session session;
        private MessageProducer messageProducer;

        public SendMessage(Session session, MessageProducer messageProducer){
            this.session = session;
            this.messageProducer = messageProducer;
        }

        @Override
        public void run() {
            try {
                String uuid = UUID.randomUUID().toString();
                TextMessage message =
                        session.createTextMessage(uuid);
                messageProducer.send(message);
                session.commit();
                logger.info("已发送 " + uuid);
            } catch (JMSException e) {
                logger.error("发送消息失败！", e);
                while(countDownLatch.getCount() > 0){
                    countDownLatch.countDown();
                }
            } finally {
                Lock lock = new ReentrantLock();
                lock.lock();
                countDownLatch.countDown();
                lock.unlock();
            }
        }
    }
}
