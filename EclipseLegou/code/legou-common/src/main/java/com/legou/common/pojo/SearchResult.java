package com.legou.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
	
	List<SearchItem> itemList;

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
}
