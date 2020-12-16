package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface FarmRepo extends MongoRepository<Farm, String> {

}
