package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.newmodels.GovtSchemes;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class GovtSchemesController extends BaseController{

	@RequestMapping(value = "/getGovtSchemes",method = RequestMethod.GET)
	public List<GovtSchemes> getMandiRate(){
    	return govtSchemesService.getGovSchemes();
    }
}
