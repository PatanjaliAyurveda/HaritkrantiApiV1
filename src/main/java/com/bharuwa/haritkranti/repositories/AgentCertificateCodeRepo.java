package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.AgentCertificateCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface AgentCertificateCodeRepo extends MongoRepository<AgentCertificateCode,String> {
}
