package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    @Query("{ 'username' : ?0 }")
    User findByUsername(String username);

    @Query("{ 'email' : ?0 }")
    User findByEmail(String email);

    @Query("{ 'id' : ?0 }")
    User findByid(String id);

}
