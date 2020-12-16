package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.SeedCenter;
import com.bharuwa.haritkranti.models.newmodels.NewsAndFeeds;
import com.bharuwa.haritkranti.service.newservice.SeedCenterService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class SeedCenterController extends BaseController{
	@RequestMapping(value = "/getSeedCenterList",method = RequestMethod.GET)
	public List<SeedCenter> getSeedCenterList(@RequestParam String phoneNumber){
    	return seedCenterService.getSeedCenterList(phoneNumber);
    }
}
