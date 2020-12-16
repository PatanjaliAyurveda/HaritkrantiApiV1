package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FieldSize;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface FieldSizeRepo extends MongoRepository<FieldSize, Long> {
}
