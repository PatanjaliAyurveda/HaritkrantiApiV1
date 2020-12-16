package com.bharuwa.haritkranti.models.location;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.LandType;
import com.bharuwa.haritkranti.models.Soil;
import com.bharuwa.haritkranti.models.crops.Crop;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class CityCropSoil extends BaseObject {

//    @DBRef
    private City city;

//    @DBRef
    private State state;

    private BigDecimal availableNutrientNitrogen;
    private BigDecimal availableNutrientPhosphrous;
    private BigDecimal availableNutrientPotash;

    private BigDecimal yield;
    private LandType.Type irrigationType;

    @Deprecated
    private BigDecimal high = BigDecimal.ZERO;
    @Deprecated
    private BigDecimal low = BigDecimal.ZERO;
    @Deprecated
    private BigDecimal veryLow = BigDecimal.ZERO;

    private BigDecimal indexNitrogen = BigDecimal.ZERO;
    private BigDecimal soilIndexNitrogen = BigDecimal.ZERO;

    private BigDecimal indexPhosphrous = BigDecimal.ZERO;
    private BigDecimal soilIndexPhosphrous = BigDecimal.ZERO;

    private BigDecimal indexPotash = BigDecimal.ZERO;
    private BigDecimal soilIndexPotash = BigDecimal.ZERO;

//    @DBRef
    private Soil soil;

//    @DBRef
    private Crop crop;

    /**
     * This Field is not used anywhere
     * It is for future use of Crop Variety
     */
    private String cropVariety;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BigDecimal getAvailableNutrientNitrogen() {
        return availableNutrientNitrogen;
    }

    public void setAvailableNutrientNitrogen(BigDecimal availableNutrientNitrogen) {
        this.availableNutrientNitrogen = availableNutrientNitrogen;
    }

    public BigDecimal getAvailableNutrientPhosphrous() {
        return availableNutrientPhosphrous;
    }

    public void setAvailableNutrientPhosphrous(BigDecimal availableNutrientPhosphrous) {
        this.availableNutrientPhosphrous = availableNutrientPhosphrous;
    }

    public BigDecimal getAvailableNutrientPotash() {
        return availableNutrientPotash;
    }

    public void setAvailableNutrientPotash(BigDecimal availableNutrientPotash) {
        this.availableNutrientPotash = availableNutrientPotash;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public LandType.Type getIrrigationType() {
        return irrigationType;
    }

    public void setIrrigationType(LandType.Type irrigationType) {
        this.irrigationType = irrigationType;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getVeryLow() {
        return veryLow;
    }

    public void setVeryLow(BigDecimal veryLow) {
        this.veryLow = veryLow;
    }

    public BigDecimal getIndexNitrogen() {
        return indexNitrogen;
    }

    public void setIndexNitrogen(BigDecimal indexNitrogen) {
        this.indexNitrogen = indexNitrogen;
    }

    public BigDecimal getSoilIndexNitrogen() {
        return soilIndexNitrogen;
    }

    public void setSoilIndexNitrogen(BigDecimal soilIndexNitrogen) {
        this.soilIndexNitrogen = soilIndexNitrogen;
    }

    public BigDecimal getIndexPhosphrous() {
        return indexPhosphrous;
    }

    public void setIndexPhosphrous(BigDecimal indexPhosphrous) {
        this.indexPhosphrous = indexPhosphrous;
    }

    public BigDecimal getSoilIndexPhosphrous() {
        return soilIndexPhosphrous;
    }

    public void setSoilIndexPhosphrous(BigDecimal soilIndexPhosphrous) {
        this.soilIndexPhosphrous = soilIndexPhosphrous;
    }

    public BigDecimal getIndexPotash() {
        return indexPotash;
    }

    public void setIndexPotash(BigDecimal indexPotash) {
        this.indexPotash = indexPotash;
    }

    public BigDecimal getSoilIndexPotash() {
        return soilIndexPotash;
    }

    public void setSoilIndexPotash(BigDecimal soilIndexPotash) {
        this.soilIndexPotash = soilIndexPotash;
    }

    public Soil getSoil() {
        return soil;
    }

    public void setSoil(Soil soil) {
        this.soil = soil;
    }

    public String getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(String cropVariety) {
        this.cropVariety = cropVariety;
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }
}
