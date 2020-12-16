package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author harman
 */
@Document
public class IncomeSources extends BaseObject {

    @Indexed
    private String userId;

    private BigDecimal agriculture = BigDecimal.ZERO;

    private BigDecimal animalHusbandry = BigDecimal.ZERO;

    private BigDecimal horticulture = BigDecimal.ZERO;

    private BigDecimal externalLabour = BigDecimal.ZERO;

    private BigDecimal meatProduction = BigDecimal.ZERO;

    private BigDecimal apiCulture = BigDecimal.ZERO;

    private BigDecimal pasture = BigDecimal.ZERO;

    private BigDecimal orchard = BigDecimal.ZERO;

    private BigDecimal job = BigDecimal.ZERO;

    private BigDecimal other = BigDecimal.ZERO;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAgriculture() {
        return agriculture;
    }

    public void setAgriculture(BigDecimal agriculture) {
        this.agriculture = agriculture;
    }

    public BigDecimal getAnimalHusbandry() {
        return animalHusbandry;
    }

    public void setAnimalHusbandry(BigDecimal animalHusbandry) {
        this.animalHusbandry = animalHusbandry;
    }

    public BigDecimal getHorticulture() {
        return horticulture;
    }

    public void setHorticulture(BigDecimal horticulture) {
        this.horticulture = horticulture;
    }

    public BigDecimal getExternalLabour() {
        return externalLabour;
    }

    public void setExternalLabour(BigDecimal externalLabour) {
        this.externalLabour = externalLabour;
    }

    public BigDecimal getMeatProduction() {
        return meatProduction;
    }

    public void setMeatProduction(BigDecimal meatProduction) {
        this.meatProduction = meatProduction;
    }

    public BigDecimal getApiCulture() {
        return apiCulture;
    }

    public void setApiCulture(BigDecimal apiCulture) {
        this.apiCulture = apiCulture;
    }

    public BigDecimal getPasture() {
        return pasture;
    }

    public void setPasture(BigDecimal pasture) {
        this.pasture = pasture;
    }

    public BigDecimal getOrchard() {
        return orchard;
    }

    public void setOrchard(BigDecimal orchard) {
        this.orchard = orchard;
    }

    public BigDecimal getJob() {
        return job;
    }

    public void setJob(BigDecimal job) {
        this.job = job;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }
}
