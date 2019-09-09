package com.legou.content.service;
import java.util.List;

import com.legou.common.pojo.EasyUITreeNode;
import com.legou.common.utils.LegouResult;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getConteCategoryList(long id);

	LegouResult addContentCategoryNode(Long parentId, String name);

	void deleteContentCategoryNode(Long id);

	void updateContentNode(Long id, String name);
}