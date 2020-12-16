package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.crops.DosagePercentage;
import com.bharuwa.haritkranti.models.crops.FruitVariety;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author anuragdhunna
 */
@Document
public class UserCrop extends BaseObject {

    @Indexed
    private String userId;

    private String agentId;

    @Deprecated
    private String cropId;

//  @NotNull(message = "Crop name must not be null")
    @DBRef
    private Crop crop;

    @Indexed
    private String khasraNo;

    private String lastCultivateCropId;
    private BigDecimal lastCultivateCropYield;
    private Fertilizer.Unit lastCultivateCropYieldUnit = Fertilizer.Unit.T;

    private BigDecimal cropYield;
    private Fertilizer.Unit cropYieldUnit = Fertilizer.Unit.T;
    private Date yearOfSowing;
    private String userLandDetailId;
    private BigDecimal landSize = BigDecimal.ZERO;
    private FieldSize.FieldSizeType landSizeType = FieldSize.FieldSizeType.Acre;

    private BigDecimal seedExpenses;
    private BigDecimal manPowerExpenses;
    private BigDecimal fertilizerExpenses;

    private int seedQuantity;
    private int fertilizerQuantity;
    private int numberOfManPower;

    // units
    private Fertilizer.Unit seedQuantityUnit = Fertilizer.Unit.KG;
    private Fertilizer.Unit fertilizerQuantityUnit = Fertilizer.Unit.KG;
    private Fertilizer.Unit numberOfManPowerUnit = Fertilizer.Unit.NUM;

    private CropGroup.Type type = CropGroup.Type.CROP;
    
    private CropGroup  cropGroup ;

    // Fruit Fields
    @DBRef
    private FruitVariety fruitVariety;
    private BigDecimal plantAge;
    private DosagePercentage.TimeUnit timeUnit;
    private BigDecimal columnSpace;
    private BigDecimal rowSpace;
    private BigDecimal totalNoOfPlants;

    private Crop.CropSeason cropSeason = Crop.CropSeason.Kharif;

    private boolean currentCrop = false;

    private boolean readyToSell = false;

    /*
    Whether the Advertisement is created for this Crop Details in Annadata
     */
    private boolean postCreated = false;

    // for location search
    private Address address;

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

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getLastCultivateCropId() {
        return lastCultivateCropId;
    }

    public void setLastCultivateCropId(String lastCultivateCropId) {
        this.lastCultivateCropId = lastCultivateCropId;
    }

    public BigDecimal getLastCultivateCropYield() {
        return lastCultivateCropYield;
    }

    public void setLastCultivateCropYield(BigDecimal lastCultivateCropYield) {
        this.lastCultivateCropYield = lastCultivateCropYield;
    }

    public Fertilizer.Unit getLastCultivateCropYieldUnit() {
        return lastCultivateCropYieldUnit;
    }

    public void setLastCultivateCropYieldUnit(Fertilizer.Unit lastCultivateCropYieldUnit) {
        this.lastCultivateCropYieldUnit = lastCultivateCropYieldUnit;
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

    public String getUserLandDetailId() {
        return userLandDetailId;
    }

    public void setUserLandDetailId(String userLandDetailId) {
        this.userLandDetailId = userLandDetailId;
    }

    public BigDecimal getLandSize() {
        return landSize;
    }

    public void setLandSize(BigDecimal landSize) {
        this.landSize = landSize;
    }

    public FieldSize.FieldSizeType getLandSizeType() {
        return landSizeType;
    }

    public void setLandSizeType(FieldSize.FieldSizeType landSizeType) {
        this.landSizeType = landSizeType;
    }

    public BigDecimal getSeedExpenses() {
        return seedExpenses;
    }

    public void setSeedExpenses(BigDecimal seedExpenses) {
        this.seedExpenses = seedExpenses;
    }

    public BigDecimal getManPowerExpenses() {
        return manPowerExpenses;
    }

    public void setManPowerExpenses(BigDecimal manPowerExpenses) {
        this.manPowerExpenses = manPowerExpenses;
    }

    public BigDecimal getFertilizerExpenses() {
        return fertilizerExpenses;
    }

    public void setFertilizerExpenses(BigDecimal fertilizerExpenses) {
        this.fertilizerExpenses = fertilizerExpenses;
    }

    public CropGroup.Type getType() {
        return type;
    }

    public void setType(CropGroup.Type type) {
        this.type = type;
    }

    public CropGroup getCropGroup() {
		return cropGroup;
	}

	public void setCropGroup(CropGroup cropGroup) {
		this.cropGroup = cropGroup;
	}

	public FruitVariety getFruitVariety() {
        return fruitVariety;
    }

    public void setFruitVariety(FruitVariety fruitVariety) {
        this.fruitVariety = fruitVariety;
    }

    public BigDecimal getPlantAge() {
        return plantAge;
    }

    public void setPlantAge(BigDecimal plantAge) {
        this.plantAge = plantAge;
    }

    public DosagePercentage.TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(DosagePercentage.TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public BigDecimal getColumnSpace() {
        return columnSpace;
    }

    public void setColumnSpace(BigDecimal columnSpace) {
        this.columnSpace = columnSpace;
    }

    public BigDecimal getRowSpace() {
        return rowSpace;
    }

    public void setRowSpace(BigDecimal rowSpace) {
        this.rowSpace = rowSpace;
    }

    public BigDecimal getTotalNoOfPlants() {
        return totalNoOfPlants;
    }

    public void setTotalNoOfPlants(BigDecimal totalNoOfPlants) {
        this.totalNoOfPlants = totalNoOfPlants;
    }

    public Crop.CropSeason getCropSeason() {
        return cropSeason;
    }

    public void setCropSeason(Crop.CropSeason cropSeason) {
        this.cropSeason = cropSeason;
    }

    public boolean isCurrentCrop() {
        return currentCrop;
    }

    public void setCurrentCrop(boolean currentCrop) {
        this.currentCrop = currentCrop;
    }

    public boolean isReadyToSell() {
        return readyToSell;
    }

    public void setReadyToSell(boolean readyToSell) {
        this.readyToSell = readyToSell;
    }

    public boolean isPostCreated() {
        return postCreated;
    }

    public void setPostCreated(boolean postCreated) {
        this.postCreated = postCreated;
    }

    public int getSeedQuantity() {
        return seedQuantity;
    }

    public void setSeedQuantity(int seedQuantity) {
        this.seedQuantity = seedQuantity;
    }

    public int getFertilizerQuantity() {
        return fertilizerQuantity;
    }

    public void setFertilizerQuantity(int fertilizerQuantity) {
        this.fertilizerQuantity = fertilizerQuantity;
    }

    public int getNumberOfManPower() {
        return numberOfManPower;
    }

    public void setNumberOfManPower(int numberOfManPower) {
        this.numberOfManPower = numberOfManPower;
    }

    public Fertilizer.Unit getSeedQuantityUnit() {
        return seedQuantityUnit;
    }

    public void setSeedQuantityUnit(Fertilizer.Unit seedQuantityUnit) {
        this.seedQuantityUnit = seedQuantityUnit;
    }

    public Fertilizer.Unit getFertilizerQuantityUnit() {
        return fertilizerQuantityUnit;
    }

    public void setFertilizerQuantityUnit(Fertilizer.Unit fertilizerQuantityUnit) {
        this.fertilizerQuantityUnit = fertilizerQuantityUnit;
    }

    public Fertilizer.Unit getNumberOfManPowerUnit() {
        return numberOfManPowerUnit;
    }

    public void setNumberOfManPowerUnit(Fertilizer.Unit numberOfManPowerUnit) {
        this.numberOfManPowerUnit = numberOfManPowerUnit;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
