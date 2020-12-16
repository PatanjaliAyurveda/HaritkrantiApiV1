package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Dealer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerRepo extends MongoRepository<Dealer,String> {

}
