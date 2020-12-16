package com.bharuwa.haritkranti.models.newmodels;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dayPartInEnglish")
public class WeatherForecastDayPartInEnglish {
	private String cloudCover;
	private String dayOrNight;
	private String daypartName;
	private String iconCode;
	private String iconCodeExtend;
	private String narrative;
	private String precipChance;
	private String precipType;
	private String qpfSnow;
	private String qualifierCode;
	private String qualifierPhrase;
	private String relativeHumidity;
	
	private String snowRange;
	private String temperature;
	private String temperatureHeatIndex;
	private String temperatureWindChill;
	private String thunderCategory;
	private String thunderIndex;
	private String uvDescription;
	private String windDirection;
	private String windDirectionCardinal;
	private String windPhrase;
	private String windSpeed;
	private String wxPhraseLong;
	private Date date;
	private String cityName;
	
	public String getCloudCover() {
		return cloudCover;
	}
	public void setCloudCover(String cloudCover) {
		this.cloudCover = cloudCover;
	}
	public String getDayOrNight() {
		return dayOrNight;
	}
	public void setDayOrNight(String dayOrNight) {
		this.dayOrNight = dayOrNight;
	}
	public String getDaypartName() {
		return daypartName;
	}
	public void setDaypartName(String daypartName) {
		this.daypartName = daypartName;
	}
	public String getIconCode() {
		return iconCode;
	}
	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
	public String getIconCodeExtend() {
		return iconCodeExtend;
	}
	public void setIconCodeExtend(String iconCodeExtend) {
		this.iconCodeExtend = iconCodeExtend;
	}
	public String getNarrative() {
		return narrative;
	}
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
	public String getPrecipChance() {
		return precipChance;
	}
	public void setPrecipChance(String precipChance) {
		this.precipChance = precipChance;
	}
	public String getPrecipType() {
		return precipType;
	}
	public void setPrecipType(String precipType) {
		this.precipType = precipType;
	}
	public String getQpfSnow() {
		return qpfSnow;
	}
	public void setQpfSnow(String qpfSnow) {
		this.qpfSnow = qpfSnow;
	}
	public String getQualifierCode() {
		return qualifierCode;
	}
	public void setQualifierCode(String qualifierCode) {
		this.qualifierCode = qualifierCode;
	}
	public String getQualifierPhrase() {
		return qualifierPhrase;
	}
	public void setQualifierPhrase(String qualifierPhrase) {
		this.qualifierPhrase = qualifierPhrase;
	}
	public String getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(String relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public String getSnowRange() {
		return snowRange;
	}
	public void setSnowRange(String snowRange) {
		this.snowRange = snowRange;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getTemperatureHeatIndex() {
		return temperatureHeatIndex;
	}
	public void setTemperatureHeatIndex(String temperatureHeatIndex) {
		this.temperatureHeatIndex = temperatureHeatIndex;
	}
	public String getTemperatureWindChill() {
		return temperatureWindChill;
	}
	public void setTemperatureWindChill(String temperatureWindChill) {
		this.temperatureWindChill = temperatureWindChill;
	}
	public String getThunderCategory() {
		return thunderCategory;
	}
	public void setThunderCategory(String thunderCategory) {
		this.thunderCategory = thunderCategory;
	}
	public String getThunderIndex() {
		return thunderIndex;
	}
	public void setThunderIndex(String thunderIndex) {
		this.thunderIndex = thunderIndex;
	}
	public String getUvDescription() {
		return uvDescription;
	}
	public void setUvDescription(String uvDescription) {
		this.uvDescription = uvDescription;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public String getWindDirectionCardinal() {
		return windDirectionCardinal;
	}
	public void setWindDirectionCardinal(String windDirectionCardinal) {
		this.windDirectionCardinal = windDirectionCardinal;
	}
	public String getWindPhrase() {
		return windPhrase;
	}
	public void setWindPhrase(String windPhrase) {
		this.windPhrase = windPhrase;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getWxPhraseLong() {
		return wxPhraseLong;
	}
	public void setWxPhraseLong(String wxPhraseLong) {
		this.wxPhraseLong = wxPhraseLong;
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
