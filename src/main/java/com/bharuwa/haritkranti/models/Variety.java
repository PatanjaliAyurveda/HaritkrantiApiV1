package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Variety extends BaseObject {
	
	private String varietyName;

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}
	
}
