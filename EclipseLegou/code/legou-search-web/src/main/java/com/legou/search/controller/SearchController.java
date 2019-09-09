package com.legou.search.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legou.common.pojo.SearchResult;
import com.legou.search.service.SolrSearchService;

@Controller
public class SearchController {
	
	@Autowired
	SolrSearchService solrSearchService;
	
	@RequestMapping("/search")
	public String search(String keyword,Model model) throws Exception {
		//将字符转化成为utf-8类型
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		//通过keyword搜索出一个searchResult类型，其中包含着itemList
		SearchResult searchResult = solrSearchService.search(keyword);
		//通过model将数据显示在前端页面
		model.addAttribute("itemList",	searchResult.getItemList());
		System.out.println("进入controller");
		return "search";
	}
	
}
