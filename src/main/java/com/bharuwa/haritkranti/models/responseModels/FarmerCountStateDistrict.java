package com.bharuwa.haritkranti.models.responseModels;

/**
 * @author harman
 */
public class FarmerCountStateDistrict {

    // State/City/Tehsil/Block Name
    private String name;

    // State/City/Tehsil/Block id
    private String id;

    private long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
