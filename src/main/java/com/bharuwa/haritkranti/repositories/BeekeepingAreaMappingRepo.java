package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.BeekeepingAreaMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface BeekeepingAreaMappingRepo extends MongoRepository<BeekeepingAreaMapping,String> {
}
