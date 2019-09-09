package com.legou.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.legou.common.pojo.EasyUITreeNode;
import com.legou.common.utils.LegouResult;
import com.legou.content.service.ContentCategoryService;
import com.legou.mapper.TbContentCategoryMapper;
import com.legou.pojo.TbContentCategory;
import com.legou.pojo.TbContentCategoryExample;
import com.legou.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getConteCategoryList(long id) {
		//新建一个对象
		TbContentCategoryExample example= new TbContentCategoryExample();
		//设置条件，设置之后即生效，影响查询
		Criteria createCriteria = example.createCriteria();
		createCriteria.andParentIdEqualTo(id);
		//查询出符合的类并放入集合中
		List<TbContentCategory> categoryList = tbContentCategoryMapper.selectByExample(example);
		//需要转换恒伟EasyUITreeNode类型的值返回
		List<EasyUITreeNode> nodelisTreeNodes= new ArrayList<EasyUITreeNode>();
		//通过遍历将值赋值给EasyUITreeNode类型的集合内
		for(TbContentCategory category :categoryList) {
			//设值
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			
			easyUITreeNode.setId(category.getId());
			
			easyUITreeNode.setText(category.getName());
			//判断状态来设置是否开关
			easyUITreeNode.setState(category.getIsParent()?"closed":"open");
			
			nodelisTreeNodes.add(easyUITreeNode);
		}
		//返回json类型
		return nodelisTreeNodes;
	}
	
	@Override
	public LegouResult addContentCategoryNode(Long parentId, String name) {
		//创建对象和设置参数并赋初始值
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		//创建时间
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		
		tbContentCategoryMapper.insert(tbContentCategory);
		
		TbContentCategory parentNodeCategory= tbContentCategoryMapper.selectByPrimaryKey(parentId);
		
		if(!parentNodeCategory.getIsParent()) {
			parentNodeCategory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parentNodeCategory);
		}
		
		return LegouResult.ok(tbContentCategory);
	}

	//删除节点
	@Override
	public void deleteContentCategoryNode(Long id) {
		//直接通过id删除节点
		tbContentCategoryMapper.deleteByPrimaryKey(id);
	}

	//修改节点名称
	@Override
	public void updateContentNode(Long id, String name) {
		//通过id找到这个类的对象，然后使用set赋值，并用update修改数据库
		TbContentCategory selectByPrimaryKey = tbContentCategoryMapper.selectByPrimaryKey(id);
		selectByPrimaryKey.setName(name);
		tbContentCategoryMapper.updateByPrimaryKey(selectByPrimaryKey);
		
	}
	
}