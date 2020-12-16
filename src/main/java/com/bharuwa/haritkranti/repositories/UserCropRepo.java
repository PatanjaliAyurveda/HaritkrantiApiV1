package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserCrop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface UserCropRepo extends MongoRepository<UserCrop, String> {
}
