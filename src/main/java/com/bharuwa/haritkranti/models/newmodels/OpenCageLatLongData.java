package com.bharuwa.haritkranti.models.newmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenCageLatLongData {
	
	private String documentation;
	private OpenCageResults results;

	public OpenCageLatLongData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OpenCageResults getResults() {
		return results;
	}

	public void setResults(OpenCageResults results) {
		this.results = results;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

}
