package com.bharuwa.haritkranti.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.FertilizerCalculations;
import com.bharuwa.haritkranti.models.FertilizerCalculationsInHindi;
import com.bharuwa.haritkranti.models.NPKStandardValue;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.service.FertilizerRecommendationService;

import java.util.List;


/**
 * @author sunaina
 */
@Service
public class FertilizerRecommendationServiceImpl implements FertilizerRecommendationService{

    private final MongoTemplate mongoTemplate;

    public FertilizerRecommendationServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List getFertilizerCal(String cropName, String language, Integer farmId, String userId) {
        switch (language) {
        
            case "Hindi":
                Query query = new Query(Criteria.where("cropNameInHindi").is(cropName).and("farmId").is(farmId).and("userId").is(userId));
                return mongoTemplate.find(query, FertilizerCalculations.class);

            case "English":
            	query = new Query(Criteria.where("cropName").is(cropName).and("farmId").is(farmId).and("userId").is(userId));
                return mongoTemplate.find(query,FertilizerCalculations.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public FertilizerCalculationsInHindi storeFertilizerCal(FertilizerCalculationsInHindi fertilizerCalculationsInHindi) {
        return mongoTemplate.save(fertilizerCalculationsInHindi);
    }
    
    @Override
    public CropYield getCropYieldByFarmId(Integer farmId) {
    	Query query = new Query(Criteria.where("farmId").is(farmId));
    	return mongoTemplate.findOne(query, CropYield.class);
    }
    
    @Override
    public CropDetail getCropDetailByFarmId(Integer farmId) {
    	Query query = new Query(Criteria.where("farmId").is(farmId));
    	return mongoTemplate.findOne(query, CropDetail.class);
    }
    
    @Override
    public NPKStandardValue getNPKStandardValue(String cropName) {
    	Query query = new Query(Criteria.where("cropName").is(cropName));
    	return mongoTemplate.findOne(query, NPKStandardValue.class);
    }
    
    @Override
    public void saveFertilizerCalculation(FertilizerCalculations calculation,Integer farmId,String userId) {
    	calculation.setFarmId(farmId);
    	calculation.setUserId(userId);
    	Query query = new Query(Criteria.where("cropName").is(calculation.getCropName()).and("farmId").is(farmId).and("userId").is(userId));
    	FertilizerCalculations calc = mongoTemplate.findOne(query, FertilizerCalculations.class);
    	if(calc==null) {
    		query = new Query(Criteria.where("cropNameInHindi").is(calculation.getCropNameInHindi()).and("farmId").is(farmId).and("userId").is(userId));
    		calc = mongoTemplate.findOne(query, FertilizerCalculations.class);
    	}
    	if(calc==null)
    		mongoTemplate.save(calculation);
    }
}


