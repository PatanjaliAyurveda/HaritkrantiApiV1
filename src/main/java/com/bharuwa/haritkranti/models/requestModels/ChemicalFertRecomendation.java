package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.FieldSize;

/**
 * @author anuragdhunna
 */
public class ChemicalFertRecomendation {

    private double reqN;
    private double reqP;
    private double reqK;

    private String nitrogenusFertilizerId;
    private String potassicFertilizerId;
    private String phosphaticFertilizerId;
    private String complexFertilizerId;

    private FieldSize.FieldSizeType fieldSizeType;
    private double fieldSize;

    public double getReqN() {
        return reqN;
    }

    public double getReqP() {
        return reqP;
    }

    public double getReqK() {
        return reqK;
    }

    public String getNitrogenusFertilizerId() {
        return nitrogenusFertilizerId;
    }

    public String getPotassicFertilizerId() {
        return potassicFertilizerId;
    }

    public String getPhosphaticFertilizerId() {
        return phosphaticFertilizerId;
    }

    public String getComplexFertilizerId() {
        return complexFertilizerId;
    }

    public FieldSize.FieldSizeType getFieldSizeType() {
        return fieldSizeType;
    }

    public double getFieldSize() {
        return fieldSize;
    }

    public void setReqN(double reqN) {
        this.reqN = reqN;
    }

    public void setReqP(double reqP) {
        this.reqP = reqP;
    }

    public void setReqK(double reqK) {
        this.reqK = reqK;
    }
}
