package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;

@Document(collection = "contactUs")
public class ContactUs {
	
    private String state;
    
    private String district;
    
    private String contactName;
    
    private String contactNumber;

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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
    
}
