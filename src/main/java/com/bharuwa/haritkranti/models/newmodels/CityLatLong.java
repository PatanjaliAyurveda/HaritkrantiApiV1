package com.bharuwa.haritkranti.models.newmodels;

public class CityLatLong {
	
	private String cityName;
	private String latitude;
	private String longitude;
	
	
	public CityLatLong(String cityName, String latitude, String longitude) {
		super();
		this.cityName = cityName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
