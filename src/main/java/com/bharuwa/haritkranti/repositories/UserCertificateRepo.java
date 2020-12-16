package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserCertificate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface UserCertificateRepo extends MongoRepository<UserCertificate,String> {
}
