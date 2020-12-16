package com.bharuwa.haritkranti.repositories.fertilizerRepositories;

import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface FarmingTypeRepo extends MongoRepository<FarmingType, Long> {

}
