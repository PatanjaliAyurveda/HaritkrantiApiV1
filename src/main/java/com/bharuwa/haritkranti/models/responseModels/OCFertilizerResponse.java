package com.bharuwa.haritkranti.models.responseModels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harman
 */
public class OCFertilizerResponse {

    private String khasraNo;

    private String landArea;

    private List<POMFertCal> pomFertCals = new ArrayList<>();


    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

    public List<POMFertCal> getPomFertCals() {
        return pomFertCals;
    }

    public void setPomFertCals(List<POMFertCal> pomFertCals) {
        this.pomFertCals = pomFertCals;
    }
}
