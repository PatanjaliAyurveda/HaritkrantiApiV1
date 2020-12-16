package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
@Document
public class CommissionRate extends BaseObject {

    public enum LocationType {
        STATE, DISTRICT, TEHSIL, BLOCK, VILLAGE
    }

    private LocationType locationType = LocationType.STATE;
    private Address address;
    private BigDecimal rate = BigDecimal.ZERO;


    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
