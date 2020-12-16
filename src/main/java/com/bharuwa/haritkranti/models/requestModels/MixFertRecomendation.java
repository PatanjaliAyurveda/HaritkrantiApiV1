package com.bharuwa.haritkranti.models.requestModels;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class MixFertRecomendation {

    private ChemicalFertRecomendation chemicalFertRecomendation;
    private OrganicFertRecomendation organicFertRecomendation;
    private BigDecimal organicRatio = BigDecimal.ZERO;

    private BigDecimal reqN = BigDecimal.ZERO;
    private BigDecimal reqP = BigDecimal.ZERO;
    private BigDecimal reqK = BigDecimal.ZERO;

    public ChemicalFertRecomendation getChemicalFertRecomendation() {
        return chemicalFertRecomendation;
    }

    public OrganicFertRecomendation getOrganicFertRecomendation() {
        return organicFertRecomendation;
    }

    public BigDecimal getOrganicRatio() {
        return organicRatio;
    }

    public BigDecimal getReqN() {
        return reqN;
    }

    public BigDecimal getReqP() {
        return reqP;
    }

    public BigDecimal getReqK() {
        return reqK;
    }
}
