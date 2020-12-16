package com.bharuwa.haritkranti.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bharuwa.haritkranti.models.PrimaryCategory;

public interface PrimaryCategoryRepository extends MongoRepository<PrimaryCategory, String> {
	
	@Query("{'primaryCategoryName':?0}")
	PrimaryCategory findByName(String primaryCategoryName);  
	
}
