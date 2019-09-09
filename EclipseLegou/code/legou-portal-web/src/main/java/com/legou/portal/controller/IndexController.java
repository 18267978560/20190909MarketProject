package com.legou.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legou.content.service.ContentService;
import com.legou.pojo.TbContent;

@Controller
public class IndexController {
	
	@Autowired
	ContentService contentService;
	
	@RequestMapping("/index")
	public String index(Model model) {
		//定义一个long类型的categoryId
		long categoryId = 89;
		//通过categoryId查询list集合返回，并通过model发送给页面
		List<TbContent> contentList = contentService.getContentByCategoryId(categoryId);
		model.addAttribute("aD1List", contentList);
		System.out.println("portal-web");
		return "index";	
	}
}