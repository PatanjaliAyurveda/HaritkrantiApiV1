package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.GovernmentSchemes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface GovernmentSchemesRepo extends MongoRepository<GovernmentSchemes,String> {
}
