package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.fertilizerModels.SubFarmingType;
import com.bharuwa.haritkranti.models.responseModels.FertilizerType;

import java.util.List;

/**
 * @author anuragdhunna
 */
public interface FarmingService {

    FarmingType storeFarmingType(FarmingType farmingType);
    void createDefaultFarmingType(FarmingType farmingType);
    List<FarmingType> getFarmingTypes();

    SubFarmingType storeSubFarmingType(SubFarmingType subFarmingType);
    void createDefaultSubFarmingType(SubFarmingType subFarmingType);
    List<SubFarmingType> getSubFarmingTypes();

    Fertilizer storeFertilizerType(Fertilizer fertilizer);
    Fertilizer createDefaultFertilizerType(Fertilizer fertilizer);
    List<Fertilizer> getFertilizers();
    Fertilizer getFertilizer(String id);

    List<SubFarmingType> getSubFarmingTypesByType(String farmingType);

    List<Fertilizer> getFertilizersBySubFarmingType(String fertilizerType);

    List<FertilizerType> getFertilizerTypes(String fertilizerType);

    List<Fertilizer> getFertilizersByType(String fertilizerType, String categoryType);

    List<Fertilizer> getComplexFertwithoutK();

    Fertilizer getByFertId(String fertId);

    Fertilizer checkFertilizerExists(String fertilizerName, String categoryType, String fertilizerType);

    List<Fertilizer> getFertilizerFromListIds(List<String> fertIds);

    Fertilizer getFertilizerById(String fertilizerId);

}
