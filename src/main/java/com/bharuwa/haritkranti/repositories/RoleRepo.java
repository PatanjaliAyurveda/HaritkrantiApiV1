package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends MongoRepository<Role,Long> {

    @Query("{ 'roleName' : ?0 }")
    Role findByName(String roleName);
}