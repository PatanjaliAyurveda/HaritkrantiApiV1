package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.Tehsil;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface TehsilRepo extends MongoRepository<Tehsil,String> {
}
