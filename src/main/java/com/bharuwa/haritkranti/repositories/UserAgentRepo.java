package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserAgent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harmanpreet
 */
@Repository
public interface UserAgentRepo extends MongoRepository<UserAgent,String> {
}
