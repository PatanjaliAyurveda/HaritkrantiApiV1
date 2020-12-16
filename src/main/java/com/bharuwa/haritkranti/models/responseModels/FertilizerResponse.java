package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;

/**
 * @author anuragdhunna
 */
public class FertilizerResponse {

    private String fertilizerName;
    private Fertilizer.CategoryType fertylizerType;
    private double requirement;
    private Fertilizer.Unit unit = Fertilizer.Unit.KG;

    private double nRatio;
    private double pRatio;
    private double kRatio;

    public String getFertilizerName() {
        return fertilizerName;
    }

    public void setFertilizerName(String fertilizerName) {
        this.fertilizerName = fertilizerName;
    }

    public Fertilizer.CategoryType getFertylizerType() {
        return fertylizerType;
    }

    public void setFertylizerType(Fertilizer.CategoryType fertylizerType) {
        this.fertylizerType = fertylizerType;
    }

    public double getRequirement() {
        return requirement;
    }

    public void setRequirement(double requirement) {
        this.requirement = requirement;
    }

    public double getnRatio() {
        return nRatio;
    }

    public void setnRatio(double nRatio) {
        this.nRatio = nRatio;
    }

    public double getpRatio() {
        return pRatio;
    }

    public void setpRatio(double pRatio) {
        this.pRatio = pRatio;
    }

    public double getkRatio() {
        return kRatio;
    }

    public void setkRatio(double kRatio) {
        this.kRatio = kRatio;
    }

    public Fertilizer.Unit getUnit() {
        return unit;
    }

    public void setUnit(Fertilizer.Unit unit) {
        this.unit = unit;
    }
}
