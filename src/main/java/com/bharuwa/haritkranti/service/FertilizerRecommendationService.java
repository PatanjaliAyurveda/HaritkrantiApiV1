package com.bharuwa.haritkranti.service;
import java.util.List;

import com.bharuwa.haritkranti.models.FertilizerCalculations;
import com.bharuwa.haritkranti.models.FertilizerCalculationsInHindi;
import com.bharuwa.haritkranti.models.NPKStandardValue;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;

/**
 * @author sunaina
 */
public interface FertilizerRecommendationService {
	
 //   List getFertilizerCal(String cropName, String agroClimaticZoneName, String language);
    List getFertilizerCal(String cropName, String language, Integer farmId, String userId);

    FertilizerCalculationsInHindi storeFertilizerCal(FertilizerCalculationsInHindi fertilizerCalculationsInHindi);
    
    public CropYield getCropYieldByFarmId(Integer farmId) ;
    
    public CropDetail getCropDetailByFarmId(Integer farmId) ;
    
    public NPKStandardValue getNPKStandardValue(String cropName);
    
    public void saveFertilizerCalculation(FertilizerCalculations calculation,Integer farmId,String userId);
    
}
