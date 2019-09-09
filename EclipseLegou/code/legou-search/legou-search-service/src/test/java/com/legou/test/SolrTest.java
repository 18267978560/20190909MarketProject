package com.legou.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SolrTest {
	//消费者代码
	public static void main(String[] args) throws IOException {
		//加载消费者监听
		ClassPathXmlApplicationContext classPathXmlApplicationContext =
		new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mq.xml");
		//阻塞进程
		System.in.read();
	}
}
