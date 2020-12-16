package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author harman
 */
public class BeekeepingDetails extends BaseObject {

    @Deprecated
    @Indexed
    private String userId;

    private FarmDetails.AnimalType animalType = FarmDetails.AnimalType.Hive;

    private BigDecimal income = BigDecimal.ZERO;

    private int quantity; // No. of Boxes

    private Fertilizer.Unit unit;

    private BigDecimal productionOutput = BigDecimal.ZERO; // honey in kg

    private Fertilizer.Unit unitProductionOutput;

    private FarmDetails.HoneyFlavour honeyFlavour = FarmDetails.HoneyFlavour.Others;

    private Date fromDate = new Date();

    private Date toDate = new Date();

    private Address address;

    private Crop.CropSeason season;

    private String batchCode;

    private boolean qrCodeGenerated;

    private String qrCodeResponseURL;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FarmDetails.AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(FarmDetails.AnimalType animalType) {
        this.animalType = animalType;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Fertilizer.Unit getUnit() {
        return unit;
    }

    public void setUnit(Fertilizer.Unit unit) {
        this.unit = unit;
    }

    public BigDecimal getProductionOutput() {
        return productionOutput;
    }

    public void setProductionOutput(BigDecimal productionOutput) {
        this.productionOutput = productionOutput;
    }

    public Fertilizer.Unit getUnitProductionOutput() {
        return unitProductionOutput;
    }

    public void setUnitProductionOutput(Fertilizer.Unit unitProductionOutput) {
        this.unitProductionOutput = unitProductionOutput;
    }

    public FarmDetails.HoneyFlavour getHoneyFlavour() {
        return honeyFlavour;
    }

    public void setHoneyFlavour(FarmDetails.HoneyFlavour honeyFlavour) {
        this.honeyFlavour = honeyFlavour;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Crop.CropSeason getSeason() {
        return season;
    }

    public void setSeason(Crop.CropSeason season) {
        this.season = season;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public boolean isQrCodeGenerated() {
        return qrCodeGenerated;
    }

    public void setQrCodeGenerated(boolean qrCodeGenerated) {
        this.qrCodeGenerated = qrCodeGenerated;
    }

    public String getQrCodeResponseURL() {
        return qrCodeResponseURL;
    }

    public void setQrCodeResponseURL(String qrCodeResponseURL) {
        this.qrCodeResponseURL = qrCodeResponseURL;
    }
}
