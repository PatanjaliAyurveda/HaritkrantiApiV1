package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Subsidy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface SubsidyRepo extends MongoRepository<Subsidy, String> {

}
