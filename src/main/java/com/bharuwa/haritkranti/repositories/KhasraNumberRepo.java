package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.KhasraNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface KhasraNumberRepo extends MongoRepository<KhasraNumber,String> {
}
