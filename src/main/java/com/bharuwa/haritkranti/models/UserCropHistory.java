package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author harman
 */
@Document
public class UserCropHistory extends BaseObject{

//    public enum SeasonalCrop {
//        Rabi, Kharif, Zaid
//    }

    private String userId;

    private String agentId;

    private String khasraNo;

    private Crop crop;

    private BigDecimal cropYield;

    private Fertilizer.Unit cropYieldUnit = Fertilizer.Unit.T;

    private Date yearOfSowing;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public BigDecimal getCropYield() {
        return cropYield;
    }

    public void setCropYield(BigDecimal cropYield) {
        this.cropYield = cropYield;
    }

    public Fertilizer.Unit getCropYieldUnit() {
        return cropYieldUnit;
    }

    public void setCropYieldUnit(Fertilizer.Unit cropYieldUnit) {
        this.cropYieldUnit = cropYieldUnit;
    }

    public Date getYearOfSowing() {
        return yearOfSowing;
    }

    public void setYearOfSowing(Date yearOfSowing) {
        this.yearOfSowing = yearOfSowing;
    }
}
