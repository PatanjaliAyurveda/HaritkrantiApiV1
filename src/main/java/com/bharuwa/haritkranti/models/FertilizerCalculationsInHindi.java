package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sunaina
 */
@Document(collection = "fertilizerCalculationsInHindi")
public class FertilizerCalculationsInHindi extends BaseObject {
    private String agroClimaticZoneName;
    private String cropName;
    private double urea;
    private double dap;
    private double mop;
    private double ssp;
    private double mixFert102626;
    private double mixFert123216;

    public String getAgroClimaticZoneName() {
        return agroClimaticZoneName;
    }

    public void setAgroClimaticZoneName(String agroClimaticZoneName) {
        this.agroClimaticZoneName = agroClimaticZoneName;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public double getUrea() {
        return urea;
    }

    public void setUrea(double urea) {
        this.urea = urea;
    }

    public double getDap() {
        return dap;
    }

    public void setDap(double dap) {
        this.dap = dap;
    }

    public double getMop() {
        return mop;
    }

    public void setMop(double mop) {
        this.mop = mop;
    }

    public double getSsp() {
        return ssp;
    }

    public void setSsp(double ssp) {
        this.ssp = ssp;
    }

    public double getMixFert102626() {
        return mixFert102626;
    }

    public void setMixFert102626(double mixFert102626) {
        this.mixFert102626 = mixFert102626;
    }

    public double getMixFert123216() {
        return mixFert123216;
    }

    public void setMixFert123216(double mixFert123216) {
        this.mixFert123216 = mixFert123216;
    }
}
