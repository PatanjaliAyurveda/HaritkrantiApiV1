package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface CityRepo extends MongoRepository<City, Long> {

}
