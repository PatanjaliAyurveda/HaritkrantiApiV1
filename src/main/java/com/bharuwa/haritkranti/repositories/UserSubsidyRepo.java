package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserSubsidy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface UserSubsidyRepo extends MongoRepository<UserSubsidy, String> {

}
