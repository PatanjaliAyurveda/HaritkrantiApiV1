package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.requestModels.GeoLocation;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author harman
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "user_khasra", unique = true, def = "{'userId' : 1, 'khasraNo': 1}")
})
public class UserLandMapping extends BaseObject{

    private String userId;

    private String agentId;

    private String khasraNo;

    private String landMapImageUrl;

    private SoilTest soilTest;

    private List<GeoLocation> locationPins;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public List<GeoLocation> getLocationPins() {
        return locationPins;
    }

    public void setLocationPins(List<GeoLocation> locationPins) {
        this.locationPins = locationPins;
    }

    public SoilTest getSoilTest() {
        return soilTest;
    }

    public void setSoilTest(SoilTest soilTest) {
        this.soilTest = soilTest;
    }

    public String getLandMapImageUrl() {
        return landMapImageUrl;
    }

    public void setLandMapImageUrl(String landMapImageUrl) {
        this.landMapImageUrl = landMapImageUrl;
    }
}
