package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.mapping.Document;

import com.bharuwa.haritkranti.models.OTP;

@Document(collection="appLoginTable")
public class AppLoginTable {
	
	private String mobileNumber;
	private OTP otp;
	private String languageName;
	private boolean registered;
	
	public AppLoginTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AppLoginTable(String mobileNumber, OTP otp, String languageName) {
		super();
		this.mobileNumber = mobileNumber;
		this.otp = otp;
		this.languageName = languageName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public OTP getOtp() {
		return otp;
	}

	public void setOtp(OTP otp) {
		this.otp = otp;
	}

	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public boolean isRegistered() {
		return registered;
	}
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
}
