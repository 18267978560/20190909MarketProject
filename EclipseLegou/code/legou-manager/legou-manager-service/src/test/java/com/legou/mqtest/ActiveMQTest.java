package com.legou.mqtest;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;



public class ActiveMQTest {

	@Test
	public void providerTest() throws Exception {
		//创建连接工厂，通过tcp协议，61616是mq的通信端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//通过工厂获取连接
		Connection connection = connectionFactory.createConnection();
		//开始连接
		connection.start();
		//创建session会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一对一的Queue
		Queue queue = session.createQueue("这是Test1");
		//使用会话创建一对一的Queue的提供者
		MessageProducer test = session.createProducer(queue);
		//使用会话创建需要传输的text文本信息
		TextMessage text = session.createTextMessage("123456");
		//发送text信息
		test.send(text);
		
		test.close();
		session.close();
		connection.close();
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void consumerTest() throws Exception {
		//创建连接工厂，通过tcp协议，61616是mq的通信端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//通过工厂获取连接
		Connection connection = connectionFactory.createConnection();
		//开始连接
		connection.start();
		//创建session会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//加入一对一的Queue
		Queue queue = session.createQueue("这是Test1");
		//使用会话创建一对一的Queue的消费者
		MessageConsumer test = session.createConsumer(queue);
		//创建监听器
		test.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					//获取queue中的message，并向下转型
					TextMessage textMessage = (TextMessage) message;
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			
		});
		//阻塞进程
		test.close();
		session.close();
		connection.close();
	}
}
