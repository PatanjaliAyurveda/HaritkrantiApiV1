package com.bharuwa.haritkranti.models.responseModels;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class CropPercentage {

    private String cropName;

    private String cropType;

    private BigDecimal cropPercentage;

    private String stateName;

    private String districtName;

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public BigDecimal getCropPercentage() {
        return cropPercentage;
    }

    public void setCropPercentage(BigDecimal cropPercentage) {
        this.cropPercentage = cropPercentage;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
