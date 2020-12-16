package com.bharuwa.haritkranti.repositories.newrepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinHindi;

public interface WeatherForecastDataIbmInHindiRepo extends MongoRepository<WeatherForecastIBMinHindi, Long>  {
	
}
