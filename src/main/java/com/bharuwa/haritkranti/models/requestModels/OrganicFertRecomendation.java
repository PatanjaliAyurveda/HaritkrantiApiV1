package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.FieldSize;
import com.bharuwa.haritkranti.models.LandType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
public class OrganicFertRecomendation {

    private BigDecimal reqN;
    private BigDecimal reqP;
    private BigDecimal reqK;

    private List<String> nitrogenusFertilizerId = new ArrayList<>();
    private List<String> potassicFertilizerId = new ArrayList<>();
    private List<String> phosphaticFertilizerId = new ArrayList<>();

    private String compostFertilizerId;
    private String oilCakeFertilizerId;

    private String gmFertilizerId;
    private String jivamritFertilizerId;

    private FieldSize.FieldSizeType fieldSizeType;
    private BigDecimal fieldSize;

    private LandType yieldType;

    private String cropId;


    public BigDecimal getReqN() {
        return reqN;
    }

    public BigDecimal getReqP() {
        return reqP;
    }

    public BigDecimal getReqK() {
        return reqK;
    }

    public List<String> getNitrogenusFertilizerId() {
        return nitrogenusFertilizerId;
    }

    public List<String> getPotassicFertilizerId() {
        return potassicFertilizerId;
    }

    public List<String> getPhosphaticFertilizerId() {
        return phosphaticFertilizerId;
    }

    public String getCompostFertilizerId() {
        return compostFertilizerId;
    }

    public String getOilCakeFertilizerId() {
        return oilCakeFertilizerId;
    }

    public FieldSize.FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public BigDecimal getFieldSize() {
        return fieldSize;
    }

    public LandType getYieldType() {
        return yieldType;
    }

    public String getCropId() {
        return cropId;
    }

    public String getGmFertilizerId() {
        return gmFertilizerId;
    }

    public String getJivamritFertilizerId() {
        return jivamritFertilizerId;
    }

    public void setReqN(BigDecimal reqN) {
        this.reqN = reqN;
    }

    public void setReqP(BigDecimal reqP) {
        this.reqP = reqP;
    }

    public void setReqK(BigDecimal reqK) {
        this.reqK = reqK;
    }
}
