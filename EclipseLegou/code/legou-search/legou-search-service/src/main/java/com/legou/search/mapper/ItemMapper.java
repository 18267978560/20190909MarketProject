package com.legou.search.mapper;

import java.util.List;

import com.legou.common.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> selectAllItem();

	SearchItem getSearchItem(long itemIdLong);
}
