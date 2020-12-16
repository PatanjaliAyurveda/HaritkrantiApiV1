package com.bharuwa.haritkranti.models.crops;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Document
public class FruitVariety extends BaseObject {

    public FruitVariety() {

    }

    public FruitVariety(String name, CropGroup cropGroup, Set<FruitVarietyLocation> fruitVarietyLocations) {
        this.name = name;
        this.cropGroup = cropGroup;
        this.fruitVarietyLocations = fruitVarietyLocations;
    }

    private String name; // Anab-e-Shahi

    @DBRef
    private CropGroup cropGroup;    // Grape

    private Set<FruitVarietyLocation> fruitVarietyLocations = new LinkedHashSet<>();   // Location

    public void addFruitVarietyLocation(FruitVarietyLocation fruitVarietyLocation) {
        if (fruitVarietyLocation == null) return;
        if (getFruitVarietyLocations() == null) this.fruitVarietyLocations = new LinkedHashSet<>();
        this.fruitVarietyLocations.add(fruitVarietyLocation);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CropGroup getCropGroup() {
        return cropGroup;
    }

    public void setCropGroup(CropGroup cropGroup) {
        this.cropGroup = cropGroup;
    }

    public Set<FruitVarietyLocation> getFruitVarietyLocations() {
        return fruitVarietyLocations;
    }

    public void setFruitVarietyLocations(Set<FruitVarietyLocation> fruitVarietyLocations) {
        this.fruitVarietyLocations = fruitVarietyLocations;
    }
}
