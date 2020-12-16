package com.bharuwa.haritkranti.repositories.fertilizerRepositories;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface FertilizerRepo  extends MongoRepository<Fertilizer, Long> {

}
