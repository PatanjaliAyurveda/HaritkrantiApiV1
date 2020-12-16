package com.bharuwa.haritkranti.models.analyticsModels;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.FieldSize;
import com.bharuwa.haritkranti.models.payments.CommissionRate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author harman
 */
@Document
public class UserLandAnalytics extends BaseObject {

    private LandTypeAnalytics landTypeAnalytics;

    private KhasraOwnershipAnalytics khasraOwnershipAnalytics;

    private FieldSize.FieldSizeType  fieldSizeType = FieldSize.FieldSizeType.Acre;

    private CommissionRate.LocationType locationType;

    private Address address;

    public LandTypeAnalytics getLandTypeAnalytics() {
        return landTypeAnalytics;
    }

    public void setLandTypeAnalytics(LandTypeAnalytics landTypeAnalytics) {
        this.landTypeAnalytics = landTypeAnalytics;
    }

    public KhasraOwnershipAnalytics getKhasraOwnershipAnalytics() {
        return khasraOwnershipAnalytics;
    }

    public void setKhasraOwnershipAnalytics(KhasraOwnershipAnalytics khasraOwnershipAnalytics) {
        this.khasraOwnershipAnalytics = khasraOwnershipAnalytics;
    }

    public FieldSize.FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public void setFieldSizeType(FieldSize.FieldSizeType fieldSizeType) {
        this.fieldSizeType = fieldSizeType;
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
}
