package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserSchemes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface UserSchemesRepo extends MongoRepository<UserSchemes,String> {
}
