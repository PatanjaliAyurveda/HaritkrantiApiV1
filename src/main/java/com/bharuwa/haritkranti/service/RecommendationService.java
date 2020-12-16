package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.UserLandDetail;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.payments.SoilTestPayment;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.requestModels.ChemicalFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.MixFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.OrganicFertRecomendation;
import com.bharuwa.haritkranti.models.responseModels.FertilizerResponse;
import com.bharuwa.haritkranti.models.responseModels.MixReqFert;
import com.bharuwa.haritkranti.models.responseModels.OrganicReqFert;
import com.bharuwa.haritkranti.models.responseModels.RequiredNPK;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public interface RecommendationService {

    RequiredNPK getNPKRecommendation(NPKRecommendation nPKRecommendation);

    Set<FertilizerResponse> getChemicalRecommendation(ChemicalFertRecomendation chemicalFertRecomendation);

    OrganicReqFert getOrganicRecommendation(OrganicFertRecomendation organicFertRecomendation);

    MixReqFert getMixRecommendation(MixFertRecomendation mixFertRecomendation);

    int calNoOfPlants(BigDecimal columnSpace, BigDecimal rowSpace, String fieldSizeType, BigDecimal area);

    SoilTest storeSoilTest(SoilTest soilTest);

    SoilTest getSoilTest(String agentId, String userCropId);
}
