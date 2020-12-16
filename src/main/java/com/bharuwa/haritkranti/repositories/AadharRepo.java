package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Aadhar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface AadharRepo extends MongoRepository<Aadhar, String> {

}
