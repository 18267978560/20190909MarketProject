package com.legou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.pojo.EasyUITreeNode;
import com.legou.common.utils.LegouResult;
import com.legou.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	ContentCategoryService contentCategoryService;
	
	//‘内容分类管理’中，点击叶子会显示出子节点或者是父节点
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategory(@RequestParam (name="id",defaultValue="0") long id){

		//创建一个接口内的方法，返回一个json类型
		List<EasyUITreeNode> easyUITreeNodes = contentCategoryService.getConteCategoryList(id);
		return easyUITreeNodes;
	}
	
	//‘内容分类管理’中创建新的节点
	@RequestMapping("/content/category/create")
	@ResponseBody
	public LegouResult createContentNode(Long parentId,String name) {
		//创建方法返回legouResult类型
		LegouResult legouResult=contentCategoryService.addContentCategoryNode(parentId,name);
		return legouResult;
	}
	
	//‘内容分类管理’中删除节点信息
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public void deleteContentNode(Long id) {
		//无返回值，创建删除的方法
		contentCategoryService.deleteContentCategoryNode(id);
	}
	
	//‘内容分类管理’中重命名节点
	@RequestMapping("/content/category/update")
	@ResponseBody
	public void updateContentNode(Long id,String name) {
		//无返回值，创建修改方法
		contentCategoryService.updateContentNode(id, name);
	}
	
}
