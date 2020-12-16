package com.bharuwa.haritkranti.models;

public class NPKRequiredValue {
	
	private float nValue=0;
	private float pValue=0;
	private float kValue=0;
	private String cropName;
	private String cropNameInHindi;
	
	public NPKRequiredValue(float nValue, float pValue, float kValue) {
		super();
		this.nValue = nValue;
		this.pValue = pValue;
		this.kValue = kValue;
	}

	public float getnValue() {
		return nValue;
	}

	public void setnValue(float nValue) {
		this.nValue = nValue;
	}

	public float getpValue() {
		return pValue;
	}

	public void setpValue(float pValue) {
		this.pValue = pValue;
	}

	public float getkValue() {
		return kValue;
	}

	public void setkValue(float kValue) {
		this.kValue = kValue;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public String getCropNameInHindi() {
		return cropNameInHindi;
	}

	public void setCropNameInHindi(String cropNameInHindi) {
		this.cropNameInHindi = cropNameInHindi;
	}
	
}
