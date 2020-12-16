package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seedCenter")
public class SeedCenter {
	
	private String companyName;
	private String code;
	private String address;
	private String block;
	private String district;
	private String state;
	private String licenseNo;
	private String validUpTo;
	private String name;
	private String phone;
	private String licenseAuthority;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getValidUpTo() {
		return validUpTo;
	}
	public void setValidUpTo(String validUpTo) {
		this.validUpTo = validUpTo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLicenseAuthority() {
		return licenseAuthority;
	}
	public void setLicenseAuthority(String licenseAuthority) {
		this.licenseAuthority = licenseAuthority;
	}

}
