package com.bharuwa.haritkranti.models.newmodels;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class WeatherForecastIBMinEnglish {
	private List<String> dayOfWeek;
	private List<String> expirationTimeUtc;
	private List<String> moonPhase;
	private List<String> moonPhaseCode;
	private List<String> moonPhaseDay;
	private List<String> moonriseTimeLocal;
	private List<String> moonriseTimeUtc;
	private List<String> moonsetTimeLocal;
	private List<String> moonsetTimeUtc;
	private List<String> narrative;
	private List<String> qpf;
	private List<String> qpfSnow;
	private List<String> sunriseTimeLocal;
	private List<String> sunriseTimeUtc;
	private List<String> sunsetTimeLocal;
	private List<String> sunsetTimeUtc;
	private List<String> temperatureMax;
	private List<String> temperatureMin;
	private List<String> validTimeLocal;
	private List<String> validTimeUtc;
	private List<DayPart> daypart;
	public List<String> getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(List<String> dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public List<String> getExpirationTimeUtc() {
		return expirationTimeUtc;
	}
	public void setExpirationTimeUtc(List<String> expirationTimeUtc) {
		this.expirationTimeUtc = expirationTimeUtc;
	}
	public List<String> getMoonPhase() {
		return moonPhase;
	}
	public void setMoonPhase(List<String> moonPhase) {
		this.moonPhase = moonPhase;
	}
	public List<String> getMoonPhaseCode() {
		return moonPhaseCode;
	}
	public void setMoonPhaseCode(List<String> moonPhaseCode) {
		this.moonPhaseCode = moonPhaseCode;
	}
	public List<String> getMoonPhaseDay() {
		return moonPhaseDay;
	}
	public void setMoonPhaseDay(List<String> moonPhaseDay) {
		this.moonPhaseDay = moonPhaseDay;
	}
	public List<String> getMoonriseTimeLocal() {
		return moonriseTimeLocal;
	}
	public void setMoonriseTimeLocal(List<String> moonriseTimeLocal) {
		this.moonriseTimeLocal = moonriseTimeLocal;
	}
	public List<String> getMoonriseTimeUtc() {
		return moonriseTimeUtc;
	}
	public void setMoonriseTimeUtc(List<String> moonriseTimeUtc) {
		this.moonriseTimeUtc = moonriseTimeUtc;
	}
	public List<String> getMoonsetTimeLocal() {
		return moonsetTimeLocal;
	}
	public void setMoonsetTimeLocal(List<String> moonsetTimeLocal) {
		this.moonsetTimeLocal = moonsetTimeLocal;
	}
	public List<String> getMoonsetTimeUtc() {
		return moonsetTimeUtc;
	}
	public void setMoonsetTimeUtc(List<String> moonsetTimeUtc) {
		this.moonsetTimeUtc = moonsetTimeUtc;
	}
	public List<String> getNarrative() {
		return narrative;
	}
	public void setNarrative(List<String> narrative) {
		this.narrative = narrative;
	}
	public List<String> getQpf() {
		return qpf;
	}
	public void setQpf(List<String> qpf) {
		this.qpf = qpf;
	}
	public List<String> getQpfSnow() {
		return qpfSnow;
	}
	public void setQpfSnow(List<String> qpfSnow) {
		this.qpfSnow = qpfSnow;
	}
	public List<String> getSunriseTimeLocal() {
		return sunriseTimeLocal;
	}
	public void setSunriseTimeLocal(List<String> sunriseTimeLocal) {
		this.sunriseTimeLocal = sunriseTimeLocal;
	}
	public List<String> getSunriseTimeUtc() {
		return sunriseTimeUtc;
	}
	public void setSunriseTimeUtc(List<String> sunriseTimeUtc) {
		this.sunriseTimeUtc = sunriseTimeUtc;
	}
	public List<String> getSunsetTimeLocal() {
		return sunsetTimeLocal;
	}
	public void setSunsetTimeLocal(List<String> sunsetTimeLocal) {
		this.sunsetTimeLocal = sunsetTimeLocal;
	}
	public List<String> getSunsetTimeUtc() {
		return sunsetTimeUtc;
	}
	public void setSunsetTimeUtc(List<String> sunsetTimeUtc) {
		this.sunsetTimeUtc = sunsetTimeUtc;
	}
	public List<String> getTemperatureMax() {
		return temperatureMax;
	}
	public void setTemperatureMax(List<String> temperatureMax) {
		this.temperatureMax = temperatureMax;
	}
	public List<String> getTemperatureMin() {
		return temperatureMin;
	}
	public void setTemperatureMin(List<String> temperatureMin) {
		this.temperatureMin = temperatureMin;
	}
	public List<String> getValidTimeLocal() {
		return validTimeLocal;
	}
	public void setValidTimeLocal(List<String> validTimeLocal) {
		this.validTimeLocal = validTimeLocal;
	}
	public List<String> getValidTimeUtc() {
		return validTimeUtc;
	}
	public void setValidTimeUtc(List<String> validTimeUtc) {
		this.validTimeUtc = validTimeUtc;
	}
	public List<DayPart> getDaypart() {
		return daypart;
	}
	public void setDaypart(List<DayPart> daypart) {
		this.daypart = daypart;
	}
}
