package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.UserCrop;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class SoilTest extends BaseObject {

    /**
     * This field is added for Payment Automation
     * So that Agent can't receive multiple payments for multiple Soil Test as he can do only one Soil Test for one season
      */
    @DBRef
    private UserCrop userCrop;

    @Indexed
    private String agentId;

    private double nValue;
    private double pValue;
    private double kValue;
    private double organicCarbon;
    private double pHValue;

    private String soilTestPaymentId;

    private boolean soilTestExist = false;

    private String parentSoilTestId;

    // location of Soil Test
    private Address address;

    public UserCrop getUserCrop() {
        return userCrop;
    }

    public void setUserCrop(UserCrop userCrop) {
        this.userCrop = userCrop;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public double getnValue() {
        return nValue;
    }

    public void setnValue(double nValue) { this.nValue = nValue; }

    public double getpValue() {
        return pValue;
    }

    public void setpValue(double pValue) { this.pValue = pValue; }

    public double getkValue() {
        return kValue;
    }

    public void setkValue(double kValue) { this.kValue = kValue; }

    public double getOrganicCarbon() {
        return organicCarbon;
    }

    public void setOrganicCarbon(double organicCarbon) {
        this.organicCarbon = organicCarbon;
    }

    public double getpHValue() {
        return pHValue;
    }

    public void setpHValue(double pHValue) {
        this.pHValue = pHValue;
    }

    public String getSoilTestPaymentId() {
        return soilTestPaymentId;
    }

    public void setSoilTestPaymentId(String soilTestPaymentId) { this.soilTestPaymentId = soilTestPaymentId; }

    public boolean isSoilTestExist() {
        return soilTestExist;
    }

    public void setSoilTestExist(boolean soilTestExist) {
        this.soilTestExist = soilTestExist;
    }

    public String getParentSoilTestId() { return parentSoilTestId; }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setParentSoilTestId(String parentSoilTestId) { this.parentSoilTestId = parentSoilTestId; }


}
