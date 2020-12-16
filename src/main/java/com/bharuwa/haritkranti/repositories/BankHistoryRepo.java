package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.responseModels.BankHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sunaina
 */
@Repository
public interface BankHistoryRepo extends MongoRepository<BankHistory,String> {
}
