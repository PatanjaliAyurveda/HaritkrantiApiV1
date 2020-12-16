package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.State;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateRepo extends MongoRepository<State,String> {

}
