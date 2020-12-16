package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
@Document
public class Rate extends BaseObject {

    private BigDecimal newFarmerKhasraRate;     // New Farmer's First Khasra Rate for Soil Test
    private BigDecimal normalKhasraRate;        // Second Khasra Rate for Soil Test
    private String modifiedByUserId;
    private BigDecimal basicDetailsRate;            // Khasra Rate for soil test of same khasra (for basic details)

    private CommissionRate.LocationType locationType = CommissionRate.LocationType.STATE;
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getBasicDetailsRate() {
        return basicDetailsRate;
    }

    public void setBasicDetailsRate(BigDecimal basicDetailsRate) {
        this.basicDetailsRate = basicDetailsRate;
    }
}
