package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.requestModels.GeoLocation;

import java.util.List;

/**
 * @author harman
 */
public class BeekeepingAreaMapping extends BaseObject {

    private String farmerId;

    private String beekeepingId;

    private String areaImageUrl;

    private List<GeoLocation> locationPins;

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getBeekeepingId() {
        return beekeepingId;
    }

    public void setBeekeepingId(String beekeepingId) {
        this.beekeepingId = beekeepingId;
    }

    public String getAreaImageUrl() {
        return areaImageUrl;
    }

    public void setAreaImageUrl(String areaImageUrl) {
        this.areaImageUrl = areaImageUrl;
    }

    public List<GeoLocation> getLocationPins() {
        return locationPins;
    }

    public void setLocationPins(List<GeoLocation> locationPins) {
        this.locationPins = locationPins;
    }
}
