package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.GeoLocationMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface GeoLocationMappingRepo extends MongoRepository<GeoLocationMapping,String> {
}
