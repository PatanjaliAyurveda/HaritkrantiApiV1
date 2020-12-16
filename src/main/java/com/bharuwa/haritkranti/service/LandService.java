package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.location.CityCropSoil;
import com.bharuwa.haritkranti.models.newmodels.GovtLandDetail;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author anuragdhunna
 */
public interface LandService {

    Crop storeCrop(Crop crop);
    Crop createDefaultCrop(Crop crop);
    Page<Crop> getCrops(int page, int size);
    Crop getCrop(String id);

    Soil storeSoil(Soil soil);
    void createDefaultSoil(Soil soil);
    List<Soil> getSoils();
    List<Soil> getSoils(List<String> soilNames);

    FieldSize storeFieldSize(FieldSize fieldSize);
    void createDefaultFieldSize(FieldSize fieldSize);
    List<FieldSize> getFieldSizes();

    List<Crop> findCropByIds(List<String> cropIds);

    List<Soil> getSoilsByCropId(String cropId);

    List<Fertilizer> getFertilizersByCropId(String cropId, String fertilizerType, String categoryType);

    Soil getSoil(String soilId);

    List<Fertilizer> getAllFertilizersByCropId(String cropId);

    List<Crop> getCropByType(String cropType);

    Crop getCropByNameAndType(String cropName, String cropType);

    Crop checkCropExists(Crop crop);

    List<Soil> checkSoilForCrop(String cropId, String soilId);

    Soil findSoilByName(String soilName);

    CityCropSoil getCityCropSoil(String cityId, String cropId, String soilId);
    CityCropSoil getCityCropSoil(String cityId, String cropId, String soilId, LandType.Type irrigationType);

    List<String> getCropTypesByDistrict(String cityId, String stateId);
    List<Crop> getCropTypesByDistrictAndType(String cityId, String stateId, String cropType);

    List<Soil> getSoilsByDistrict(String cityId, String stateId);

    Horticulture addHorticultureDetails(Horticulture horticulture);

    List<Horticulture> getHorticultureDetails(String userId);

    Horticulture getHorticultureDetailById(String horticultureId);

    void cropIdtoCropModel();

    void bighaToAcre(String stateId, String size);

    void organiseCityCropSoil();

    void removeDuplicateRecordsCityCropSoil();

    List<Crop> getCropsBySeason(String season);

    String uploadSoilImage(MultipartFile file, String soilId);

    Soil addSoilLocale(Locale locale, String soilId);

    <T> T  genericAddLocale(Locale locale, String id, String type);
    
    public List<String> getAllKhasraByFarmerId(String farmerId);
    
    public List<UserCrop> getAllKhasraCropByFarmerId(String farmerId);
    
    public List<GovtLandDetail> getGovtLandDetail(String khasraNumber,String laguage);
    
    public UserKhasraMapping storeUserKhasraMapping(UserKhasraMapping UserKhasraMapping);

}
