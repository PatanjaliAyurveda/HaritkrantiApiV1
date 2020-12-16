package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.SeedCenter;
import com.bharuwa.haritkranti.models.newmodels.FertilizerCenter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FertilizerCenterController extends BaseController{
	@RequestMapping(value = "/getFertilizerCenterList",method = RequestMethod.GET)
	public List<FertilizerCenter> getFertilizerCenterList(@RequestParam String phoneNumber){
    	return fertilizerCenterService.getFertilizerCenterList(phoneNumber);
    }
}
