package com.bharuwa.haritkranti.models;

public class FilterByPrimaryCategoryReqModel {
	
	private String primaryCategoryname;
	
	private String language;
	
	private String state;
	
	private String district;

	public String getPrimaryCategoryname() {
		return primaryCategoryname;
	}

	public void setPrimaryCategoryname(String primaryCategoryname) {
		this.primaryCategoryname = primaryCategoryname;
	}

	public FilterByPrimaryCategoryReqModel() {
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
