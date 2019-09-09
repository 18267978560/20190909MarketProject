package com.legou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.pojo.EasyUITreeNode;
import com.legou.service.ItemCatService;

@Controller
public class ItemCatController {

	@Autowired
	ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCat(@RequestParam(name="id",defaultValue="0") long id){
		
		//点击树结构传入对应的id，如果是父节点则打开，如果是子节点则关闭
		//调用接口中获取存放数据集合的方法并返回
		List<EasyUITreeNode> itemCatList = itemCatService.getItemCatList(id);
		return itemCatList;
	}
}
