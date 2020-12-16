package com.bharuwa.haritkranti.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.WeatherAlert;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinEnglish;
import com.bharuwa.haritkranti.service.WeatherForcastDataIbmInEnglishService;

@Service
public class WeatherForcastDataIbmInEnglishServiceImpl implements WeatherForcastDataIbmInEnglishService{
	private final MongoTemplate mongoTemplate;
	
	public WeatherForcastDataIbmInEnglishServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public void saveWeatherDataInEnglish(WeatherForecastDataInEnglish data) {
		System.out.println("Data is going to save .......");
		mongoTemplate.save(data);
		System.out.println("Data is saved");
	}
	
	@Override
	public void saveWeatherDayPartInEnglish(WeatherForecastDayPartInEnglish daypart) {
		System.out.println("Data is going to save .......");
		mongoTemplate.save(daypart);
		System.out.println("Data is saved");
	}
	
	@Override
	public WeatherAlert getWeatherAlert(int windSpeed,String language) {
		Criteria minCriteria = new Criteria("minRange").lte(windSpeed);
		Criteria maxCriteria = new Criteria("maxRange").gte(windSpeed);
		Query query = new Query();
		query.addCriteria(minCriteria);
		query.addCriteria(maxCriteria);
		WeatherAlert alert = mongoTemplate.findOne(query,WeatherAlert.class);
		if(language.equals("English")) {
			alert.setDisplayAlert(alert.getAlertInEnglish());
		}else if(language.equals("Hindi")){
			alert.setDisplayAlert(alert.getAlertInHindi());
		}
		return alert;
	}
	
}
