package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.IncomeSources;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface IncomeSourcesRepo extends MongoRepository<IncomeSources,String> {
}
