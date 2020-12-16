package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "agroClimaticZone")
public class AgroClimaticZone extends BaseObject {
	private String reasonId;
    private String reasonName; //agroClimaticZoneName
    private String state;
    private String district;

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
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

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
   
}