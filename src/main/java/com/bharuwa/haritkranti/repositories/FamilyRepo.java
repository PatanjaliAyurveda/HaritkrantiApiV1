package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Family;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface FamilyRepo extends MongoRepository<Family, String> {

}
