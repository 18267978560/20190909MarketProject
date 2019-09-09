package com.legou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.pojo.EasyUIDataGridResult;
import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbItem;
import com.legou.pojo.TbItemDesc;
import com.legou.service.ItemService;

@Controller
public class ItemController {
	
	//需要调用service查询
	@Autowired
	private ItemService itemService;

	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId) {
		TbItem item = itemService.getItem(itemId);
		return item;
	}
	
	//商品管理中'查询商品'的方法(前台中传入一个页数和行数)
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		return itemService.getItemList(page, rows);
	}
	
	//商品管理中'新增商品'的方法(前台传入一个类和商品描述栏的属性)
	@RequestMapping("/item/save")
	@ResponseBody
	public LegouResult save(TbItem tbItem , String desc){
		
		LegouResult legouResult = itemService.save(tbItem,desc);
		return legouResult;
	}

}