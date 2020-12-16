package com.bharuwa.haritkranti.models;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PrimaryCategoryInHindi {
	
	private String primaryCategoryName;
	
	private String primaryCategoryImage;
	
	private List<Category> categoryList;

	public String getPrimaryCategoryName() {
		return primaryCategoryName;
	}

	public void setPrimaryCategoryName(String primaryCategoryName) {
		this.primaryCategoryName = primaryCategoryName;
	}

	public String getPrimaryCategoryImage() {
		return primaryCategoryImage;
	}

	public void setPrimaryCategoryImage(String primaryCategoryImage) {
		this.primaryCategoryImage = primaryCategoryImage;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	
}
