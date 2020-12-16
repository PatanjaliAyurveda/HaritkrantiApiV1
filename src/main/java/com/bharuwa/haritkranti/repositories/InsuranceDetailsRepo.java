package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.InsuranceDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface InsuranceDetailsRepo extends MongoRepository<InsuranceDetails,String> {
}
