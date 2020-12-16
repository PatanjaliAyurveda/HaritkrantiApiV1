package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author harman
 */
public class MilkingAnimalDetails extends BaseObject {

    public enum MilkingAnimalType {
        Cow, Buffalo, Goat
    }

    public enum AnimalBreed {
        Desi, Mix
    }

    @Deprecated
    @Indexed
    private String userId;

    private MilkingAnimalType animalType;

    private AnimalBreed animalBreed ;

    private BigDecimal income = BigDecimal.ZERO;

    private Horticulture.IncomePeriod incomePeriod = Horticulture.IncomePeriod.Quater;

    private int totalNoOfAnimals;

    private int mix;

    private int desi;

    private int milkingCondition;       // total number of milk giving animals

    private int nonMilkingCondition;    // total number of NON milk giving animals

    private int hifer;

    private BigDecimal milkProductionOutput = BigDecimal.ZERO;

    private BigDecimal homeUsage = BigDecimal.ZERO;

    private BigDecimal sale = BigDecimal.ZERO;

    private Fertilizer.Unit unitProductionOutput;

    private Date date = new Date();

    // for location search
    private Address address;

    public MilkingAnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(MilkingAnimalType animalType) {
        this.animalType = animalType;
    }

    public AnimalBreed getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(AnimalBreed animalBreed) {
        this.animalBreed = animalBreed;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public int getTotalNoOfAnimals() {
        return totalNoOfAnimals;
    }

    public void setTotalNoOfAnimals(int totalNoOfAnimals) {
        this.totalNoOfAnimals = totalNoOfAnimals;
    }

    public int getMix() {
        return mix;
    }

    public void setMix(int mix) {
        this.mix = mix;
    }

    public int getDesi() {
        return desi;
    }

    public void setDesi(int desi) {
        this.desi = desi;
    }

    public int getMilkingCondition() {
        return milkingCondition;
    }

    public void setMilkingCondition(int milkingCondition) {
        this.milkingCondition = milkingCondition;
    }

    public int getNonMilkingCondition() {
        return nonMilkingCondition;
    }

    public void setNonMilkingCondition(int nonMilkingCondition) {
        this.nonMilkingCondition = nonMilkingCondition;
    }

    public int getHifer() {
        return hifer;
    }

    public void setHifer(int hifer) {
        this.hifer = hifer;
    }

    public BigDecimal getMilkProductionOutput() {
        return milkProductionOutput;
    }

    public void setMilkProductionOutput(BigDecimal milkProductionOutput) {
        this.milkProductionOutput = milkProductionOutput;
    }

    public BigDecimal getHomeUsage() {
        return homeUsage;
    }

    public void setHomeUsage(BigDecimal homeUsage) {
        this.homeUsage = homeUsage;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

    public Fertilizer.Unit getUnitProductionOutput() {
        return unitProductionOutput;
    }

    public void setUnitProductionOutput(Fertilizer.Unit unitProductionOutput) {
        this.unitProductionOutput = unitProductionOutput;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Horticulture.IncomePeriod getIncomePeriod() {
        return incomePeriod;
    }

    public void setIncomePeriod(Horticulture.IncomePeriod incomePeriod) {
        this.incomePeriod = incomePeriod;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
