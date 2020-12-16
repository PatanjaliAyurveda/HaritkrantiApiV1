package com.bharuwa.haritkranti.models.newmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenCageComponents {
	
	private String city;
	private String continent;
    private String country;
    private String country_code;
	private String county;
    private String postcode;
    private String road;
	private String road_reference;
    private String road_type;
    private String state;
	private String state_code;
    private String state_district;
    
	public OpenCageComponents() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getRoad_reference() {
		return road_reference;
	}
	public void setRoad_reference(String road_reference) {
		this.road_reference = road_reference;
	}
	public String getRoad_type() {
		return road_type;
	}
	public void setRoad_type(String road_type) {
		this.road_type = road_type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState_code() {
		return state_code;
	}
	public void setState_code(String state_code) {
		this.state_code = state_code;
	}
	public String getState_district() {
		return state_district;
	}
	public void setState_district(String state_district) {
		this.state_district = state_district;
	}
	
}
