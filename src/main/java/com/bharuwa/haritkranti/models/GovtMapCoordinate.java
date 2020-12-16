package com.bharuwa.haritkranti.models;

public class GovtMapCoordinate {
	
	private String khasraNumber;
    private float lat;
    private float lon;
    private float latMin;
    private float latMax;
    private float lonMin;
    private float lonMax;
	public String getKhasraNumber() {
		return khasraNumber;
	}
	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
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
	public float getLatMin() {
		return latMin;
	}
	public void setLatMin(float latMin) {
		this.latMin = latMin;
	}
	public float getLatMax() {
		return latMax;
	}
	public void setLatMax(float latMax) {
		this.latMax = latMax;
	}
	public float getLonMin() {
		return lonMin;
	}
	public void setLonMin(float lonMin) {
		this.lonMin = lonMin;
	}
	public float getLonMax() {
		return lonMax;
	}
	public void setLonMax(float lonMax) {
		this.lonMax = lonMax;
	}
	
}
