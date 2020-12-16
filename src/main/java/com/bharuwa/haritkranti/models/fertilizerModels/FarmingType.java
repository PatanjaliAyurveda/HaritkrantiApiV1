package com.bharuwa.haritkranti.models.fertilizerModels;

import com.bharuwa.haritkranti.models.BaseObject;

/**
 * @author anuragdhunna
 */
public class FarmingType extends BaseObject {

    public FarmingType(Type farmingType, String farmingTypeName) {
        super();
        this.farmingType = farmingType;
        this.farmingTypeName = farmingTypeName;
    }

    public enum Type {
        Organic, Chemical, INM_MIX
    }

    private Type farmingType = Type.Organic;

    private String farmingTypeName;

    public Type getFarmingType() {
        return farmingType;
    }

    public void setFarmingType(Type farmingType) {
        this.farmingType = farmingType;
    }

    public String getFarmingTypeName() {
        return farmingTypeName;
    }

    public void setFarmingTypeName(String farmingTypeName) {
        this.farmingTypeName = farmingTypeName;
    }
}
