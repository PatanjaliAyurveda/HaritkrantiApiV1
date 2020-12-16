package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.AgentVillage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface AgentVillageRepo extends MongoRepository<AgentVillage,String> {
}
