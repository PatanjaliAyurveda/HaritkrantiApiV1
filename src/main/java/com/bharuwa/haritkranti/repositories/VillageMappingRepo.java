package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.VillageMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface VillageMappingRepo extends MongoRepository<VillageMapping,String> {
}
