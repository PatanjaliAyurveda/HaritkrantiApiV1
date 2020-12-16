/**
 * 
 */
package com.bharuwa.haritkranti.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinHindi;
import com.bharuwa.haritkranti.service.WeatherForcastDataIbmInHindiService;

/**
 * @author User
 *
 */
@Service
public class WeatherForcastDataIbmInHindiServiceImpl implements WeatherForcastDataIbmInHindiService{
	
	private final MongoTemplate mongoTemplate;
	
	public WeatherForcastDataIbmInHindiServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public void saveWeatherDataInHindi(WeatherForecastDataInHindi data) {
		System.out.println("Data is going to save .......");
		mongoTemplate.save(data);
		System.out.println("Data is saved");
	}
	
	@Override
	public void saveWeatherDayPartInHindi(WeatherForecastDayPartInHindi daypart) {
		System.out.println("Data is going to save .......");
		mongoTemplate.save(daypart);
		System.out.println("Data is saved");
	}
}
