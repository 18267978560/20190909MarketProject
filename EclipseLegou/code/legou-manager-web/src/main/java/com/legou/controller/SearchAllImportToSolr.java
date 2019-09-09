package com.legou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.utils.LegouResult;
import com.legou.search.service.SearchAllToSolrService;

@Controller
public class SearchAllImportToSolr {
	
	@Autowired
	SearchAllToSolrService searchAllToSolrService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public LegouResult ImportAllToSolr() {
		//创建select方法查询出所有searchItem的集合，返回一个result给前端
		 LegouResult legouResult = searchAllToSolrService.selectAllItem();
		//给前端页面返回一个ok，表示插入成功
		return legouResult.ok();
	}
}
