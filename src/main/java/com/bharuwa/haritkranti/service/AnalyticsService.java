package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.analyticsModels.UserLandAnalytics;
import com.bharuwa.haritkranti.models.analyticsModels.SoilTestAnalytics;

import java.util.List;

/**
 * @author harman
 */
public interface AnalyticsService {

    String storeSoilNutrientAnalyticsByLocation(String locationType, String locationId);

    SoilTestAnalytics storeSoilNutrientAnalytics(String soilTestId);

    String storeSoilNutrientAnalyticsAll(String locationType);

    List<SoilTestAnalytics> getSoilNutrientAnalyticsByNutrientTypeAndLocation(String nutrientType, String locationType, String locationId);

    UserLandAnalytics storeUserLandAnalytics(String userLandDetailId);

    String storeUserLandAnalyticsByLocation(String locationType, String locationId);

    String storeUserLandAnalyticsAll(String locationType);

    List<UserLandAnalytics> getUserLandAnalyticsByLocation(String type, String locationType, String locationId);
}
