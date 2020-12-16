package com.bharuwa.haritkranti.models.crops;

import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.location.State;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public class FruitVarietyLocation {

    public FruitVarietyLocation() {
    }

    public FruitVarietyLocation(State state, SpacingUnit spacingUnit, BigDecimal columnSpace, BigDecimal rowSpace, int noOfPlantsPerAcre,
                                Fertilizer.Unit fertilizerUnit, BigDecimal fym, BigDecimal n, BigDecimal p, BigDecimal k, Set<DosagePercentage> dosagePercentages) {
        this.state = state;
        this.spacingUnit = spacingUnit;
        this.columnSpace = columnSpace;
        this.rowSpace = rowSpace;
        this.noOfPlantsPerAcre = noOfPlantsPerAcre;
        this.fertilizerUnit = fertilizerUnit;
        this.fym = fym;
        this.n = n;
        this.p = p;
        this.k = k;
        this.dosagePercentages = dosagePercentages;
    }

    public enum SpacingUnit {
        Meter
    }

    @DBRef
    private State state; // Punjab

    // Space between the plants
    private SpacingUnit spacingUnit = SpacingUnit.Meter;
    private BigDecimal columnSpace;
    private BigDecimal rowSpace;
    private int noOfPlantsPerAcre;

    // Base Fertilizer Values
    private Fertilizer.Unit fertilizerUnit = Fertilizer.Unit.KG;
    private BigDecimal fym = BigDecimal.ZERO;
    private BigDecimal n = BigDecimal.ZERO;
    private BigDecimal p = BigDecimal.ZERO;
    private BigDecimal k = BigDecimal.ZERO;

    private Set<DosagePercentage> dosagePercentages = new LinkedHashSet<>();


    public void addDosagePercentage(DosagePercentage dosagePercentage) {
        if (dosagePercentage == null) return;
        if (getDosagePercentages() == null) {
            this.dosagePercentages = new LinkedHashSet<>();
        }
        this.dosagePercentages.add(dosagePercentage);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public SpacingUnit getSpacingUnit() {
        return spacingUnit;
    }

    public void setSpacingUnit(SpacingUnit spacingUnit) {
        this.spacingUnit = spacingUnit;
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

    public int getNoOfPlantsPerAcre() {
        return noOfPlantsPerAcre;
    }

    public void setNoOfPlantsPerAcre(int noOfPlantsPerAcre) {
        this.noOfPlantsPerAcre = noOfPlantsPerAcre;
    }

    public Fertilizer.Unit getFertilizerUnit() {
        return fertilizerUnit;
    }

    public void setFertilizerUnit(Fertilizer.Unit fertilizerUnit) {
        this.fertilizerUnit = fertilizerUnit;
    }

    public BigDecimal getN() {
        return n;
    }

    public void setN(BigDecimal n) {
        this.n = n;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    public BigDecimal getK() {
        return k;
    }

    public void setK(BigDecimal k) {
        this.k = k;
    }

    public BigDecimal getFym() {
        return fym;
    }

    public void setFym(BigDecimal fym) {
        this.fym = fym;
    }

    public Set<DosagePercentage> getDosagePercentages() {
        return dosagePercentages;
    }

    public void setDosagePercentages(Set<DosagePercentage> dosagePercentages) {
        this.dosagePercentages = dosagePercentages;
    }
}
