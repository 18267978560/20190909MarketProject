package com.legou.service;

import java.util.List;

import com.legou.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatList(Long id);
}
