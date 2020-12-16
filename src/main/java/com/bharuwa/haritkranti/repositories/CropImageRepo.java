package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.CropImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface CropImageRepo extends MongoRepository<CropImage,String> {
}
