package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.crops.DosagePercentage;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.FieldSize;
import com.bharuwa.haritkranti.models.LandType;
import com.bharuwa.haritkranti.models.payments.SoilTest;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class NPKRecommendation extends BaseObject {

    // Location Fields
    private String stateId;
    private String cityId;
    private String tehsilId;
    private String blockId;
    private String villageId;


    private String userId;          //for agentapp purpose
    private String cropId;
    private FieldSize.FieldSizeType fieldSizeType;
    private double fieldSize;
    private String soilId;
    private LandType yieldType;
//    private int yieldTarget;
    private FarmingType.Type farmingType;
    private SoilTest soilTest;

    // Fields for Maintaining History
    private String khasraNo;
    private String village;
    private String tehsil;
    private String lastCultivateCropId;
    private BigDecimal lastCultivateCropYield;
    private Fertilizer.Unit lastCultivateCropYieldUnit = Fertilizer.Unit.T;

    // Location
    private String latitude;
    private String longitude;


    // Add Fruit Fields
    private String fruitVarietyId;
    private int plantAge;
    private DosagePercentage.TimeUnit timeUnit;
    private BigDecimal columnSpace;
    private BigDecimal rowSpace;
    private BigDecimal totalNoOfPlants;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public FieldSize.FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public void setFieldSizeType(FieldSize.FieldSizeType fieldSizeType) {
        this.fieldSizeType = fieldSizeType;
    }

    public double getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(double fieldSize) {
        this.fieldSize = fieldSize;
    }

    public String getSoilId() {
        return soilId;
    }

    public LandType getYieldType() {
        return yieldType;
    }

    public void setYieldType(LandType yieldType) {
        this.yieldType = yieldType;
    }

    public void setSoilId(String soilId) {
        this.soilId = soilId;
    }

    public FarmingType.Type getFarmingType() {
        return farmingType;
    }

    public void setFarmingType(FarmingType.Type farmingType) {
        this.farmingType = farmingType;
    }

    public SoilTest getSoilTest() {
        return soilTest;
    }

    public void setSoilTest(SoilTest soilTest) {
        this.soilTest = soilTest;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Fertilizer.Unit getLastCultivateCropYieldUnit() {
        return lastCultivateCropYieldUnit;
    }

    public void setLastCultivateCropYieldUnit(Fertilizer.Unit lastCultivateCropYieldUnit) {
        this.lastCultivateCropYieldUnit = lastCultivateCropYieldUnit;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTehsilId() {
        return tehsilId;
    }

    public void setTehsilId(String tehsilId) {
        this.tehsilId = tehsilId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getFruitVarietyId() {
        return fruitVarietyId;
    }

    public void setFruitVarietyId(String fruitVarietyId) {
        this.fruitVarietyId = fruitVarietyId;
    }

    public int getPlantAge() {
        return plantAge;
    }

    public void setPlantAge(int plantAge) {
        this.plantAge = plantAge;
    }

    public DosagePercentage.TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(DosagePercentage.TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public BigDecimal getTotalNoOfPlants() {
        return totalNoOfPlants;
    }

    public void setTotalNoOfPlants(BigDecimal totalNoOfPlants) {
        this.totalNoOfPlants = totalNoOfPlants;
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
}
