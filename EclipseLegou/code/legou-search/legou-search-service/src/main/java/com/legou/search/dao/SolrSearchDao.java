package com.legou.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.legou.common.pojo.SearchItem;
import com.legou.common.pojo.SearchResult;

@Repository
public class SolrSearchDao {

	@Autowired
	SolrServer solrServer;

	//通过solr数据库中搜索商品信息
	public SearchResult search(SolrQuery query) {
		//创建返回值
		SearchResult searchResult = new SearchResult();
		try {
			//通过传入的数据查询出solr中的数据并拿到相应的类型
			QueryResponse queryResponse = solrServer.query(query);
			SolrDocumentList results = queryResponse.getResults();
			//新建一个返回值的类型
			List<SearchItem> itemList = new ArrayList<SearchItem>();
			for (SolrDocument solrDocument : results) {
				//通过遍历将值传入itemList中
				SearchItem item = new SearchItem();
				item.setId((String) solrDocument.get("id"));
				item.setName((String) solrDocument.get("item_category_name"));
				item.setImage((String) solrDocument.get("item_image"));
				item.setSell_point((String) solrDocument.get("item_sell_point"));
				item.setPrice((long) solrDocument.get("item_price"));
				item.setTitle((String) solrDocument.get("item_title"));
				itemList.add(item);

			}
			//将itemList设置进入需要返回的类型中并返回
			searchResult.setItemList(itemList);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return searchResult;
	}

}
