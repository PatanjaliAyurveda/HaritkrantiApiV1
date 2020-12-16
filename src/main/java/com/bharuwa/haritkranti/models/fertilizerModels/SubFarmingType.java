package com.bharuwa.haritkranti.models.fertilizerModels;

import com.bharuwa.haritkranti.models.BaseObject;

/**
 * @author anuragdhunna
 */
public class SubFarmingType extends BaseObject {

    public SubFarmingType (String name, FarmingType.Type farmingType, FertilizerType fertilizerType) {
        super();
        this.name = name;
        this.farmingType = farmingType;
        this.fertilizerType = fertilizerType;
    }

    public enum FertilizerType {
        Bio, OrganicManure, OilCake, NPKchemical, Chemical, Organic, Mix,
        // Manure and GreenManure are sub-categories of OrganicManure TODO: remove ApplicationMethod
        Manure, GreenManure,
    }

    private String name;
    private FarmingType.Type farmingType = FarmingType.Type.Organic;
    private FertilizerType fertilizerType = FertilizerType.Bio;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FarmingType.Type getFarmingType() {
        return farmingType;
    }

    public void setFarmingType(FarmingType.Type farmingType) {
        this.farmingType = farmingType;
    }

    public FertilizerType getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(FertilizerType fertilizerType) {
        this.fertilizerType = fertilizerType;
    }
}
