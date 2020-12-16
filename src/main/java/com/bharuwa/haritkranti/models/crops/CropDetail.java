package com.bharuwa.haritkranti.models.crops;

import org.springframework.data.mongodb.core.mapping.Document;

import com.bharuwa.haritkranti.models.BaseObject;

@Document(collection = "cropDetail")
public class CropDetail extends BaseObject{
	private Integer farmId;
	private String userId;
	private String state;
	private String district;
	private String tehsil;
	private String block;
	private String village;
	private String khasraNumber;
	
	private String satelliteCrop;
	private String actualCropCategory;
	private String actualCrop;
	private String actualCropVariety;
	private String actualCropCategoryInHindi;
	private String actualCropInHindi;
	private String actualCropVarietyInHindi;
	private String alreadySown;
	private String beforeDays;
	private String afterDays;
	private String farmerName;
	private String mobileNumber;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getKhasraNumber() {
		return khasraNumber;
	}
	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}
	public String getSatelliteCrop() {
		return satelliteCrop;
	}
	public void setSatelliteCrop(String satelliteCrop) {
		this.satelliteCrop = satelliteCrop;
	}
	public String getActualCrop() {
		return actualCrop;
	}
	public void setActualCrop(String actualCrop) {
		this.actualCrop = actualCrop;
	}
	public String getAlreadySown() {
		return alreadySown;
	}
	public void setAlreadySown(String alreadySown) {
		this.alreadySown = alreadySown;
	}
	public String getBeforeDays() {
		return beforeDays;
	}
	public void setBeforeDays(String beforeDays) {
		this.beforeDays = beforeDays;
	}
	public String getAfterDays() {
		return afterDays;
	}
	public void setAfterDays(String afterDays) {
		this.afterDays = afterDays;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getActualCropCategory() {
		return actualCropCategory;
	}
	public void setActualCropCategory(String actualCropCategory) {
		this.actualCropCategory = actualCropCategory;
	}
	public String getActualCropVariety() {
		return actualCropVariety;
	}
	public void setActualCropVariety(String actualCropVariety) {
		this.actualCropVariety = actualCropVariety;
	}
	public String getActualCropCategoryInHindi() {
		return actualCropCategoryInHindi;
	}
	public void setActualCropCategoryInHindi(String actualCropCategoryInHindi) {
		this.actualCropCategoryInHindi = actualCropCategoryInHindi;
	}
	public String getActualCropInHindi() {
		return actualCropInHindi;
	}
	public void setActualCropInHindi(String actualCropInHindi) {
		this.actualCropInHindi = actualCropInHindi;
	}
	public String getActualCropVarietyInHindi() {
		return actualCropVarietyInHindi;
	}
	public void setActualCropVarietyInHindi(String actualCropVarietyInHindi) {
		this.actualCropVarietyInHindi = actualCropVarietyInHindi;
	}
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}
	
}
