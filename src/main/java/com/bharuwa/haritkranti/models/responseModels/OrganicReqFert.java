package com.bharuwa.haritkranti.models.responseModels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
public class OrganicReqFert {

    private List<POMFertCal> pomFertCals = new ArrayList<>();


    public List<POMFertCal> getPomFertCals() {
        return pomFertCals;
    }

    public void setPomFertCals(List<POMFertCal> pomFertCals) {
        this.pomFertCals = pomFertCals;
    }
}
