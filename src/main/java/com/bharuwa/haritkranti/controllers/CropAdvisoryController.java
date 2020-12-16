package com.bharuwa.haritkranti.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.models.newmodels.CropSelection;
import com.bharuwa.haritkranti.service.CropAdvisoryService;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CropAdvisoryController extends BaseController{
	
	@Autowired
	CropAdvisoryService cropAdvisoryService;
	
	@RequestMapping(value = "/getCropList", method = RequestMethod.GET)
    public List<String> getCropGroupsByType() {
        return cropAdvisoryService.getCropList();
    }
	
    @RequestMapping(value = "/getCropVarietyList", method = RequestMethod.GET)
    public List<String> getCropGroupsByNameAndType(@RequestParam String cropId) {
        return cropAdvisoryService.getCropVarietyList(cropId);
    }
	
	@RequestMapping(value = "/getCropSelectionList", method = RequestMethod.GET)
    public List<CropSelection> getCropGroupsByNameAndType(@RequestParam String cropId,@RequestParam String varityId) {
        return cropAdvisoryService.getCropSelectionList(cropId, varityId);
    }
    
}
