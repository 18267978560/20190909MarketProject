package com.legou.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.legou.common.pojo.EasyUITreeNode;
import com.legou.mapper.TbItemCatMapper;
import com.legou.pojo.TbItemCat;
import com.legou.pojo.TbItemCatExample;
import com.legou.pojo.TbItemCatExample.Criteria;
import com.legou.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	TbItemCatMapper tbItemCatMapper;

	//叶子节点点击了之后传入id，如果是父节点则打开，如果是子节点则关闭
	@Override
	public List<EasyUITreeNode> getItemCatList(Long id) {
		//创建对象
		TbItemCatExample example= new TbItemCatExample();
		//创建条件
		Criteria createCriteria = example.createCriteria();
		//添加条件
		createCriteria.andParentIdEqualTo(id);
		//查找出带有数据的集合
		List<TbItemCat> selectByExampleList = tbItemCatMapper.selectByExample(example);
		//创建需要返回的值进行转换
		List<EasyUITreeNode> easyUITreeNodes = new ArrayList<EasyUITreeNode>();
		//通过foreach循环得到其中的值，并赋值给需要return的类型内
		for (TbItemCat tbItemCat : selectByExampleList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUITreeNodes.add(easyUITreeNode);
		}
		return easyUITreeNodes;
	}
}
