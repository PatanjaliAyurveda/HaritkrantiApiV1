package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.crops.Crop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepo extends MongoRepository<Crop, String> {


}
