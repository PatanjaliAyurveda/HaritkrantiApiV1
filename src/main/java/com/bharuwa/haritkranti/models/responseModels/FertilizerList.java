package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;

import java.util.List;

/**
 * @author anuragdhunna
 */
public class FertilizerList {

    List<Fertilizer> fertilizers;

    public List<Fertilizer> getFertilizers() {
        return fertilizers;
    }

    public void setFertilizers(List<Fertilizer> fertilizers) {
        this.fertilizers = fertilizers;
    }
}
