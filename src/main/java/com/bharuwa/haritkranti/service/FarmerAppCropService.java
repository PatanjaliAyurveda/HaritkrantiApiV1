package com.bharuwa.haritkranti.service;


import java.util.List;

import com.bharuwa.haritkranti.models.AgroClimaticZoneInHindi;
import com.bharuwa.haritkranti.models.CropDiseaseInHindi;
import com.bharuwa.haritkranti.models.CropPesticidesInHindi;
import com.bharuwa.haritkranti.models.CropSeedInHindi;
import com.bharuwa.haritkranti.models.CropWeedInHindi;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinaterResponse;


/**
 * @author sunaina
 */
public interface FarmerAppCropService {

    CropYield fetchCropNameFromCropYield(String khasraNo,String language);

    CropDetail storeCropDetail(CropDetail cropDetail,String khasraNo, boolean status,String language);

    CropDetail findByid(String id);

    CropDetail updateAlreadySownCropDetail(String cropDetailId,boolean status);

    CropDetail updateCropDetail(String days, boolean status, String cropDetailId);

    FarmCordinaterResponse getfarmCordinate(String khasraNo);

    List getCropPesticides(String cropName, String state, String district, String sowingTime,String language);

    List getCropDisease(String cropName, String state, String district, String sowingTime,String language);

    List getCropWeed(String cropName, String state, String district, String sowingTime,String language);

    List getCropSeed(String cropName, String state, String district, String sowingTime,String language);

    CropSeedInHindi storeCropSeed(CropSeedInHindi seed);

    CropWeedInHindi storeCropWeed(CropWeedInHindi weed);

    CropDiseaseInHindi storeCropDisease(CropDiseaseInHindi disease);

    CropPesticidesInHindi storeCropPesticides(CropPesticidesInHindi pesticides);

    AgroClimaticZoneInHindi storeAgroClimaticZoneInHindi(AgroClimaticZoneInHindi agroClimaticZoneInHindi);
    
    public CropDetail updateCropName(String cropCategory,String cropName,String cropVariety, String cropDetailId);

}
