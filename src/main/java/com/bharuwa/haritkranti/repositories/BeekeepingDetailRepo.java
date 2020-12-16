package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.BeekeepingDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface BeekeepingDetailRepo extends MongoRepository<BeekeepingDetails,String> {
}
