package com.bharuwa.haritkranti.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bharuwa.haritkranti.models.Employee;
import com.bharuwa.haritkranti.models.EmployeeVO;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.CityLatLong;
import com.bharuwa.haritkranti.models.newmodels.MandiRate;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.newmodels.WeatherAlert;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDataInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInEnglish;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastDayPartInHindi;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinHindi;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastIBMinEnglish;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class WeatherController extends BaseController{
	
	@ApiOperation(value = "Get 15 Days Weather Forecast Data in English")
    @RequestMapping(value = "/getWeatherForecastInHindi", method = RequestMethod.GET)
    public void getWeatherForecastInHindi() throws Exception {
		List<CityLatLong> cityList=new ArrayList<CityLatLong>();
		cityList.add(new CityLatLong("Mumbai","19.076090","72.877426"));
		cityList.add(new CityLatLong("Kolkata","22.5726","88.3639"));
		cityList.add(new CityLatLong("Chennai","13.0827","80.2707"));
		cityList.add(new CityLatLong("Delhi","28.7041","77.1025"));
		cityList.add(new CityLatLong("Haridwar","29.9457","78.1642"));
		cityList.add(new CityLatLong("Morena","26.4947","77.9940"));
		cityList.add(new CityLatLong("Hamirpur","31.6862","76.5213"));
		
		for(CityLatLong city:cityList) {
			
		String uri = "https://api.weather.com/v3/wx/forecast/daily/15day?geocode="+city.getLatitude()+","+city.getLongitude()+"&format=json&units=m&language=hi-IN&apiKey=69b97723778443beb97723778443be7d";
	    
		RestTemplate restTemplate = new RestTemplate();
	    WeatherForecastIBMinHindi result = restTemplate.getForObject(uri,WeatherForecastIBMinHindi.class);
	    WeatherForecastDataInHindi weatherData = new WeatherForecastDataInHindi();
	    WeatherForecastDayPartInHindi dayPart = new WeatherForecastDayPartInHindi();
	    
	    for(int i=0;i<result.getDayOfWeek().size();i++) {
	    	System.out.println("Days of week : "+result.getDayOfWeek().get(i));
	    	weatherData.setDayOfWeek(result.getDayOfWeek().get(i));
	    	weatherData.setCityName(city.getCityName());
	    	weatherData.setDate(new Date());
	    	weatherData.setExpirationTimeUtc(result.getExpirationTimeUtc().get(i));
	    	weatherData.setMoonPhase(result.getMoonPhase().get(i));
	    	weatherData.setMoonPhaseCode(result.getMoonPhaseCode().get(i));
	    	weatherData.setMoonPhaseDay(result.getMoonPhaseDay().get(i));
	    	weatherData.setMoonriseTimeLocal(result.getMoonriseTimeLocal().get(i));
	    	weatherData.setMoonriseTimeUtc(result.getMoonriseTimeUtc().get(i));
	    	weatherData.setMoonsetTimeLocal(result.getMoonsetTimeLocal().get(i));
	    	weatherData.setMoonsetTimeUtc(result.getMoonsetTimeUtc().get(i));
	    	weatherData.setNarrative(result.getNarrative().get(i));
	    	weatherData.setQpf(result.getQpf().get(i));
	    	weatherData.setQpfSnow(result.getQpfSnow().get(i));
	    	weatherData.setSunriseTimeLocal(result.getSunriseTimeLocal().get(i));
	    	weatherData.setSunriseTimeUtc(result.getSunriseTimeUtc().get(i));
	    	weatherData.setSunsetTimeLocal(result.getSunsetTimeLocal().get(i));
	    	weatherData.setSunsetTimeUtc(result.getSunsetTimeUtc().get(i));
	    	weatherData.setTemperatureMax(result.getTemperatureMax().get(i));
	    	weatherData.setTemperatureMin(result.getTemperatureMin().get(i));
	    	weatherData.setValidTimeLocal(result.getValidTimeLocal().get(i));
	    	weatherData.setValidTimeUtc(result.getValidTimeUtc().get(i));
	    	weatherForcastDataIbmInHindiService.saveWeatherDataInHindi(weatherData);
	    }
	    for(int i=0;i<15;i++) {
	    	dayPart.setCityName(city.getCityName());
	    	dayPart.setCloudCover(result.getDaypart().get(0).getCloudCover().get(i));
	    	dayPart.setDate(new Date());
	    	dayPart.setDayOrNight(result.getDaypart().get(0).getDayOrNight().get(i));
	    	dayPart.setDaypartName(result.getDaypart().get(0).getDaypartName().get(i));
	    	dayPart.setIconCode(result.getDaypart().get(0).getIconCode().get(i));
	    	dayPart.setIconCodeExtend(result.getDaypart().get(0).getIconCodeExtend().get(i));
	    	dayPart.setNarrative(result.getDaypart().get(0).getNarrative().get(i));
	    	dayPart.setPrecipChance(result.getDaypart().get(0).getPrecipChance().get(i));
	    	dayPart.setPrecipType(result.getDaypart().get(0).getPrecipType().get(i));
	    	dayPart.setQpfSnow(result.getDaypart().get(0).getQpfSnow().get(i));
	    	dayPart.setQualifierCode(result.getDaypart().get(0).getQualifierCode().get(i));
	    	dayPart.setQualifierPhrase(result.getDaypart().get(0).getQualifierPhrase().get(i));
	    	dayPart.setRelativeHumidity(result.getDaypart().get(0).getRelativeHumidity().get(i));
	    	dayPart.setSnowRange(result.getDaypart().get(0).getSnowRange().get(i));
	    	dayPart.setTemperature(result.getDaypart().get(0).getTemperature().get(i));
	    	dayPart.setTemperatureHeatIndex(result.getDaypart().get(0).getTemperatureHeatIndex().get(i));
	    	dayPart.setTemperatureWindChill(result.getDaypart().get(0).getTemperatureWindChill().get(i));
	    	dayPart.setThunderCategory(result.getDaypart().get(0).getThunderCategory().get(i));
	    	dayPart.setThunderIndex(result.getDaypart().get(0).getThunderIndex().get(i));
	    	dayPart.setUvDescription(result.getDaypart().get(0).getUvDescription().get(i));
	    	dayPart.setWindDirection(result.getDaypart().get(0).getWindDirection().get(i));
	    	dayPart.setWindDirectionCardinal(result.getDaypart().get(0).getWindDirectionCardinal().get(i));
	    	dayPart.setWindPhrase(result.getDaypart().get(0).getWindPhrase().get(i));
	    	dayPart.setWindSpeed(result.getDaypart().get(0).getWindSpeed().get(i));
	    	weatherForcastDataIbmInHindiService.saveWeatherDayPartInHindi(dayPart);
	    }
		}
	}
	
	@ApiOperation(value = "Get 15 Days Weather Forecast Data in English")
    @RequestMapping(value = "/getWeatherForecastInEnglish", method = RequestMethod.GET)
    public void getWeatherForecastInEnglish() throws Exception {
		List<CityLatLong> cityList=new ArrayList<CityLatLong>();
		cityList.add(new CityLatLong("Mumbai","19.076090","72.877426"));
		cityList.add(new CityLatLong("Kolkata","22.5726","88.3639"));
		cityList.add(new CityLatLong("Chennai","13.0827","80.2707"));
		cityList.add(new CityLatLong("Delhi","28.7041","77.1025"));
		cityList.add(new CityLatLong("Haridwar","29.9457","78.1642"));
		cityList.add(new CityLatLong("Morena","26.4947","77.9940"));
		cityList.add(new CityLatLong("Hamirpur","31.6862","76.5213"));
		for(CityLatLong city:cityList) {
			String uri = "https://api.weather.com/v3/wx/forecast/daily/15day?geocode="+city.getLatitude()+","+city.getLongitude()+"&format=json&units=m&language=en-IN&apiKey=69b97723778443beb97723778443be7d";
	    RestTemplate restTemplate = new RestTemplate();
	    WeatherForecastIBMinEnglish result = restTemplate.getForObject(uri,WeatherForecastIBMinEnglish.class);
	    WeatherForecastDataInEnglish weatherData = new WeatherForecastDataInEnglish();
	    WeatherForecastDayPartInEnglish dayPart = new WeatherForecastDayPartInEnglish();
	    
	    for(int i=0;i<result.getDayOfWeek().size();i++) {
	    	weatherData.setDayOfWeek(result.getDayOfWeek().get(i));
	    	weatherData.setCityName(city.getCityName());
	    	weatherData.setDate(new Date());
	    	weatherData.setExpirationTimeUtc(result.getExpirationTimeUtc().get(i));
	    	weatherData.setMoonPhase(result.getMoonPhase().get(i));
	    	weatherData.setMoonPhaseCode(result.getMoonPhaseCode().get(i));
	    	weatherData.setMoonPhaseDay(result.getMoonPhaseDay().get(i));
	    	weatherData.setMoonriseTimeLocal(result.getMoonriseTimeLocal().get(i));
	    	weatherData.setMoonriseTimeUtc(result.getMoonriseTimeUtc().get(i));
	    	weatherData.setMoonsetTimeLocal(result.getMoonsetTimeLocal().get(i));
	    	weatherData.setMoonsetTimeUtc(result.getMoonsetTimeUtc().get(i));
	    	weatherData.setNarrative(result.getNarrative().get(i));
	    	weatherData.setQpf(result.getQpf().get(i));
	    	weatherData.setQpfSnow(result.getQpfSnow().get(i));
	    	weatherData.setSunriseTimeLocal(result.getSunriseTimeLocal().get(i));
	    	weatherData.setSunriseTimeUtc(result.getSunriseTimeUtc().get(i));
	    	weatherData.setSunsetTimeLocal(result.getSunsetTimeLocal().get(i));
	    	weatherData.setSunsetTimeUtc(result.getSunsetTimeUtc().get(i));
	    	weatherData.setTemperatureMax(result.getTemperatureMax().get(i));
	    	weatherData.setTemperatureMin(result.getTemperatureMin().get(i));
	    	weatherData.setValidTimeLocal(result.getValidTimeLocal().get(i));
	    	weatherData.setValidTimeUtc(result.getValidTimeUtc().get(i));
	    	weatherForcastDataIbmInEnglishService.saveWeatherDataInEnglish(weatherData);
	    }
	    for(int i=0;i<15;i++) {
	    	dayPart.setCityName(city.getCityName());
	    	dayPart.setCloudCover(result.getDaypart().get(0).getCloudCover().get(i));
	    	dayPart.setDate(new Date());
	    	dayPart.setDayOrNight(result.getDaypart().get(0).getDayOrNight().get(i));
	    	dayPart.setDaypartName(result.getDaypart().get(0).getDaypartName().get(i));
	    	dayPart.setIconCode(result.getDaypart().get(0).getIconCode().get(i));
	    	dayPart.setIconCodeExtend(result.getDaypart().get(0).getIconCodeExtend().get(i));
	    	dayPart.setNarrative(result.getDaypart().get(0).getNarrative().get(i));
	    	dayPart.setPrecipChance(result.getDaypart().get(0).getPrecipChance().get(i));
	    	dayPart.setPrecipType(result.getDaypart().get(0).getPrecipType().get(i));
	    	dayPart.setQpfSnow(result.getDaypart().get(0).getQpfSnow().get(i));
	    	dayPart.setQualifierCode(result.getDaypart().get(0).getQualifierCode().get(i));
	    	dayPart.setQualifierPhrase(result.getDaypart().get(0).getQualifierPhrase().get(i));
	    	dayPart.setRelativeHumidity(result.getDaypart().get(0).getRelativeHumidity().get(i));
	    	dayPart.setSnowRange(result.getDaypart().get(0).getSnowRange().get(i));
	    	dayPart.setTemperature(result.getDaypart().get(0).getTemperature().get(i));
	    	dayPart.setTemperatureHeatIndex(result.getDaypart().get(0).getTemperatureHeatIndex().get(i));
	    	dayPart.setTemperatureWindChill(result.getDaypart().get(0).getTemperatureWindChill().get(i));
	    	dayPart.setThunderCategory(result.getDaypart().get(0).getThunderCategory().get(i));
	    	dayPart.setThunderIndex(result.getDaypart().get(0).getThunderIndex().get(i));
	    	dayPart.setUvDescription(result.getDaypart().get(0).getUvDescription().get(i));
	    	dayPart.setWindDirection(result.getDaypart().get(0).getWindDirection().get(i));
	    	dayPart.setWindDirectionCardinal(result.getDaypart().get(0).getWindDirectionCardinal().get(i));
	    	dayPart.setWindPhrase(result.getDaypart().get(0).getWindPhrase().get(i));
	    	dayPart.setWindSpeed(result.getDaypart().get(0).getWindSpeed().get(i));
	    	weatherForcastDataIbmInEnglishService.saveWeatherDayPartInEnglish(dayPart);
	    }
		}
	  //  weatherForcastDataIbmInEnglishService.saveWeatherForecastDataInEnglish(weatherData,dayPart);
    }
	
	@RequestMapping(value = "/createEmployeeTest", method = RequestMethod.POST)
    public EmployeeVO registerFarmer(@RequestBody List<Employee> userReqBody) throws Exception {
       // return loginService.createFarmer(userReqBody);
		List<Employee> empList = new ArrayList<Employee>();
		Employee emp=new Employee();
		emp.setId("5");
		emp.setName("Harish");
		empList.add(emp);
		EmployeeVO Obj = new EmployeeVO();
		Obj.setEmployees(empList);
		return Obj;
    }
	
	@RequestMapping(value = "/getWeatherAlert", method = RequestMethod.GET)
    public WeatherAlert getWeatherAlert(@RequestParam String lat,@RequestParam String lon,@RequestParam String language) throws Exception {
	//   String uri = "https://api.weather.com/v3/wx/forecast/daily/15day?geocode=29.9457,78.1642&format=json&units=m&language=hi-IN&apiKey=a377e19639294e4ab7e1963929ae4a28";
	   String uri = "https://api.weather.com/v3/wx/forecast/daily/15day?geocode="+lat+","+lon+"&format=json&units=m&language=hi-IN&apiKey=a377e19639294e4ab7e1963929ae4a28";
	   
	   RestTemplate restTemplate = new RestTemplate();
	   WeatherForecastIBMinEnglish result = restTemplate.getForObject(uri,WeatherForecastIBMinEnglish.class);
	   System.out.println("wind speed : "+result.getDaypart().get(0).getWindSpeed().get(1));
       int windSpeed = Integer.parseInt(result.getDaypart().get(0).getWindSpeed().get(1));
       WeatherAlert alert = weatherForcastDataIbmInEnglishService.getWeatherAlert(windSpeed,language);
	   return alert;
    }
}
