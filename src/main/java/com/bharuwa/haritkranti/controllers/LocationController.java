package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.weather.WeatherYdnJava;
import com.bharuwa.haritkranti.models.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**


 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LocationController extends BaseController {

    @RequestMapping(value = "/storeState", method = RequestMethod.POST)
    public State storeState(@RequestBody State state) {
        return locationServices.saveState(state);
    }

    @RequestMapping(value = "/getStates", method = RequestMethod.GET)
    public List<State> getStates() {
        return locationServices.getStates();
    }


//    @RequestMapping(value = "/storeCity", method = RequestMethod.POST)
//    public City storeCity(@RequestParam String stateId,@RequestParam String cityName) {
//        return locationServices.storeCity(stateId, cityName);
//    }

    @RequestMapping(value = "/saveCity", method = RequestMethod.POST)
    public City saveCity(@RequestBody City city) {
        return locationServices.saveCity(city);
    }

    @RequestMapping(value = "/saveTehsil", method = RequestMethod.POST)
    public Tehsil saveTehsil(@RequestBody Tehsil tehsil) {
        return locationServices.saveTehsil(tehsil);
    }

    @RequestMapping(value = "/getCitiesByStateId", method = RequestMethod.GET)
    public List<City> getCitiesByStateId(@RequestParam String stateId) {
        return farmerAppLocationService.getCitiesByStateId(stateId,"Hindi");
    }
    
	/*
	 * @RequestMapping(value = "/getCityListByStateId", method = RequestMethod.GET)
	 * public List<City> getCitiesListByStateId(@RequestParam String stateId) {
	 * return farmerAppLocationService.getCitiesByStateId(stateId); }
	 */
    
    
    @RequestMapping(value = "/storeKhasraNum", method = RequestMethod.POST)
    public void storeKhasraNum(@RequestParam String userId, @RequestParam String khasraNum) {
        User user = userService.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Set<String> landKhasraNos = user.getLandKhasraNos();
        if (landKhasraNos == null) {
            landKhasraNos = new HashSet<>();
        }
        landKhasraNos.add(khasraNum);
        user.setLandKhasraNos(landKhasraNos);
        userService.saveUser(user);
    }

    @RequestMapping(value = "/storeVillage", method = RequestMethod.POST)
    public Village storeVillage(@RequestBody Village village) {
        return locationServices.saveVillage(village);
    }

    @RequestMapping(value = "/getVillage", method = RequestMethod.GET)
    public Village getVillage(@RequestParam String villageId) {
        return locationServices.getVillage(villageId);
    }

    @RequestMapping(value = "/getTehsil", method = RequestMethod.GET)
    public Tehsil getTehsil(@RequestParam String tehsilId) {
        return locationServices.getTehsil(tehsilId);
    }

    @RequestMapping(value = "/getBlock", method = RequestMethod.GET)
    public Block getBlock(@RequestParam String blockId) {
        return locationServices.getBlock(blockId);
    }

    @RequestMapping(value = "/getVillages", method = RequestMethod.GET)
    public List<Village> getVillages(@RequestParam String cityId, @RequestParam String tehsilId) {


        return locationServices.getVillages(cityId, tehsilId);
    }

//    @RequestMapping(value = "/storeTehsil", method = RequestMethod.POST)


//    public void storeTehsil(@RequestParam String userId, @RequestParam String tehsil) {





//        User user = userService.findByid(userId);
//        if (user == null) {
//            throw new ResourceNotFoundException("User not found");
//        }
//        Set<String> tehsils = user.getTehsils();




//        if (tehsils == null) {
//            tehsils = new HashSet<>();
//        }
//        tehsils.add(tehsil);
//        user.setLandKhasraNos(tehsils);
//        userService.saveUser(user);
//    }

//    @RequestMapping(value = "/getTehsilList", method = RequestMethod.GET)
//    public Set<String> getTehsilList(@RequestParam String userId) {

//        return userService.getTehsilList(userId);
//    }










    /**
     * get weather information for any location, including 10-day forecast, wind, atmosphere, astronomy conditions, and more.
     *@param latitude
     * @param longitude
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getweather", method = RequestMethod.GET)
    public String getWeather(@RequestParam String latitude, @RequestParam String longitude) throws Exception {
        return WeatherYdnJava.getWeather(latitude,longitude);
    }

    @RequestMapping(value = "/saveBlock", method = RequestMethod.POST)
    public Block saveBlock(@RequestBody Block block) {
        return locationServices.saveBlock(block);
    }

}
