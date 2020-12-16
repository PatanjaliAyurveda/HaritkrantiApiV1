package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinEnglish;

public interface WeatherForecastDataIbmInEnglishRepo extends MongoRepository<WeatherForecastIBMinEnglish, Long>  {

}
