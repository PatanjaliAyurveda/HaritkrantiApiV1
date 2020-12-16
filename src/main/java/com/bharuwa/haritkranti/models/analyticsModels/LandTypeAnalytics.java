package com.bharuwa.haritkranti.models.analyticsModels;

import com.bharuwa.haritkranti.models.FieldSize;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class LandTypeAnalytics {

    private int irrigatedKhasraCount;

    private int semiIrrigatedKhasraCount;

    private int rainfedKhasraCount;

    private int totalKhasraCount;

    private BigDecimal irrigatedLandSize = BigDecimal.ZERO;

    private BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;

    private BigDecimal rainfedLandSize = BigDecimal.ZERO;

    private BigDecimal totalLandSize = BigDecimal.ZERO;

    private BigDecimal irrigatedLandPercentage = BigDecimal.ZERO;

    private BigDecimal semiIrrigatedLandPercentage = BigDecimal.ZERO;

    private BigDecimal rainfedLandPercentage = BigDecimal.ZERO;

    public int getIrrigatedKhasraCount() {
        return irrigatedKhasraCount;
    }

    public void setIrrigatedKhasraCount(int irrigatedKhasraCount) {
        this.irrigatedKhasraCount = irrigatedKhasraCount;
    }

    public int getSemiIrrigatedKhasraCount() {
        return semiIrrigatedKhasraCount;
    }

    public void setSemiIrrigatedKhasraCount(int semiIrrigatedKhasraCount) {
        this.semiIrrigatedKhasraCount = semiIrrigatedKhasraCount;
    }

    public int getRainfedKhasraCount() {
        return rainfedKhasraCount;
    }

    public void setRainfedKhasraCount(int rainfedKhasraCount) {
        this.rainfedKhasraCount = rainfedKhasraCount;
    }

    public int getTotalKhasraCount() {
        return totalKhasraCount;
    }

    public void setTotalKhasraCount(int totalKhasraCount) {
        this.totalKhasraCount = totalKhasraCount;
    }

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
}
