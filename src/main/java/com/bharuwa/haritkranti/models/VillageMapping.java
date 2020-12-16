package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.requestModels.GeoLocation;

import java.util.List;

/**
 * @author harman
 */
public class VillageMapping extends BaseObject {

    private String agentId;

    private String villageId;

    private String villageMapImageUrl;

    private List<GeoLocation> locationPins;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageMapImageUrl() {
        return villageMapImageUrl;
    }

    public void setVillageMapImageUrl(String villageMapImageUrl) {
        this.villageMapImageUrl = villageMapImageUrl;
    }

    public List<GeoLocation> getLocationPins() {
        return locationPins;
    }

    public void setLocationPins(List<GeoLocation> locationPins) {
        this.locationPins = locationPins;
    }
}
