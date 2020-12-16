package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FamilyMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface FamilyMemberRepo extends MongoRepository<FamilyMember,String> {
}
