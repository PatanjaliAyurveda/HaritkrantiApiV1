package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserIdentificationDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface UserIdentificationDetailRepo extends MongoRepository<UserIdentificationDetails,String> {
}
