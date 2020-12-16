package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FinancialDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface FinancialDetailRepo extends MongoRepository<FinancialDetails,String> {
}
