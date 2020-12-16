package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.Village;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface VillageRepo extends MongoRepository<Village, String> {
}
