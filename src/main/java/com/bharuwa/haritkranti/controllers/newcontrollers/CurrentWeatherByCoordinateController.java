package com.bharuwa.haritkranti.controllers.newcontrollers;

import org.springframework.web.client.RestTemplate;

import com.bharuwa.haritkranti.models.newmodels.MandiRate;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;

public class CurrentWeatherByCoordinateController {
	public static void main(String args[]) {
		CurrentWeatherByCoordinateController obj=new CurrentWeatherByCoordinateController();
    	obj.addWeatherTest123();
    }
    
    public void addWeatherTest() {
    	final String uri = "https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=439d4b804bc8187953eb36d2a8c26a02";
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri,String.class);
	    System.out.println(result);
		/*
		 * for(MandiRateRecord rate:result.getRecords()) {
		 * System.out.println(rate.getState()+ " " + rate.getDistrict());
		 * mandiRateService.saveMandiRateRecords(rate); }
		 */
    }
    
    public void addWeatherTest123() {
    	System.out.println("123");
    	final String uri = "https://samples.openweathermap.org/data/2.5/weather?zip=94040,us&appid=439d4b804bc8187953eb36d2a8c26a02";
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri,String.class);
	    System.out.println(result);
		/*
		 * for(MandiRateRecord rate:result.getRecords()) {
		 * System.out.println(rate.getState()+ " " + rate.getDistrict());
		 * mandiRateService.saveMandiRateRecords(rate); }
		 */
    }
}
