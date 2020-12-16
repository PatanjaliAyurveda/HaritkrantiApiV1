package com.bharuwa.haritkranti.models.responseModels;

import java.util.HashSet;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public class MixReqFert {

    private OrganicReqFert organicReqFert;
    private Set<FertilizerResponse> fertilizerResponseList = new HashSet<>();

    public OrganicReqFert getOrganicReqFert() {
        return organicReqFert;
    }

    public void setOrganicReqFert(OrganicReqFert organicReqFert) {
        this.organicReqFert = organicReqFert;
    }

    public Set<FertilizerResponse> getFertilizerResponseList() {
        return fertilizerResponseList;
    }

    public void setFertilizerResponseList(Set<FertilizerResponse> fertilizerResponseList) {
        this.fertilizerResponseList = fertilizerResponseList;
    }
}
