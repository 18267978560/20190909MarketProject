package com.legou.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.legou.common.pojo.SearchItem;
import com.legou.common.utils.LegouResult;
import com.legou.search.mapper.ItemMapper;
import com.legou.search.service.SearchAllToSolrService;

@Service
public class SearchAllToSolrServiceImpl implements SearchAllToSolrService{

	@Autowired
	ItemMapper itemMapper;
	
	//配置文件中配置，使用@Autowired自动注入
	@Autowired 
	SolrServer solrServer;

	//搜索出所有的商品信息并添加进入solr中
	@Override
	public LegouResult selectAllItem(){
		try {
			//通过mapper接口搜索出所有需要的返回值的集合并返回
			List<SearchItem> selectAllItem = itemMapper.selectAllItem();
			for (SearchItem searchItem : selectAllItem) {
				SolrInputDocument solrInputDocument = new SolrInputDocument();
				solrInputDocument.setField("id", searchItem.getId());
				solrInputDocument.setField("item_title", searchItem.getTitle());
				solrInputDocument.setField("item_sell_point", searchItem.getSell_point());
				solrInputDocument.setField("item_price", searchItem.getPrice());
				solrInputDocument.setField("item_image", searchItem.getImage());
				solrInputDocument.setField("item_category_name",searchItem.getName());
				solrServer.add(solrInputDocument);
			}
			//提交事务
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return LegouResult.ok();
	}
	
}
