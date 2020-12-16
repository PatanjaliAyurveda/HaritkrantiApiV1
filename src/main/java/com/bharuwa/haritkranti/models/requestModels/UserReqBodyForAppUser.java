package com.bharuwa.haritkranti.models.requestModels;

import java.util.List;

import com.bharuwa.haritkranti.models.AddressForAppUser;
import com.bharuwa.haritkranti.models.AddressInHindiForAppUser;
import com.bharuwa.haritkranti.models.UserKhasraMapping;

public class UserReqBodyForAppUser {
	
	private AddressForAppUser addressModel = new AddressForAppUser();
	private AddressInHindiForAppUser addressModelInHindi = new AddressInHindiForAppUser();
	private String khasraNumber;
	private String noOfMale;
	private String noOfFeMale;
	private String noOfChildren;
	private String primaryPhone;
	private List<UserKhasraMapping> UserKhasraMappingList;
	
	public AddressForAppUser getAddressModel() {
		return addressModel;
	}
	public void setAddressModel(AddressForAppUser addressModel) {
		this.addressModel = addressModel;
	}
	public String getKhasraNumber() {
		return khasraNumber;
	}
	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}
	public String getNoOfMale() {
		return noOfMale;
	}
	public void setNoOfMale(String noOfMale) {
		this.noOfMale = noOfMale;
	}
	public String getNoOfFeMale() {
		return noOfFeMale;
	}
	public void setNoOfFeMale(String noOfFeMale) {
		this.noOfFeMale = noOfFeMale;
	}
	public String getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(String noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	public List<UserKhasraMapping> getUserKhasraMappingList() {
		return UserKhasraMappingList;
	}
	public void setUserKhasraMappingList(List<UserKhasraMapping> userKhasraMappingList) {
		UserKhasraMappingList = userKhasraMappingList;
	}
	public AddressInHindiForAppUser getAddressModelInHindi() {
		return addressModelInHindi;
	}
	public void setAddressModelInHindi(AddressInHindiForAppUser addressModelInHindi) {
		this.addressModelInHindi = addressModelInHindi;
	}

}
