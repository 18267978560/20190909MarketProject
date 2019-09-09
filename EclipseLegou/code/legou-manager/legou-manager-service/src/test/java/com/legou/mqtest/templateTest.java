package com.legou.mqtest;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class templateTest {
	
	@Test
	public void test1() throws SQLException{
		//从配置文件中或许容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mq.xml");
		//得到配置好的jdbcTemplate
		JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
		//查询语句
	    String sql = "select * from tb_item where id = 536563";
	    List list = jdbcTemplate.queryForList(sql);
	    System.out.println(list.size());
	}
}