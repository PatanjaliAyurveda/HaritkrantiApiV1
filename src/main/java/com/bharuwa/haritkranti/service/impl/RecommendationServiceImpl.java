package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.DosagePercentage;
import com.bharuwa.haritkranti.models.crops.FruitVariety;
import com.bharuwa.haritkranti.models.crops.FruitVarietyLocation;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.payments.*;
import com.bharuwa.haritkranti.models.requestModels.ChemicalFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.MixFertRecomendation;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.requestModels.OrganicFertRecomendation;
import com.bharuwa.haritkranti.models.responseModels.*;
import com.bharuwa.haritkranti.repositories.CityRepo;
import com.bharuwa.haritkranti.repositories.StateRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;

/**
 * @author anuragdhunna
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final MongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private static final double phosphaticFertilizerPercentage = 70;
    private static final double acreValue = 2.5;

    @Autowired
    private LocationServices locationServices;

    @Autowired
    private LandService landService;

    @Autowired
    private StateRepo stateRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private FarmingService farmingService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private CropService cropService;

    @Autowired
    private PaymentCycleService paymentCycleService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private RateService rateService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private AnalyticsService analyticsService;

    public RecommendationServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /*{
        "duration": 1,
            "farmingType": "Organic",
            "fieldSize": 1,
            "fieldSizeType": "Acre",
            "fruitVarietyId": "5dd2764d5cfe2f40261e0768",
            "plantAge": 1,
            "timeUnit": "Month",

            "columnSpace": 4,
            "rowSpace": 3,

            "stateId": "5dccd082d9c4976a5c4e264c"

    }*/
    @Override
    public RequiredNPK getNPKRecommendation(NPKRecommendation nPKRecommendation) {

        if (!StringUtils.isEmpty(nPKRecommendation.getFruitVarietyId())) {
            logger.info("Recomendation for Fruit");
            return getRequiredFruitNPK(nPKRecommendation);
        }
        logger.info("Recomendation for Crop");

        // TODO: get Yield value according to land type select

        System.out.println(nPKRecommendation.getStateId());


        String cropId = nPKRecommendation.getCropId();
        Crop crop = landService.getCrop(cropId);
        String khasraNo = nPKRecommendation.getKhasraNo();
        String cityId = nPKRecommendation.getCityId();
        System.out.println("=======nPKRecommendation.getCityId======"+ cityId);
        System.out.println("=======nPKRecommendation.getCropId======"+ cropId);
        String soilId = nPKRecommendation.getSoilId();
        System.out.println("=======nPKRecommendation.getSoilId======"+ soilId);
//        =======nPKRecommendation.getCityId======5cf8cc6437136e3730b63fe6
//                =======nPKRecommendation.getCropId======5cff76961824f25b7bd14c3f
//                =======nPKRecommendation.getSoilId======5cf7b209ea97082a867ce338

//        City city = locationServices.getCity(cityId);
//        Soil soil = landService.getSoil(nPKRecommendation.getSoilId());

        // TODO: get CityCropSoil
//        CityCropSoil cityCropSoil = new CityCropSoil();

        // When Agent get Recommendation
        if (StringUtils.isEmpty(cityId)) {
            // Get Location from Kharsa Num
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(khasraNo, nPKRecommendation.getUserId());
//            cityId = userLandDetail.getCityId();
            cityId = userLandDetail.getCity().getId();

//            UserCrop userCrop = agentService.getUserCropByKhasraNo(String.valueOf(khasraNo));
            soilId = userLandDetail.getSoil().getId();
//            cropId = userCrop.getCropId();
        }

        // TODO: find CityCropSoil according to Irrigation Type
        CityCropSoil cityCropSoil = landService.getCityCropSoil(cityId, cropId, soilId, nPKRecommendation.getYieldType().getType());
        BigDecimal yield = BigDecimal.ZERO;
        System.out.println("=======cityCropSoil====new====="+cityCropSoil);

        /**
         * This if block will be removed in future
         * When all the data for CityCropSoil is uploaded in new format.
         */
        if (cityCropSoil == null) {
            cityCropSoil = landService.getCityCropSoil(cityId, cropId, soilId);

            if (cityCropSoil == null) {
                throw new CustomException("Data not found for entered information");
            }
            System.out.println("=======cityCropSoil=====old===="+cityCropSoil);
            switch (nPKRecommendation.getYieldType().getType()) {

                case Irrigated:
                    yield = cityCropSoil.getHigh();
                    break;

                case SemiIrrigated:
                    yield = cityCropSoil.getLow();
                    break;

                case Rainfed:
                    yield = cityCropSoil.getVeryLow();
                    break;
            }
        } else {
            yield = cityCropSoil.getYield();
        }

        System.out.println("yield========="+yield);
        SoilTest soilTest = nPKRecommendation.getSoilTest();

        double reqN;
        double reqP;
        double reqK;

        System.out.println("==========soilTest========"+soilTest);
        RequiredNPK requiredNPK = new RequiredNPK();

        if (soilTest != null & crop != null) {
            // Calculate the Required Fertilizer for the Land
            logger.info("has soil test");
            double nValue = soilTest.getnValue();
            double pValue = soilTest.getpValue();
            double kValue = soilTest.getkValue();

            System.out.println("======city nitrogenidex==="+ cityCropSoil.getIndexNitrogen()+"   ====yield==="+ yield);
            System.out.println("=IN*Y==="+ cityCropSoil.getIndexNitrogen().multiply(yield));
//
            System.out.println("======city.getSoilIndex()==="+ cityCropSoil.getSoilIndexNitrogen()+"   ====nValue==="+ nValue);
            System.out.println("=SI*Y==="+ cityCropSoil.getSoilIndexNitrogen().multiply(new BigDecimal(nValue)).doubleValue());

            BigDecimal indexNitroYied  = cityCropSoil.getIndexNitrogen().multiply(yield);
            double indexSoilAvail  = cityCropSoil.getSoilIndexNitrogen().multiply(new BigDecimal(nValue)).doubleValue();

//            System.out.println(indexNitroYied - indexSoilAvail);
            reqN =  indexNitroYied.subtract(new BigDecimal(indexSoilAvail)).doubleValue();

            System.out.println("===========reqN=========="+reqN);
            reqP = (cityCropSoil.getIndexPhosphrous().multiply(yield)).subtract((cityCropSoil.getSoilIndexPhosphrous().multiply(new BigDecimal(pValue)))).doubleValue();
            reqK = (cityCropSoil.getIndexPotash().multiply(yield)).subtract((cityCropSoil.getSoilIndexPotash().multiply(new BigDecimal(kValue)))).doubleValue();
            requiredNPK.setReqN(reqN);
            requiredNPK.setReqP(reqP);
            requiredNPK.setReqK(reqK);
            // TODO: Role of organic carbon and pH value
        } else if (crop != null) {
            logger.info("NO soil test");

            reqN = (cityCropSoil.getIndexNitrogen().multiply(yield)).subtract((cityCropSoil.getSoilIndexNitrogen().multiply(cityCropSoil.getAvailableNutrientNitrogen()))).doubleValue();
            reqP = (cityCropSoil.getIndexPhosphrous().multiply(yield)).subtract((cityCropSoil.getSoilIndexPhosphrous().multiply(cityCropSoil.getAvailableNutrientPhosphrous()))).doubleValue();
            reqK = (cityCropSoil.getIndexPotash().multiply(yield)).subtract((cityCropSoil.getSoilIndexPotash().multiply(cityCropSoil.getAvailableNutrientPotash()))).doubleValue();

            requiredNPK.setReqN(reqN);
            requiredNPK.setReqP(reqP);
            requiredNPK.setReqK(reqK);
        }
        return requiredNPK;
    }

    /**
     * Get Total Required Fertilizer for Fruits
     * @param nPKRecommendation request model
     * @return required fertilizer's quantity
     */
    private RequiredNPK getRequiredFruitNPK(NPKRecommendation nPKRecommendation) {
        RequiredNPK requiredNPK = new RequiredNPK();
        Query query = new Query(Criteria.where("_id").is(nPKRecommendation.getFruitVarietyId()));
        FruitVariety fruitVariety = mongoTemplate.findOne(query, FruitVariety.class);
        System.out.println("============fruitVariety======"+fruitVariety);
        if (fruitVariety != null) {

            // Get State, then check its region
            String khasraNo = nPKRecommendation.getKhasraNo();
            System.out.println("=====khasraNo======="+khasraNo);
            UserLandDetail userLandDetailByKhasraNo = agentService.getUserLandDetailByKhasraNo(khasraNo, nPKRecommendation.getUserId());
            System.out.println("=====userLandDetailByKhasraNo======="+userLandDetailByKhasraNo);
            if (userLandDetailByKhasraNo != null) {
                String stateId = userLandDetailByKhasraNo.getState().getId();

                String cropGroupId = fruitVariety.getCropGroup().getId();
                System.out.println("=====cropGroupId======="+cropGroupId);
                System.out.println("=====stateId======="+stateId);

                List<FruitVarietyLocation> fruitVarietyLocationByState = cropService.getFruitVarietyLocationByState(stateId, cropGroupId);

                System.out.println("=====fruitVarietyLocationByState.size======="+fruitVarietyLocationByState.size());

                FruitVarietyLocation fruitVarietyLocation = fruitVarietyLocationByState.size() >0 ? fruitVarietyLocationByState.get(0) : null;
                System.out.println("=====fruitVarietyLocation======="+fruitVarietyLocation);

                if (fruitVarietyLocation == null) {
                    throw new CustomException("Data Not Found for Fruit Recommendation.");
                }

                FieldSize.FieldSizeType fieldSizeType = nPKRecommendation.getFieldSizeType();
                double fieldSize = nPKRecommendation.getFieldSize();
                System.out.println("=====fieldSizeType======="+fieldSizeType);

                int plantAge = nPKRecommendation.getPlantAge();
                DosagePercentage.TimeUnit timeUnit = nPKRecommendation.getTimeUnit();
                System.out.println("=====timeUnit======="+timeUnit);
                Set<DosagePercentage> dosagePercentages = fruitVarietyLocation.getDosagePercentages();
                System.out.println("=====dosagePercentages======="+dosagePercentages.size());

                System.out.println("=====plantAge======="+plantAge);
                System.out.println("=====timeUnit======="+timeUnit);

                DosagePercentage dosagePercentage = dosagePercentages.stream().filter(dp -> plantAge == dp.getTime() && dp.getTimeUnit() == timeUnit)
                        .findAny().orElse(null);

                if (dosagePercentage == null) {
                    logger.warn("dosagePercentage not found.");
                    throw new CustomException("Data Not Found for Fruit Recommendation..");
                }

                //Farmer knows how many plants he have, acc to that recommend npk
                BigDecimal reqFym;
                BigDecimal reqN;
                BigDecimal reqP;
                BigDecimal reqK;

                System.out.println("=====dosagePercentage.getFymPercentage()======="+dosagePercentage.getFymPercentage());
                System.out.println("=====fruitVarietyLocation.getFym()======="+fruitVarietyLocation.getFym());

                System.out.println("=====dosagePercentage.getnPercentage()======="+dosagePercentage.getnPercentage());
                System.out.println("=====fruitVarietyLocation.getN()======="+fruitVarietyLocation.getN());

                System.out.println("=====dosagePercentage.getFymPercentage()======="+dosagePercentage.getpPercentage());
                System.out.println("=====fruitVarietyLocation.getP()======="+fruitVarietyLocation.getP());

                System.out.println("=====dosagePercentage.getFymPercentage()======="+dosagePercentage.getkPercentage());
                System.out.println("=====getkPercentage.getK()======="+fruitVarietyLocation.getK());

                BigDecimal columnSpace = nPKRecommendation.getColumnSpace();
                BigDecimal rowSpace = nPKRecommendation.getRowSpace();
                //TODO: Calculate according to area
                BigDecimal totalNoOfPlants = nPKRecommendation.getTotalNoOfPlants();
                System.out.println("=======totalNoOfPlants=========="+totalNoOfPlants);
                if (totalNoOfPlants != null) {
                    System.out.println("=======totalNoOfPlants=========if=======");
//                    reqFym = totalNoOfPlants.multiply()
                    reqFym = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getFymPercentage(), fruitVarietyLocation.getFym());
                    reqN = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getnPercentage(), fruitVarietyLocation.getN());
                    reqP = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getpPercentage(), fruitVarietyLocation.getP());
                    reqK = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getkPercentage(), fruitVarietyLocation.getK());

                }
//                else if (columnSpace != null && rowSpace != null) {
//                    System.out.println("=======totalNoOfPlants=========else=======");
//                    BigDecimal area = columnSpace.multiply(rowSpace);
//
//                    final BigDecimal HECTARE = new BigDecimal(10000);
//                    totalNoOfPlants = HECTARE.divide(area, RoundingMode.HALF_DOWN);
//
//                    System.out.println("totalNoOfPlants======" + totalNoOfPlants);
//
//                    reqFym = totalNoOfPlants.multiply(dosagePercentage.getFymPercentage().divide(hundred, RoundingMode.HALF_DOWN).multiply(fruitVarietyLocation.getFym()));
//                    reqN = totalNoOfPlants.multiply(dosagePercentage.getnPercentage().divide(hundred, RoundingMode.HALF_DOWN).multiply(fruitVarietyLocation.getN()));
//                    reqP = totalNoOfPlants.multiply(dosagePercentage.getpPercentage().divide(hundred, RoundingMode.HALF_DOWN).multiply(fruitVarietyLocation.getP()));
//                    reqK = totalNoOfPlants.multiply(dosagePercentage.getkPercentage().divide(hundred, RoundingMode.HALF_DOWN).multiply(fruitVarietyLocation.getK()));
//                }

                else {
                    // Calculate by Area OR get from FruitVarietyLocation
                    totalNoOfPlants = BigDecimal.valueOf(fruitVarietyLocation.getNoOfPlantsPerAcre());
                    System.out.println("totalNoOfPlants======" + totalNoOfPlants);

                    // Calculate percentage of the Fertilizer according to the Age

                    // multiply
                    reqFym = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getFymPercentage(), fruitVarietyLocation.getFym());
                    reqN = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getnPercentage(), fruitVarietyLocation.getN());
                    reqP = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getpPercentage(), fruitVarietyLocation.getP());
                    reqK = calReqFertDosageFruit(totalNoOfPlants, dosagePercentage.getkPercentage(), fruitVarietyLocation.getK());

                }

                System.out.println("======reqN======="+reqN.doubleValue());
                System.out.println("======reqP======="+reqP.doubleValue());
                System.out.println("======reqK======="+reqK.doubleValue());
                System.out.println("======reqFym======="+reqFym.doubleValue());
                requiredNPK.setReqN(reqN.doubleValue());
                requiredNPK.setReqP(reqP.doubleValue());
                requiredNPK.setReqK(reqK.doubleValue());
                requiredNPK.setReqFym(reqFym.doubleValue());
                return requiredNPK;
            }

        }
        return requiredNPK;
    }

    private BigDecimal calReqFertDosageFruit(BigDecimal totalNoOfPlants, BigDecimal fymPercentage, BigDecimal fym) {
        return totalNoOfPlants.multiply(fymPercentage.divide(hundred, RoundingMode.HALF_DOWN).multiply(fym));
    }

    @Override
    public int calNoOfPlants(BigDecimal columnSpace, BigDecimal rowSpace, String fieldSizeType, BigDecimal area) {

        BigDecimal colRow = columnSpace.multiply(rowSpace);
        BigDecimal totalNoOfPlants;
        totalNoOfPlants = new BigDecimal(10000).divide(colRow, RoundingMode.HALF_DOWN);

        if (fieldSizeType.equals(FieldSize.FieldSizeType.Acre.toString())) {
            totalNoOfPlants = totalNoOfPlants.divide(BigDecimal.valueOf(acreValue), RoundingMode.HALF_DOWN);
        }

        return totalNoOfPlants.intValue() * area.intValue();
    }

    @Override
    public Set<FertilizerResponse> getChemicalRecommendation(ChemicalFertRecomendation chemicalFertRecomendation) {

        if (chemicalFertRecomendation.getFieldSize() <= 0) {
            throw new CustomException("Area can't not be 0");
        }

        Set<FertilizerResponse> fertilizerResponseList = new HashSet<>();

        Fertilizer complexFertilizer = null;
        if (!StringUtils.isEmpty(chemicalFertRecomendation.getComplexFertilizerId())) {
            System.out.println("======================chemicalFertRecomendation.getComplexFertilizerId()=================="+chemicalFertRecomendation.getComplexFertilizerId());
            complexFertilizer = farmingService.getFertilizer(chemicalFertRecomendation.getComplexFertilizerId());
        }

        Fertilizer nitrogenusFertilizer = null;
        if (!StringUtils.isEmpty(chemicalFertRecomendation.getNitrogenusFertilizerId())) {
            nitrogenusFertilizer = farmingService.getFertilizer(chemicalFertRecomendation.getNitrogenusFertilizerId());
        }

        Fertilizer phosphaticFertilizer = null;
        if (!StringUtils.isEmpty(chemicalFertRecomendation.getPhosphaticFertilizerId())) {
            phosphaticFertilizer = farmingService.getFertilizer(chemicalFertRecomendation.getPhosphaticFertilizerId());
        }

        Fertilizer potassicFertilizer = null;
        if (!StringUtils.isEmpty(chemicalFertRecomendation.getPotassicFertilizerId())) {
            potassicFertilizer = farmingService.getFertilizer(chemicalFertRecomendation.getPotassicFertilizerId());
        }

        double nRatioComplex = 0;
        double pRatioComplex = 0;
        double kRatioComplex = 0;

        FertilizerResponse complexFR = new FertilizerResponse();
        if (complexFertilizer != null) {
            complexFR.setFertilizerName(complexFertilizer.getName());
            complexFR.setFertylizerType(complexFertilizer.getCategoryType());
            complexFR.setnRatio(complexFertilizer.getnRatio());
            complexFR.setpRatio(complexFertilizer.getpRatio());
            complexFR.setkRatio(complexFertilizer.getkRatio());
            complexFR.setUnit(complexFertilizer.getUnit());

            System.out.println("=========complexFertilizer=====KKKKK=========="+complexFertilizer.getkRatio());
            System.out.println("=========complexFertilizer=====NNNNN=========="+complexFertilizer.getnRatio());
            System.out.println("=========complexFertilizer=====PPPPP====="+complexFertilizer.getpRatio());
            kRatioComplex = complexFertilizer.getkRatio();
            nRatioComplex = complexFertilizer.getnRatio();
            pRatioComplex = complexFertilizer.getpRatio();
        }

        FertilizerResponse nitrogenousFR = new FertilizerResponse();
        if (nitrogenusFertilizer != null) {
            nitrogenousFR.setFertilizerName(nitrogenusFertilizer.getName());
            nitrogenousFR.setFertylizerType(nitrogenusFertilizer.getCategoryType());

            System.out.println("=========nitrogenusFertilizer=====NNNNN=========="+nitrogenusFertilizer.getnRatio());
            nitrogenousFR.setpRatio(nitrogenusFertilizer.getnRatio());
            nitrogenousFR.setUnit(nitrogenusFertilizer.getUnit());
        }

        FertilizerResponse potassicFR = new FertilizerResponse();
        if (potassicFertilizer != null) {

            System.out.println("=========potassicFertilizer=====KKK=========="+potassicFertilizer.getkRatio());

            potassicFR.setFertilizerName(potassicFertilizer.getName());
            potassicFR.setFertylizerType(potassicFertilizer.getCategoryType());
            potassicFR.setkRatio(potassicFertilizer.getkRatio());
            potassicFR.setUnit(potassicFertilizer.getUnit());
        }

        double reqN = chemicalFertRecomendation.getReqN();
        double reqP = chemicalFertRecomendation.getReqP();
        double reqK = chemicalFertRecomendation.getReqK();

        System.out.println("============reqN=========="+reqN);
        System.out.println("============reqP=========="+reqP);
        System.out.println("============reqK=========="+reqK);

        double min = min(reqN, reqP, reqK);
        System.out.println("============min=========="+min);

        FertilizerResponse phosphaticFR = new FertilizerResponse();


        // TODO: if only P is positive then user have to select P Fert
        // TODO: N P K all are negative then don't show any Fertilizers

        double seventyPercentOfP = (phosphaticFertilizerPercentage * reqP) / 100;

        if (phosphaticFertilizer != null) {
            System.out.println("======phosphaticFertilizer != null=======");

            System.out.println("=========phosphaticFertilizer=====PPPP=========="+phosphaticFertilizer.getpRatio());

            phosphaticFR.setFertilizerName(phosphaticFertilizer.getName());
            phosphaticFR.setFertylizerType(phosphaticFertilizer.getCategoryType());
            phosphaticFR.setpRatio(phosphaticFertilizer.getpRatio());
            phosphaticFR.setUnit(phosphaticFertilizer.getUnit());

            // if reqN <= 0 ignore N Fert and Complex
            if (reqN <= 0 && reqP > 0 && reqK > 0) {
                System.out.println("======reqN <= 0 && reqP > 0 && reqK > 0=======");
                // Calculate on P and K
                phosphaticFR.setRequirement(calculatePhosphoricFert(reqP, phosphaticFertilizer));
                if (potassicFertilizer != null) {
                    potassicFR.setRequirement(calculatePotassicFert(reqK, potassicFertilizer));
                }
            }

            // if reqK <= 0 ignore K Fert but show Complex who have kRatioComplex 0
            if (reqK <= 0 && reqN > 0 && reqP > 0) {
                System.out.println("=======reqK <= 0 && reqN > 0 && reqP > 0======");
                // Calculate on N and P

                // If Complex fert is select (DAP) where kratioComplex then first calculate according to Complex Fert

                if (complexFertilizer != null) {
                    min = Math.min(reqN, reqP);

                    if (min == reqN) {
                        System.out.println("N is min====111====kRatioComplex="+kRatioComplex);
                        reqNLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
                    }

                    if (min == reqP) {
                        System.out.println("P is min====111====kRatioComplex="+kRatioComplex);
                        reqPLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
                    }
                }
//                else {
//                    if (nitrogenusFertilizer != null) {
//                        nitrogenousFR.setRequirement(calculateNitrogenousFert(reqN, nitrogenusFertilizer));
//                    }
//                    phosphaticFR.setRequirement(calculatePhosphoricFert(reqP, phosphaticFertilizer));
//                }

            }

            if (reqN <= 0 && reqK <= 0 ) {
                System.out.println("=====reqN <= 0 && reqK <= 0========");
                // Calculate only P
                phosphaticFR.setRequirement(calculatePhosphoricFert(reqP, phosphaticFertilizer));
            }
        }

        if (!StringUtils.isEmpty(phosphaticFR.getFertilizerName())) {
            fertilizerResponseList.add(phosphaticFR);
        }
        // if reqP <= 0 ignore P Fert and Complex
        if (reqP <= 0 && reqN > 0 && reqK > 0) {
            System.out.println("======reqP <= 0 && reqN > 0 && reqK > 0=======");
            // Calculate on N and K
            if (nitrogenusFertilizer != null) {
                nitrogenousFR.setRequirement(calculateNitrogenousFert(reqN, nitrogenusFertilizer));
            }
            if (potassicFertilizer != null) {
                potassicFR.setRequirement(calculatePotassicFert(reqK, potassicFertilizer));
            }
        }

        if (reqP <= 0 && reqK <= 0 ) {
            System.out.println("=======reqP <= 0 && reqK <= 0======");
            // Calculate only N
            if (nitrogenusFertilizer != null) {
                nitrogenousFR.setRequirement(calculateNitrogenousFert(reqN, nitrogenusFertilizer));
            }
        }

        System.out.println("====111==22==nitrogenousFR.getRequirement()========="+nitrogenousFR.getRequirement());

        if (reqN <= 0 && reqP <= 0 ) {
            System.out.println("=======reqN <= 0 && reqP <= 0======");
            // Calculate only K
            if (potassicFertilizer != null) {
                potassicFR.setRequirement(calculatePotassicFert(reqK, potassicFertilizer));
            }
        }

        System.out.println("====111====nitrogenousFR.getRequirement()========="+nitrogenousFR.getRequirement());

        System.out.println("====111====nRatioComplex========="+nRatioComplex);
        System.out.println("====111====pRatioComplex========="+pRatioComplex);
        System.out.println("====111====kRatioComplex========="+kRatioComplex);

        if (phosphaticFertilizer == null && reqP > 0 && nRatioComplex >0) {
            System.out.println("======phosphaticFertilizer == null && reqP > 0=======");
            // Take all the required Phoprous from the Complex fertilizer
            // Problem value for N OR K might exceed

            double reqFertP = (reqP / pRatioComplex) * 100;
            // Calculate All the Fertilzers in
            complexFR.setRequirement(reqFertP);
            System.out.println("====else========1111===reqFertP=="+reqFertP);

            double nOfNPK = reqN - (nRatioComplex / 100) * reqFertP;
            double pOfNPK = reqP - (pRatioComplex / 100) * reqFertP;
            double kOfNPK = reqK - (kRatioComplex / 100) * reqFertP;

            System.out.println("=====else=======nOfNPK==========" + nOfNPK);
            System.out.println("======else======pOfNPK==========" + pOfNPK);
            System.out.println("======else======kOfNPK==========" + kOfNPK);

            // Calculate the other fertilizer composition ration for N and K as the value of P is completed by the Complex Fertilizer
            // So there is no need to calculate for the Potassic Fertilizer

            // TODO: Calculate the Exceed value


            double exceedK = kOfNPK;
            double exceedN = nOfNPK;

            if (exceedK > 0 && exceedN > 0) {
                // In this case the Requirement for Least NPK Value i.e. K(Postasic) is fulfilled by NPK(Complex) Fertilizer
                // Now calculate value for the Fertilizer for Nitrogenous, Phosphatic

                double nitrogenousFert = calculateNitrogenousFert(exceedN, nitrogenusFertilizer);
                double potassicFert = calculatePotassicFert(exceedK, potassicFertilizer);

                System.out.println("============nitrogenousFert=========="+nitrogenousFert);
                System.out.println("============potassicFert=========="+potassicFert);

                nitrogenousFR.setRequirement(nitrogenousFert);
                potassicFR.setRequirement(potassicFert);
                complexFR.setRequirement(reqFertP);

            } else {
                System.out.println("============&&&&&&&&&&&&&&&&&&&&&&&&&&--------------ELSE------------------------%%%%%%%%%%%%==========");

                // ExceedK is greater(comparing negative values)
                if (exceedK > 0 && exceedK < exceedN || exceedK == exceedN) {
                    System.out.println("=====if  exceed K==is more=");
                    System.out.println("=============kRatioComplex========---=="+kRatioComplex);
                    double reqFertK = (reqK / kRatioComplex) * 100;
                    // Calculate All the Fertilzers in

                    double nOfReqFert = (nRatioComplex / 100) * reqFertK;
                    double pOfReqFert = (pRatioComplex / 100) * reqFertK;
                    double kOfReqFert = (kRatioComplex / 100) * reqFertK;
                    System.out.println("============nOfReqFert==========" + nOfReqFert);
                    System.out.println("============pOfReqFert==========" + pOfReqFert);
                    System.out.println("============kOfReqFert==========" + kOfReqFert);

                    nOfNPK = reqN - nOfReqFert;
                    pOfNPK = reqP - pOfReqFert;
                    kOfNPK = reqK - kOfReqFert;

                    System.out.println("============nOfNPK==========" + nOfNPK);
                    System.out.println("============pOfNPK==========" + pOfNPK);
                    System.out.println("============kOfNPK==========" + kOfNPK);

                    complexFR.setRequirement(reqFertK);
                    System.out.println("====if========1111=====");

                    // Check if pOfNPK is negative
                    if (nOfNPK < 0) {
                        System.out.println("============if======nOfNPK < 0===");
                        double reqFertN = (reqN / nRatioComplex) * 100;

                        System.out.println("===pOfNPK=========nOfReqFert==========" + nOfReqFert);
                        System.out.println("=====pOfNPK=======pOfReqFert==========" + pOfReqFert);
                        System.out.println("=====pOfNPK=======kOfReqFert==========" + kOfReqFert);

                        nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                        pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                        kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                        System.out.println("=====pOfNPK=======nOfNPK==========" + nOfNPK);
                        System.out.println("====pOfNPK========pOfNPK==========" + pOfNPK);
                        System.out.println("=====pOfNPK=======kOfNPK==========" + kOfNPK);

                        complexFR.setRequirement(reqFertN);
                        // Calculate the value for P and K
//                        phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                        potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                    } else {
//                        phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                        nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                    }
                } else {
                    System.out.println("=====else  exceed N== is more=");
                    double reqFertN = (reqN / nRatioComplex) * 100;
                    // Calculate All the Fertilzers in

                    System.out.println("====else========1111=====");
                    nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                    pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                    kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                    System.out.println("=====else=======nOfNPK==========" + nOfNPK);
                    System.out.println("======else======pOfNPK==========" + pOfNPK);
                    System.out.println("======else======kOfNPK==========" + kOfNPK);


                    // Calculate the other fertilizer composition ration for N and K as the value of P is completed by the Complex Fertilizer
                    // So there is no need to calculate for the Potassic Fertilizer

                    // Required Value for Nitrogenous and Phosphatic Fertilizer
//                    nOfNitrogenous = (nOfNPK/nRatioNitro) *100;
//                    kOfPhostphatic = (kOfNPK/kRatioPotassic) *100;

//                    potassicFR.setRequirement(pOfPotassic);
//                    phosphaticFR.setRequirement(kOfPhostphatic);

                    //TODO: Check negative between p and k greater

                    // Check if pOfNPK is negative
                    if (kOfNPK < 0) {
                        System.out.println("============if======nOfNPK < 0===");
                        double reqFertK = (reqP / pRatioComplex) * 100;

                        nOfNPK = reqN - (nRatioComplex / 100) * reqFertK;
                        pOfNPK = reqP - (pRatioComplex / 100) * reqFertK;
//                    kOfNPK = reqK - (kRatioComplex / 100) * reqFertK;

                        System.out.println("============nOfNPK==========" + nOfNPK);
                        System.out.println("============pOfNPK==========" + pOfNPK);
//                    System.out.println("============kOfNPK==========" + kOfNPK);


                        // Calculate the value for P and K
                        System.out.println("============phosphaticFR==========" + calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                        System.out.println("============nitrogenousFR==========" + calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));

                        complexFR.setRequirement(reqFertK);
//                        phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                        nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                    } else {
                        System.out.println("else===========");
                        complexFR.setRequirement(reqFertN);
//                        phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                        potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));

                    }
                }
            }

//
//            // Required Value for Nitrogenous and Phosphatic Fertilizer
//            complexFR.setRequirement(reqFertP);
//            if (nitrogenusFertilizer != null) {
//                nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
//            }
//            if (potassicFertilizer != null) {
//                potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
//            }
        } else if (reqN > 0 && reqP > 0 && reqK > 0 && phosphaticFertilizer != null) {
            // If all N,P,K are greater than Zero and phosphaticFertilizer is also Selected

            System.out.println("======reqN > 0 && reqP > 0 && reqK > 0 && phosphaticFertilizer != null::::::::::=====");

            phosphaticFR.setFertilizerName(phosphaticFertilizer.getName());
            phosphaticFR.setFertylizerType(phosphaticFertilizer.getCategoryType());
            phosphaticFR.setpRatio(phosphaticFertilizer.getpRatio());
            phosphaticFR.setUnit(phosphaticFertilizer.getUnit());

            // Calculate the 70% of the reqP

            double remainingP = reqP - seventyPercentOfP;
            // Calculate the required Phosphorus Fertilzer for remainingP and add it to the final potassicFR
            double phosphoricFert = calculatePhosphoricFert(remainingP, phosphaticFertilizer);

            System.out.println("===========remainingP :::::::==========="+remainingP);
            if (min == reqN) {
                System.out.println("N is min");
                reqNLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
            }

            if (min == reqP) {
                System.out.println("P is min");
                reqPLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
            }

            if (min == reqK && kRatioComplex >0) {
                System.out.println("K is min");
                reqKLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
            } else {
                // Check smaller between N and P
                min = Math.min(reqN, reqP);

                if (min == reqN) {
                    System.out.println("N is min========kRatioComplex="+kRatioComplex);
                    reqNLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
                }

                if (min == reqP) {
                    System.out.println("P is min========kRatioComplex="+kRatioComplex);
                    reqPLess(nitrogenusFertilizer, phosphaticFertilizer, potassicFertilizer, complexFR, nitrogenousFR, phosphaticFR, potassicFR, reqN, seventyPercentOfP, reqK, kRatioComplex, nRatioComplex, pRatioComplex);
                }
            }

            System.out.println("phosphoricFert===="+phosphoricFert);
            System.out.println("phosphaticFR.getRequirement()========"+phosphaticFR.getRequirement());
            System.out.println("phosphaticFR.getRequirement()+phosphoricFert========"+phosphaticFR.getRequirement()+phosphoricFert);

            double totalPhosphaticFertReq = phosphaticFR.getRequirement() + phosphoricFert;

            System.out.println("==============totalPhosphaticFertReq===::::::::======="+totalPhosphaticFertReq);

            finalFertReq(chemicalFertRecomendation, phosphaticFR, totalPhosphaticFertReq);
            if (!StringUtils.isEmpty(phosphaticFR.getFertilizerName())) {
                fertilizerResponseList.add(phosphaticFR);
            }
        }

        finalFertReq(chemicalFertRecomendation, complexFR, complexFR.getRequirement());
        finalFertReq(chemicalFertRecomendation, nitrogenousFR, nitrogenousFR.getRequirement());
        finalFertReq(chemicalFertRecomendation, potassicFR, potassicFR.getRequirement());

        if (!StringUtils.isEmpty(complexFR.getFertilizerName())) {
            fertilizerResponseList.add(complexFR);
        }
        if (!StringUtils.isEmpty(nitrogenousFR.getFertilizerName())) {
            fertilizerResponseList.add(nitrogenousFR);
        }
        if (!StringUtils.isEmpty(potassicFR.getFertilizerName())) {
            fertilizerResponseList.add(potassicFR);
        }

        // format for requirement
        for (FertilizerResponse fertilizerResponse : fertilizerResponseList) {
            final DecimalFormat f = new DecimalFormat("##.00");
            fertilizerResponse.setRequirement(Double.parseDouble(f.format(fertilizerResponse.getRequirement())));
        }
        return fertilizerResponseList;
    }

    private final static BigDecimal hundred = new BigDecimal(100);

    @Override
    public OrganicReqFert getOrganicRecommendation(OrganicFertRecomendation organicFertRecomendation) {

        if (organicFertRecomendation.getFieldSize().compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("Area can't not be 0");
        }

        BigDecimal reqN = organicFertRecomendation.getReqN();
        BigDecimal reqP = organicFertRecomendation.getReqP();
        BigDecimal reqK = organicFertRecomendation.getReqK();

        System.out.println("============reqN=========="+reqN);
        System.out.println("============reqP=========="+reqP);
        System.out.println("============reqK=========="+reqK);

        OrganicReqFert organicReqFert = new OrganicReqFert();
        List<POMFertCal> pomFertCalList = new ArrayList<>();

//        List<Double> totalComplexRatio = new ArrayList<>();
//        List<Fertilizer> complexFertilizers = new ArrayList<>();
//        for (String id : organicFertRecomendation.getComplexFertilizerId()) {
//            Fertilizer fertilizer = farmingService.getFertilizer(id);
//            complexFertilizers.add(fertilizer);
//            totalComplexRatio.add(fertilizer.getnRatio());
//        }


        BigDecimal totalNFulfil = BigDecimal.ZERO;
        BigDecimal remainingN = BigDecimal.ZERO;
        BigDecimal average;

        if (organicFertRecomendation.getNitrogenusFertilizerId().size() > 1) {
            for (String id : organicFertRecomendation.getNitrogenusFertilizerId()) {
                Fertilizer fertilizer = farmingService.getFertilizer(id);
                POMFertCal pomFertCal = new POMFertCal();
                pomFertCal.setFertilizerName(fertilizer.getName());
                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
                pomFertCal.setCategoryType(fertilizer.getCategoryType());
                pomFertCal.setN(BigDecimal.valueOf(fertilizer.getnRatio()));
                pomFertCal.setRequiredFert(fertilizer.getQuantityGood().divide(new BigDecimal(2), BigDecimal.ROUND_HALF_DOWN));
                pomFertCal.setUnit(fertilizer.getUnit());

                /////////
                System.out.println("nRatio=====if======="+fertilizer.getnRatio());
                BigDecimal ratio = BigDecimal.valueOf(fertilizer.getnRatio());
                totalNFulfil = totalNFulfil.add(ratio);
                ///////////
                pomFertCalList.add(pomFertCal);
            }
        } else {
            for (String id : organicFertRecomendation.getNitrogenusFertilizerId()) {
                Fertilizer fertilizer = farmingService.getFertilizer(id);
                POMFertCal pomFertCal = new POMFertCal();
                pomFertCal.setFertilizerName(fertilizer.getName());
                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
                pomFertCal.setCategoryType(fertilizer.getCategoryType());
                pomFertCal.setN(BigDecimal.valueOf(fertilizer.getnRatio()));
                pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
                pomFertCal.setUnit(fertilizer.getUnit());

                ///////////
                System.out.println("nRatio====else========"+fertilizer.getnRatio());
                BigDecimal ratio = BigDecimal.valueOf(fertilizer.getnRatio());
                totalNFulfil = totalNFulfil.add(ratio);
                ////////////
                pomFertCalList.add(pomFertCal);
            }
        }

//        if (reqN.compareTo(BigDecimal.ZERO) > 0) {
//            for (String id : organicFertRecomendation.getNitrogenusFertilizerId()) {
//                Fertilizer fertilizer = farmingService.getFertilizer(id);
//                POMFertCal pomFertCal = new POMFertCal();
//                pomFertCal.setFertilizerName(fertilizer.getName());
//                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
//                pomFertCal.setCategoryType(fertilizer.getCategoryType());
//                pomFertCal.setN(BigDecimal.valueOf(fertilizer.getnRatio()));
//                pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
//                pomFertCal.setUnit(fertilizer.getUnit());
//
//                System.out.println("nRatio============"+fertilizer.getnRatio());
//                BigDecimal ratio = BigDecimal.valueOf(fertilizer.getnRatio());
//                totalNFulfil = totalNFulfil.add(ratio);
//
//                pomFertCalList.add(pomFertCal);
//            }


        /////////
        average = totalNFulfil.divide(BigDecimal.valueOf(organicFertRecomendation.getNitrogenusFertilizerId().size()), BigDecimal.ROUND_HALF_DOWN);
        System.out.println("remainingN=====average==="+average);
        remainingN = reqN.subtract(average);
        System.out.println("remainingN====after Nitrogen Fert====like Azeto==="+remainingN);
//        }

        ////////

//        if (organicFertRecomendation.getPhosphaticFertilizerId().size() >1) {
//            for (String id : organicFertRecomendation.getPhosphaticFertilizerId()) {
//                Fertilizer fertilizer = farmingService.getFertilizer(id);
//
//                POMFertCal pomFertCal = new POMFertCal();
//                pomFertCal.setFertilizerName(fertilizer.getName());
//                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
//                pomFertCal.setCategoryType(fertilizer.getCategoryType());
//                pomFertCal.setP(BigDecimal.valueOf(fertilizer.getpRatio()));
//                if (fertilizer.getName().contains("PSB")) {
//                    pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
//                } else {
//                    pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
//                }
//                pomFertCal.setUnit(fertilizer.getUnit());
//
//                pomFertCalList.add(pomFertCal);
//            }
//        } else {
//            for (String id : organicFertRecomendation.getPhosphaticFertilizerId()) {
//                Fertilizer fertilizer = farmingService.getFertilizer(id);
//
//                POMFertCal pomFertCal = new POMFertCal();
//                pomFertCal.setFertilizerName(fertilizer.getName());
//                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
//                pomFertCal.setCategoryType(fertilizer.getCategoryType());
//                pomFertCal.setP(BigDecimal.valueOf(fertilizer.getpRatio()));
//                pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
//                pomFertCal.setUnit(fertilizer.getUnit());
//
//                pomFertCalList.add(pomFertCal);
//            }
//        }

        BigDecimal remainingP = BigDecimal.ZERO;
        BigDecimal totalPFulfil = BigDecimal.ZERO;
        if (reqP.compareTo(BigDecimal.ZERO) > 0) {
            for (String id : organicFertRecomendation.getPhosphaticFertilizerId()) {
                Fertilizer fertilizer = farmingService.getFertilizer(id);

                POMFertCal pomFertCal = new POMFertCal();
                pomFertCal.setFertilizerName(fertilizer.getName());
                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
                pomFertCal.setCategoryType(fertilizer.getCategoryType());
                pomFertCal.setP(BigDecimal.valueOf(fertilizer.getpRatio()));
                pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
                pomFertCal.setUnit(fertilizer.getUnit());
                BigDecimal ratio = BigDecimal.valueOf(fertilizer.getpRatio());
                System.out.println("ratio============"+ratio);
                totalPFulfil = totalPFulfil.add(ratio);

                pomFertCalList.add(pomFertCal);
            }

            average = totalPFulfil.divide(BigDecimal.valueOf(organicFertRecomendation.getPhosphaticFertilizerId().size()), BigDecimal.ROUND_HALF_DOWN);
            remainingP = reqP.subtract(average);
            System.out.println("remainingP=====remainingP==="+remainingP);

        }

        BigDecimal totalKFulfil = BigDecimal.ZERO;
        BigDecimal totalKRatio = BigDecimal.ZERO;
        BigDecimal remainingK = BigDecimal.ZERO;
        if (reqK.compareTo(BigDecimal.ZERO) > 0) {
            for (String id : organicFertRecomendation.getPotassicFertilizerId()) {
                Fertilizer fertilizer = farmingService.getFertilizer(id);
                totalKRatio = totalKRatio.add(BigDecimal.valueOf(fertilizer.getkRatio()));

                POMFertCal pomFertCal = new POMFertCal();
                pomFertCal.setFertilizerName(fertilizer.getName());
                pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
                pomFertCal.setCategoryType(fertilizer.getCategoryType());
                pomFertCal.setK(BigDecimal.valueOf(fertilizer.getkRatio()));
                pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
                pomFertCal.setUnit(fertilizer.getUnit());

                BigDecimal ratio = BigDecimal.valueOf(fertilizer.getkRatio());
                System.out.println("getkRatio============"+ratio);
                BigDecimal kFulfil = reqK.multiply(ratio).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
                totalKFulfil = totalKFulfil.add(kFulfil);

                pomFertCalList.add(pomFertCal);
            }
            average = totalKFulfil.divide(BigDecimal.valueOf(organicFertRecomendation.getPotassicFertilizerId().size()), BigDecimal.ROUND_HALF_DOWN);
            remainingK = reqK.subtract(average);
            System.out.println("remainingK=====remainingP==="+remainingK);
        }

        // TODO: Calculate the remaining requirement by using the organic manure

        logger.info("=====================================Calculate Patanjali OM====================================================================");
        // Get Patanjali Manure Fertilizer List for Crop
        String cropId = organicFertRecomendation.getCropId();
        if (StringUtils.isEmpty(cropId)) {
            logger.info("cropId is empty");
            throw new CustomException("crop is not selected");
        }
        List<Fertilizer> patanjaliManureFertList = landService.getFertilizersByCropId(cropId, "Manure", "PatanjaliManure");

        if (patanjaliManureFertList == null) {
            logger.info("no fertilizer found for cropId: " + cropId);
            throw new ResourceNotFoundException("No fertilizer found for cropId "+ cropId);
        }

        BigDecimal totalNByPOM = BigDecimal.ZERO;
        BigDecimal totalPByPOM = BigDecimal.ZERO;
        BigDecimal totalKByPOM = BigDecimal.ZERO;
        for (Fertilizer fertilizer : patanjaliManureFertList) {
            BigDecimal value = BigDecimal.ZERO;

            switch (organicFertRecomendation.getYieldType().getType()) {

                case Irrigated:
                    value = fertilizer.getQuantityGood();
                    break;

                case SemiIrrigated:
                    value = fertilizer.getQuantityMedium();
                    break;

                case Rainfed:
                    value = fertilizer.getQuantityPoor();
                    break;
            }

            System.out.println("===========name========"+fertilizer.getName());
            System.out.println("===========value========"+value);

            // TODO: Need delete db
            BigDecimal nOfFert = value.multiply(BigDecimal.valueOf(fertilizer.getnRatio())).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal pOfFert = value.multiply(BigDecimal.valueOf(fertilizer.getpRatio())).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal kOfFert = value.multiply(BigDecimal.valueOf(fertilizer.getkRatio())).divide(hundred, BigDecimal.ROUND_HALF_DOWN);

            totalNByPOM = totalNByPOM.add(nOfFert);
            totalPByPOM = totalPByPOM.add(pOfFert);
            totalKByPOM = totalKFulfil.add(kOfFert);

            POMFertCal pomFertCal = new POMFertCal();
            pomFertCal.setFertilizerName(fertilizer.getName());
            pomFertCal.setCategoryType(fertilizer.getCategoryType());
            pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
            pomFertCal.setN(nOfFert);
            pomFertCal.setP(pOfFert);
            pomFertCal.setK(kOfFert);
            pomFertCal.setRequiredFert(value);
            pomFertCal.setUnit(fertilizer.getUnit());

            pomFertCalList.add(pomFertCal);

        }

        remainingN = remainingN.subtract(totalNByPOM);
        remainingP = remainingP.subtract(totalPByPOM);
        remainingK = remainingK.subtract(totalKByPOM);

        System.out.println("======last===========remainingN==============================="+remainingN);
        System.out.println("======last===========remainingP==============================="+remainingP);
        System.out.println("======last===========remainingK==============================="+remainingK);
        organicReqFert.setPomFertCals(pomFertCalList);

        Fertilizer compostFertilizer = farmingService.getFertilizer(organicFertRecomendation.getCompostFertilizerId());
        if (compostFertilizer != null) {

            BigDecimal quantity = BigDecimal.ZERO;

            switch (organicFertRecomendation.getYieldType().getType()) {

                case Irrigated:
                    quantity = compostFertilizer.getQuantityGood();
                    break;

                case SemiIrrigated:
                    quantity = compostFertilizer.getQuantityMedium();
                    break;

                case Rainfed:
                    quantity = compostFertilizer.getQuantityPoor();
                    break;
            }

//            quantityGood = quantityGood.multiply(new BigDecimal(1000));
            BigDecimal nRatio = BigDecimal.valueOf(compostFertilizer.getnRatio());

            System.out.println("==compostFertilizer=======quantityGood========"+quantity);
            System.out.println("==compostFertilizer=======nRatio========"+nRatio);

            BigDecimal reqNFulfil = nRatio.divide(hundred, RoundingMode.HALF_DOWN).multiply(quantity);
            BigDecimal reqPFulfil = BigDecimal.valueOf(compostFertilizer.getpRatio()).divide(hundred, RoundingMode.HALF_DOWN).multiply(quantity);
            BigDecimal reqKFulfil = BigDecimal.valueOf(compostFertilizer.getkRatio()).divide(hundred, RoundingMode.HALF_DOWN).multiply(quantity);

            System.out.println("==compostFertilizer=======reqNFulfil========"+reqNFulfil);
            System.out.println("==compostFertilizer=======reqPFulfil========"+reqPFulfil);
            System.out.println("==compostFertilizer=======reqKFulfil========"+reqKFulfil);

            // Calculate the reaming N P K

            remainingN = remainingN.subtract(reqNFulfil);
            remainingP = remainingP.subtract(reqPFulfil);
            remainingK = remainingK.subtract(reqKFulfil);

            System.out.println("===compostFertilizer======remainingN========"+remainingN);
            System.out.println("===compostFertilizer======remainingP========"+remainingP);
            System.out.println("===compostFertilizer======remainingK========"+remainingK);


            System.out.println("===compostFertilizer======getName========"+compostFertilizer.getName());

            POMFertCal pomFertCal = new POMFertCal();
            pomFertCal.setFertilizerName(compostFertilizer.getName());
            pomFertCal.setFertilizerType(compostFertilizer.getFertilizerType());
            pomFertCal.setCategoryType(compostFertilizer.getCategoryType());
            pomFertCal.setN(BigDecimal.valueOf(compostFertilizer.getnRatio()));
            pomFertCal.setP(BigDecimal.valueOf(compostFertilizer.getpRatio()));
            pomFertCal.setK(BigDecimal.valueOf(compostFertilizer.getkRatio()));
            pomFertCal.setRequiredFert(compostFertilizer.getQuantityGood());
            pomFertCal.setUnit(compostFertilizer.getUnit());

            if (StringUtils.isEmpty(organicFertRecomendation.getGmFertilizerId()) && StringUtils.isEmpty(organicFertRecomendation.getJivamritFertilizerId()) && StringUtils.isEmpty(organicFertRecomendation.getOilCakeFertilizerId())) {
                // TODO: check remiaingN then recommend like 10-100 kg

                System.out.println(":::::::::::::::::remainingN:::::::::::::::::"+remainingN);
                if (remainingN.compareTo(BigDecimal.TEN) < 0) {
                    quantity = quantity.add(new BigDecimal(1000));
                    remainingN = remainingN.subtract(new BigDecimal(4));
                }

                if (remainingN.compareTo(BigDecimal.TEN) > 0 && remainingN.compareTo(new BigDecimal(20)) < 0) {
                    quantity = quantity.add(new BigDecimal(2000));
                    remainingN = remainingN.subtract(new BigDecimal(8));
                }

                if (remainingN.compareTo(new BigDecimal(20)) > 0 && remainingN.compareTo(new BigDecimal(30)) < 0) {
                    quantity = quantity.add(new BigDecimal(2500));
                    remainingN = remainingN.subtract(new BigDecimal(10));
                }

                if (remainingN.compareTo(new BigDecimal(30)) > 0 && remainingN.compareTo(new BigDecimal(40)) < 0) {
                    quantity = quantity.add(new BigDecimal(3000));
                    remainingN = remainingN.subtract(new BigDecimal(12));
                }

                if (remainingN.compareTo(new BigDecimal(40)) > 0) {
                    quantity = quantity.add(new BigDecimal(4000));
                    remainingN = remainingN.subtract(new BigDecimal(16));
                }
            }

            System.out.println("=====Total=======remainingN======"+remainingN);

            pomFertCal.setRequiredFert(quantity);
            pomFertCalList.add(pomFertCal);

            finalOrganicFertReq(pomFertCalList, organicFertRecomendation);

            organicReqFert.setPomFertCals(pomFertCalList);

            return organicReqFert;
        }

        if (!StringUtils.isEmpty(organicFertRecomendation.getGmFertilizerId())) {
            calOrgFertReq(organicFertRecomendation, pomFertCalList, remainingN, farmingService.getFertilizer(organicFertRecomendation.getGmFertilizerId()));
        }

        System.out.println("=====gmFertilizer=======remainingN======"+remainingN);

        Fertilizer jivamritFertilizer;
        if (!StringUtils.isEmpty(organicFertRecomendation.getJivamritFertilizerId())) {
            jivamritFertilizer = farmingService.getFertilizer(organicFertRecomendation.getJivamritFertilizerId());
            if (jivamritFertilizer != null) {
                System.out.println("===jivamritFertilizer==============" + jivamritFertilizer);

                POMFertCal pomFertCal = new POMFertCal();
                if (remainingN.compareTo(BigDecimal.TEN) < 0) {
                    remainingN = BigDecimal.ZERO;
                    pomFertCal.setSpray("1-2 Spray");
                }

                if (remainingN.compareTo(BigDecimal.TEN) > 0 && remainingN.compareTo(new BigDecimal(20)) < 0) {
                    remainingN = BigDecimal.ZERO;
                    pomFertCal.setSpray("2-3 Spray");
                }

                if (remainingN.compareTo(new BigDecimal(20)) > 0 && remainingN.compareTo(new BigDecimal(30)) < 0) {
                    remainingN = BigDecimal.ZERO;
                    pomFertCal.setSpray("3 Spray");
                }

                if (remainingN.compareTo(new BigDecimal(30)) > 0 && remainingN.compareTo(new BigDecimal(40)) < 0) {
                    remainingN = BigDecimal.ZERO;
                    pomFertCal.setSpray("3-4 Spray");
                }

                if (remainingN.compareTo(new BigDecimal(40)) > 0) {
                    remainingN = remainingN.subtract(new BigDecimal(50));
                    pomFertCal.setSpray("3-4 Spray");
                }


//                remainingP = remainingP.subtract(reqPFulfil);
//                remainingK = remainingK.subtract(reqKFulfil);

                System.out.println("===jivamritFertilizer======remainingN========" + remainingN);

                pomFertCal.setFertilizerName(jivamritFertilizer.getName());
                pomFertCal.setFertilizerType(jivamritFertilizer.getFertilizerType());
                pomFertCal.setCategoryType(jivamritFertilizer.getCategoryType());
                pomFertCal.setN(BigDecimal.valueOf(jivamritFertilizer.getnRatio()));
                pomFertCal.setP(BigDecimal.valueOf(jivamritFertilizer.getpRatio()));
                pomFertCal.setK(BigDecimal.valueOf(jivamritFertilizer.getkRatio()));
                pomFertCal.setRequiredFert(jivamritFertilizer.getQuantityGood());
                pomFertCal.setUnit(jivamritFertilizer.getUnit());

                // The Quantity is set ZERO because Jivamrit is calculated in Sprays
                pomFertCal.setRequiredFert(BigDecimal.ZERO);
                pomFertCalList.add(pomFertCal);
            }
        }

        // If the N P K requirement is not fulfilled by the Bio Fertilizers and Compost
        // then calculate remaining Requirement from Oil Cake Fertilizer
        System.out.println("=====oilCakeFertilizer=======remainingN===11==="+remainingN);
        if (remainingN.compareTo(BigDecimal.ZERO) > 0) {
            if (!StringUtils.isEmpty(organicFertRecomendation.getOilCakeFertilizerId())) {
                calOrgFertReq(organicFertRecomendation, pomFertCalList, remainingN, farmingService.getFertilizer(organicFertRecomendation.getOilCakeFertilizerId()));
            }
        }

        System.out.println("=====oilCakeFertilizer=======remainingN====22=="+remainingN);

        finalOrganicFertReq(pomFertCalList, organicFertRecomendation);
        organicReqFert.setPomFertCals(pomFertCalList);

        return organicReqFert;
    }

    private void finalOrganicFertReq(List<POMFertCal> pomFertCalList, OrganicFertRecomendation fertilizerResponse) {
        System.out.println("============pomFertCalList.size====================="+pomFertCalList.size());

        BigDecimal fieldSize = fertilizerResponse.getFieldSize();

        pomFertCalList.forEach(pomFertCal -> {

            BigDecimal requiredFert = pomFertCal.getRequiredFert();
            System.out.println(requiredFert+"==requiredFert===acreValue==="+acreValue+"=======fieldSize=============="+fieldSize);

            if (fertilizerResponse.getFieldSizeType().equals(FieldSize.FieldSizeType.Acre)) {
                BigDecimal divide = requiredFert.divide(new BigDecimal(acreValue));
                System.out.println("=========divide==========="+ divide);
                System.out.println(pomFertCal.getFertilizerName()+"===name===REQ========Acre====="+ divide.multiply(fieldSize));
                pomFertCal.setRequiredFert(divide.multiply(fieldSize));
            } else {
                System.out.println(requiredFert+"requirement=============fertilizerResponse.getFieldSize()"+ fieldSize);
                System.out.println("======REQ========HEC====="+requiredFert.multiply(fieldSize));
                pomFertCal.setRequiredFert(requiredFert.multiply(fieldSize));
            }
        });
    }

    /**
     * For Calculating Oil Cake and Green Manure Fertilizer
     * @param organicFertRecomendation
     * @param pomFertCalList
     * @param remainingN
     * @return
     */
    private Fertilizer calOrgFertReq(OrganicFertRecomendation organicFertRecomendation, List<POMFertCal> pomFertCalList, BigDecimal remainingN, Fertilizer fertilizer) {
        if (fertilizer != null) {

            BigDecimal quantityGood = BigDecimal.ZERO;

            switch (organicFertRecomendation.getYieldType().getType()) {

                case Irrigated:
                    quantityGood = fertilizer.getQuantityGood();
                    break;

                case SemiIrrigated:
                    quantityGood = fertilizer.getQuantityMedium();
                    break;

                case Rainfed:
                    quantityGood = fertilizer.getQuantityPoor();
                    break;
            }

            BigDecimal nRatio = BigDecimal.valueOf(fertilizer.getnRatio());

            System.out.println("==fertilizer=======quantityGood========"+quantityGood);
            System.out.println("==fertilizer=======nRatio========"+nRatio);

            BigDecimal reqFert = hundred.divide(nRatio, RoundingMode.HALF_DOWN).multiply(remainingN);

//                BigDecimal reqPFulfil = BigDecimal.valueOf(oilCakeFertilizer.getpRatio()).divide(hundred, RoundingMode.HALF_DOWN).multiply(quantityGood);
//                BigDecimal reqKFulfil = BigDecimal.valueOf(oilCakeFertilizer.getkRatio()).divide(hundred, RoundingMode.HALF_DOWN).multiply(quantityGood);
//
//                System.out.println("==oilCakeFertilizer=======reqNFulfil========"+reqNFulfil);
//                System.out.println("==oilCakeFertilizer=======reqPFulfil========"+reqPFulfil);
//                System.out.println("==oilCakeFertilizer=======reqKFulfil========"+reqKFulfil);

            // Calculate the reaming N P K

            remainingN = remainingN.subtract(reqFert);
//                remainingP = remainingP.subtract(reqPFulfil);
//                remainingK = remainingK.subtract(reqKFulfil);

            System.out.println("===oilCakeFertilizer======remainingN========"+remainingN);

            POMFertCal pomFertCal = new POMFertCal();
            pomFertCal.setFertilizerName(fertilizer.getName());
            pomFertCal.setFertilizerType(fertilizer.getFertilizerType());
            pomFertCal.setCategoryType(fertilizer.getCategoryType());
            pomFertCal.setN(BigDecimal.valueOf(fertilizer.getnRatio()));
            pomFertCal.setP(BigDecimal.valueOf(fertilizer.getpRatio()));
            pomFertCal.setK(BigDecimal.valueOf(fertilizer.getkRatio()));
            pomFertCal.setRequiredFert(fertilizer.getQuantityGood());
            pomFertCal.setUnit(fertilizer.getUnit());

            pomFertCal.setRequiredFert(reqFert);
            pomFertCalList.add(pomFertCal);
        }
        return fertilizer;
    }

    @Override
    public MixReqFert getMixRecommendation(MixFertRecomendation mixFertRecomendation) {

        ChemicalFertRecomendation chemicalFertRecomendation = mixFertRecomendation.getChemicalFertRecomendation();
        OrganicFertRecomendation organicFertRecomendation = mixFertRecomendation.getOrganicFertRecomendation();

        // TODO : set Ratio
        BigDecimal organicRatio = mixFertRecomendation.getOrganicRatio();
        BigDecimal chemicalRatio = new BigDecimal(100).subtract(organicRatio);

        System.out.println("###############organicRatio#################"+organicRatio);
        System.out.println("###############chemicalRatio#################"+chemicalRatio);
        BigDecimal reqN = mixFertRecomendation.getReqN();
        BigDecimal reqP = mixFertRecomendation.getReqP();
        BigDecimal reqK = mixFertRecomendation.getReqK();

        System.out.println("###############reqN#################"+reqN);
        System.out.println("###############reqP#################"+reqP);
        System.out.println("###############reqK#################"+reqK);


        // Get ORatio for N P K
        BigDecimal oPercentOfN = organicRatio.multiply(reqN).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal oPercentOfP = organicRatio.multiply(reqP).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal oPercentOfK = organicRatio.multiply(reqK).divide(hundred, BigDecimal.ROUND_HALF_DOWN);
        System.out.println("###############oPercentOfN#################"+oPercentOfN);
        System.out.println("###############oPercentOfP#################"+oPercentOfP);
        System.out.println("###############oPercentOfK#################"+oPercentOfK);

        organicFertRecomendation.setReqN(oPercentOfN);
        organicFertRecomendation.setReqP(oPercentOfP);
        organicFertRecomendation.setReqK(oPercentOfK);

        MixReqFert mixReqFert = new MixReqFert();

        OrganicReqFert organicRecommendation = getOrganicRecommendation(organicFertRecomendation);
        mixReqFert.setOrganicReqFert(organicRecommendation);


        double cPercentOfN = (chemicalRatio.multiply(reqN)).divide(hundred, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        double cPercentOfP = (chemicalRatio.multiply(reqP)).divide(hundred, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        double cPercentOfK = (chemicalRatio.multiply(reqK)).divide(hundred, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        System.out.println("###############cPercentOfN#################"+cPercentOfN);
        System.out.println("###############cPercentOfP#################"+cPercentOfP);
        System.out.println("###############cPercentOfK#################"+cPercentOfK);

        chemicalFertRecomendation.setReqN(cPercentOfN);
        chemicalFertRecomendation.setReqP(cPercentOfP);
        chemicalFertRecomendation.setReqK(cPercentOfK);
        Set<FertilizerResponse> chemicalRecommendation = getChemicalRecommendation(chemicalFertRecomendation);
        mixReqFert.setFertilizerResponseList(chemicalRecommendation);

        System.out.println("#############mixReqFert#######"+mixReqFert);
        return mixReqFert;
    }

    /**
     * Calculate Fertilizer Recommendation according to Field Size and Field Size Type(Acre/Hectare)
     * @param chemicalFertRecomendation
     * @param fertilizerResponse
     * @param requirement
     */
    private void finalFertReq(ChemicalFertRecomendation chemicalFertRecomendation, FertilizerResponse fertilizerResponse, double requirement) {

        System.out.println("==========name========="+fertilizerResponse.getFertilizerName());
        System.out.println(requirement+"requirement======acreValue"+acreValue+"=========chemicalFertRecomendation.getFieldSize()"+chemicalFertRecomendation.getFieldSize());
        if (chemicalFertRecomendation.getFieldSizeType().equals(FieldSize.FieldSizeType.Acre)) {

            System.out.println("======REQ========Acre====="+(requirement / acreValue) * chemicalFertRecomendation.getFieldSize());
            fertilizerResponse.setRequirement((requirement / acreValue) * chemicalFertRecomendation.getFieldSize());
        } else {
            System.out.println(requirement+"requirement=============chemicalFertRecomendation.getFieldSize()"+chemicalFertRecomendation.getFieldSize());

            System.out.println("======REQ========HEC====="+requirement * chemicalFertRecomendation.getFieldSize());
            fertilizerResponse.setRequirement(requirement * chemicalFertRecomendation.getFieldSize());
        }
    }

    private void reqNLess(Fertilizer nitrogenusFertilizer, Fertilizer phosphaticFertilizer, Fertilizer potassicFertilizer,
                          FertilizerResponse complexFR, FertilizerResponse nitrogenousFR, FertilizerResponse phosphaticFR,
                          FertilizerResponse potassicFR, double reqN, double reqP, double reqK, double kRatioComplex, double nRatioComplex,
                          double pRatioComplex) {

        double nOfNPK;
        double pOfNPK;
        double kOfNPK;
        System.out.println("============if=======N is smaller===");

        System.out.println("=========reqN=====" + reqN);
        System.out.println("=========nRatioComplex=====" + nRatioComplex);
        double reqFertN = (reqN / nRatioComplex) * 100;
        System.out.println("============reqFertN==========" + reqFertN);

        // Check N and P according to the reqFertK
//            find the N value for reqFertK

        double nOfReqFert;

        double pOfReqFert = 0;
        if (pRatioComplex > 0) {
            System.out.println("============pRatioComplex==========" + pRatioComplex);
            pOfReqFert = (pRatioComplex / 100) * reqFertN;
        }
        System.out.println("============pOfReqFert==========" + pOfReqFert);

        double kOfReqFert = 0;
        if (kRatioComplex > 0) {
            System.out.println("============kRatioComplex==========" + kRatioComplex);
            kOfReqFert = (kRatioComplex / 100) * reqFertN;
        } else {
            potassicFR.setRequirement(calculatePotassicFert(reqK, potassicFertilizer));
        }

        System.out.println("============kOfReqFert==========" + kOfReqFert);

        // Check the More Exceeded Fertilizer N or P
        double exceedK = reqK - kOfReqFert;
        double exceedP = reqP - pOfReqFert;

        System.out.println("============exceedK====reqNLess======" + exceedK);
        System.out.println("============exceedP====reqNLess======" + exceedP);

        if (exceedK > 0 && exceedP > 0) {
            // In this case the Requirement for Least NPK Value i.e. K(Postasic) is fulfilled by NPK(Complex) Fertilizer
            // Now calculate value for the Fertilizer for Nitrogenous, Phosphatic

            if (phosphaticFertilizer != null) {
                double phosphoricFert = calculatePhosphoricFert(exceedP, phosphaticFertilizer);
                System.out.println("============phosphoricFert=========="+phosphoricFert);
                phosphaticFR.setRequirement(phosphoricFert);
            }
            double potassicFert = calculatePotassicFert(exceedK, potassicFertilizer);
            System.out.println("============potassicFert=========="+potassicFert);

            potassicFR.setRequirement(potassicFert);
            complexFR.setRequirement(reqFertN);

        } else {

            System.out.println("============&&&&&&&&&&&&&&&&&&&&&&&&&&------reqNLess--------ELSE------------------------%%%%%%%%%%%%==========");

            // ExceedK is greater(comparing negative values)
            if ((exceedK < exceedP || exceedK == exceedP) && kRatioComplex > 0) {
                System.out.println("=====if  exceed K==is more=");

                System.out.println("<<<<<<<<<<<<<<=====kRatioComplex===reqNLess===="+kRatioComplex);
                double reqFertK = (reqK / kRatioComplex) * 100;
                // Calculate All the Fertilzers in

                nOfReqFert = (nRatioComplex / 100) * reqFertK;
                pOfReqFert = (pRatioComplex / 100) * reqFertK;
                kOfReqFert = (kRatioComplex / 100) * reqFertK;
                System.out.println("============nOfReqFert=====reqNLess=====" + nOfReqFert);
                System.out.println("============pOfReqFert=====reqNLess=====" + pOfReqFert);
                System.out.println("============kOfReqFert======reqNLess====" + kOfReqFert);

                nOfNPK = reqN - nOfReqFert;
                pOfNPK = reqP - pOfReqFert;
                kOfNPK = reqK - kOfReqFert;

                System.out.println("============nOfNPK======reqNLess====" + nOfNPK);
                System.out.println("============pOfNPK=====reqNLess=====" + pOfNPK);
                System.out.println("============kOfNPK=====reqNLess=====" + kOfNPK);

                complexFR.setRequirement(reqFertK);
                System.out.println("====if========1111====reqNLess=");

                // Calculate the value for P and K
//                    pOfPotassic = (pOfNPK/pRatioPhosphatic) *100;
//                    kOfPhostphatic = (kOfNPK/kRatioPotassic) *100;

//                    System.out.println("============pOfPotassic=========="+pOfPotassic);
//                    System.out.println("============kOfPhostphatic=========="+kOfPhostphatic);

                // Check if pOfNPK is negative
                if (pOfNPK < 0) {
                    pOfNpkIncrease(potassicFertilizer, complexFR, nitrogenousFR, potassicFR, reqN, reqP, reqK, kRatioComplex, nRatioComplex, pRatioComplex, pRatioComplex / 100, reqP - pOfReqFert, calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                } else {
                    if (phosphaticFertilizer != null) {
                        phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    }
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                }
            } else {
                System.out.println("==reqNLess===else  exceed P== is more=");
                double reqFertP = (reqP / pRatioComplex) * 100;
                // Calculate All the Fertilzers in

                complexFR.setRequirement(reqFertP);
                System.out.println("====else========1111==reqNLess===");
                nOfNPK = reqN - (nRatioComplex / 100) * reqFertP;
                pOfNPK = reqP - (pRatioComplex / 100) * reqFertP;
                kOfNPK = reqK - (kRatioComplex / 100) * reqFertP;

                System.out.println("=====else=======nOfNPK====reqNLess======" + nOfNPK);
                System.out.println("======else======pOfNPK=====reqNLess=====" + pOfNPK);
                System.out.println("======else======kOfNPK=====reqNLess=====" + kOfNPK);


                // Calculate the other fertilizer composition ration for N and K as the value of P is completed by the Complex Fertilizer
                // So there is no need to calculate for the Potassic Fertilizer

                // Required Value for Nitrogenous and Phosphatic Fertilizer

                complexFR.setRequirement(reqFertP);

                // Check if pOfNPK is negative
                if (kOfNPK < 0 && kRatioComplex > 0) {
                    System.out.println("=====reqNLess=======if======nOfNPK < 0===");
                    double reqFertK = (reqK / kRatioComplex) * 100;

                    nOfReqFert = (nRatioComplex / 100) * reqFertK;
                    pOfReqFert = (pRatioComplex / 100) * reqFertK;
                    kOfReqFert = (kRatioComplex / 100) * reqFertK;

                    nOfNPK = reqN - nOfReqFert;
                    pOfNPK = reqP - pOfReqFert;
                    kOfNPK = reqK - kOfReqFert;

                    System.out.println("=====reqNLess=======nOfReqFert==========" + nOfReqFert);
                    System.out.println("=====reqNLess=======pOfReqFert==========" + pOfReqFert);
                    System.out.println("=====reqNLess=======kOfReqFert==========" + kOfReqFert);

                    System.out.println("=====reqNLess=======nOfNPK==========" + nOfNPK);
                    System.out.println("=====reqNLess=======pOfNPK==========" + pOfNPK);
                    System.out.println("====reqNLess========kOfNPK==========" + kOfNPK);


                    complexFR.setRequirement(reqFertK);
                    // Calculate the value for P and K
//                        nOfNitrogenous= (nOfNPK/nOfNitrogenous) *100;
//                        kOfPhostphatic = (kOfNPK/kRatioPotassic) *100;
//
                    System.out.println("====reqNLess========phosphaticFR==========" + calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                } else {
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                    if (kRatioComplex > 0) {
                        potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                    }
                }
            }
        }
    }

    private void reqPLess(Fertilizer nitrogenusFertilizer, Fertilizer phosphaticFertilizer, Fertilizer potassicFertilizer, FertilizerResponse complexFR, FertilizerResponse nitrogenousFR, FertilizerResponse phosphaticFR, FertilizerResponse potassicFR, double reqN, double reqP, double reqK, double kRatioComplex, double nRatioComplex, double pRatioComplex) {
        double nOfNPK;
        double pOfNPK;
        double kOfNPK;
        System.out.println("============if=======P is smaller===");

        System.out.println("=========reqP=====" + reqP);
        System.out.println("=========pRatioComplex=====" + pRatioComplex);
        double reqFertP = (reqP / pRatioComplex) * 100;
        System.out.println("============reqFertP==========" + reqFertP);

        // Check N and P according to the reqFertK
//            find the N value for reqFertK

        double pOfReqFert;

        double nOfReqFert = 0;
        if (nRatioComplex > 0) {
            System.out.println("============nRatioComplex==========" + nRatioComplex);
            nOfReqFert = (nRatioComplex / 100) * reqFertP;
        }
        System.out.println("============nOfReqFert==========" + nOfReqFert);

        double kOfReqFert = 0;
        if (kRatioComplex > 0) {
            System.out.println("============kRatioComplex==========" + kRatioComplex);
            kOfReqFert = (kRatioComplex / 100) * reqFertP;
        }
        System.out.println("============kOfReqFert==========" + kOfReqFert);

        // Check the More Exceeded Fertilizer N or P
        double exceedK = reqK - kOfReqFert;
        double exceedN = reqN - nOfReqFert;

        System.out.println("============exceedK==========" + exceedK);
        System.out.println("============exceedN==========" + exceedN);

        if (exceedK > 0 && exceedN > 0) {
            System.out.println("====if========exceedK > 0 && exceedN > 0==========");

            // In this case the Requirement for Least NPK Value i.e. K(Postasic) is fulfilled by NPK(Complex) Fertilizer
            // Now calculate value for the Fertilizer for Nitrogenous, Phosphatic

            double nitrogenousFert = calculateNitrogenousFert(exceedN, nitrogenusFertilizer);
            double potassicFert = calculatePotassicFert(exceedK, potassicFertilizer);

            System.out.println("============nitrogenousFert=========="+nitrogenousFert);
            System.out.println("============potassicFert=========="+potassicFert);

            nitrogenousFR.setRequirement(nitrogenousFert);
            potassicFR.setRequirement(potassicFert);
            complexFR.setRequirement(reqFertP);

        } else {
            System.out.println("====else========exceedK > 0 && exceedN > 0==========");
            System.out.println("============&&&&&&&&&&&&&&&&&&&&&&&&&&--------------ELSE------------------------%%%%%%%%%%%%==========");

            // ExceedK is greater(comparing negative values)
            if (exceedK > 0 && exceedK < exceedN || exceedK == exceedN) {
                System.out.println("=====if  exceed K==is more=");
                System.out.println("=============kRatioComplex========---=="+kRatioComplex);
                double reqFertK = (reqK / kRatioComplex) * 100;
                // Calculate All the Fertilzers in

                nOfReqFert = (nRatioComplex / 100) * reqFertK;
                pOfReqFert = (pRatioComplex / 100) * reqFertK;
                kOfReqFert = (kRatioComplex / 100) * reqFertK;
                System.out.println("============nOfReqFert==========" + nOfReqFert);
                System.out.println("============pOfReqFert==========" + pOfReqFert);
                System.out.println("============kOfReqFert==========" + kOfReqFert);

                nOfNPK = reqN - nOfReqFert;
                pOfNPK = reqP - pOfReqFert;
                kOfNPK = reqK - kOfReqFert;

                System.out.println("============nOfNPK==========" + nOfNPK);
                System.out.println("============pOfNPK==========" + pOfNPK);
                System.out.println("============kOfNPK==========" + kOfNPK);

                complexFR.setRequirement(reqFertK);
                System.out.println("====if========1111=====");

                // Check if pOfNPK is negative
                if (nOfNPK < 0) {
                    System.out.println("============if======nOfNPK < 0===");
                    double reqFertN = (reqN / nRatioComplex) * 100;

                    System.out.println("===pOfNPK=========nOfReqFert==========" + nOfReqFert);
                    System.out.println("=====pOfNPK=======pOfReqFert==========" + pOfReqFert);
                    System.out.println("=====pOfNPK=======kOfReqFert==========" + kOfReqFert);

                    nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                    pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                    kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                    System.out.println("=====pOfNPK=======nOfNPK==========" + nOfNPK);
                    System.out.println("====pOfNPK========pOfNPK==========" + pOfNPK);
                    System.out.println("=====pOfNPK=======kOfNPK==========" + kOfNPK);

                    complexFR.setRequirement(reqFertN);
                    // Calculate the value for P and K
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                } else {
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                }
            } else {
                System.out.println("=====else  exceed N== is more=");
                double reqFertN = (reqN / nRatioComplex) * 100;
                // Calculate All the Fertilzers in

                System.out.println("====else========1111=====");
                nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                System.out.println("=====else=======nOfNPK==========" + nOfNPK);
                System.out.println("======else======pOfNPK==========" + pOfNPK);
                System.out.println("======else======kOfNPK==========" + kOfNPK);


                // Calculate the other fertilizer composition ration for N and K as the value of P is completed by the Complex Fertilizer
                // So there is no need to calculate for the Potassic Fertilizer

                // Required Value for Nitrogenous and Phosphatic Fertilizer
//                    nOfNitrogenous = (nOfNPK/nRatioNitro) *100;
//                    kOfPhostphatic = (kOfNPK/kRatioPotassic) *100;

//                    potassicFR.setRequirement(pOfPotassic);
//                    phosphaticFR.setRequirement(kOfPhostphatic);

                //TODO: Check negative between p and k greater

                // Check if pOfNPK is negative
                if (kOfNPK < 0) {
                    System.out.println("============if======nOfNPK < 0===");
                    double reqFertK = (reqP / pRatioComplex) * 100;

                    nOfNPK = reqN - (nRatioComplex / 100) * reqFertK;
                    pOfNPK = reqP - (pRatioComplex / 100) * reqFertK;
//                    kOfNPK = reqK - (kRatioComplex / 100) * reqFertK;

                    System.out.println("============nOfNPK==========" + nOfNPK);
                    System.out.println("============pOfNPK==========" + pOfNPK);
//                    System.out.println("============kOfNPK==========" + kOfNPK);


                    // Calculate the value for P and K
                    System.out.println("============phosphaticFR==========" + calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    System.out.println("============nitrogenousFR==========" + calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));

                    complexFR.setRequirement(reqFertK);
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                } else {
                    System.out.println("else======kOfNPK < 0=====");
                    complexFR.setRequirement(reqFertN);
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                }
            }
        }
    }

    private void reqKLess(Fertilizer nitrogenusFertilizer, Fertilizer phosphaticFertilizer, Fertilizer potassicFertilizer, FertilizerResponse complexFR, FertilizerResponse nitrogenousFR, FertilizerResponse phosphaticFR, FertilizerResponse potassicFR, double reqN, double reqP, double reqK, double kRatioComplex, double nRatioComplex, double pRatioComplex) {
        double nOfNPK;
        double pOfNPK;
        double kOfNPK;
        System.out.println("============if=======K is smaller===");

        System.out.println("=========reqK=====" + reqK);
        System.out.println("=========kRatioComplex=====" + kRatioComplex);
        double reqFertK = 0;
        if (kRatioComplex > 0) {
            reqFertK = (reqK / kRatioComplex) * 100;
            System.out.println("============reqFertK==========" + reqFertK);
        }

        // Check N and P according to the reqFertK
//            find the N value for reqFertK

        double nOfReqFert = 0;
        if (nRatioComplex > 0) {
            System.out.println("============nRatioComplex==========" + nRatioComplex);
            nOfReqFert = (nRatioComplex / 100) * reqFertK;
        }
        System.out.println("============nOfReqFert==========" + nOfReqFert);

        double pOfReqFert = 0;
        if (pRatioComplex > 0) {
            System.out.println("============pRatioComplex==========" + pRatioComplex);
            pOfReqFert = (pRatioComplex / 100) * reqFertK;
        }
        System.out.println("============pOfReqFert==========" + pOfReqFert);

        // Check the More Exceeded Fertilizer N or P
        double exceedN = reqN - nOfReqFert;
        double exceedP = reqP - pOfReqFert;

        System.out.println("============exceedN==========" + exceedN);
        System.out.println("============exceedP==========" + exceedP);

        if (exceedN > 0 && exceedP > 0) {
            // In this case the Requirement for Least NPK Value i.e. K(Postasic) is fulfilled by NPK(Complex) Fertilizer
            // Now calculate value for the Fertilizer for Nitrogenous, Phosphatic

//                System.out.println("============reqFertK=========="+reqFertK);

            nitrogenousFR.setRequirement(calculateNitrogenousFert(exceedN, nitrogenusFertilizer));
            phosphaticFR.setRequirement(calculatePhosphoricFert(exceedP, phosphaticFertilizer));
            complexFR.setRequirement(reqFertK);

        } else {

            System.out.println("============&&&&&&&&&&&&&&&&&&&&&&&&&&--------------ELSE------------------------%%%%%%%%%%%%==========");

            // ExceedN is greater(comparing negative values)
            if (exceedN < exceedP || exceedN == exceedP) {
                System.out.println("=====if  exceed N==is more=");
                double reqFertN = (reqN / nRatioComplex) * 100;
                // Calculate All the Fertilzers in

                nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                System.out.println("============nOfNPK==========" + nOfNPK);
                System.out.println("============pOfNPK==========" + pOfNPK);
                System.out.println("============kOfNPK==========" + kOfNPK);

                complexFR.setRequirement(reqFertN);
                System.out.println("====if========1111=====");

                // Calculate the value for P and K

                // Check if pOfNPK is negative
                if (pOfNPK < 0) {
                    pOfNpkIncrease(potassicFertilizer, complexFR, nitrogenousFR, potassicFR, reqN, reqP, reqK, kRatioComplex, nRatioComplex, pRatioComplex, pRatioComplex / 100, reqP - pOfReqFert, calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                } else {
                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                }
            } else {
                System.out.println("=====else  exceed P== is more=");
                double reqFertP = (reqP / pRatioComplex) * 100;
                // Calculate All the Fertilzers in

                complexFR.setRequirement(reqFertP);
                System.out.println("====else========1111=====");
                nOfNPK = reqN - (nRatioComplex / 100) * reqFertP;
                pOfNPK = reqP - (pRatioComplex / 100) * reqFertP;
                kOfNPK = reqK - (kRatioComplex / 100) * reqFertP;

                System.out.println("=====else=======nOfNPK==========" + nOfNPK);
                System.out.println("======else======pOfNPK==========" + pOfNPK);
                System.out.println("======else======kOfNPK==========" + kOfNPK);


                // Calculate the other fertilizer composition ration for N and K as the value of P is completed by the Complex Fertilizer
                // So there is no need to calculate for the Potassic Fertilizer

                // Required Value for Nitrogenous and Phosphatic Fertilizer
                complexFR.setRequirement(reqFertP);

                // Check if pOfNPK is negative
                if (nOfNPK < 0) {
                    System.out.println("============if======nOfNPK < 0===");
                    double reqFertN = (reqP / pRatioComplex) * 100;

                    nOfNPK = reqN - (nRatioComplex / 100) * reqFertN;
                    pOfNPK = reqP - (pRatioComplex / 100) * reqFertN;
                    kOfNPK = reqK - (kRatioComplex / 100) * reqFertN;

                    System.out.println("============nOfNPK==========" + nOfNPK);
                    System.out.println("============pOfNPK==========" + pOfNPK);
                    System.out.println("============kOfNPK==========" + kOfNPK);


                    complexFR.setRequirement(reqFertN);
                    // Calculate the value for P and K
                    System.out.println("============phosphaticFR==========" + calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    System.out.println("============potassicFR==========" + calculatePotassicFert(pOfNPK, potassicFertilizer));

                    phosphaticFR.setRequirement(calculatePhosphoricFert(pOfNPK, phosphaticFertilizer));
                    potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
                } else {
                    nitrogenousFR.setRequirement(calculateNitrogenousFert(nOfNPK, nitrogenusFertilizer));
                    potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));

                }
            }
        }
    }

    private void pOfNpkIncrease(Fertilizer potassicFertilizer, FertilizerResponse complexFR, FertilizerResponse nitrogenousFR, FertilizerResponse potassicFR, double reqN, double reqP, double reqK, double kRatioComplex, double nRatioComplex, double pRatioComplex, double v, double v2, double v3) {
        double pOfReqFert;
        double kOfReqFert;
        double nOfNPK;
        double pOfNPK;
        double kOfNPK;
        System.out.println("============if======pOfNPK < 0===");
        double reqFertP = (reqP / pRatioComplex) * 100;

        pOfReqFert = (v) * reqFertP;
        kOfReqFert = (kRatioComplex / 100) * reqFertP;

        System.out.println("=====pOfNPK=======pOfReqFert==========" + pOfReqFert);
        System.out.println("=====pOfNPK=======kOfReqFert==========" + kOfReqFert);

        nOfNPK = reqN - (nRatioComplex / 100) * reqFertP;
        pOfNPK = v2;
        kOfNPK = reqK - kOfReqFert;

        System.out.println("=====pOfNPK=======nOfNPK==========" + nOfNPK);
        System.out.println("====pOfNPK========pOfNPK==========" + pOfNPK);
        System.out.println("=====pOfNPK=======kOfNPK==========" + kOfNPK);

        complexFR.setRequirement(reqFertP);
        // Calculate the value for P and K
        nitrogenousFR.setRequirement(v3);
        potassicFR.setRequirement(calculatePotassicFert(kOfNPK, potassicFertilizer));
    }

    private double calculateNitrogenousFert(double exceedN, Fertilizer nitrogenusFertilizer) {
        System.out.println("=======exceedN====="+exceedN);
        System.out.println("nitrogenusFertilizer========="+nitrogenusFertilizer.getnRatio());
        return (exceedN/nitrogenusFertilizer.getnRatio()) * 100;
    }

    private double calculatePhosphoricFert(double exceedP, Fertilizer phosphaticFertilizer) {
        System.out.println("=======exceedP====="+exceedP);
        System.out.println("phosphaticFertilizer====="+phosphaticFertilizer.getpRatio());

        return (exceedP/phosphaticFertilizer.getpRatio()) * 100;
    }

    private double calculatePotassicFert(double exceedK, Fertilizer potassicFertilizer) {
        System.out.println("=======exceedK====="+exceedK);
        System.out.println("potassicFertilizer====="+potassicFertilizer.getkRatio());

        return (exceedK/potassicFertilizer.getkRatio()) * 100;
    }

    private static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    @Override
    public SoilTest storeSoilTest(SoilTest soilTest) {
        soilTest.setLastModifiedDate(new Date());
        SoilTest storedSoilTest = mongoTemplate.save(soilTest);
        // If the Soil Test is done for First Time then create SoilTestPayment
        System.out.println("=======storedSoilTest.getSoilTestPaymentId()======"+storedSoilTest.getSoilTestPaymentId());
        if (StringUtils.isEmpty(storedSoilTest.getSoilTestPaymentId())) {
            PaymentCycle currentPaymentCycle = paymentCycleService.getCurrentPaymentCycle();
            if (currentPaymentCycle == null) {
                logger.error("Payment Cycle is not created.");
                throw new ResourceNotFoundException("Please contact your Manager.");
            }
            System.out.println("======currentPaymentCycle.getId()==========="+currentPaymentCycle.getId());
            User agent = genericMongoTemplate.findById(storedSoilTest.getAgentId(), User.class);
            if (agent == null) {
                throw new ResourceNotFoundException("Agent not found");
            }
            FinancialDetails financialDetails = userBankService.getPrimaryBankDetails(agent.getId());

            if (financialDetails == null) {
                throw new ResourceNotFoundException("Please contact your Manager for adding your Bank Details.");
            }
            System.out.println("===================financialDetails=getId====="+financialDetails.getId());
            SoilTestPayment payment = new SoilTestPayment();
            payment.setPaymentCycle(currentPaymentCycle);
            payment.setSoilTest(storedSoilTest);
            payment.setAgent(agent);
            payment.setPaymentStatus(SoilTestPayment.PaymentStatus.Pending);
            payment.setStatus(SoilTestPayment.Status.Pending);
            payment.setKhasraNo(storedSoilTest.getUserCrop().getKhasraNo());

            // Find Khasra Location
            if (storedSoilTest.getUserCrop() == null) {
                throw new CustomException("User Crop not found");
            }
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(storedSoilTest.getUserCrop().getKhasraNo()
                    , storedSoilTest.getUserCrop().getUserId());

            if (userLandDetail == null) {
                throw new CustomException("Land Details not found");
            }
            System.out.println("===================userLandDetail=getId====="+userLandDetail.getId());

            // Add Location Fields
            Address address = new Address();
            address.setState(userLandDetail.getState());
            address.setCity(userLandDetail.getCity());
       //     address.setTehsil(userLandDetail.getTehsil()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
       //     address.setBlock(userLandDetail.getBlock());
      //      address.setVillageModel(userLandDetail.getVillageModel());

            Village village = address.getVillageModel();
            Block block = address.getBlock();
            Tehsil tehsil = address.getTehsil();
            City city = address.getCity();
            State state = address.getState();

            // Get Khasra Rate for Agents according to the location
            //TODO: Check if the Rate is set according to the Location
            Rate rate = null;

            if (village != null && rateService.getCurrentRateByLocation("VILLAGE", village.getId()) != null) {
                rate = rateService.getCurrentRateByLocation("VILLAGE", village.getId());
                payment.setKhasraRateLocationType(CommissionRate.LocationType.VILLAGE);
                System.out.println("=============VILLAGE======rate=getId====="+rate.getId());
            } else if (block != null && rateService.getCurrentRateByLocation("BLOCK", block.getId()) != null) {
                rate = rateService.getCurrentRateByLocation("BLOCK", block.getId());
                payment.setKhasraRateLocationType(CommissionRate.LocationType.BLOCK);
                System.out.println("===========BLOCK========rate=getId====="+rate.getId());
            } else if (tehsil != null && rateService.getCurrentRateByLocation("TEHSIL", tehsil.getId()) != null) {
                rate = rateService.getCurrentRateByLocation("TEHSIL", tehsil.getId());
                payment.setKhasraRateLocationType(CommissionRate.LocationType.TEHSIL);
                System.out.println("============TEHSIL=======rate=getId====="+rate.getId());
            } else if (city != null && rateService.getCurrentRateByLocation("DISTRICT", city.getId()) != null) {
                rate = rateService.getCurrentRateByLocation("DISTRICT", city.getId());
                payment.setKhasraRateLocationType(CommissionRate.LocationType.DISTRICT);
                System.out.println("============DISTRICT=======rate=getId====="+rate.getId());
            } else if (state != null && rateService.getCurrentRateByLocation("STATE", state.getId()) != null) {
                rate = rateService.getCurrentRateByLocation("STATE", state.getId());
                payment.setKhasraRateLocationType(CommissionRate.LocationType.STATE);
                System.out.println("===========STATE========rate=getId====="+rate.getId());
            }

            if (rate == null) {
                throw new ResourceNotFoundException("Khasra Rate is not added.");
            }

            if(soilTest.isSoilTestExist()) {
                payment.setPaymentType(SoilTestPayment.PaymentType.BasicDetailPayment);
                // get rate for filling basic details of farmers
                payment.setKhasraRate(rate.getBasicDetailsRate());
            } else {
                payment.setPaymentType(SoilTestPayment.PaymentType.SoilTestPayment);
                String agentId = storedSoilTest.getAgentId();
                String farmerId = storedSoilTest.getUserCrop().getUserId();
                Query query = new Query(Criteria.where("agentId").is(agentId).and("farmerId").is(farmerId));
                AgentFarmerPayment agentFarmerPayment = mongoTemplate.findOne(query, AgentFarmerPayment.class);
                if (agentFarmerPayment == null) {
                    System.out.println("=======NEW======");
                    agentFarmerPayment = new AgentFarmerPayment();
                    agentFarmerPayment.setAgentId(agentId);
                    agentFarmerPayment.setFarmerId(farmerId);
                    mongoTemplate.save(agentFarmerPayment);
                    payment.setKhasraRate(rate.getNewFarmerKhasraRate());
                } else {
                    System.out.println("=======OLD======");
                    payment.setKhasraRate(rate.getNormalKhasraRate());
                }
            }

            // Get Commission according to the location
            //TODO: Check if the Commission is set according to the Location
            BigDecimal commission = null;

            if (village != null && commissionService.getCurrentCommissionRateByLocation("VILLAGE", village.getId()) != null) {
                commission = commissionService.getCurrentCommissionRateByLocation("VILLAGE", village.getId()).getRate();
                payment.setCommissionRateLocationType(CommissionRate.LocationType.VILLAGE);
                System.out.println("===========VILLAGE========commission====="+commission);
            } else if (block != null && commissionService.getCurrentCommissionRateByLocation("BLOCK", block.getId()) != null) {
                commission = commissionService.getCurrentCommissionRateByLocation("BLOCK", block.getId()).getRate();
                payment.setCommissionRateLocationType(CommissionRate.LocationType.BLOCK);
                System.out.println("===========BLOCK========commission====="+commission);
            } else if (tehsil != null && commissionService.getCurrentCommissionRateByLocation("TEHSIL", tehsil.getId()) != null) {
                commission = commissionService.getCurrentCommissionRateByLocation("TEHSIL", tehsil.getId()).getRate();
                payment.setCommissionRateLocationType(CommissionRate.LocationType.TEHSIL);
                System.out.println("===========TEHSIL========commission====="+commission);
            } else if (city != null && commissionService.getCurrentCommissionRateByLocation("DISTRICT", city.getId()) != null) {
                commission = commissionService.getCurrentCommissionRateByLocation("DISTRICT", city.getId()).getRate();
                payment.setCommissionRateLocationType(CommissionRate.LocationType.DISTRICT);
                System.out.println("===========DISTRICT========commission====="+commission);
            } else if (state != null && commissionService.getCurrentCommissionRateByLocation("STATE", state.getId()) != null) {
                commission = commissionService.getCurrentCommissionRateByLocation("STATE", state.getId()).getRate();
                payment.setCommissionRateLocationType(CommissionRate.LocationType.STATE);
                System.out.println("===========STATE========commission====="+commission);
            }

            if (commission == null) {
                logger.info("Commission Rate is not defined for Location");
                throw new CustomException("Commission Rate is not defined for Location,Please contact your manager.");
            }
            payment.setCommissionRate(commission);

            payment.setAddress(address);
            payment.setManagerId(agent.getCreatedByUserId());
            SoilTestPayment soilTestPayment = mongoTemplate.save(payment);
            System.out.println("=======soilTestPayment.getId()=:::::::::==="+soilTestPayment.getId());

            // store/update Soil Test Analytics for Soil Test according to location
            analyticsService.storeSoilNutrientAnalytics(storedSoilTest.getId());

            // This is required to check if the soilTestPayment is stored for the soil test or not
            // If any exception occurs storedSoilTest is not Rolled Back.
            storedSoilTest.setSoilTestPaymentId(soilTestPayment.getId());
            storedSoilTest.setAddress(address);
            mongoTemplate.save(storedSoilTest);
        }
        return storedSoilTest;
    }

    @Override
    public SoilTest getSoilTest(String agentId, String userCropId) {
        System.out.println("=============agentId====="+agentId);
        System.out.println("=============userCropId====="+userCropId);
        UserCrop userCrop = genericMongoTemplate.findById(userCropId, UserCrop.class);
        if (userCrop == null) {
            throw new CustomException("User Crop not found.");
        }
        Query query = new Query(Criteria.where("agentId").is(agentId).and("userCrop.$id").is(new ObjectId(userCropId)));
        return mongoTemplate.findOne(query, SoilTest.class);
    }
}