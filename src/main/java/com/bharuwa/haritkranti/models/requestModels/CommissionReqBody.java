package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.payments.CommissionRate;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class CommissionReqBody {

    private CommissionRate.LocationType locationType = CommissionRate.LocationType.STATE;
    private String locationId;
    private BigDecimal rate = BigDecimal.ZERO;

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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
