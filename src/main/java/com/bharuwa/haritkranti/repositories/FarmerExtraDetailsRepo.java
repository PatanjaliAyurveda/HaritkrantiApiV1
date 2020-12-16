package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FarmerExtraDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface FarmerExtraDetailsRepo extends MongoRepository<FarmerExtraDetails,String> {
}
