package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author harman
 */
@Document
public class InsuranceDetails extends BaseObject{

    public enum InsuranceType {
        Crop_Insurance,
        Car_Insurance, Farm_Insurance, Contents_Insurance, Flood_Insurance, Health_Insurance, Home_Insurance,
        Personel_Item_Insurance, Medical_Indemnity_Insurance, Sports_Insurance, Business_Insurance
    }

    private String userId;

    private InsuranceType insuranceType = InsuranceType.Health_Insurance;

    private BigDecimal amount = BigDecimal.ZERO;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
