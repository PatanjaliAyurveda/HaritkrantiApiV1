package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.VillageAsset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageAssetRepo extends MongoRepository<VillageAsset, String> {
}