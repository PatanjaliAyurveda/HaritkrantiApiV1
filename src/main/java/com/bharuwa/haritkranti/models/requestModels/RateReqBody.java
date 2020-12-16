package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.payments.CommissionRate;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class RateReqBody {

    private BigDecimal newFarmerKhasraRate;     // New Farmer's First Khasra Rate for Soil Test
    private BigDecimal normalKhasraRate;        // Second Khasra Rate for Soil Test
    private String modifiedByUserId;
    private BigDecimal basicDetailsRate;            // Khasra Rate for soil test of same khasra

    private CommissionRate.LocationType locationType = CommissionRate.LocationType.STATE;
    private String locationId;

    public BigDecimal getNewFarmerKhasraRate() {
        return newFarmerKhasraRate;
    }

    public void setNewFarmerKhasraRate(BigDecimal newFarmerKhasraRate) {
        this.newFarmerKhasraRate = newFarmerKhasraRate;
    }

    public BigDecimal getNormalKhasraRate() {
        return normalKhasraRate;
    }

    public void setNormalKhasraRate(BigDecimal normalKhasraRate) {
        this.normalKhasraRate = normalKhasraRate;
    }

    public String getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(String modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public CommissionRate.LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(CommissionRate.LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getBasicDetailsRate() {
        return basicDetailsRate;
    }

    public void setBasicDetailsRate(BigDecimal basicDetailsRate) {
        this.basicDetailsRate = basicDetailsRate;
    }
}
