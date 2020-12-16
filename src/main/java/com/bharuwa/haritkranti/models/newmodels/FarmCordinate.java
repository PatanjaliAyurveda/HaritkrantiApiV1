package com.bharuwa.haritkranti.models.newmodels;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "farmCoordinate")
public class FarmCordinate {
	
	private Integer farmId;
	private String coordinateId;
	private float lat;
	private float lon;
	
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}
	public String getCoordinateId() {
		return coordinateId;
	}
	public void setCoordinateId(String coordinateId) {
		this.coordinateId = coordinateId;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
}
