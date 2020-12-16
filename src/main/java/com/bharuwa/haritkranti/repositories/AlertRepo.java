package com.bharuwa.haritkranti.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.Alert;

public interface AlertRepo extends MongoRepository<Alert, String>{

}
