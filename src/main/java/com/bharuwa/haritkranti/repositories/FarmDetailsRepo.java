package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FarmDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface FarmDetailsRepo extends MongoRepository<FarmDetails,String> {
}
