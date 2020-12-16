package com.bharuwa.haritkranti.models.analyticsModels;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class PHDetail {

    private SoilNutrientDetail.NutrientType nutrientType =  SoilNutrientDetail.NutrientType.pH;

    private SoilNutrientDetail.Rating rating;

    // factors for ph value
    private int acidicSoil;

    private int neutralSoil;

    private int alkalineSoil;

    private int stronglyAlkaline;

    private int totalCount;

    private BigDecimal acidicSoilPercentage = BigDecimal.ZERO;

    private BigDecimal neutralSoilPercentage = BigDecimal.ZERO;

    private BigDecimal alkalineSoilPercentage = BigDecimal.ZERO;

    private BigDecimal stronglyAlkalinePercentage = BigDecimal.ZERO;

    //  Count for highest rating
    private int ratingValue;

    public SoilNutrientDetail.NutrientType getNutrientType() {
        return nutrientType;
    }

    public void setNutrientType(SoilNutrientDetail.NutrientType nutrientType) {
        this.nutrientType = nutrientType;
    }

    public SoilNutrientDetail.Rating getRating() {
        return rating;
    }

    public void setRating(SoilNutrientDetail.Rating rating) {
        this.rating = rating;
    }

    public int getAcidicSoil() {
        return acidicSoil;
    }

    public void setAcidicSoil(int acidicSoil) {
        this.acidicSoil = acidicSoil;
    }

    public int getNeutralSoil() {
        return neutralSoil;
    }

    public void setNeutralSoil(int neutralSoil) {
        this.neutralSoil = neutralSoil;
    }

    public int getAlkalineSoil() {
        return alkalineSoil;
    }

    public void setAlkalineSoil(int alkalineSoil) {
        this.alkalineSoil = alkalineSoil;
    }

    public int getStronglyAlkaline() {
        return stronglyAlkaline;
    }

    public void setStronglyAlkaline(int stronglyAlkaline) {
        this.stronglyAlkaline = stronglyAlkaline;
    }

    public BigDecimal getAcidicSoilPercentage() {
        return acidicSoilPercentage;
    }

    public void setAcidicSoilPercentage(BigDecimal acidicSoilPercentage) {
        this.acidicSoilPercentage = acidicSoilPercentage;
    }

    public BigDecimal getNeutralSoilPercentage() {
        return neutralSoilPercentage;
    }

    public void setNeutralSoilPercentage(BigDecimal neutralSoilPercentage) {
        this.neutralSoilPercentage = neutralSoilPercentage;
    }

    public BigDecimal getAlkalineSoilPercentage() {
        return alkalineSoilPercentage;
    }

    public void setAlkalineSoilPercentage(BigDecimal alkalineSoilPercentage) {
        this.alkalineSoilPercentage = alkalineSoilPercentage;
    }

    public BigDecimal getStronglyAlkalinePercentage() {
        return stronglyAlkalinePercentage;
    }

    public void setStronglyAlkalinePercentage(BigDecimal stronglyAlkalinePercentage) {
        this.stronglyAlkalinePercentage = stronglyAlkalinePercentage;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
