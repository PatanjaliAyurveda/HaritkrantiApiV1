package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserCropHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harman
 */
@Repository
public interface UserCropHistoryRepo extends MongoRepository<UserCropHistory,String> {
}
