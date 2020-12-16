package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "npkStandardValue")
public class NPKStandardValue {
	
	private String cropName;
	private int nStandardValue;
	private int pStandardValue;
	private int kStandardValue;
	
	
	public NPKStandardValue(int nStandardValue, int pStandardValue, int kStandardValue) {
		super();
		this.nStandardValue = nStandardValue;
		this.pStandardValue = pStandardValue;
		this.kStandardValue = kStandardValue;
	}
	
	public String getCropName() {
		return cropName;
	}
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public int getnStandardValue() {
		return nStandardValue;
	}

	public void setnStandardValue(int nStandardValue) {
		this.nStandardValue = nStandardValue;
	}

	public int getpStandardValue() {
		return pStandardValue;
	}

	public void setpStandardValue(int pStandardValue) {
		this.pStandardValue = pStandardValue;
	}

	public int getkStandardValue() {
		return kStandardValue;
	}

	public void setkStandardValue(int kStandardValue) {
		this.kStandardValue = kStandardValue;
	}
	
}
