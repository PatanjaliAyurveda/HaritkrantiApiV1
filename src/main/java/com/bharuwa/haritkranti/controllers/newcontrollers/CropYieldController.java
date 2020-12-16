package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CropYieldController extends BaseController{
	
	@RequestMapping(value = "/getCropYieldList",method = RequestMethod.GET)
	public List<CropYield> getCropYieldList(@RequestParam String phoneNumber){
    	return cropYieldService.getCropYieldList();
    }
	
	@RequestMapping(value = "/getCropYield",method = RequestMethod.GET)
	public CropYield getCropYield(@RequestParam String blockName,@RequestParam String villageName,@RequestParam String khasraNumber){
    	return cropYieldService.getCropYield(blockName, villageName, khasraNumber);
    }
	
	@RequestMapping(value = "/getFarmCordinate",method = RequestMethod.GET)
	public FarmCordinate getFarmCordinate(@RequestParam String blockName,@RequestParam String villageName,@RequestParam String khasraNumber){
    	return cropYieldService.getFarmCordinate(blockName, villageName, khasraNumber);
    }
	
	@RequestMapping(value = "/getCropYieldTehsilList",method = RequestMethod.GET)
	public List<String> getCropYieldTehsilList(@RequestParam String districtName){
    	return cropYieldService.getTehsilList(districtName);
    }
	
	@RequestMapping(value = "/getCropYieldBlockList",method = RequestMethod.GET)
	public List<String> getCropYieldBlockList(@RequestParam String tehsilName){
    	return cropYieldService.getBlockList(tehsilName);
    }
	
	@RequestMapping(value = "/getCropYieldVillageList",method = RequestMethod.GET)
	public List<String> getCropYieldVillageList(@RequestParam String blockName){
    	return cropYieldService.getVillageList(blockName);
    }
	
	@RequestMapping(value = "/getCropYieldKhasraList",method = RequestMethod.GET)
	public List<String> getCropYieldKhasraList(@RequestParam String villageName){
    	return cropYieldService.getKhasraList(villageName);
    }
	
}
