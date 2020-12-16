package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.FieldSize;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class LandTypeSize {

    private BigDecimal irrigatedLandSize = BigDecimal.ZERO;

    private BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;

    private BigDecimal rainfedLandSize = BigDecimal.ZERO;

    private BigDecimal totalLandSize = BigDecimal.ZERO;

    private FieldSize.FieldSizeType  fieldSizeType = FieldSize.FieldSizeType.Acre;

    private String stateId;

    private String districtId;

    private String stateName;

    private String districtName;

    public BigDecimal getIrrigatedLandSize() {
        return irrigatedLandSize;
    }

    public void setIrrigatedLandSize(BigDecimal irrigatedLandSize) {
        this.irrigatedLandSize = irrigatedLandSize;
    }

    public BigDecimal getSemiIrrigatedLandSize() {
        return semiIrrigatedLandSize;
    }

    public void setSemiIrrigatedLandSize(BigDecimal semiIrrigatedLandSize) {
        this.semiIrrigatedLandSize = semiIrrigatedLandSize;
    }

    public BigDecimal getRainfedLandSize() {
        return rainfedLandSize;
    }

    public void setRainfedLandSize(BigDecimal rainfedLandSize) {
        this.rainfedLandSize = rainfedLandSize;
    }

    public BigDecimal getTotalLandSize() {
        return totalLandSize;
    }

    public void setTotalLandSize(BigDecimal totalLandSize) {
        this.totalLandSize = totalLandSize;
    }

    public FieldSize.FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public void setFieldSizeType(FieldSize.FieldSizeType fieldSizeType) {
        this.fieldSizeType = fieldSizeType;
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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }
}
