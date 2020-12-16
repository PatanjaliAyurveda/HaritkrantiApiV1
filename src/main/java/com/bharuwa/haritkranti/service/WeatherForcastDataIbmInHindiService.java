package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInHindi;



public interface WeatherForcastDataIbmInHindiService {
	public void saveWeatherDataInHindi(WeatherForecastDataInHindi data);
	public void saveWeatherDayPartInHindi(WeatherForecastDayPartInHindi daypart);
}
