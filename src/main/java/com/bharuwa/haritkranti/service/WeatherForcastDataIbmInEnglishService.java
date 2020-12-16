package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.newmodels.WeatherAlert;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinEnglish;

public interface WeatherForcastDataIbmInEnglishService {
	public void saveWeatherDataInEnglish(WeatherForecastDataInEnglish data);
	public void saveWeatherDayPartInEnglish(WeatherForecastDayPartInEnglish daypart);
	public WeatherAlert getWeatherAlert(int windSpeed,String language);
}
