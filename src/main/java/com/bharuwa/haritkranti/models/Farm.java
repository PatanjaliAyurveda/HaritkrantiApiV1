package com.bharuwa.haritkranti.models;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class Farm extends BaseObject {

    private enum Unit {
        Acre, Hectare
    }

    private String userId;
    private BigDecimal area = BigDecimal.ZERO;
    private Unit unit = Unit.Acre;
    private String cropType;
    private BigDecimal yeildPerUnit = BigDecimal.ZERO;
    private BigDecimal incomePerUnit = BigDecimal.ZERO;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public BigDecimal getYeildPerUnit() {
        return yeildPerUnit;
    }

    public void setYeildPerUnit(BigDecimal yeildPerUnit) {
        this.yeildPerUnit = yeildPerUnit;
    }

    public BigDecimal getIncomePerUnit() {
        return incomePerUnit;
    }

    public void setIncomePerUnit(BigDecimal incomePerUnit) {
        this.incomePerUnit = incomePerUnit;
    }
}
