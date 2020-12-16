package com.bharuwa.haritkranti.models.responseModels;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class LandTypePercentage {

    private BigDecimal irrigatedLandPercentage = BigDecimal.ZERO;

    private BigDecimal semiIrrigatedLandPercentage = BigDecimal.ZERO;

    private BigDecimal rainfedLandPercentage = BigDecimal.ZERO;

    private String stateName;

    private String districtName;

    public BigDecimal getIrrigatedLandPercentage() {
        return irrigatedLandPercentage;
    }

    public void setIrrigatedLandPercentage(BigDecimal irrigatedLandPercentage) {
        this.irrigatedLandPercentage = irrigatedLandPercentage;
    }

    public BigDecimal getSemiIrrigatedLandPercentage() {
        return semiIrrigatedLandPercentage;
    }

    public void setSemiIrrigatedLandPercentage(BigDecimal semiIrrigatedLandPercentage) {
        this.semiIrrigatedLandPercentage = semiIrrigatedLandPercentage;
    }

    public BigDecimal getRainfedLandPercentage() {
        return rainfedLandPercentage;
    }

    public void setRainfedLandPercentage(BigDecimal rainfedLandPercentage) {
        this.rainfedLandPercentage = rainfedLandPercentage;
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
