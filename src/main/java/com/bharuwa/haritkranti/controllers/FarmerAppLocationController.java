package com.bharuwa.haritkranti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bharuwa.haritkranti.models.location.BlockInHindi;
import com.bharuwa.haritkranti.models.location.CityInHindi;
import com.bharuwa.haritkranti.models.location.StateInHindi;
import com.bharuwa.haritkranti.models.location.TehsilInHindi;
import com.bharuwa.haritkranti.models.location.VillageInHindi;
import com.bharuwa.haritkranti.service.FarmerAppLocationService;

import java.util.List;

/**
 * @author sunaina
 */
@RestController
@RequestMapping("/api")
public class FarmerAppLocationController {

    @Autowired
    public FarmerAppLocationService farmerAppLocationService;

    @RequestMapping(value = "/storeStateInHindi", method = RequestMethod.POST)
    public StateInHindi storeStateInHindi(@RequestBody StateInHindi state) {
        return farmerAppLocationService.storeStateInHindi(state);
    }

    @RequestMapping(value = "/storeCityInHindi", method = RequestMethod.POST)
    public CityInHindi storeCityInHindi(@RequestBody CityInHindi city) {
        return farmerAppLocationService.storeCityInHindi(city);
    }

    @RequestMapping(value = "/storeTehsilInHindi", method = RequestMethod.POST)
    public TehsilInHindi storeTehsilInHindi(@RequestBody TehsilInHindi tehsil) {
        return farmerAppLocationService.storeTehsilInHindi(tehsil);
    }

    @RequestMapping(value = "/storeBlockInHindi", method = RequestMethod.POST)
    public BlockInHindi storeBlockInHindi(@RequestBody BlockInHindi block) {
        return farmerAppLocationService.storeBlockInHindi(block);
    }

    @RequestMapping(value = "/storeVillageInHindi", method = RequestMethod.POST)
    public VillageInHindi storeVillageInHindi(@RequestBody VillageInHindi village) {
        return farmerAppLocationService.storeVillageInHindi(village);
    }

    /**
     * @return List of States
     */
    @RequestMapping(value = "/getStateList", method = RequestMethod.GET)
    public List getStateList(@RequestParam String language) {
        return farmerAppLocationService.getStateList(language);
    }

    /**
     * @param stateId : On The Basis of stateId get cities
     * @return List of Cities under particular State
     */
    @RequestMapping(value = "/getCityListByStateId", method = RequestMethod.GET)
    public List getCitiesListByStateId(@RequestParam String stateId,@RequestParam String language) {
        return farmerAppLocationService.getCitiesByStateId(stateId,language);
    }

    /**
     * @param districtId : On The Basis of districtId get tehsils
     * @return List of Tehsil under particular Tehsil District
     */
    @RequestMapping(value = "/getTehsilListByDistrict", method = RequestMethod.GET)
    public List getTehsilListByDistrict(@RequestParam String districtId,@RequestParam String language) {
        return farmerAppLocationService.getTehsilListByDistrict(districtId,language);
    }

    /**
     * @param tehsilId : On The Basis of tehsilId get blocks
     * @return List of Block under particular Tehsil
     */
    @RequestMapping(value = "/getBlockListByTehsil", method = RequestMethod.GET)
    public List getBlockListByTehsil(@RequestParam String tehsilId,@RequestParam String language) {
        return farmerAppLocationService.getBlockListByTehsil(tehsilId,language);
    }

    /**
     * @param blockId : On The Basis of blockId get villages
     * @return List of Villages under particular Block
     */
    @RequestMapping(value = "/getVillageListByBlockId", method = RequestMethod.GET)
    public List getVillageListByBlockId(@RequestParam String blockId,@RequestParam String language) {
        return farmerAppLocationService.getVillageListByBlockId(blockId,language);
    }
}
