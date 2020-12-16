package com.bharuwa.haritkranti.models.newmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenCageResult {
	
	public OpenCageResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	private OpenCageComponents components;

	public OpenCageComponents getComponents() {
		return components;
	}

	public void setComponents(OpenCageComponents components) {
		this.components = components;
	}
	
}
