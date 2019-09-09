package com.legou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.pojo.EasyUIDataGridResult;
import com.legou.common.utils.LegouResult;
import com.legou.content.service.ContentService;
import com.legou.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	ContentService contentService;

	//保存
	@RequestMapping("/content/save")
	@ResponseBody
	public LegouResult saveContent(TbContent tbContent) {
		//创建接口内的方法
		LegouResult legouResult = contentService.saveContent(tbContent);
		return legouResult;
	}

	//查找
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId,Integer page,Integer rows) {
		//前台需要一个查询的叶子的id，与分页的page和rows
		return contentService.getContentList(categoryId,page,rows);
	}
	
}
