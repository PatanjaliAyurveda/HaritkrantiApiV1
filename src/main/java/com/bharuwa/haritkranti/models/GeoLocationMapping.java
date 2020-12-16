package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.requestModels.GeoLocation;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author harman
 */
@Document
public class GeoLocationMapping extends BaseObject {

    public enum MappedLocationType {
        User_Land, Village, Beekeeping_Area, Milking_Animal_Shed
    }

    private String userId;

    private String agentId;

    private String khasraNo;

    private MappedLocationType mappedLocationType;

//    ObjectIds for mapped location types (e.g. UserLandDetail's ObjectId, MilkingAnimalDetails ObjectId)
    private String typeId;

    private String geoMapImageUrl;

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

    public MappedLocationType getMappedLocationType() {
        return mappedLocationType;
    }

    public void setMappedLocationType(MappedLocationType mappedLocationType) {
        this.mappedLocationType = mappedLocationType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getGeoMapImageUrl() {
        return geoMapImageUrl;
    }

    public void setGeoMapImageUrl(String geoMapImageUrl) {
        this.geoMapImageUrl = geoMapImageUrl;
    }

    public SoilTest getSoilTest() {
        return soilTest;
    }

    public void setSoilTest(SoilTest soilTest) {
        this.soilTest = soilTest;
    }

    public List<GeoLocation> getLocationPins() {
        return locationPins;
    }

    public void setLocationPins(List<GeoLocation> locationPins) {
        this.locationPins = locationPins;
    }
}
