package com.legou.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.legou.common.pojo.SearchResult;
import com.legou.search.dao.SolrSearchDao;
import com.legou.search.service.SolrSearchService;

@Service
public class SolrSearchServiceImpl implements SolrSearchService {

	//通过容器注入的solrSearchDao
	@Autowired
	SolrSearchDao solrSearchDao;

	//通过关键词从solr中搜索商品
	@Override
	public SearchResult search(String keyword) throws Exception {
		System.out.println("进入了实现类");
		//solr搜索
		SolrQuery query = new SolrQuery();
		//set需要查询的词
		query.setQuery(keyword);
		//df为固定
		query.set("df", "item_title");
		//设置为出现一百行
		query.setRows(100);
		//返回一个searchResult，其中有itemList
		SearchResult searchResult = solrSearchDao.search(query);
		return searchResult;
	}
}
