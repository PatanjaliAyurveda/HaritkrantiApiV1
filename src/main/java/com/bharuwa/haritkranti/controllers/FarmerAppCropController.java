package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.AgroClimaticZoneInHindi;
import com.bharuwa.haritkranti.models.CropDiseaseInHindi;
import com.bharuwa.haritkranti.models.CropPesticidesInHindi;
import com.bharuwa.haritkranti.models.CropSeedInHindi;
import com.bharuwa.haritkranti.models.CropWeedInHindi;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinaterResponse;
import com.bharuwa.haritkranti.service.FarmerAppCropService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author sunaina
 */
@RestController
@RequestMapping("/api")
public class FarmerAppCropController {

    @Autowired
    public FarmerAppCropService farmerAppCropService;
    
    /**
     * get crop name from cropYield table against the khasra number.
     * @param khasraNo : for getting crop name on the basis of khasra no.
     * @return CropYield model
     */
    
    @RequestMapping(value = "/fetchCropNameFromCropYield", method = RequestMethod.GET)
    public CropYield fetchCropNameFromCropYield(@RequestParam String khasraNo,@RequestParam String language) {
        return farmerAppCropService.fetchCropNameFromCropYield(khasraNo,language);
    }

    /**
     * store CropDetail
     * @param status   : If input parameter would be yes then satellite crop and actual crop would be same and if no then actual crop input will be given by the user.
     * @param khasraNo :for fetching crop name from cropYield table against the khasra number.
     * @return CropDetail model
     */
    @RequestMapping(value = "/storeCropDetail", method = RequestMethod.POST)
    public CropDetail storeCropDetail(@RequestBody CropDetail cropDetail, @RequestParam String khasraNo, @RequestParam boolean status,@RequestParam String language) {
        return farmerAppCropService.storeCropDetail(cropDetail, khasraNo, status,language);
    }

    /**
     * Update one field of CropDetail table i.e alreadySown.
     * @param status : value would be either Y or N.
     * @return CropDetail model
     */
    @RequestMapping(value = "/updateAlreadySownCropDetail", method = RequestMethod.PUT)
    public CropDetail updateAlreadySownCropDetail(@RequestParam String cropDetailId, @RequestParam boolean status) {
        return farmerAppCropService.updateAlreadySownCropDetail(cropDetailId, status);
    }

    /**
     * update before days or after days field of cropDetail table.
     * input param : days
     * @param status : value would be either Y or N.
     * @return CropDetail model
     */
    @RequestMapping(value = "/updateCropDetail", method = RequestMethod.PUT)
    public CropDetail updateCropDetail(@RequestParam String days, @RequestParam boolean status, @RequestParam String cropDetailId) {
        return farmerAppCropService.updateCropDetail(days, status, cropDetailId);
    }
    
    @RequestMapping(value = "/updateCropName", method = RequestMethod.PUT)
    public CropDetail updateCropName(@RequestParam String cropCategory, @RequestParam String cropName, @RequestParam String cropVariety, @RequestParam String cropDetailId) {
        return farmerAppCropService.updateCropName(cropCategory, cropName,cropVariety, cropDetailId);
    }


    /**
     * get Farm coordinate against khasra number
     * @param khasraNo :for fetching data from CropYield table against the khasra number.
     * @return FarmCordinate model
     */
    @RequestMapping(value = "/getfarmCordinate", method = RequestMethod.GET)
    public FarmCordinaterResponse getfarmCordinate(@RequestParam String khasraNo) {
        return farmerAppCropService.getfarmCordinate(khasraNo);
    }

    /**
     * get list of CropPesticides against cropName, agroclimaticZone and sowingTime
     */
    @RequestMapping(value = "/getCropPesticides", method = RequestMethod.GET)
    public List getCropPesticides(@RequestParam String cropName, @RequestParam String state, @RequestParam String district, @RequestParam String sowingTime,@RequestParam String language) {
        return farmerAppCropService.getCropPesticides(cropName, state, district, sowingTime,language);
    }


    /**
     * get list of CropDisease against cropName, agroclimaticZone and sowingTime
     */
    @RequestMapping(value = "/getCropDisease", method = RequestMethod.GET)
    public List getCropDisease(@RequestParam String cropName, @RequestParam String state, @RequestParam String district, @RequestParam String sowingTime,@RequestParam String language) {
        return farmerAppCropService.getCropDisease(cropName, state, district, sowingTime,language);
    }

    /**
     * get list of CropWeed against cropName, agroclimaticZone and sowingTime
     */
    @RequestMapping(value = "/getCropWeed", method = RequestMethod.GET)
    public List getCropWeed(@RequestParam String cropName, @RequestParam String state, @RequestParam String district, @RequestParam String sowingTime,@RequestParam String language) {
        return farmerAppCropService.getCropWeed(cropName, state, district, sowingTime,language);
    }

    /**
     * get list of CropSeed against cropName, agroclimaticZone and sowingTime
     */
    @RequestMapping(value = "/getCropSeed", method = RequestMethod.GET)
    public List getCropSeed(@RequestParam String cropName, @RequestParam String state, @RequestParam String district, @RequestParam String sowingTime,@RequestParam String language) {
        return farmerAppCropService.getCropSeed(cropName, state, district, sowingTime,language);
    }

    @RequestMapping(value = "/storeAgroClimaticZoneInHindi", method = RequestMethod.POST)
    public AgroClimaticZoneInHindi storeAgroClimaticZoneInHindi(@RequestBody AgroClimaticZoneInHindi agroClimaticZoneInHindi) {
        return farmerAppCropService.storeAgroClimaticZoneInHindi(agroClimaticZoneInHindi);
    }


    @RequestMapping(value = "/storeCropSeed", method = RequestMethod.POST)
    public CropSeedInHindi storeCropSeed(@RequestBody CropSeedInHindi seed) {
        return farmerAppCropService.storeCropSeed(seed);
    }

    @RequestMapping(value = "/storeCropWeed", method = RequestMethod.POST)
    public CropWeedInHindi storeCropWeed(@RequestBody CropWeedInHindi weed) {
        return farmerAppCropService.storeCropWeed(weed);
    }

    @RequestMapping(value = "/storeCropDisease", method = RequestMethod.POST)
    public CropDiseaseInHindi storeCropDisease(@RequestBody CropDiseaseInHindi disease) {
        return farmerAppCropService.storeCropDisease(disease);
    }

    @RequestMapping(value = "/storeCropPesticides", method = RequestMethod.POST)
    public CropPesticidesInHindi storeCropPesticides(@RequestBody CropPesticidesInHindi pesticides) {
        return farmerAppCropService.storeCropPesticides(pesticides);
    }
}

