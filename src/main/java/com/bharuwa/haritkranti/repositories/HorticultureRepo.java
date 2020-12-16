package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Horticulture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface HorticultureRepo extends MongoRepository<Horticulture,String> {
}
