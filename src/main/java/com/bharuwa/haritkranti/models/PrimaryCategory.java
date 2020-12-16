package com.bharuwa.haritkranti.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PrimaryCategory extends BaseObject {

	private String primaryCategoryName;
	
	private String primaryCategoryImage;
	
	public String getPrimaryCategoryImage() {
		return primaryCategoryImage;
	}

	public void setPrimaryCategoryImage(String primaryCategoryImage) {
		this.primaryCategoryImage = primaryCategoryImage;
	}

	private List<Category> categoryList;

	public PrimaryCategory() {
	}

	public String getPrimaryCategoryName() {
		return primaryCategoryName;
	}

	public void setPrimaryCategoryName(String primaryCategoryName) {
		this.primaryCategoryName = primaryCategoryName;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

}