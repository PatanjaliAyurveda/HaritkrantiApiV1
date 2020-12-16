package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.fertilizerModels.SubFarmingType;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class POMFertCal {

    private String fertilizerName;
    private BigDecimal n = BigDecimal.ZERO;
    private BigDecimal p = BigDecimal.ZERO;
    private BigDecimal k = BigDecimal.ZERO;
    private BigDecimal requiredFert = BigDecimal.ZERO;
    private String spray;
    private Fertilizer.Unit unit = Fertilizer.Unit.KG;

    private Fertilizer.CategoryType categoryType;
    private SubFarmingType.FertilizerType fertilizerType;

    public String getFertilizerName() {
        return fertilizerName;
    }

    public void setFertilizerName(String fertilizerName) {
        this.fertilizerName = fertilizerName;
    }

    public BigDecimal getN() {
        return n;
    }

    public void setN(BigDecimal n) {
        this.n = n;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    public BigDecimal getK() {
        return k;
    }

    public void setK(BigDecimal k) {
        this.k = k;
    }

    public Fertilizer.CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Fertilizer.CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public SubFarmingType.FertilizerType getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(SubFarmingType.FertilizerType fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

    public BigDecimal getRequiredFert() {
        return requiredFert;
    }

    public void setRequiredFert(BigDecimal requiredFert) {
        this.requiredFert = requiredFert;
    }

    public String getSpray() {
        return spray;
    }

    public void setSpray(String spray) {
        this.spray = spray;
    }

    public Fertilizer.Unit getUnit() {
        return unit;
    }

    public void setUnit(Fertilizer.Unit unit) {
        this.unit = unit;
    }
}
