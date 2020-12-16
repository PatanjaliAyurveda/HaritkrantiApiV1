package com.bharuwa.haritkranti.models.newmodels;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "govtLandDetail")
public class GovtLandDetail {
	
	private String state;
    private String district;
    private String tehsil;
    private String block;
    private String village;
    private String khasraNumber;
    private String farmerName;
    private String farmerNameInHindi;
    private String farmerDisplayName;
    
	public GovtLandDetail() {
		// TODO Auto-generated constructor stub
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

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getFarmerNameInHindi() {
		return farmerNameInHindi;
	}

	public void setFarmerNameInHindi(String farmerNameInHindi) {
		this.farmerNameInHindi = farmerNameInHindi;
	}

	public String getFarmerDisplayName() {
		return farmerDisplayName;
	}

	public void setFarmerDisplayName(String farmerDisplayName) {
		this.farmerDisplayName = farmerDisplayName;
	}
	
}
