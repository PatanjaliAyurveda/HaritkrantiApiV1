package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.responseModels.AssignLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignLocationRepo extends MongoRepository<AssignLocation,String>{
}
