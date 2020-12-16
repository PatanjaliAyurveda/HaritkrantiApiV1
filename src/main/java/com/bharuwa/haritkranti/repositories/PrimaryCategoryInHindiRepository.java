package com.bharuwa.haritkranti.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bharuwa.haritkranti.models.PrimaryCategory;
import com.bharuwa.haritkranti.models.PrimaryCategoryInHindi;

public interface PrimaryCategoryInHindiRepository extends MongoRepository<PrimaryCategoryInHindi, String> {
	@Query("{'primaryCategoryName':?0}")
	PrimaryCategoryInHindi findByName(String primaryCategoryName);  
}
