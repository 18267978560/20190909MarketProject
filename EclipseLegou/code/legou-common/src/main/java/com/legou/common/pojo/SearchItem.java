package com.legou.common.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable{
	
	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String name;
	
	public String getImage() {
		return image;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String[] getImages() {
		if(image != null && !"".equals(image)) {
			String[] images= image.split(",");
			return images;
		}
		return null;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "SearchItem [id=" + id + ", title=" + title + ", sell_point=" + sell_point + ", price=" + price
				+ ", image=" + image + ", name=" + name + "]";
	}
}
