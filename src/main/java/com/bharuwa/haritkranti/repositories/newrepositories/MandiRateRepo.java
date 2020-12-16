package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;

@Repository
public interface MandiRateRepo extends MongoRepository<MandiRateRecord, Long> {

}
