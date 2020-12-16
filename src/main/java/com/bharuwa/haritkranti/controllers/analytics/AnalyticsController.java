/*
 * package com.bharuwa.haritkranti.controllers.analytics;
 * 
 * import com.bharuwa.haritkranti.models.analyticsModels.UserLandAnalytics;
 * import com.bharuwa.haritkranti.models.analyticsModels.SoilTestAnalytics;
 * import com.bharuwa.haritkranti.service.AnalyticsService; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.lang.Nullable; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.util.List;
 * 
 *//**
	 * @author harman
	 */
/*
 * @CrossOrigin(origins = "*", maxAge = 3600)
 * 
 * @RestController
 * 
 * @RequestMapping(value = "/api") public class AnalyticsController {
 * 
 * @Autowired public AnalyticsService analyticsService;
 * 
 * @RequestMapping(value = "/storeSoilNutrientAnalyticsByLocation",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public String
 * storeSoilNutrientAnalyticsByLocation(@RequestParam String locationType,
 * 
 * @RequestParam String locationId){ return
 * analyticsService.storeSoilNutrientAnalyticsByLocation(locationType,locationId
 * ); }
 * 
 * @RequestMapping(value = "/storeSoilNutrientAnalytics",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public SoilTestAnalytics
 * storeSoilNutrientAnalytics(@RequestParam String soilTestId){ return
 * analyticsService.storeSoilNutrientAnalytics(soilTestId); }
 * 
 * @RequestMapping(value = "/storeSoilNutrientAnalyticsAll",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public String storeSoilNutrientAnalyticsAll(@RequestParam
 * String locationType){ return
 * analyticsService.storeSoilNutrientAnalyticsAll(locationType); }
 * 
 * @RequestMapping(value =
 * "/getSoilNutrientAnalyticsByNutrientTypeAndLocation",method =
 * RequestMethod.GET)
 * 
 * @ResponseBody public List<SoilTestAnalytics>
 * getSoilNutrientAnalyticsByNutrientTypeAndLocation(@RequestParam String
 * nutrientType,
 * 
 * @RequestParam String locationType,
 * 
 * @Nullable @RequestParam String locationId){ return
 * analyticsService.getSoilNutrientAnalyticsByNutrientTypeAndLocation(
 * nutrientType,locationType,locationId); }
 * 
 * 
 * @RequestMapping(value = "/storeUserLandAnalytics",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public UserLandAnalytics storeUserLandAnalytics(@RequestParam
 * String userLandDetailId){ return
 * analyticsService.storeUserLandAnalytics(userLandDetailId); }
 * 
 * @RequestMapping(value = "/storeUserLandAnalyticsByLocation",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public String storeUserLandAnalyticsByLocation(@RequestParam
 * String locationType,
 * 
 * @RequestParam String locationId){ return
 * analyticsService.storeUserLandAnalyticsByLocation(locationType,locationId); }
 * 
 * @RequestMapping(value = "/storeUserLandAnalyticsAll",method =
 * RequestMethod.POST)
 * 
 * @ResponseBody public String storeUserLandAnalyticsAll(@RequestParam String
 * locationType){ return
 * analyticsService.storeUserLandAnalyticsAll(locationType); }
 * 
 *//**
	 * get Land Type and Ownership Type Analytics By Location Type(provides All
	 * document according Location Type)
	 * 
	 * @param locationType
	 * @param locationId
	 * @return
	 *//*
		 * @RequestMapping(value = "/getUserLandAnalyticsByLocation",method =
		 * RequestMethod.GET)
		 * 
		 * @ResponseBody public List<UserLandAnalytics>
		 * getUserLandAnalyticsByLocation(@RequestParam String type,
		 * 
		 * @RequestParam String locationType,
		 * 
		 * @Nullable @RequestParam String locationId){ return
		 * analyticsService.getUserLandAnalyticsByLocation(type,locationType,locationId)
		 * ; } }
		 */