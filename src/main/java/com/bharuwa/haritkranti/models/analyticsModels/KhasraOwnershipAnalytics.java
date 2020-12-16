package com.bharuwa.haritkranti.models.analyticsModels;

import com.bharuwa.haritkranti.models.FieldSize;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class KhasraOwnershipAnalytics {

    private int purchasedKhasraCount;

    private int rentedKhasraCount;

    private int leasedKhasraCount;

    private int inheritedFromAncestorsKhasraCount;

    private int selfKhasraCount;

    private int totalKhasraCount;

    private BigDecimal purchasedLandSize = BigDecimal.ZERO;

    private BigDecimal rentedLandSize = BigDecimal.ZERO;

    private BigDecimal leasedLandSize = BigDecimal.ZERO;

    private BigDecimal inheritedFromAncestorsLandSize = BigDecimal.ZERO;

    private BigDecimal selfLandSize = BigDecimal.ZERO;

    private BigDecimal totalLandSize = BigDecimal.ZERO;

    private BigDecimal purchasedLandPercentage = BigDecimal.ZERO;

    private BigDecimal rentedLandPercentage = BigDecimal.ZERO;

    private BigDecimal leasedLandPercentage = BigDecimal.ZERO;

    private BigDecimal inheritedFromAncestorsLandPercentage = BigDecimal.ZERO;

    private BigDecimal selfLandPercentage = BigDecimal.ZERO;

    public int getPurchasedKhasraCount() {
        return purchasedKhasraCount;
    }

    public void setPurchasedKhasraCount(int purchasedKhasraCount) {
        this.purchasedKhasraCount = purchasedKhasraCount;
    }

    public int getLeasedKhasraCount() {
        return leasedKhasraCount;
    }

    public void setLeasedKhasraCount(int leasedKhasraCount) {
        this.leasedKhasraCount = leasedKhasraCount;
    }

    public int getInheritedFromAncestorsKhasraCount() {
        return inheritedFromAncestorsKhasraCount;
    }

    public void setInheritedFromAncestorsKhasraCount(int inheritedFromAncestorsKhasraCount) {
        this.inheritedFromAncestorsKhasraCount = inheritedFromAncestorsKhasraCount;
    }

    public int getSelfKhasraCount() {
        return selfKhasraCount;
    }

    public void setSelfKhasraCount(int selfKhasraCount) {
        this.selfKhasraCount = selfKhasraCount;
    }

    public int getTotalKhasraCount() {
        return totalKhasraCount;
    }

    public void setTotalKhasraCount(int totalKhasraCount) {
        this.totalKhasraCount = totalKhasraCount;
    }

    public BigDecimal getPurchasedLandSize() {
        return purchasedLandSize;
    }

    public void setPurchasedLandSize(BigDecimal purchasedLandSize) {
        this.purchasedLandSize = purchasedLandSize;
    }

    public BigDecimal getLeasedLandSize() {
        return leasedLandSize;
    }

    public void setLeasedLandSize(BigDecimal leasedLandSize) {
        this.leasedLandSize = leasedLandSize;
    }

    public BigDecimal getInheritedFromAncestorsLandSize() {
        return inheritedFromAncestorsLandSize;
    }

    public void setInheritedFromAncestorsLandSize(BigDecimal inheritedFromAncestorsLandSize) {
        this.inheritedFromAncestorsLandSize = inheritedFromAncestorsLandSize;
    }

    public BigDecimal getSelfLandSize() {
        return selfLandSize;
    }

    public void setSelfLandSize(BigDecimal selfLandSize) {
        this.selfLandSize = selfLandSize;
    }

    public BigDecimal getTotalLandSize() {
        return totalLandSize;
    }

    public void setTotalLandSize(BigDecimal totalLandSize) {
        this.totalLandSize = totalLandSize;
    }

    public BigDecimal getPurchasedLandPercentage() {
        return purchasedLandPercentage;
    }

    public void setPurchasedLandPercentage(BigDecimal purchasedLandPercentage) {
        this.purchasedLandPercentage = purchasedLandPercentage;
    }

    public BigDecimal getLeasedLandPercentage() {
        return leasedLandPercentage;
    }

    public void setLeasedLandPercentage(BigDecimal leasedLandPercentage) {
        this.leasedLandPercentage = leasedLandPercentage;
    }

    public BigDecimal getInheritedFromAncestorsLandPercentage() {
        return inheritedFromAncestorsLandPercentage;
    }

    public void setInheritedFromAncestorsLandPercentage(BigDecimal inheritedFromAncestorsLandPercentage) {
        this.inheritedFromAncestorsLandPercentage = inheritedFromAncestorsLandPercentage;
    }

    public BigDecimal getSelfLandPercentage() {
        return selfLandPercentage;
    }

    public void setSelfLandPercentage(BigDecimal selfLandPercentage) {
        this.selfLandPercentage = selfLandPercentage;
    }

    public int getRentedKhasraCount() {
        return rentedKhasraCount;
    }

    public void setRentedKhasraCount(int rentedKhasraCount) {
        this.rentedKhasraCount = rentedKhasraCount;
    }

    public BigDecimal getRentedLandSize() {
        return rentedLandSize;
    }

    public void setRentedLandSize(BigDecimal rentedLandSize) {
        this.rentedLandSize = rentedLandSize;
    }

    public BigDecimal getRentedLandPercentage() {
        return rentedLandPercentage;
    }

    public void setRentedLandPercentage(BigDecimal rentedLandPercentage) {
        this.rentedLandPercentage = rentedLandPercentage;
    }
}
