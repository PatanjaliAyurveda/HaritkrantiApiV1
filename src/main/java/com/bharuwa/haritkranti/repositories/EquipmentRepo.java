package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface EquipmentRepo extends MongoRepository<Equipment,String> {
}
