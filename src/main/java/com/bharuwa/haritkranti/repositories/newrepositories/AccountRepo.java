package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.bharuwa.haritkranti.models.newmodels.Account;

@Repository
public interface AccountRepo extends MongoRepository<Account, Long>{

}
