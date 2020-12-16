package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserLandMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author harman
 */
public interface UserLandMappingRepo extends MongoRepository<UserLandMapping,String> {
}
