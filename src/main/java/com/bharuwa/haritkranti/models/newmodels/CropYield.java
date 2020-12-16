package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cropYield")
public class CropYield {
	private Integer farmId;
	private String state;
	private String district;
	private String tehsil;
	private String block;
	private String village;
	private String farmSize;
	private String cropName;
	private String nValue;
	private String pValue;
	private String kValue;
	private String organicCarbon;
	private String phValue;
	private String soilMoisture;
	private String expectedYeild;
	private String uom;
	private String date;
	private String cropNameInHindi;
	private String cropDisplayName;
	
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
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
	public String getTehsil() {
		return tehsil;
	}
	public void setTehsil(String tehsil) {
		this.tehsil = tehsil;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getFarmSize() {
		return farmSize;
	}
	public void setFarmSize(String farmSize) {
		this.farmSize = farmSize;
	}
	public String getCropName() {
		return cropName;
	}
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}
	public String getnValue() {
		return nValue;
	}
	public void setnValue(String nValue) {
		this.nValue = nValue;
	}
	public String getpValue() {
		return pValue;
	}
	public void setpValue(String pValue) {
		this.pValue = pValue;
	}
	public String getkValue() {
		return kValue;
	}
	public void setkValue(String kValue) {
		this.kValue = kValue;
	}
	public String getOrganicCarbon() {
		return organicCarbon;
	}
	public void setOrganicCarbon(String organicCarbon) {
		this.organicCarbon = organicCarbon;
	}
	public String getPhValue() {
		return phValue;
	}
	public void setPhValue(String phValue) {
		this.phValue = phValue;
	}
	public String getSoilMoisture() {
		return soilMoisture;
	}
	public void setSoilMoisture(String soilMoisture) {
		this.soilMoisture = soilMoisture;
	}
	public String getExpectedYeild() {
		return expectedYeild;
	}
	public void setExpectedYeild(String expectedYeild) {
		this.expectedYeild = expectedYeild;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCropNameInHindi() {
		return cropNameInHindi;
	}
	public void setCropNameInHindi(String cropNameInHindi) {
		this.cropNameInHindi = cropNameInHindi;
	}
	public String getCropDisplayName() {
		return cropDisplayName;
	}
	public void setCropDisplayName(String cropDisplayName) {
		this.cropDisplayName = cropDisplayName;
	}
	
}
