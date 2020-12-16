package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.OrganicCertificate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface OrganicCertificateRepo extends MongoRepository<OrganicCertificate,String> {
}
