package com.bharuwa.haritkranti.models;

public class CropSeed extends BaseObject {

	private String reasonId;
    private String agroClimaticZone;
    private String cropName;
    private String cropNameInHindi;
    private String cropVariety;
    private String seed;
    private String sowingTime;
    private String harvestingTime;
    private String quantityInKgPerAcre;
	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
	public String getAgroClimaticZone() {
		return agroClimaticZone;
	}
	public void setAgroClimaticZone(String agroClimaticZone) {
		this.agroClimaticZone = agroClimaticZone;
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
	public String getCropVariety() {
		return cropVariety;
	}
	public void setCropVariety(String cropVariety) {
		this.cropVariety = cropVariety;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getSowingTime() {
		return sowingTime;
	}
	public void setSowingTime(String sowingTime) {
		this.sowingTime = sowingTime;
	}
	public String getHarvestingTime() {
		return harvestingTime;
	}
	public void setHarvestingTime(String harvestingTime) {
		this.harvestingTime = harvestingTime;
	}
	public String getQuantityInKgPerAcre() {
		return quantityInKgPerAcre;
	}
	public void setQuantityInKgPerAcre(String quantityInKgPerAcre) {
		this.quantityInKgPerAcre = quantityInKgPerAcre;
	}
    
}
