package com.legou.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legou.item.pojo.ItemDetail;
import com.legou.pojo.TbItem;
import com.legou.pojo.TbItemDesc;
import com.legou.service.ItemService;

@Controller
public class itemController {
	
	@Autowired
	ItemService itemService;
	
	
	//'搜索商品详情'，搜索出商品详情，通过id搜索出商品的详情信息跳转到新的页面，将商品的详情信息显示
	@RequestMapping("/item/{itemid}")
	public String ItemInfo(@PathVariable Long itemid,Model model) {
		//通过itemid搜索出tbitem并发送给前端
		TbItem tbItem= itemService.getItem(itemid);
		//通过itemid搜索出tbitemDesc并发送给前端
		ItemDetail itemDetail = new ItemDetail(tbItem);

		TbItemDesc tbItemDesc = itemService.getItemDescById(itemid);
		//传给model中返回到前端页面
		model.addAttribute("item", itemDetail);
		 model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
