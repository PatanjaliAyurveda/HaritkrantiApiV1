package com.bharuwa.haritkranti.models.responseModels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
public class RequiredFertilizer {


    private List<FertilizerResponse> fertilizerResponseList = new ArrayList<>();

    public List<FertilizerResponse> getFertilizerResponseList() {
        return fertilizerResponseList;
    }

    public void setFertilizerResponseList(List<FertilizerResponse> fertilizerResponseList) {
        this.fertilizerResponseList = fertilizerResponseList;
    }
}
