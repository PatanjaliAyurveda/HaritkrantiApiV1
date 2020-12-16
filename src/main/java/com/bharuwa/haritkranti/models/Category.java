package com.bharuwa.haritkranti.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category extends BaseObject {

	private String categoryName;
	
	private String categoryImage;
	
	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}

	private List<Variety> varietyList;
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Variety> getVarietyList() {
		return varietyList;
	}

	public void setVarietyList(List<Variety> varietyList) {
		this.varietyList = varietyList;
	}

}