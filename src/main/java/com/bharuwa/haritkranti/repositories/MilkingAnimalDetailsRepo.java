package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.MilkingAnimalDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface MilkingAnimalDetailsRepo extends MongoRepository<MilkingAnimalDetails,String> {
}
