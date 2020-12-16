package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.location.Block;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface BlockRepo extends MongoRepository<Block,String> {
}
