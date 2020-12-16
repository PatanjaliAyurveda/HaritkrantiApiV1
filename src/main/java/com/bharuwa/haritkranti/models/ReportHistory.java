package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.requestModels.ChemicalFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.MixFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.requestModels.OrganicFertRecomendation;
import com.bharuwa.haritkranti.models.responseModels.FertilizerResponse;
import com.bharuwa.haritkranti.models.responseModels.MixReqFert;
import com.bharuwa.haritkranti.models.responseModels.OrganicReqFert;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
public class ReportHistory extends BaseObject {

    @Indexed
    private String userId;
    private NPKRecommendation npkRecommendation;

    // Chemical
    private ChemicalFertRecomendation chemicalFertRecomendation;
    private List<FertilizerResponse> fertilizerResponse = new ArrayList<>();

    // Mix
    private MixFertRecomendation mixFertRecomendation;
    private MixReqFert mixReqFert;

    // Organic
    private OrganicFertRecomendation organicFertRecomendation;
    private OrganicReqFert organicReqFert;

    private String soilReportNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NPKRecommendation getNpkRecommendation() {
        return npkRecommendation;
    }

    public void setNpkRecommendation(NPKRecommendation npkRecommendation) {
        this.npkRecommendation = npkRecommendation;
    }

    public ChemicalFertRecomendation getChemicalFertRecomendation() {
        return chemicalFertRecomendation;
    }

    public void setChemicalFertRecomendation(ChemicalFertRecomendation chemicalFertRecomendation) {
        this.chemicalFertRecomendation = chemicalFertRecomendation;
    }

    public List<FertilizerResponse> getFertilizerResponse() {
        return fertilizerResponse;
    }

    public void setFertilizerResponse(List<FertilizerResponse> fertilizerResponse) {
        this.fertilizerResponse = fertilizerResponse;
    }

    public MixFertRecomendation getMixFertRecomendation() {
        return mixFertRecomendation;
    }

    public void setMixFertRecomendation(MixFertRecomendation mixFertRecomendation) {
        this.mixFertRecomendation = mixFertRecomendation;
    }

    public MixReqFert getMixReqFert() {
        return mixReqFert;
    }

    public void setMixReqFert(MixReqFert mixReqFert) {
        this.mixReqFert = mixReqFert;
    }

    public OrganicFertRecomendation getOrganicFertRecomendation() {
        return organicFertRecomendation;
    }

    public void setOrganicFertRecomendation(OrganicFertRecomendation organicFertRecomendation) {
        this.organicFertRecomendation = organicFertRecomendation;
    }

    public OrganicReqFert getOrganicReqFert() {
        return organicReqFert;
    }

    public void setOrganicReqFert(OrganicReqFert organicReqFert) {
        this.organicReqFert = organicReqFert;
    }

    public String getSoilReportNumber() {
        return soilReportNumber;
    }

    public void setSoilReportNumber(String soilReportNumber) {
        this.soilReportNumber = soilReportNumber;
    }
}
