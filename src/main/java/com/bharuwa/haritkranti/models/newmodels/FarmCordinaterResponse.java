package com.bharuwa.haritkranti.models.newmodels;

import java.util.List;

public class FarmCordinaterResponse {
	private Integer farmId;
	List<LocationPin> locationPins;
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}
	public List<LocationPin> getLocationPins() {
		return locationPins;
	}
	public void setLocationPins(List<LocationPin> locationPins) {
		this.locationPins = locationPins;
	}
}
