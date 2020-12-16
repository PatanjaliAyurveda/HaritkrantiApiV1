package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "newsAndFeed")
public class NewsAndFeeds {
	String label;
	String url;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
