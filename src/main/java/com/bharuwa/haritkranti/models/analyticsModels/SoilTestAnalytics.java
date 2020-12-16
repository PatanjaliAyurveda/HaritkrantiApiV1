package com.bharuwa.haritkranti.models.analyticsModels;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.payments.CommissionRate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunaina
 */
public class SoilTestAnalytics extends BaseObject {

    private SoilNutrientDetail nitrogenDetail;

    private SoilNutrientDetail phosphorusDetail;

    private SoilNutrientDetail potassiumDetail;

    private SoilNutrientDetail organicCarbonDetail;

    private PHDetail pHDetail;

    private CommissionRate.LocationType locationType;

    private Address address;

    public SoilNutrientDetail getNitrogenDetail() {
        return nitrogenDetail;
    }

    public void setNitrogenDetail(SoilNutrientDetail nitrogenDetail) {
        this.nitrogenDetail = nitrogenDetail;
    }

    public SoilNutrientDetail getPhosphorusDetail() {
        return phosphorusDetail;
    }

    public void setPhosphorusDetail(SoilNutrientDetail phosphorusDetail) {
        this.phosphorusDetail = phosphorusDetail;
    }

    public SoilNutrientDetail getPotassiumDetail() {
        return potassiumDetail;
    }

    public void setPotassiumDetail(SoilNutrientDetail potassiumDetail) {
        this.potassiumDetail = potassiumDetail;
    }

    public SoilNutrientDetail getOrganicCarbonDetail() {
        return organicCarbonDetail;
    }

    public void setOrganicCarbonDetail(SoilNutrientDetail organicCarbonDetail) {
        this.organicCarbonDetail = organicCarbonDetail;
    }

    public CommissionRate.LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(CommissionRate.LocationType locationType) {
        this.locationType = locationType;
    }

    public PHDetail getpHDetail() {
        return pHDetail;
    }

    public void setpHDetail(PHDetail pHDetail) {
        this.pHDetail = pHDetail;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}