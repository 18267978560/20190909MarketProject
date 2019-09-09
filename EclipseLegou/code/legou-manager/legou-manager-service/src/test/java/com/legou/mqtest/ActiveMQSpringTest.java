package com.legou.mqtest;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMQSpringTest {

	@Test
	public void providerQueue() {
		//将配置文件中的bean创建成对象并放入容器context中
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mq.xml");
		//从容器中获取activeMQConnectionFactory
		ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) context.getBean("activeMQConnectionFactory");
		//从容器中获取已经配置好的模板类
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		//从容器中获取配置好的activeMQQueue并返回其父类类型(多态)
		Destination destination = (Destination) context.getBean("activeMQQueue");
		//使用封装好的jmsTemplate的方法设置需要传输到的name和需要传输的信息，构建匿名内部类
		jmsTemplate.send(destination, new MessageCreator() {
			@Override//session已经被封装好，可以直接使用，直接使用session创建需要发送的信息，并返回
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("test111");
			}
		});
	}
	
}
