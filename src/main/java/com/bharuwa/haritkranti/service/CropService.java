package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.crops.FruitVariety;
import com.bharuwa.haritkranti.models.crops.FruitVarietyLocation;
import com.bharuwa.haritkranti.models.requestModels.CropGroupReq;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;

import java.util.List;

/**
 * @author anuragdhunna
 */
public interface CropService {

    CropGroup addCropGroup(CropGroupReq cropGroup);
    
    List<CropGroup> getCropGroupsByType(String type);

    FruitVariety addFruitVariety(FruitVariety fruitVariety);

    List<FruitVarietyLocation> getFruitVarietyLocationByState(String stateId, String cropGroupId);

    List<FruitVariety> getFruitsByGroup(String cropGroupId);

    CropGroup updateCropGroup(CropGroup cropGroup);

    CropGroup getCropGroupsByNameAndType(String name, String type);

    Crop findCropByNameAndType(String cropGroupName, String cropGroupType, String cropName);
    
    public void saveCropYield(List<CropYield> list);

    public void saveFarmCoordinate(List<FarmCordinate> list);
}
