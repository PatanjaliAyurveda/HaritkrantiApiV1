package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.CropYield;

public interface CropYieldRepo extends MongoRepository<CropYield, Long>{

}
