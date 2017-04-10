package com.changingpay.tspring.business.activemq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Liuxin on 2017/4/2.
 */
@Service
public class JmsMessClient extends AbstractJmsMess{

    private static Log logger = LogFactory.getLog(JmsMessClient.class);

    // 发送或接收消息的线程
    private Session session;

    // 消息的目的地
    private Destination destination;

    // 消息接收者
    private MessageConsumer consumer;

    private static final int CONUMER_NUMBER = 5;

    private static final CountDownLatch countDownLatch =
            new CountDownLatch(CONUMER_NUMBER);

    /**
     * 点对点模式发送接收消息
     */
    public void initP2PClient(){
        Connection conn = AbstractJmsMess.getConnection();
        try {
            conn.start();
            session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("LG_MessQueue");
            consumer = session.createConsumer(destination);
            for(int i = 0; i < CONUMER_NUMBER; i++){
                Thread t = new Thread(new ReciveSendMess(consumer));
                t.start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("等待读取线程时被中断！", e);
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

    public void initSubscribe(){
        Connection conn = AbstractJmsMess.getConnection();
        try {
            conn.start();
            Session session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("LG_TOPIC");
            for(int i = 0; i < CONUMER_NUMBER; i++){
                Thread t = new Thread(new ReciveTopicMess(session, destination), i == 0 ? "A" : "B");
                t.start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("等待读取线程时被中断！", e);
            }
        } catch (JMSException e) {
            logger.error("获取 ActiveMQ链接失败！", e);
        }
    }

    private class ReciveSendMess implements Runnable{
        private MessageConsumer consumer;
        public ReciveSendMess(MessageConsumer consumer){
            this.consumer = consumer;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    //阻塞方法直到生产者向LG_MessQueue推送消息
                    //不设置超时时间将重复阻塞在此
                    TextMessage message = (TextMessage) consumer.receive(5000);
                    if (message != null) {
                        logger.info("已接受 " + message.getText());
                    } else {
                        break;
                    }
                }
            } catch (JMSException e){
                logger.error("读取推送消息失败！ ", e);
            } finally {
                Lock lock = new ReentrantLock();
                lock.lock();
                countDownLatch.countDown();
                lock.unlock();
            }
        }
    }

    private class ReciveTopicMess implements Runnable{
        private Session session;
        private Destination destination;

        public ReciveTopicMess(Session session, Destination destination){
            this.session = session;
            this.destination = destination;
        }

        @Override
        public void run() {
            logger.info(Thread.currentThread().getName()+"已运行。");
            MessageConsumer consumer = createConsumer();
            while(true){
                try {
                    TextMessage message = (TextMessage)consumer.receive(5000);
                    if(message != null){
                        logger.info("已接收 "+message.getText());
                    } else {
                        break;
                    }
                } catch (JMSException e) {
                    logger.error("获取订阅信息失败！ ", e);
                } finally {
                    Lock lock = new ReentrantLock();
                    lock.lock();
                    countDownLatch.countDown();
                    lock.unlock();
                }
            }
        }

        private MessageConsumer createConsumer(){
            MessageConsumer consumer = null;
            try {
                consumer = session.createConsumer(destination);
            } catch (JMSException e) {
                logger.error("创建消息消费者失败！ ", e);
            }
            return consumer;
        }
    }
}
