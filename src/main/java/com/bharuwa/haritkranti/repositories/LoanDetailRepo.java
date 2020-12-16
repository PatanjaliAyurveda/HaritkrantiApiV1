package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.LoanDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface LoanDetailRepo extends MongoRepository<LoanDetails,String> {
}
