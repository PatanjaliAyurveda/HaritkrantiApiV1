package com.bharuwa.haritkranti.models.newmodels;
import java.util.List;

public class WeatherForecastWeekly {
	
	private String cod;
	private String message;
	private String cnt;
	private City city;
	private List<WeatherForcast> list;
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public List<WeatherForcast> getList() {
		return list;
	}
	public void setList(List<WeatherForcast> list) {
		this.list = list;
	}

}
