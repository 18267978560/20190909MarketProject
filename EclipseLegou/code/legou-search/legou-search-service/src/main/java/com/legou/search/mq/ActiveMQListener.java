package com.legou.search.mq;

	import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.legou.common.pojo.SearchItem;
import com.legou.search.mapper.ItemMapper;

//监听代码
public class ActiveMQListener implements MessageListener{

	@Autowired
	ItemMapper itemMapper;
	
	//配置文件中配置，使用@Autowired自动注入
	@Autowired 
	SolrServer solrServer;

	//每当有新的一条商品添加时，不能重新导入一遍索引库，所以每当有一个商品添加进数据库时，同时也需要放到solr索引库中
	//被监听的类，每当mq中有新的消息入队时，就会被监听到，然后将mq中的信息取出
	@Override
	public void onMessage(Message message) {
		try {
			//获取到mq的信息
			TextMessage textMessage = (TextMessage) message;
			//获取到文本信息并转化成为long类型
			String itemId = textMessage.getText();
			System.out.println(itemId);
			long itemIdLong = Long.parseLong(itemId);
			//通过itemId查询出searchItem
			SearchItem searchItem = itemMapper.getSearchItem(itemIdLong);
			//将获取到的值传入solrInputDocument
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.setField("id", searchItem.getId());
			solrInputDocument.setField("item_title", searchItem.getTitle());
			solrInputDocument.setField("item_sell_point", searchItem.getSell_point());
			solrInputDocument.setField("item_price", searchItem.getPrice());
			solrInputDocument.setField("item_image", searchItem.getImage());
			solrInputDocument.setField("item_category_name",searchItem.getName());
			//添加入solr并提交事务
			solrServer.add(solrInputDocument);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
