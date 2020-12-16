package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.*;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.fertilizerModels.SubFarmingType;
import com.bharuwa.haritkranti.models.newmodels.GovtLandDetail;
import com.bharuwa.haritkranti.models.requestModels.UserReqBodyForAppUser;
import com.bharuwa.haritkranti.models.responseModels.FertilizerType;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LandController extends BaseController {

    @RequestMapping(value = "/storeCrop", method = RequestMethod.POST)
    public Crop storeCrop(@RequestBody Crop crop) {
        return landService.storeCrop(crop);
    }

    @RequestMapping(value = "/getCropById", method = RequestMethod.GET)
    public Crop getCrop(@RequestParam String id){
        return landService.getCrop(id);
    }

    /**
     * list of crops by pagination
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/getCrops", method = RequestMethod.GET)
    public Page<Crop> getCrops(@RequestParam(value = "page", defaultValue = "0")int page,
                               @RequestParam(value = "size", defaultValue = "20")int size) {
        return landService.getCrops(page,size);
    }

    @RequestMapping(value = "/storeSoil", method = RequestMethod.POST)
    public Soil storeSoil(@RequestBody Soil soil) {
        return landService.storeSoil(soil);
    }

    @RequestMapping(value = "/getCropTypes", method = RequestMethod.GET)
    public List<Crop.CropType> getCropTypes() {
        return Arrays.asList(Crop.CropType.values());
    }

    @RequestMapping(value = "/getCropByType", method = RequestMethod.GET)
    public List<Crop> getCropByType(@RequestParam String cropType) {
        return landService.getCropByType(cropType);
    }

    @RequestMapping(value = "/getSoils", method = RequestMethod.GET)
    public List<Soil> getSoils() {
        return landService.getSoils();
    }

    @RequestMapping(value = "/getSoilsByCropId", method = RequestMethod.GET)
    public List<Soil> getSoilsByCropId(@RequestParam String cropId) {
        return landService.getSoilsByCropId(cropId);
    }

    @RequestMapping(value = "/storeFieldSize", method = RequestMethod.POST)
    public FieldSize storeFieldSize(@RequestBody FieldSize fieldSize) {
        return landService.storeFieldSize(fieldSize);
    }

    @RequestMapping(value = "/getFieldSizes", method = RequestMethod.GET)
    public List<FieldSize> getFieldSizes() {
        return landService.getFieldSizes();
    }

    @RequestMapping(value = "/storeFarmingType", method = RequestMethod.POST)
    public FarmingType storeFarmingType(@RequestBody FarmingType farmingType) {
        return farmingService.storeFarmingType(farmingType);
    }

    @RequestMapping(value = "/getFarmingTypes", method = RequestMethod.GET)
    public List<FarmingType> getFarmingTypes() {
        return farmingService.getFarmingTypes();
    }

    @RequestMapping(value = "/storeSubFarmingType", method = RequestMethod.POST)
    public SubFarmingType storeFarmingType(@RequestBody SubFarmingType subFarmingType) {
        return farmingService.storeSubFarmingType(subFarmingType);
    }

    @RequestMapping(value = "/getSubFarmingTypes", method = RequestMethod.GET)
    public List<SubFarmingType> getSubFarmingTypes() {
        return farmingService.getSubFarmingTypes();
    }

    @RequestMapping(value = "/storeFertilizerType", method = RequestMethod.POST)
    public Fertilizer storeFertilizerType(@RequestBody Fertilizer fertilizer) {
//        fertilizer.setCropList(landService.findCropByIds(cropIds));
        return farmingService.storeFertilizerType(fertilizer);
    }

    @RequestMapping(value = "/getFertilizers", method = RequestMethod.GET)
    public List<Fertilizer> getFertilizers() {
        return farmingService.getFertilizers();
    }

    @RequestMapping(value = "/getFertilizersByCropId", method = RequestMethod.GET)
    public List<Fertilizer> getFertilizersByCropId(@RequestParam String cropId, @RequestParam String fertilizerType, @RequestParam String categoryType) {
        return landService.getFertilizersByCropId(cropId, fertilizerType, categoryType);
    }

    @RequestMapping(value = "/getAllFertilizersByCropId", method = RequestMethod.GET)
    public List<Fertilizer> getAllFertilizersByCropId(@RequestParam String cropId) {
        return landService.getAllFertilizersByCropId(cropId);
    }

    @RequestMapping(value = "/getSubFarmingTypesByType", method = RequestMethod.GET)
    public List<SubFarmingType> getSubFarmingTypesByType(@RequestParam String farmingType) {
        return farmingService.getSubFarmingTypesByType(farmingType);
    }

    @RequestMapping(value = "/getFertilizersBySubFarmingType", method = RequestMethod.GET)
    public List<Fertilizer> getFertilizersBySubFarmingType(@RequestParam String fertilizerType) {
        return farmingService.getFertilizersBySubFarmingType(fertilizerType);
    }

    @RequestMapping(value = "/getFertilizerTypes", method = RequestMethod.GET)
    public List<FertilizerType> getFertilizerTypes(@RequestParam String fertilizerType) {
        return farmingService.getFertilizerTypes(fertilizerType);
    }

    @RequestMapping(value = "/getFertilizersByType", method = RequestMethod.GET)
    public List<Fertilizer> getFertilizersByType(@RequestParam String fertilizerType, @RequestParam String categoryType) {
        return farmingService.getFertilizersByType(fertilizerType, categoryType);
    }

    @RequestMapping(value = "/getLandKhasraNos", method = RequestMethod.GET)
    public Set<String> getLandKhasraNos(@RequestParam String userId) {
        return userService.getLandKhasraNos(userId);
    }
    
    @RequestMapping(value = "/getKhasraList", method = RequestMethod.GET)
    public List<String> getKhasraList(@RequestParam String state,@RequestParam String district,@RequestParam String tehsil,@RequestParam String block,@RequestParam String village,@RequestParam String language) {
        return userService.getKhasraList(state,district,tehsil,block,village,language);
    }

    @RequestMapping(value = "/getComplexFertwithoutK", method = RequestMethod.GET)
    public List<Fertilizer> getComplexFertwithoutK() {
        return farmingService.getComplexFertwithoutK();
    }

    @RequestMapping(value = "/getLandTypes", method = RequestMethod.GET)
    public List<LandType.Type> getLandTypes() {
        return Arrays.asList(LandType.Type.values());
    }

    @Deprecated
    @RequestMapping(value = "/checkSoilForCrop", method = RequestMethod.GET)
    public List<Soil> checkSoilForCrop(@RequestParam String cropId, @RequestParam String soilId) {
        return landService.checkSoilForCrop(cropId, soilId);
    }

    @RequestMapping(value = "/getCropTypesByDistrict", method = RequestMethod.GET)
    public List<String> getCropTypesByDistrict(@RequestParam String cityId, @RequestParam String stateId) {
        return landService.getCropTypesByDistrict(cityId, stateId);
    }

    @RequestMapping(value = "/getCropTypesByDistrictAndType", method = RequestMethod.GET)
    public List<Crop> getCropTypesByDistrictAndType(@RequestParam String cityId, @RequestParam String stateId, @RequestParam String cropType) {
        return landService.getCropTypesByDistrictAndType(cityId, stateId, cropType);
    }

    @RequestMapping(value = "/getSoilsByDistrict", method = RequestMethod.GET)
    public List<Soil> getSoilsByDistrict(@RequestParam String cityId, @RequestParam String stateId) {
        return landService.getSoilsByDistrict(cityId, stateId);
    }

    @RequestMapping(value = "/addHorticultureDetails", method = RequestMethod.POST)
    public Horticulture addHorticultureDetails(@RequestBody Horticulture horticulture) {
        return landService.addHorticultureDetails(horticulture);
    }

    @RequestMapping(value = "/getHorticultureDetails", method = RequestMethod.GET)
    public List<Horticulture> getHorticultureDetails(@RequestParam String userId) {
        return landService.getHorticultureDetails(userId);
    }

    @RequestMapping(value = "/getTreesList", method = RequestMethod.GET)
    public List<Horticulture.TreeType> getTreesList() {
        return Arrays.asList(Horticulture.TreeType.values());
    }

    @RequestMapping(value = "/getHorticultureDetailById", method = RequestMethod.GET)
    public Horticulture getHorticultureDetailById(@RequestParam String horticultureId) {
        return landService.getHorticultureDetailById(horticultureId);
    }

    @RequestMapping(value = "/bighaToAcre", method = RequestMethod.GET)
    public void bighaToAcre(@Nullable @RequestParam String stateId, @RequestParam String size) {
        landService.bighaToAcre(stateId, size);
    }

    @RequestMapping(value = "/getFertilizerById", method = RequestMethod.GET)
    public Fertilizer getFertilizerById(@RequestParam String fertilizerId) {
        return farmingService.getFertilizerById(fertilizerId);
    }
    
    @RequestMapping(value = "/getAllKhasraByFarmerId", method = RequestMethod.GET)
    public List<String> getAllKhasraByFarmerId(@RequestParam String farmerId) {
        return landService.getAllKhasraByFarmerId(farmerId);
    }

    @ApiOperation(value = "Add Locale for Fertilizer, Soil or Crop")
    @RequestMapping(value = "/genericAddLocale", method = RequestMethod.POST)
    public <T> T  addSoilLocale(@RequestBody Locale locale, @RequestParam String id, @RequestParam String  type) {
        return landService.genericAddLocale(locale, id, type);
    }
    
    
    @RequestMapping(value = "/getAllKhasraCropByFarmerId", method = RequestMethod.GET)
    public List<UserCrop> getAllKhasraCropByFarmerId(@RequestParam String farmerId) {
        return landService.getAllKhasraCropByFarmerId(farmerId);
    }
    
    @RequestMapping(value = "/getGovtLandDetail", method = RequestMethod.GET)
    public List<GovtLandDetail> getGovtLandDetail(@RequestParam String khasraNumber,@RequestParam String laguage) {
        return landService.getGovtLandDetail(khasraNumber,laguage);
    }
    
    @ApiOperation(value = "SignUp API for Farmers from App")
    @RequestMapping(value = "/addKhasra", method = RequestMethod.POST)
    public List<UserKhasraMapping> addKhasra(@RequestBody List<UserKhasraMapping> userKhasraMappingList) throws Exception {
    	List<UserKhasraMapping> list = new ArrayList<UserKhasraMapping>();
    	for(UserKhasraMapping mapping:userKhasraMappingList) {
    		UserKhasraMapping khasraMapping=landService.storeUserKhasraMapping(mapping);
    		list.add(khasraMapping);
       }
       return list;
    }
    
}
