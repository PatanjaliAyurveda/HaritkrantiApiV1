package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FamilyMemberHealthRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface FamilyMemberHeathRepo extends MongoRepository<FamilyMemberHealthRecord,String> {
}
