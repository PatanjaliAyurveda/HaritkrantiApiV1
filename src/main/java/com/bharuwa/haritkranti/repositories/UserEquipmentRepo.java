package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserEquipments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface UserEquipmentRepo extends MongoRepository<UserEquipments,String> {
}
