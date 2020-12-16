package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.FieldSize;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class CropArea {

    private String cropName;

    private String cropType;

    private String stateName;

    private String districtName;

    private BigDecimal cropLandSize = BigDecimal.ZERO;

    private BigDecimal totalLandSize = BigDecimal.ZERO;

    private FieldSize.FieldSizeType  fieldSizeType = FieldSize.FieldSizeType.Acre;

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

    public BigDecimal getCropLandSize() {
        return cropLandSize;
    }

    public void setCropLandSize(BigDecimal cropLandSize) {
        this.cropLandSize = cropLandSize;
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
}
