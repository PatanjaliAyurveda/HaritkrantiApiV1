package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.CityCropSoil;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface CityCropSoilRepo extends MongoRepository<CityCropSoil, Long> {
}
