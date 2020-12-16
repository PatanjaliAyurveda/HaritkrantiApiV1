package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fertilizerCenter")
public class FertilizerCenter {
	
	private String sapCode;
	private String name;
	private String fetilizerclass;
	private String type;
	private String gstNumber;
	private String town;
	private String longitude;
	private String latitude;
	private String ownerName;
	private String ownerEmail;
	private String ownerContactNumber;
	private String billingAddress;
	private String billingCity;
	private String billingDistrict;
	private String billingState;
	private String shippingAddress;
	private String shippingCity;
	private String shippingDistrict;
	private String shippingState;
	private String shippingPincode;
	private String cAndfCode;
	private String beatName;
	private String status;
	private String dsmCode;
	
	public String getSapCode() {
		return sapCode;
	}
	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFetilizerclass() {
		return fetilizerclass;
	}
	public void setFetilizerclass(String fetilizerclass) {
		this.fetilizerclass = fetilizerclass;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public String getOwnerContactNumber() {
		return ownerContactNumber;
	}
	public void setOwnerContactNumber(String ownerContactNumber) {
		this.ownerContactNumber = ownerContactNumber;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getBillingCity() {
		return billingCity;
	}
	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}
	public String getBillingDistrict() {
		return billingDistrict;
	}
	public void setBillingDistrict(String billingDistrict) {
		this.billingDistrict = billingDistrict;
	}
	public String getBillingState() {
		return billingState;
	}
	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getShippingCity() {
		return shippingCity;
	}
	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}
	public String getShippingDistrict() {
		return shippingDistrict;
	}
	public void setShippingDistrict(String shippingDistrict) {
		this.shippingDistrict = shippingDistrict;
	}
	public String getShippingState() {
		return shippingState;
	}
	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}
	public String getShippingPincode() {
		return shippingPincode;
	}
	public void setShippingPincode(String shippingPincode) {
		this.shippingPincode = shippingPincode;
	}
	public String getcAndfCode() {
		return cAndfCode;
	}
	public void setcAndfCode(String cAndfCode) {
		this.cAndfCode = cAndfCode;
	}
	public String getBeatName() {
		return beatName;
	}
	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDsmCode() {
		return dsmCode;
	}
	public void setDsmCode(String dsmCode) {
		this.dsmCode = dsmCode;
	}
}
