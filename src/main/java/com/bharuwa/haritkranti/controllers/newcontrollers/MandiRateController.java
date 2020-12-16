package com.bharuwa.haritkranti.controllers.newcontrollers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.newmodels.MandiRate;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.newmodels.OpenCageLatLongData;
import com.bharuwa.haritkranti.models.newmodels.MandiRateBean;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class MandiRateController extends BaseController{
	
    @RequestMapping(value = "/UpdateMandiRateData",method = RequestMethod.GET)
    @ResponseBody
    public void addMandiRateData(){
    //	final String uri = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0";
    	final String uri = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001f9948c3bf52b480750c8cac5508acf54&format=json&offset=0&limit=10000";
	    RestTemplate restTemplate = new RestTemplate();
	    MandiRate result = restTemplate.getForObject(uri,MandiRate.class);
	    System.out.println(result.getDesc());
	    for(MandiRateRecord rate:result.getRecords()) {
	    	System.out.println(rate.getState()+ " " + rate.getDistrict());
	    	mandiRateService.saveMandiRateRecords(rate);
	    }
	    
    }
    
    
    
    @RequestMapping(value = "/getMandiRateDataByLatLong",method = RequestMethod.GET)
    public List<MandiRateRecord> getMandiRateDataByLatLong(@RequestParam String lat,@RequestParam String lon){
    	String uri = "https://api.opencagedata.com/geocode/v1/json?key=63e1c9eae77a4df79e8f74366402f131&q="+lat+","+lon+"&pretty=1";
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri,String.class);
	    JSONObject rootObject = new JSONObject(result);
	    JSONArray resultsArray = rootObject.getJSONArray("results");
	    JSONObject componentObject = resultsArray.getJSONObject(0);
	    String districtName = componentObject.getJSONObject("components").getString("state_district");
	//    System.out.println("District Name : "+componentObject.getJSONObject("components").getString("state_district"));
	    return mandiRateService.getMandiRateForAnnadata(districtName);
    }

    @RequestMapping(value = "/getMandiRate",method = RequestMethod.GET)
    public List<MandiRateRecord> getMandiRate(@RequestParam String phoneNumber){
    	phoneNumber = phoneNumber.substring(0,10);
    	return mandiRateService.getMandiRate(phoneNumber);
    }
    
    @RequestMapping(value = "/getMandiRateData",method = RequestMethod.GET)
    public List<MandiRateRecord> getMandiRateData(@RequestParam String phoneNumber,@RequestParam String commodity){
    	return mandiRateService.getMandiRateData(phoneNumber,commodity);
    }
    
    @RequestMapping(value = "/getCommodityList",method = RequestMethod.GET)
    public List<String> getCommodityList(@RequestParam String phoneNumber){
    	return mandiRateService.getCommodityList(phoneNumber);
    }
    
    @RequestMapping(value = "/getVarietyList",method = RequestMethod.GET)
    public List<String> getVarietyList(@RequestParam String commodity,@RequestParam String phoneNumber){
    	return mandiRateService.getVarietyList(commodity,phoneNumber);
    }
    
    @RequestMapping(value = "/getMarketList",method = RequestMethod.GET)
    public List<String> getMarketList(@RequestParam String phoneNumber){
    	return mandiRateService.getMarketList(phoneNumber);
    }
    
    @RequestMapping(value = "/getFilteredMandiRateData",method = RequestMethod.GET)
    public List<MandiRateRecord> getFilteredMandiRateData(@RequestParam String phoneNumber,@RequestParam String commodity,@RequestParam String variety,@RequestParam String arrivalDate){
    	return mandiRateService.getFilteredMandiRateData(phoneNumber,commodity,variety,arrivalDate);
    }
    
    @RequestMapping(value = "/getFilteredMarketRateData",method = RequestMethod.GET)
    public List<MandiRateRecord> getFilteredMarketRateData(@RequestParam String phoneNumber,@RequestParam String commodity,@RequestParam String variety,@RequestParam String market,@RequestParam String arrivalDate){
    	return mandiRateService.getFilteredMarketRateData(phoneNumber, commodity, variety, market, arrivalDate);
    }

}
