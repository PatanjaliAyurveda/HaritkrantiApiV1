package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.newmodels.Alert;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AlertController extends BaseController{
	@RequestMapping(value = "/getAlertList",method = RequestMethod.GET)
	public List<Alert> getAlertList(@RequestParam String phoneNumber){
    	return alertService.getAlert(phoneNumber);
    }
}
