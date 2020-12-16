package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Soil;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface SoilRepo extends MongoRepository<Soil, String> {

}
