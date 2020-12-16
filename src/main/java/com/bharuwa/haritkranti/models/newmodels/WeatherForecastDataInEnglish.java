package com.bharuwa.haritkranti.models.newmodels;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weatherDataIbmInEnglish")
public class WeatherForecastDataInEnglish {
	private String dayOfWeek;
	private String expirationTimeUtc;
	private String moonPhase;
	private String moonPhaseCode;
	private String moonPhaseDay;
	private String moonriseTimeLocal;
	private String moonriseTimeUtc;
	private String moonsetTimeLocal;
	private String moonsetTimeUtc;
	private String narrative;
	private String qpf;
	private String qpfSnow;
	private String sunriseTimeLocal;
	private String sunriseTimeUtc;
	private String sunsetTimeLocal;
	private String sunsetTimeUtc;
	private String temperatureMax;
	private String temperatureMin;
	private String validTimeLocal;
	private String validTimeUtc;
	private Date date;
	private String cityName;
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getExpirationTimeUtc() {
		return expirationTimeUtc;
	}
	public void setExpirationTimeUtc(String expirationTimeUtc) {
		this.expirationTimeUtc = expirationTimeUtc;
	}
	public String getMoonPhase() {
		return moonPhase;
	}
	public void setMoonPhase(String moonPhase) {
		this.moonPhase = moonPhase;
	}
	public String getMoonPhaseCode() {
		return moonPhaseCode;
	}
	public void setMoonPhaseCode(String moonPhaseCode) {
		this.moonPhaseCode = moonPhaseCode;
	}
	public String getMoonPhaseDay() {
		return moonPhaseDay;
	}
	public void setMoonPhaseDay(String moonPhaseDay) {
		this.moonPhaseDay = moonPhaseDay;
	}
	public String getMoonriseTimeLocal() {
		return moonriseTimeLocal;
	}
	public void setMoonriseTimeLocal(String moonriseTimeLocal) {
		this.moonriseTimeLocal = moonriseTimeLocal;
	}
	public String getMoonriseTimeUtc() {
		return moonriseTimeUtc;
	}
	public void setMoonriseTimeUtc(String moonriseTimeUtc) {
		this.moonriseTimeUtc = moonriseTimeUtc;
	}
	public String getMoonsetTimeLocal() {
		return moonsetTimeLocal;
	}
	public void setMoonsetTimeLocal(String moonsetTimeLocal) {
		this.moonsetTimeLocal = moonsetTimeLocal;
	}
	public String getMoonsetTimeUtc() {
		return moonsetTimeUtc;
	}
	public void setMoonsetTimeUtc(String moonsetTimeUtc) {
		this.moonsetTimeUtc = moonsetTimeUtc;
	}
	public String getNarrative() {
		return narrative;
	}
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
	public String getQpf() {
		return qpf;
	}
	public void setQpf(String qpf) {
		this.qpf = qpf;
	}
	public String getQpfSnow() {
		return qpfSnow;
	}
	public void setQpfSnow(String qpfSnow) {
		this.qpfSnow = qpfSnow;
	}
	public String getSunriseTimeLocal() {
		return sunriseTimeLocal;
	}
	public void setSunriseTimeLocal(String sunriseTimeLocal) {
		this.sunriseTimeLocal = sunriseTimeLocal;
	}
	public String getSunriseTimeUtc() {
		return sunriseTimeUtc;
	}
	public void setSunriseTimeUtc(String sunriseTimeUtc) {
		this.sunriseTimeUtc = sunriseTimeUtc;
	}
	public String getSunsetTimeLocal() {
		return sunsetTimeLocal;
	}
	public void setSunsetTimeLocal(String sunsetTimeLocal) {
		this.sunsetTimeLocal = sunsetTimeLocal;
	}
	public String getSunsetTimeUtc() {
		return sunsetTimeUtc;
	}
	public void setSunsetTimeUtc(String sunsetTimeUtc) {
		this.sunsetTimeUtc = sunsetTimeUtc;
	}
	public String getTemperatureMax() {
		return temperatureMax;
	}
	public void setTemperatureMax(String temperatureMax) {
		this.temperatureMax = temperatureMax;
	}
	public String getTemperatureMin() {
		return temperatureMin;
	}
	public void setTemperatureMin(String temperatureMin) {
		this.temperatureMin = temperatureMin;
	}
	public String getValidTimeLocal() {
		return validTimeLocal;
	}
	public void setValidTimeLocal(String validTimeLocal) {
		this.validTimeLocal = validTimeLocal;
	}
	public String getValidTimeUtc() {
		return validTimeUtc;
	}
	public void setValidTimeUtc(String validTimeUtc) {
		this.validTimeUtc = validTimeUtc;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
