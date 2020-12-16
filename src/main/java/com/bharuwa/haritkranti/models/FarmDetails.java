package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author harman
 */
@Document("farmDetails")
public class FarmDetails extends BaseObject {

    public enum AnimalType {
        Cow, Buffalo, Hen, Goat, Sheep, Fish, Crab, Hive, Pupa, Prawn, Pig
    }

    public enum HoneyFlavour {
         Mustard, Eucalyptus, Multi_flora, Litchi, Neem, Ajwain, Jamun, Beri, Others
    }

    @Deprecated
    @Indexed
    private String userId;

    private AnimalType animalType;

    private MilkingAnimalDetails.AnimalBreed animalBreed ;

    private BigDecimal income = BigDecimal.ZERO;

    private BigDecimal incomeFromHair = BigDecimal.ZERO;

    private BigDecimal incomeFromPig = BigDecimal.ZERO;

    private Horticulture.IncomePeriod incomePeriod = Horticulture.IncomePeriod.Quater;

    private int quantity;

    private int mix;

    private int desi;

    private Fertilizer.Unit unit;

    private BigDecimal productionOutput = BigDecimal.ZERO;
    private Fertilizer.Unit unitProductionOutput;

    private Date date = new Date();

    private int cow;

    private int buffalo;

    private int poultry;

    private int goat;

    private int sheep;

    private boolean fish;

    private boolean prawn;

    private boolean crab;

    private boolean beekeeping;

    private boolean sericulture;

    private BigDecimal pondSize = BigDecimal.ZERO;

    private FieldSize.FieldSizeType pondSizeType = FieldSize.FieldSizeType.Acre;

    private HoneyFlavour honeyFlavour = HoneyFlavour.Others;

    // for location search
    private  Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCow() {
        return cow;
    }

    public void setCow(int cow) {
        this.cow = cow;
    }

    public int getBuffalo() {
        return buffalo;
    }

    public void setBuffalo(int buffalo) {
        this.buffalo = buffalo;
    }

    public int getPoultry() {
        return poultry;
    }

    public void setPoultry(int poultry) {
        this.poultry = poultry;
    }

    public int getGoat() {
        return goat;
    }

    public void setGoat(int goat) {
        this.goat = goat;
    }

    public int getSheep() {
        return sheep;
    }

    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public boolean isFish() {
        return fish;
    }

    public void setFish(boolean fish) {
        this.fish = fish;
    }

    public boolean isPrawn() {
        return prawn;
    }

    public void setPrawn(boolean prawn) {
        this.prawn = prawn;
    }

    public boolean isCrab() {
        return crab;
    }

    public void setCrab(boolean crab) {
        this.crab = crab;
    }

    public Horticulture.IncomePeriod getIncomePeriod() {
        return incomePeriod;
    }

    public void setIncomePeriod(Horticulture.IncomePeriod incomePeriod) {
        this.incomePeriod = incomePeriod;
    }

    public boolean isBeekeeping() {
        return beekeeping;
    }

    public void setBeekeeping(boolean beekeeping) {
        this.beekeeping = beekeeping;
    }

    public boolean isSericulture() {
        return sericulture;
    }

    public void setSericulture(boolean sericulture) {
        this.sericulture = sericulture;
    }

    public BigDecimal getPondSize() {
        return pondSize;
    }

    public void setPondSize(BigDecimal pondSize) {
        this.pondSize = pondSize;
    }

    public FieldSize.FieldSizeType getPondSizeType() {
        return pondSizeType;
    }

    public void setPondSizeType(FieldSize.FieldSizeType pondSizeType) {
        this.pondSizeType = pondSizeType;
    }

    public MilkingAnimalDetails.AnimalBreed getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(MilkingAnimalDetails.AnimalBreed animalBreed) {
        this.animalBreed = animalBreed;
    }

    public BigDecimal getIncomeFromHair() {
        return incomeFromHair;
    }

    public void setIncomeFromHair(BigDecimal incomeFromHair) {
        this.incomeFromHair = incomeFromHair;
    }

    public BigDecimal getIncomeFromPig() {
        return incomeFromPig;
    }

    public void setIncomeFromPig(BigDecimal incomeFromPig) {
        this.incomeFromPig = incomeFromPig;
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

    public HoneyFlavour getHoneyFlavour() {
        return honeyFlavour;
    }

    public void setHoneyFlavour(HoneyFlavour honeyFlavour) {
        this.honeyFlavour = honeyFlavour;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
