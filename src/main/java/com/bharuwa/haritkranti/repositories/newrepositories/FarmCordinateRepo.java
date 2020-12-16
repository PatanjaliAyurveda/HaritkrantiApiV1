package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;

public interface FarmCordinateRepo extends MongoRepository<FarmCordinate, Long>{

}
