package com.bharuwa.haritkranti.models.analyticsModels;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class SoilNutrientDetail {

    public enum NutrientType {
        Nitrogen, Phosphorus, Potassium, Organic_Carbon, pH
    }

    public enum Rating {
        VL, L, M, MH, H, VH, AS, NS, ALS, SAS
    }

    private NutrientType nutrientType;

    private Rating rating;

    // factors for Nitrogen,Phosphorus,Potassium and Organic Carbon
    private int veryLow = 0;

    private int low = 0;

    private int moderate = 0;

    private int moderateHigh = 0;

    private int high = 0;

    private int veryHigh = 0;

    private BigDecimal veryLowPercentage = BigDecimal.ZERO;

    private BigDecimal lowPercentage = BigDecimal.ZERO;

    private BigDecimal moderatePercentage = BigDecimal.ZERO;

    private BigDecimal moderateHighPercentage = BigDecimal.ZERO;

    private BigDecimal highPercentage = BigDecimal.ZERO;

    private BigDecimal veryHighPercentage = BigDecimal.ZERO;

    //  Count for highest rating
    private int ratingValue;

    private int totalCount = 0;

    public NutrientType getNutrientType() {
        return nutrientType;
    }

    public void setNutrientType(NutrientType nutrientType) {
        this.nutrientType = nutrientType;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getVeryLow() {
        return veryLow;
    }

    public void setVeryLow(int veryLow) {
        this.veryLow = veryLow;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getModerate() {
        return moderate;
    }

    public void setModerate(int moderate) {
        this.moderate = moderate;
    }

    public int getModerateHigh() {
        return moderateHigh;
    }

    public void setModerateHigh(int moderateHigh) {
        this.moderateHigh = moderateHigh;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getVeryHigh() {
        return veryHigh;
    }

    public void setVeryHigh(int veryHigh) {
        this.veryHigh = veryHigh;
    }

    public BigDecimal getVeryLowPercentage() {
        return veryLowPercentage;
    }

    public void setVeryLowPercentage(BigDecimal veryLowPercentage) {
        this.veryLowPercentage = veryLowPercentage;
    }

    public BigDecimal getLowPercentage() {
        return lowPercentage;
    }

    public void setLowPercentage(BigDecimal lowPercentage) {
        this.lowPercentage = lowPercentage;
    }

    public BigDecimal getModeratePercentage() {
        return moderatePercentage;
    }

    public void setModeratePercentage(BigDecimal moderatePercentage) {
        this.moderatePercentage = moderatePercentage;
    }

    public BigDecimal getModerateHighPercentage() {
        return moderateHighPercentage;
    }

    public void setModerateHighPercentage(BigDecimal moderateHighPercentage) {
        this.moderateHighPercentage = moderateHighPercentage;
    }

    public BigDecimal getHighPercentage() {
        return highPercentage;
    }

    public void setHighPercentage(BigDecimal highPercentage) {
        this.highPercentage = highPercentage;
    }

    public BigDecimal getVeryHighPercentage() {
        return veryHighPercentage;
    }

    public void setVeryHighPercentage(BigDecimal veryHighPercentage) {
        this.veryHighPercentage = veryHighPercentage;
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
