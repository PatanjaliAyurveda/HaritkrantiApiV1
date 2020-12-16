package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.FieldSize;
import com.bharuwa.haritkranti.models.UserLandDetail;
import com.bharuwa.haritkranti.models.analyticsModels.*;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.payments.CommissionRate;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.service.AnalyticsService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bharuwa.haritkranti.models.analyticsModels.SoilNutrientDetail.NutrientType.Nitrogen;
import static com.bharuwa.haritkranti.models.payments.CommissionRate.LocationType.*;
import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;

/**
 * @author harman
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String storeSoilNutrientAnalyticsByLocation(String locationType, String locationId) {
        //gives list of soil test
        String searchLocationKey = getLocationKey(locationType);
        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)));
        query.fields().include("nValue").include("pValue").include("kValue").include("organicCarbon").include("pHValue");
        List<SoilTest> soilTestList = mongoTemplate.find(query,SoilTest.class);

        // Count factors for Nitrogen range
        int nCountVL = 0;
        int nCountL = 0;
        int nCountM = 0;
        int nCountMH = 0;
        int nCountH = 0;
        int nCountVH = 0;

        // Count factors for Phosphorus range
        int pCountVL = 0;
        int pCountL = 0;
        int pCountM = 0;
        int pCountMH = 0;
        int pCountH = 0;
        int pCountVH = 0;

        // Count factors for Potassium range
        int kCountVL = 0;
        int kCountL = 0;
        int kCountM = 0;
        int kCountMH = 0;
        int kCountH = 0;
        int kCountVH = 0;

        // Count factors for Organic Carbon range
        int ocCountVL = 0;
        int ocCountL = 0;
        int ocCountM = 0;
        int ocCountMH = 0;
        int ocCountH = 0;
        int ocCountVH = 0;

        // Count factors for pH range
        int pHcountAS = 0;
        int pHcountNS = 0;
        int pHcountALS = 0;
        int pHcountSAS = 0;

        int totalCount = 0;
        if(!soilTestList.isEmpty()) {

            // calculate Soil Nutrient factors count according to soil Test of particular location
            for (SoilTest soilTest : soilTestList) {
                // Nitrogen
                if (soilTest.getnValue() >= 50 && soilTest.getnValue() <= 140) {
                    nCountVL = nCountVL + 1;
                } else if (soilTest.getnValue() >= 141 && soilTest.getnValue() <= 280) {
                    nCountL = nCountL + 1;
                } else if (soilTest.getnValue() >= 281 && soilTest.getnValue() <= 420) {
                    nCountM = nCountM + 1;
                } else if (soilTest.getnValue() >= 421 && soilTest.getnValue() <= 560) {
                    nCountMH = nCountMH + 1;
                } else if (soilTest.getnValue() >= 561 && soilTest.getnValue() <= 700) {
                    nCountH = nCountH + 1;
                } else if (soilTest.getnValue() > 700) {
                    nCountVH = nCountVH + 1;
                }

                // Phosphorus
                if (soilTest.getpValue() <= 5) {
                    pCountVL = pCountVL + 1;
                } else if (soilTest.getpValue() >= 6 && soilTest.getpValue() <= 10) {
                    pCountL = pCountL + 1;
                } else if (soilTest.getpValue() >= 11 && soilTest.getpValue() <= 17) {
                    pCountM = pCountM + 1;
                } else if (soilTest.getpValue() >= 18 && soilTest.getpValue() <= 25) {
                    pCountMH = pCountMH + 1;
                } else if (soilTest.getpValue() >= 26 && soilTest.getpValue() <= 35) {
                    pCountH = pCountH + 1;
                } else if (soilTest.getpValue() > 35) {
                    pCountVH = pCountVH + 1;
                }

                // Potassium
                if (soilTest.getkValue() <= 100) {
                    kCountVL = kCountVL + 1;
                } else if (soilTest.getkValue() >= 101 && soilTest.getkValue() <= 150) {
                    kCountL = kCountL + 1;
                } else if (soilTest.getkValue() >= 151 && soilTest.getkValue() <= 200) {
                    kCountM = kCountM + 1;
                } else if (soilTest.getkValue() >= 201 && soilTest.getkValue() <= 250) {
                    kCountMH = kCountMH + 1;
                } else if (soilTest.getkValue() >= 251 && soilTest.getkValue() <= 300) {
                    kCountH = kCountH + 1;
                } else if (soilTest.getkValue() > 300) {
                    kCountVH = kCountVH + 1;
                }

                // Organic Carbon
                if (soilTest.getOrganicCarbon() <= 0.30) {
                    ocCountVL = ocCountVL + 1;
                } else if (soilTest.getOrganicCarbon() >= 0.31 && soilTest.getOrganicCarbon() <= 0.50) {
                    ocCountL = ocCountL + 1;
                } else if (soilTest.getOrganicCarbon() >= 0.51 && soilTest.getOrganicCarbon() <= 0.75) {
                    ocCountM = ocCountM + 1;
                } else if (soilTest.getOrganicCarbon() >= 0.76 && soilTest.getOrganicCarbon() <= 1.0) {
                    ocCountMH = ocCountMH + 1;
                } else if (soilTest.getOrganicCarbon() >= 1.1 && soilTest.getOrganicCarbon() <= 1.3) {
                    ocCountH = ocCountH + 1;
                } else if (soilTest.getOrganicCarbon() > 1.3) {
                    ocCountVH = ocCountVH + 1;
                }

                //pH
                if (soilTest.getpHValue() <= 6.4) {
                    pHcountAS = pHcountAS + 1;
                } else if (soilTest.getpHValue() >= 6.5 && soilTest.getpHValue() <= 7.5) {
                    pHcountNS = pHcountNS + 1;
                } else if (soilTest.getpHValue() >= 7.6 && soilTest.getpHValue() <= 8.5) {
                    pHcountALS = pHcountALS + 1;
                } else if (soilTest.getpHValue() >= 8.60) {
                    pHcountSAS = pHcountSAS + 1;
                }
                totalCount = totalCount + 1;
            }

            SoilTestAnalytics soilTestAnalytics = checkSoilTestAnalytisExist(locationId, locationType);
            SoilNutrientDetail nitrogenDetail;
            SoilNutrientDetail phosphorusDetail;
            SoilNutrientDetail potassiumDetail;
            SoilNutrientDetail organicCarbonDetail;
            PHDetail phDetail;

            if (soilTestAnalytics == null) {
                System.out.println("==================null===============");
                soilTestAnalytics = new SoilTestAnalytics();
                nitrogenDetail = new SoilNutrientDetail();
                phosphorusDetail = new SoilNutrientDetail();
                potassiumDetail = new SoilNutrientDetail();
                organicCarbonDetail = new SoilNutrientDetail();
                phDetail = new PHDetail();

                // store location of soil test By LocationType and LocationId
                // get address by location
                Address address = getAddressByLocationTypeAndLocationId(locationType, locationId);
                soilTestAnalytics.setAddress(address);
                soilTestAnalytics.setLocationType(CommissionRate.LocationType.valueOf(locationType));

            } else {
                System.out.println("==================null===============");
                nitrogenDetail = soilTestAnalytics.getNitrogenDetail();
                phosphorusDetail = soilTestAnalytics.getPhosphorusDetail();
                potassiumDetail = soilTestAnalytics.getPotassiumDetail();
                organicCarbonDetail = soilTestAnalytics.getOrganicCarbonDetail();
                phDetail = soilTestAnalytics.getpHDetail();
            }

            // Nitogen
            nitrogenDetail.setNutrientType(Nitrogen);
            nitrogenDetail.setVeryLow(nCountVL);
            nitrogenDetail.setLow(nCountL);
            nitrogenDetail.setModerate(nCountM);
            nitrogenDetail.setModerateHigh(nCountMH);
            nitrogenDetail.setHigh(nCountH);
            nitrogenDetail.setVeryHigh(nCountVH);
            nitrogenDetail.setTotalCount(totalCount);

            nitrogenDetail.setVeryLowPercentage(getPercentage(nCountVL, totalCount));
            nitrogenDetail.setLowPercentage(getPercentage(nCountL, totalCount));
            nitrogenDetail.setModeratePercentage(getPercentage(nCountM, totalCount));
            nitrogenDetail.setModerateHighPercentage(getPercentage(nCountMH, totalCount));
            nitrogenDetail.setHighPercentage(getPercentage(nCountH, totalCount));
            nitrogenDetail.setVeryHighPercentage(getPercentage(nCountVH, totalCount));

            // calculate Rating
            Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntry = calculateRating(nCountVL, nCountL, nCountM, nCountMH, nCountH, nCountVH);

            nitrogenDetail.setRating(maxEntry.getKey());
            nitrogenDetail.setRatingValue(maxEntry.getValue());

            soilTestAnalytics.setNitrogenDetail(nitrogenDetail);


            // Phosphorus
            phosphorusDetail.setNutrientType(SoilNutrientDetail.NutrientType.Phosphorus);
            phosphorusDetail.setVeryLow(pCountVL);
            phosphorusDetail.setLow(pCountL);
            phosphorusDetail.setModerate(pCountM);
            phosphorusDetail.setModerateHigh(pCountMH);
            phosphorusDetail.setHigh(pCountH);
            phosphorusDetail.setVeryHigh(pCountVH);
            phosphorusDetail.setTotalCount(totalCount);


            phosphorusDetail.setVeryLowPercentage(getPercentage(pCountVL, totalCount));
            phosphorusDetail.setLowPercentage(getPercentage(pCountL, totalCount));
            phosphorusDetail.setModeratePercentage(getPercentage(pCountM, totalCount));
            phosphorusDetail.setModerateHighPercentage(getPercentage(pCountMH, totalCount));
            phosphorusDetail.setHighPercentage(getPercentage(pCountH, totalCount));
            phosphorusDetail.setVeryHighPercentage(getPercentage(pCountVH, totalCount));

            // calculate Rating
            Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryPhosphorus = calculateRating(pCountVL, pCountL, pCountM, pCountMH, pCountH, pCountVH);

            phosphorusDetail.setRating(maxEntryPhosphorus.getKey());
            phosphorusDetail.setRatingValue(maxEntryPhosphorus.getValue());

            soilTestAnalytics.setPhosphorusDetail(phosphorusDetail);

            // Potassium
            potassiumDetail.setNutrientType(SoilNutrientDetail.NutrientType.Potassium);
            potassiumDetail.setVeryLow(kCountVL);
            potassiumDetail.setLow(kCountL);
            potassiumDetail.setModerate(kCountM);
            potassiumDetail.setModerateHigh(kCountMH);
            potassiumDetail.setHigh(kCountH);
            potassiumDetail.setVeryHigh(kCountVH);
            potassiumDetail.setTotalCount(totalCount);


            potassiumDetail.setVeryLowPercentage(getPercentage(kCountVL, totalCount));
            potassiumDetail.setLowPercentage(getPercentage(kCountL, totalCount));
            potassiumDetail.setModeratePercentage(getPercentage(kCountM, totalCount));
            potassiumDetail.setModerateHighPercentage(getPercentage(kCountMH, totalCount));
            potassiumDetail.setHighPercentage(getPercentage(kCountH, totalCount));
            potassiumDetail.setVeryHighPercentage(getPercentage(kCountVH, totalCount));

            // calculate Rating
            Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryPotassium = calculateRating(kCountVL, kCountL, kCountM, kCountMH, kCountH, kCountVH);

            potassiumDetail.setRating(maxEntryPotassium.getKey());
            potassiumDetail.setRatingValue(maxEntryPotassium.getValue());

            soilTestAnalytics.setPotassiumDetail(potassiumDetail);


            // Organic Carbon
            organicCarbonDetail.setNutrientType(SoilNutrientDetail.NutrientType.Organic_Carbon);
            organicCarbonDetail.setVeryLow(ocCountVL);
            organicCarbonDetail.setLow(ocCountL);
            organicCarbonDetail.setModerate(ocCountM);
            organicCarbonDetail.setModerateHigh(ocCountMH);
            organicCarbonDetail.setHigh(ocCountH);
            organicCarbonDetail.setVeryHigh(ocCountVH);
            organicCarbonDetail.setTotalCount(totalCount);


            organicCarbonDetail.setVeryLowPercentage(getPercentage(ocCountVL, totalCount));
            organicCarbonDetail.setLowPercentage(getPercentage(ocCountL, totalCount));
            organicCarbonDetail.setModeratePercentage(getPercentage(ocCountM, totalCount));
            organicCarbonDetail.setModerateHighPercentage(getPercentage(ocCountMH, totalCount));
            organicCarbonDetail.setHighPercentage(getPercentage(ocCountH, totalCount));
            organicCarbonDetail.setVeryHighPercentage(getPercentage(ocCountVH, totalCount));

            // calculate Rating
            Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryOrganicCarbon = calculateRating(ocCountVL, ocCountL, ocCountM, ocCountMH, ocCountH, ocCountVH);

            organicCarbonDetail.setRating(maxEntryOrganicCarbon.getKey());
            organicCarbonDetail.setRatingValue(maxEntryOrganicCarbon.getValue());

            soilTestAnalytics.setOrganicCarbonDetail(organicCarbonDetail);
            //pH
            phDetail.setNutrientType(SoilNutrientDetail.NutrientType.pH);
            phDetail.setAcidicSoil(pHcountAS);
            phDetail.setNeutralSoil(pHcountNS);
            phDetail.setAlkalineSoil(pHcountALS);
            phDetail.setStronglyAlkaline(pHcountSAS);
            phDetail.setTotalCount(totalCount);


            phDetail.setAcidicSoilPercentage(getPercentage(pHcountAS, totalCount));
            phDetail.setNeutralSoilPercentage(getPercentage(pHcountNS, totalCount));
            phDetail.setAlkalineSoilPercentage(getPercentage(pHcountALS, totalCount));
            phDetail.setStronglyAlkalinePercentage(getPercentage(pHcountSAS, totalCount));

            // calculate Rating
            HashMap<SoilNutrientDetail.Rating, Integer> map = new HashMap<>(); //hash map for storing value of count
            map.put(SoilNutrientDetail.Rating.AS, pHcountAS);
            map.put(SoilNutrientDetail.Rating.NS, pHcountNS);
            map.put(SoilNutrientDetail.Rating.ALS, pHcountALS);
            map.put(SoilNutrientDetail.Rating.SAS, pHcountSAS);

            Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntrypH = null; //  max value in the Hashmap
            for (Map.Entry<SoilNutrientDetail.Rating, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                if (maxEntrypH == null || entry.getValue().compareTo(maxEntrypH.getValue()) >= 0) {
                    maxEntrypH = entry;
                }
            }

            phDetail.setRating(maxEntrypH.getKey());
            phDetail.setRatingValue(maxEntrypH.getValue());

            soilTestAnalytics.setpHDetail(phDetail);

            mongoTemplate.save(soilTestAnalytics);
        }
        return "success";
    }

    private Address getAddressByLocationTypeAndLocationId(String locationType, String locationId) {
        Address address = new Address();
        switch (locationType) {
            case "STATE":
                State state = genericMongoTemplate.findById(locationId, State.class);
                address.setState(state);
                break;
            case "DISTRICT":
                City city = genericMongoTemplate.findById(locationId, City.class);
                address.setState(city.getState());
                address.setCity(city);
                break;
            case "TEHSIL":
                Tehsil tehsil = genericMongoTemplate.findById(locationId, Tehsil.class);
                address.setState(tehsil.getState());
                address.setCity(tehsil.getCity());
                address.setTehsil(tehsil);
                break;
            case "BLOCK":
                Block block = genericMongoTemplate.findById(locationId, Block.class);
                address.setState(block.getState());
                address.setCity(block.getCity());
                address.setTehsil(block.getTehsil());
                address.setBlock(block);
                break;
            case "VILLAGE":
                Village village = genericMongoTemplate.findById(locationId, Village.class);
                address.setState(village.getState());
                address.setCity(village.getCity());
                address.setTehsil(village.getTehsil());
                address.setBlock(village.getBlock());
                address.setVillageModel(village);
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return address;
    }

    private SoilTestAnalytics checkSoilTestAnalytisExist(String locationId, String locationType) {
        String searchLocationKey = getLocationKey(locationType);
        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)).and("locationType").is(locationType));
        return mongoTemplate.findOne(query, SoilTestAnalytics.class);
    }

    private BigDecimal getPercentage(double count, double totalCount) {
        return BigDecimal.valueOf(count / totalCount).multiply(BigDecimal.valueOf(100));
    }

    private Map.Entry<SoilNutrientDetail.Rating, Integer> calculateRating(int countVL, int countL, int countM, int countMH, int countH, int countVH) {
        // calculate Rating
        HashMap<SoilNutrientDetail.Rating, Integer> map = new HashMap<>(); //hash map for storing value of count
        map.put(SoilNutrientDetail.Rating.VL, countVL);
        map.put(SoilNutrientDetail.Rating.L, countL);
        map.put(SoilNutrientDetail.Rating.M, countM);
        map.put(SoilNutrientDetail.Rating.MH, countMH);
        map.put(SoilNutrientDetail.Rating.H, countH);
        map.put(SoilNutrientDetail.Rating.VH, countVH);

        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntry = null; //  max value in the Hashmap
        for (Map.Entry<SoilNutrientDetail.Rating, Integer> entry : map.entrySet()) {  // Itrate through hashmap
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    @Override
    public SoilTestAnalytics storeSoilNutrientAnalytics(String soilTestId) {
        //get soil test
        SoilTest soilTest = genericMongoTemplate.findById(soilTestId, SoilTest.class);
        if(soilTest == null){
            throw new ResourceNotFoundException("Soil Test Not Found");
        }

        SoilTestAnalytics soilTestAnalytics = checkSoilTestAnalytisExist(soilTest.getAddress().getVillageModel().getId(), "VILLAGE");
        SoilNutrientDetail nitrogenDetail;
        SoilNutrientDetail phosphorusDetail;
        SoilNutrientDetail potassiumDetail;
        SoilNutrientDetail organicCarbonDetail;
        PHDetail phDetail;

        if (soilTestAnalytics == null) {
            soilTestAnalytics = new SoilTestAnalytics();
            nitrogenDetail = new SoilNutrientDetail();
            phosphorusDetail = new SoilNutrientDetail();
            potassiumDetail = new SoilNutrientDetail();
            organicCarbonDetail = new SoilNutrientDetail();
            phDetail = new PHDetail();

            soilTestAnalytics.setLocationType(CommissionRate.LocationType.VILLAGE);
            // store address according to SOIL TEST Location
            soilTestAnalytics.setAddress(soilTest.getAddress());

        } else {
            nitrogenDetail = soilTestAnalytics.getNitrogenDetail();
            phosphorusDetail = soilTestAnalytics.getPhosphorusDetail();
            potassiumDetail = soilTestAnalytics.getPotassiumDetail();
            organicCarbonDetail = soilTestAnalytics.getOrganicCarbonDetail();
            phDetail = soilTestAnalytics.getpHDetail();
        }

        // Nitogen
        // Count factors for Nitrogen range
        int nCountVL = nitrogenDetail.getVeryLow();
        int nCountL = nitrogenDetail.getLow();
        int nCountM = nitrogenDetail.getModerate();
        int nCountMH = nitrogenDetail.getModerateHigh();
        int nCountH = nitrogenDetail.getHigh();
        int nCountVH = nitrogenDetail.getVeryHigh();
        int nTotalCount = nitrogenDetail.getTotalCount() + 1;

        if (soilTest.getnValue() >= 50 && soilTest.getnValue() <= 140) {
            nitrogenDetail.setVeryLow(nitrogenDetail.getVeryLow() + 1);
            nCountVL = nCountVL + 1;
        } else if (soilTest.getnValue() >= 141 && soilTest.getnValue() <= 280) {
            nCountL = nCountL + 1;
        } else if (soilTest.getnValue() >= 281 && soilTest.getnValue() <= 420) {
            nCountM = nCountM + 1;
        } else if (soilTest.getnValue() >= 421 && soilTest.getnValue() <= 560) {
            nCountMH = nCountMH + 1;
        } else if (soilTest.getnValue() >= 561 && soilTest.getnValue() <= 700) {
            nCountH = nCountH + 1;
        } else if (soilTest.getnValue() > 700) {
            nCountVH = nCountVH + 1;
        }

        nitrogenDetail.setNutrientType(Nitrogen);
        nitrogenDetail.setVeryLow(nCountVL);
        nitrogenDetail.setLow(nCountL);
        nitrogenDetail.setModerate(nCountM);
        nitrogenDetail.setModerateHigh(nCountMH);
        nitrogenDetail.setHigh(nCountH);
        nitrogenDetail.setVeryHigh(nCountVH);
        nitrogenDetail.setTotalCount(nTotalCount);

        nitrogenDetail.setVeryLowPercentage(getPercentage(nCountVL, nTotalCount));
        nitrogenDetail.setLowPercentage(getPercentage(nCountL, nTotalCount));
        nitrogenDetail.setModeratePercentage(getPercentage(nCountM, nTotalCount));
        nitrogenDetail.setModerateHighPercentage(getPercentage(nCountMH, nTotalCount));
        nitrogenDetail.setHighPercentage(getPercentage(nCountH, nTotalCount));
        nitrogenDetail.setVeryHighPercentage(getPercentage(nCountVH, nTotalCount));

        // calculate Rating
        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntry = calculateRating(nCountVL, nCountL, nCountM, nCountMH, nCountH, nCountVH);

        nitrogenDetail.setRating(maxEntry.getKey());
        nitrogenDetail.setRatingValue(maxEntry.getValue());

        soilTestAnalytics.setNitrogenDetail(nitrogenDetail);

        // Phosphorus
        // Count factors for Phosphorus range
        int pCountVL = phosphorusDetail.getVeryLow();
        int pCountL = phosphorusDetail.getLow();
        int pCountM = phosphorusDetail.getModerate();
        int pCountMH = phosphorusDetail.getModerateHigh();
        int pCountH = phosphorusDetail.getHigh();
        int pCountVH = phosphorusDetail.getVeryHigh();
        int pTotalCount = phosphorusDetail.getTotalCount() + 1;

        if (soilTest.getpValue() <= 5) {
            pCountVL = pCountVL + 1;
        } else if (soilTest.getpValue() >= 6 && soilTest.getpValue() <= 10) {
            pCountL = pCountL + 1;
        } else if (soilTest.getpValue() >= 11 && soilTest.getpValue() <= 17) {
            pCountM = pCountM + 1;
        } else if (soilTest.getpValue() >= 18 && soilTest.getpValue() <= 25) {
            pCountMH = pCountMH + 1;
        } else if (soilTest.getpValue() >= 26 && soilTest.getpValue() <= 35) {
            pCountH = pCountH + 1;
        } else if (soilTest.getpValue() > 35) {
            pCountVH = pCountVH + 1;
        }

        phosphorusDetail.setNutrientType(SoilNutrientDetail.NutrientType.Phosphorus);
        phosphorusDetail.setVeryLow(pCountVL);
        phosphorusDetail.setLow(pCountL);
        phosphorusDetail.setModerate(pCountM);
        phosphorusDetail.setModerateHigh(pCountMH);
        phosphorusDetail.setHigh(pCountH);
        phosphorusDetail.setVeryHigh(pCountVH);
        phosphorusDetail.setTotalCount(pTotalCount);

        phosphorusDetail.setVeryLowPercentage(getPercentage(pCountVL, pTotalCount));
        phosphorusDetail.setLowPercentage(getPercentage(pCountL, pTotalCount));
        phosphorusDetail.setModeratePercentage(getPercentage(pCountM, pTotalCount));
        phosphorusDetail.setModerateHighPercentage(getPercentage(pCountMH, pTotalCount));
        phosphorusDetail.setHighPercentage(getPercentage(pCountH, pTotalCount));
        phosphorusDetail.setVeryHighPercentage(getPercentage(pCountVH, pTotalCount));

        // calculate Rating
        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryPhosphorus = calculateRating(pCountVL, pCountL, pCountM, pCountMH, pCountH, pCountVH);

        phosphorusDetail.setRating(maxEntryPhosphorus.getKey());
        phosphorusDetail.setRatingValue(maxEntryPhosphorus.getValue());

        soilTestAnalytics.setPhosphorusDetail(phosphorusDetail);

        // Potassium
        // Count factors for Potassium range
        int kCountVL = potassiumDetail.getVeryLow();
        int kCountL = potassiumDetail.getLow();
        int kCountM = potassiumDetail.getModerate();
        int kCountMH = potassiumDetail.getModerateHigh();
        int kCountH = potassiumDetail.getHigh();
        int kCountVH = potassiumDetail.getVeryHigh();
        int kTotalCount = potassiumDetail.getTotalCount() + 1;

        if (soilTest.getkValue() <= 100) {
            kCountVL = kCountVL + 1;
        } else if (soilTest.getkValue() >= 101 && soilTest.getkValue() <= 150) {
            kCountL = kCountL + 1;
        } else if (soilTest.getkValue() >= 151 && soilTest.getkValue() <= 200) {
            kCountM = kCountM + 1;
        } else if (soilTest.getkValue() >= 201 && soilTest.getkValue() <= 250) {
            kCountMH = kCountMH + 1;
        } else if (soilTest.getkValue() >= 251 && soilTest.getkValue() <= 300) {
            kCountH = kCountH + 1;
        } else if (soilTest.getkValue() > 300) {
            kCountVH = kCountVH + 1;
        }

        potassiumDetail.setNutrientType(SoilNutrientDetail.NutrientType.Potassium);
        potassiumDetail.setVeryLow(kCountVL);
        potassiumDetail.setLow(kCountL);
        potassiumDetail.setModerate(kCountM);
        potassiumDetail.setModerateHigh(kCountMH);
        potassiumDetail.setHigh(kCountH);
        potassiumDetail.setVeryHigh(kCountVH);
        potassiumDetail.setTotalCount(kTotalCount);

        potassiumDetail.setVeryLowPercentage(getPercentage(kCountVL, kTotalCount));
        potassiumDetail.setLowPercentage(getPercentage(kCountL, kTotalCount));
        potassiumDetail.setModeratePercentage(getPercentage(kCountM, kTotalCount));
        potassiumDetail.setModerateHighPercentage(getPercentage(kCountMH, kTotalCount));
        potassiumDetail.setHighPercentage(getPercentage(kCountH, kTotalCount));
        potassiumDetail.setVeryHighPercentage(getPercentage(kCountVH, kTotalCount));

        // calculate Rating
        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryPotassium = calculateRating(kCountVL, kCountL, kCountM, kCountMH, kCountH, kCountVH);

        potassiumDetail.setRating(maxEntryPotassium.getKey());
        potassiumDetail.setRatingValue(maxEntryPotassium.getValue());

        soilTestAnalytics.setPotassiumDetail(potassiumDetail);


        // Organic Carbon
        // Count factors for Organic Carbon range
        int ocCountVL = organicCarbonDetail.getVeryLow();
        int ocCountL = organicCarbonDetail.getLow();
        int ocCountM = organicCarbonDetail.getModerate();
        int ocCountMH = organicCarbonDetail.getModerateHigh();
        int ocCountH = organicCarbonDetail.getHigh();
        int ocCountVH = organicCarbonDetail.getVeryHigh();
        int ocTotalCount = organicCarbonDetail.getTotalCount() + 1;

        if (soilTest.getOrganicCarbon() <= 0.30) {
            ocCountVL = ocCountVL + 1;
        } else if (soilTest.getOrganicCarbon() >= 0.31 && soilTest.getOrganicCarbon() <= 0.50) {
            ocCountL = ocCountL + 1;
        } else if (soilTest.getOrganicCarbon() >= 0.51 && soilTest.getOrganicCarbon() <= 0.75) {
            ocCountM = ocCountM + 1;
        } else if (soilTest.getOrganicCarbon() >= 0.76 && soilTest.getOrganicCarbon() <= 1.0) {
            ocCountMH = ocCountMH + 1;
        } else if (soilTest.getOrganicCarbon() >= 1.1 && soilTest.getOrganicCarbon() <= 1.3) {
            ocCountH = ocCountH + 1;
        } else if (soilTest.getOrganicCarbon() > 1.3) {
            ocCountVH = ocCountVH + 1;
        }

        organicCarbonDetail.setNutrientType(SoilNutrientDetail.NutrientType.Organic_Carbon);
        organicCarbonDetail.setVeryLow(ocCountVL);
        organicCarbonDetail.setLow(ocCountL);
        organicCarbonDetail.setModerate(ocCountM);
        organicCarbonDetail.setModerateHigh(ocCountMH);
        organicCarbonDetail.setHigh(ocCountH);
        organicCarbonDetail.setVeryHigh(ocCountVH);
        organicCarbonDetail.setTotalCount(ocTotalCount);

        organicCarbonDetail.setVeryLowPercentage(getPercentage(ocCountVL, ocTotalCount));
        organicCarbonDetail.setLowPercentage(getPercentage(ocCountL, ocTotalCount));
        organicCarbonDetail.setModeratePercentage(getPercentage(ocCountM, ocTotalCount));
        organicCarbonDetail.setModerateHighPercentage(getPercentage(ocCountMH, ocTotalCount));
        organicCarbonDetail.setHighPercentage(getPercentage(ocCountH, ocTotalCount));
        organicCarbonDetail.setVeryHighPercentage(getPercentage(ocCountVH, ocTotalCount));

        // calculate Rating
        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntryOrganicCarbon = calculateRating(ocCountVL, ocCountL, ocCountM, ocCountMH, ocCountH, ocCountVH);

        organicCarbonDetail.setRating(maxEntryOrganicCarbon.getKey());
        organicCarbonDetail.setRatingValue(maxEntryOrganicCarbon.getValue());

        soilTestAnalytics.setOrganicCarbonDetail(organicCarbonDetail);

        //pH
        // Count factors for pH range
        int pHcountAS = phDetail.getAcidicSoil();
        int pHcountNS = phDetail.getNeutralSoil();
        int pHcountALS = phDetail.getAlkalineSoil();
        int pHcountSAS = phDetail.getStronglyAlkaline();
        int pHTotalCount = phDetail.getTotalCount() + 1;

        if (soilTest.getpHValue() <= 6.4) {
            pHcountAS = pHcountAS + 1;
        } else if (soilTest.getpHValue() >= 6.5 && soilTest.getpHValue() <= 7.5) {
            pHcountNS = pHcountNS + 1;
        } else if (soilTest.getpHValue() >= 7.6 && soilTest.getpHValue() <= 8.5) {
            pHcountALS = pHcountALS + 1;
        } else if (soilTest.getpHValue() >= 8.60) {
            pHcountSAS = pHcountSAS + 1;
        }

        phDetail.setNutrientType(SoilNutrientDetail.NutrientType.pH);
        phDetail.setAcidicSoil(pHcountAS);
        phDetail.setNeutralSoil(pHcountNS);
        phDetail.setAlkalineSoil(pHcountALS);
        phDetail.setStronglyAlkaline(pHcountSAS);
        phDetail.setTotalCount(pHTotalCount);

        phDetail.setAcidicSoilPercentage(getPercentage(pHcountAS, pHTotalCount));
        phDetail.setNeutralSoilPercentage(getPercentage(pHcountNS, pHTotalCount));
        phDetail.setAlkalineSoilPercentage(getPercentage(pHcountALS, pHTotalCount));
        phDetail.setStronglyAlkalinePercentage(getPercentage(pHcountSAS, pHTotalCount));

        // calculate Rating
        HashMap<SoilNutrientDetail.Rating, Integer> map = new HashMap<>(); //hash map for storing value of count
        map.put(SoilNutrientDetail.Rating.AS, pHcountAS);
        map.put(SoilNutrientDetail.Rating.NS, pHcountNS);
        map.put(SoilNutrientDetail.Rating.ALS, pHcountALS);
        map.put(SoilNutrientDetail.Rating.SAS, pHcountSAS);

        Map.Entry<SoilNutrientDetail.Rating, Integer> maxEntrypH = null; //  max value in the Hashmap
        for (Map.Entry<SoilNutrientDetail.Rating, Integer> entry : map.entrySet()) {  // Itrate through hashmap
            if (maxEntrypH == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0) {
                maxEntrypH = entry;
            }
        }

        phDetail.setRating(maxEntrypH.getKey());
        phDetail.setRatingValue(maxEntrypH.getValue());
        soilTestAnalytics.setpHDetail(phDetail);

        return mongoTemplate.save(soilTestAnalytics);
    }

    /**
     * get location key and Query Criteria for Analytics according to locationType
     * @param locationType
     * @return
     */
    public static Criteria getQueryCriteriaForAnalytics(String locationType, String locationId) {
        Criteria criteria = new Criteria();

        switch (locationType) {
            case "COUNTRY":
                criteria.and("locationType").is("STATE");
                break;
            case "STATE":
                criteria.and("address.state.$id").is(new ObjectId(locationId)).and("locationType").is(DISTRICT);
                break;
            case "DISTRICT":
                criteria.and("address.city.$id").is(new ObjectId(locationId)).and("locationType").is(TEHSIL);
                break;
            case "TEHSIL":
                criteria.and("address.tehsil.$id").is(new ObjectId(locationId)).and("locationType").is(BLOCK);
                break;
            case "BLOCK":
                criteria.and("address.block.$id").is(new ObjectId(locationId)).and("locationType").is(VILLAGE);
                break;
            case "VILLAGE":
                criteria.and("address.villageModel.$id").is(new ObjectId(locationId)).and("locationType").is(VILLAGE);
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return criteria;
    }

    @Override
    public String storeSoilNutrientAnalyticsAll(String locationType) {
        // store SoilTestAnalytics according to locationType
        if(locationType.equals(CommissionRate.LocationType.STATE.toString())){
            List<State> stateList = mongoTemplate.findAll(State.class);
            for (State state : stateList){
                storeSoilNutrientAnalyticsByLocation(locationType,state.getId());
            }
        } else if(locationType.equals(DISTRICT.toString())){
            List<City> cityList = mongoTemplate.findAll(City.class);
            for (City city : cityList){
                storeSoilNutrientAnalyticsByLocation(locationType,city.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.TEHSIL.toString())){
            List<Tehsil> tehsilList = mongoTemplate.findAll(Tehsil.class);
            for (Tehsil tehsil : tehsilList){
                storeSoilNutrientAnalyticsByLocation(locationType,tehsil.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.BLOCK.toString())){
            List<Block> blocks = mongoTemplate.findAll(Block.class);
            for (Block block : blocks){
                storeSoilNutrientAnalyticsByLocation(locationType,block.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.VILLAGE.toString())){
            List<Village> villageList = mongoTemplate.findAll(Village.class);
            for (Village village : villageList){
                storeSoilNutrientAnalyticsByLocation(locationType,village.getId());
            }
        }

        return "Success";
    }

    @Override
    public List<SoilTestAnalytics> getSoilNutrientAnalyticsByNutrientTypeAndLocation(String nutrientType, String locationType, String locationId) {

        Criteria  locationKeyCriteria = getQueryCriteriaForAnalytics(locationType,locationId);

        Query query = new Query(locationKeyCriteria);
        // to add Soil Nutrient Detail in Response according to Nutrient Type
        switch (SoilNutrientDetail.NutrientType.valueOf(nutrientType)) {
            case Nitrogen:
                query.fields().include("nitrogenDetail");
                break;
            case Phosphorus:
                query.fields().include("phosphorusDetail");
                break;
            case Potassium:
                query.fields().include("potassiumDetail");
                break;
            case Organic_Carbon:
                query.fields().include("organicCarbonDetail");
                break;
            case pH:
                query.fields().include("pHDetail");
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        query.fields().include("address").include("locationType");
        return mongoTemplate.find(query,SoilTestAnalytics.class);
    }

    @Override
    public UserLandAnalytics storeUserLandAnalytics(String userLandDetailId) {

        // check if land detail exist
        UserLandDetail userLandDetail = genericMongoTemplate.findById(userLandDetailId, UserLandDetail.class);
        if (userLandDetail == null){
            throw new ResourceNotFoundException("Land Detail not found");
        }

        // check if LandTypeAnalyticsstoreUserLandAnalytics already exist or not
   //     UserLandAnalytics userLandAnalytics = checkLandTypeAnalyticsExist("VILLAGE", userLandDetail.getVillageModel().getId());
        UserLandAnalytics userLandAnalytics = null;
        LandTypeAnalytics landTypeAnalytics;
        KhasraOwnershipAnalytics khasraOwnershipAnalytics;

        if (userLandAnalytics == null) {
            userLandAnalytics = new UserLandAnalytics();
            landTypeAnalytics = new LandTypeAnalytics();
            khasraOwnershipAnalytics = new KhasraOwnershipAnalytics();
            // store address from UserLandDetail By LocationType and LocationId
 //           Address address = getAddressByLocationTypeAndLocationId("VILLAGE", userLandDetail.getVillageModel().getId());
 //           userLandAnalytics.setAddress(address);
            userLandAnalytics.setLocationType(CommissionRate.LocationType.VILLAGE);
        } else {
            // set values if already exist
            landTypeAnalytics = userLandAnalytics.getLandTypeAnalytics();
            khasraOwnershipAnalytics = userLandAnalytics.getKhasraOwnershipAnalytics();
        }


        // count factors for khasras Land Type(irrigated,semi-irrigated and rainfed) for particuler village
        int irrigatedKhasraCount = landTypeAnalytics.getIrrigatedKhasraCount();
        int semiIrrigatedKhasraCount = landTypeAnalytics.getSemiIrrigatedKhasraCount();
        int rainfedKhasraCount = landTypeAnalytics.getRainfedKhasraCount();
        int totalKhasraCount = landTypeAnalytics.getTotalKhasraCount();

        // land type size factors
        BigDecimal totalLandSize = landTypeAnalytics.getTotalLandSize();
        BigDecimal irrigatedLandSize = landTypeAnalytics.getIrrigatedLandSize();
        BigDecimal semiIrrigatedLandSize = landTypeAnalytics.getSemiIrrigatedLandSize();
        BigDecimal rainfedLandSize = landTypeAnalytics.getRainfedLandSize();

        BigDecimal convertedlandSize = userLandDetail.getLandSize();
        // convert land size to Acre (in case Hectare is used)
        if (userLandDetail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
            convertedlandSize = convertedlandSize.multiply(BigDecimal.valueOf(2.5));
        }

        switch (userLandDetail.getFarmType()) {
            case Irrigated:
                irrigatedKhasraCount = irrigatedKhasraCount + 1;
                irrigatedLandSize = irrigatedLandSize.add(convertedlandSize);
                break;
            case SemiIrrigated:
                semiIrrigatedKhasraCount = semiIrrigatedKhasraCount + 1;
                semiIrrigatedLandSize = semiIrrigatedLandSize.add(convertedlandSize);
                break;
            case Rainfed:
                rainfedKhasraCount = rainfedKhasraCount + 1;
                rainfedLandSize = rainfedLandSize.add(convertedlandSize);
                break;
        }

        // count factors for khasras Ownership Type(irrigated,semi-irrigated and rainfed) for particuler village
        int purchasedKhasraCount = khasraOwnershipAnalytics.getPurchasedKhasraCount();
        int rentedKhasraCount = khasraOwnershipAnalytics.getRentedKhasraCount();
        int leasedKhasraCount = khasraOwnershipAnalytics.getLeasedKhasraCount();
        int inheritedFromAncestorsKhasraCount = khasraOwnershipAnalytics.getInheritedFromAncestorsKhasraCount();
        int selfKhasraCount = khasraOwnershipAnalytics.getSelfKhasraCount();

        // land Ownership type size factors
        BigDecimal purchasedLandSize = khasraOwnershipAnalytics.getPurchasedLandSize();
        BigDecimal rentedLandSize = khasraOwnershipAnalytics.getRentedLandSize();
        BigDecimal leasedLandSize = khasraOwnershipAnalytics.getLeasedLandSize();
        BigDecimal inheritedFromAncestorsLandSize = khasraOwnershipAnalytics.getInheritedFromAncestorsLandSize();
        BigDecimal selfLandSize = khasraOwnershipAnalytics.getSelfLandSize();

        // for Ownership type
        switch (userLandDetail.getOwnershipType()) {
            case Purchased:
                purchasedKhasraCount = purchasedKhasraCount + 1;
                purchasedLandSize = purchasedLandSize.add(convertedlandSize);
                break;
            case Rented:
                rentedKhasraCount = rentedKhasraCount + 1;
                rentedLandSize = rentedLandSize.add(convertedlandSize);
                break;
            case Leased:
                leasedKhasraCount = leasedKhasraCount + 1;
                leasedLandSize = leasedLandSize.add(convertedlandSize);
                break;
            case Inherited_From_Ancestors:
                inheritedFromAncestorsKhasraCount = inheritedFromAncestorsKhasraCount + 1;
                inheritedFromAncestorsLandSize = inheritedFromAncestorsLandSize.add(convertedlandSize);
                break;
            case Self:
                selfKhasraCount = selfKhasraCount + 1;
                selfLandSize = selfLandSize.add(convertedlandSize);
                break;
        }

        // add total land size
        totalLandSize = totalLandSize.add(convertedlandSize);
        // increment total khasra count
        totalKhasraCount = totalKhasraCount + 1;

        // set values for land type analytics
        landTypeAnalytics.setIrrigatedLandPercentage(calculateLandTypePercentage(totalLandSize, irrigatedLandSize));
        landTypeAnalytics.setRainfedLandPercentage(calculateLandTypePercentage(totalLandSize, rainfedLandSize));
        landTypeAnalytics.setSemiIrrigatedLandPercentage(calculateLandTypePercentage(totalLandSize, semiIrrigatedLandSize));

        landTypeAnalytics.setIrrigatedLandSize(irrigatedLandSize);
        landTypeAnalytics.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
        landTypeAnalytics.setRainfedLandSize(rainfedLandSize);
        landTypeAnalytics.setTotalLandSize(totalLandSize);

        landTypeAnalytics.setIrrigatedKhasraCount(irrigatedKhasraCount);
        landTypeAnalytics.setSemiIrrigatedKhasraCount(semiIrrigatedKhasraCount);
        landTypeAnalytics.setRainfedKhasraCount(rainfedKhasraCount);
        landTypeAnalytics.setTotalKhasraCount(totalKhasraCount);

        // set values for Ownership Type

        khasraOwnershipAnalytics.setPurchasedLandPercentage(calculateLandTypePercentage(totalLandSize, purchasedLandSize));
        khasraOwnershipAnalytics.setRentedLandPercentage(calculateLandTypePercentage(totalLandSize,rentedLandSize));
        khasraOwnershipAnalytics.setLeasedLandPercentage(calculateLandTypePercentage(totalLandSize, leasedLandSize));
        khasraOwnershipAnalytics.setInheritedFromAncestorsLandPercentage(calculateLandTypePercentage(totalLandSize, inheritedFromAncestorsLandSize));
        khasraOwnershipAnalytics.setSelfLandPercentage(calculateLandTypePercentage(totalLandSize, selfLandSize));

        khasraOwnershipAnalytics.setPurchasedLandSize(purchasedLandSize);
        khasraOwnershipAnalytics.setRentedLandSize(rentedLandSize);
        khasraOwnershipAnalytics.setLeasedLandSize(leasedLandSize);
        khasraOwnershipAnalytics.setInheritedFromAncestorsLandSize(inheritedFromAncestorsLandSize);
        khasraOwnershipAnalytics.setSelfLandSize(selfLandSize);
        khasraOwnershipAnalytics.setTotalLandSize(totalLandSize);

        khasraOwnershipAnalytics.setPurchasedKhasraCount(purchasedKhasraCount);
        khasraOwnershipAnalytics.setRentedKhasraCount(rentedKhasraCount);
        khasraOwnershipAnalytics.setLeasedKhasraCount(leasedKhasraCount);
        khasraOwnershipAnalytics.setInheritedFromAncestorsKhasraCount(inheritedFromAncestorsKhasraCount);
        khasraOwnershipAnalytics.setSelfKhasraCount(selfKhasraCount);
        khasraOwnershipAnalytics.setTotalKhasraCount(totalKhasraCount);

        userLandAnalytics.setFieldSizeType(FieldSize.FieldSizeType.Acre);
        userLandAnalytics.setLandTypeAnalytics(landTypeAnalytics);
        userLandAnalytics.setKhasraOwnershipAnalytics(khasraOwnershipAnalytics);
        return mongoTemplate.save(userLandAnalytics);
    }

    @Override
    public String storeUserLandAnalyticsByLocation(String locationType, String locationId) {

        // get search key according to location type for land details
        String searchLocationKey = getLocationKeyLandDetails(locationType);

        // get land details list for particular location
        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)));
        query.fields().include("id").include("userId").include("landSize").include("landSizeType").include("farmType").include("ownershipType");
        List<UserLandDetail> userLandDetailList = mongoTemplate.find(query, UserLandDetail.class);
        System.out.println("===========size============="+userLandDetailList.size());

        // land type size factors
        BigDecimal totalLandSize = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        // count factors for khasras(irrigated,semi-irrigated and rainfed) for particuler village
        int irrigatedKhasraCount = 0;
        int semiIrrigatedKhasraCount = 0;
        int rainfedKhasraCount = 0;
        int totalKhasraCount = 0;

        // land Ownership type size factors
        BigDecimal purchasedLandSize = BigDecimal.ZERO;
        BigDecimal rentedLandSize = BigDecimal.ZERO;
        BigDecimal leasedLandSize = BigDecimal.ZERO;
        BigDecimal inheritedFromAncestorsLandSize = BigDecimal.ZERO;
        BigDecimal selfLandSize = BigDecimal.ZERO;

        // count factors for khasras Ownership Type(irrigated,semi-irrigated and rainfed) for particuler village
        int purchasedKhasraCount = 0;
        int rentedKhasraCount = 0;
        int leasedKhasraCount = 0;
        int inheritedFromAncestorsKhasraCount = 0;
        int selfKhasraCount = 0;

        int count = 0;
        if(!userLandDetailList.isEmpty()) {

            for (UserLandDetail detail : userLandDetailList) {
                count++;

                System.out.println(count + "===id===" + detail.getId() + "=====userId======" + detail.getUserId());
                BigDecimal convertedlandSize = detail.getLandSize();
                // convert land size to Acre (in case Hectare is used)
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = convertedlandSize.multiply(BigDecimal.valueOf(2.5));
                }

                switch (detail.getFarmType()) {
                    case Irrigated:
                        irrigatedKhasraCount = irrigatedKhasraCount + 1;
                        irrigatedLandSize = irrigatedLandSize.add(convertedlandSize);
                        break;
                    case SemiIrrigated:
                        semiIrrigatedKhasraCount = semiIrrigatedKhasraCount + 1;
                        semiIrrigatedLandSize = semiIrrigatedLandSize.add(convertedlandSize);
                        break;
                    case Rainfed:
                        rainfedKhasraCount = rainfedKhasraCount + 1;
                        rainfedLandSize = rainfedLandSize.add(convertedlandSize);
                        break;
                }

                // for Ownership type
                switch (detail.getOwnershipType()) {
                    case Purchased:
                        purchasedKhasraCount = purchasedKhasraCount + 1;
                        purchasedLandSize = purchasedLandSize.add(convertedlandSize);
                        break;
                    case Rented:
                        rentedKhasraCount = rentedKhasraCount + 1;
                        rentedLandSize = rentedLandSize.add(convertedlandSize);
                        break;
                    case Leased:
                        leasedKhasraCount = leasedKhasraCount + 1;
                        leasedLandSize = leasedLandSize.add(convertedlandSize);
                        break;
                    case Inherited_From_Ancestors:
                        inheritedFromAncestorsKhasraCount = inheritedFromAncestorsKhasraCount + 1;
                        inheritedFromAncestorsLandSize = inheritedFromAncestorsLandSize.add(convertedlandSize);
                        break;
                    case Self:
                        selfKhasraCount = selfKhasraCount + 1;
                        selfLandSize = selfLandSize.add(convertedlandSize);
                        break;
                }
                // add total land size
                totalLandSize = totalLandSize.add(convertedlandSize);
                totalKhasraCount = totalKhasraCount + 1;
            }


            // check if LandTypeAnalytics already exist
            UserLandAnalytics userLandAnalytics = checkLandTypeAnalyticsExist(locationType, locationId);
            LandTypeAnalytics landTypeAnalytics;
            KhasraOwnershipAnalytics khasraOwnershipAnalytics;
            if (userLandAnalytics == null) {
                userLandAnalytics = new UserLandAnalytics();
                landTypeAnalytics = new LandTypeAnalytics();
                khasraOwnershipAnalytics = new KhasraOwnershipAnalytics();
                // store location from UserLandDetail  By LocationType and LocationId
                Address address = getAddressByLocationTypeAndLocationId(locationType, locationId);
                userLandAnalytics.setAddress(address);
                userLandAnalytics.setLocationType(CommissionRate.LocationType.valueOf(locationType));
            } else {
                landTypeAnalytics = userLandAnalytics.getLandTypeAnalytics();
                khasraOwnershipAnalytics = userLandAnalytics.getKhasraOwnershipAnalytics();
            }

            // set values for LandTypeAnalytics
            landTypeAnalytics.setIrrigatedLandPercentage(calculateLandTypePercentage(totalLandSize, irrigatedLandSize));
            landTypeAnalytics.setRainfedLandPercentage(calculateLandTypePercentage(totalLandSize, rainfedLandSize));
            landTypeAnalytics.setSemiIrrigatedLandPercentage(calculateLandTypePercentage(totalLandSize, semiIrrigatedLandSize));

            landTypeAnalytics.setIrrigatedLandSize(irrigatedLandSize);
            landTypeAnalytics.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
            landTypeAnalytics.setRainfedLandSize(rainfedLandSize);
            landTypeAnalytics.setTotalLandSize(totalLandSize);

            landTypeAnalytics.setIrrigatedKhasraCount(irrigatedKhasraCount);
            landTypeAnalytics.setSemiIrrigatedKhasraCount(semiIrrigatedKhasraCount);
            landTypeAnalytics.setRainfedKhasraCount(rainfedKhasraCount);
            landTypeAnalytics.setTotalKhasraCount(totalKhasraCount);

            // set values for Ownership Type

            khasraOwnershipAnalytics.setPurchasedLandPercentage(calculateLandTypePercentage(totalLandSize, purchasedLandSize));
            khasraOwnershipAnalytics.setRentedLandPercentage(calculateLandTypePercentage(totalLandSize,rentedLandSize));
            khasraOwnershipAnalytics.setLeasedLandPercentage(calculateLandTypePercentage(totalLandSize, leasedLandSize));
            khasraOwnershipAnalytics.setInheritedFromAncestorsLandPercentage(calculateLandTypePercentage(totalLandSize, inheritedFromAncestorsLandSize));
            khasraOwnershipAnalytics.setSelfLandPercentage(calculateLandTypePercentage(totalLandSize, selfLandSize));

            khasraOwnershipAnalytics.setPurchasedLandSize(purchasedLandSize);
            khasraOwnershipAnalytics.setRentedLandSize(rentedLandSize);
            khasraOwnershipAnalytics.setLeasedLandSize(leasedLandSize);
            khasraOwnershipAnalytics.setInheritedFromAncestorsLandSize(inheritedFromAncestorsLandSize);
            khasraOwnershipAnalytics.setSelfLandSize(selfLandSize);
            khasraOwnershipAnalytics.setTotalLandSize(totalLandSize);

            khasraOwnershipAnalytics.setPurchasedKhasraCount(purchasedKhasraCount);
            khasraOwnershipAnalytics.setRentedKhasraCount(rentedKhasraCount);
            khasraOwnershipAnalytics.setLeasedKhasraCount(leasedKhasraCount);
            khasraOwnershipAnalytics.setInheritedFromAncestorsKhasraCount(inheritedFromAncestorsKhasraCount);
            khasraOwnershipAnalytics.setSelfKhasraCount(selfKhasraCount);
            khasraOwnershipAnalytics.setTotalKhasraCount(totalKhasraCount);

            userLandAnalytics.setFieldSizeType(FieldSize.FieldSizeType.Acre);
            userLandAnalytics.setLandTypeAnalytics(landTypeAnalytics);
            userLandAnalytics.setKhasraOwnershipAnalytics(khasraOwnershipAnalytics);
            mongoTemplate.save(userLandAnalytics);
        }
        return "success";
    }

    /**
     * calcultate percentage for land size according to type
     * @param totalLandSize
     * @param landTypeSize
     * @return
     */
    private BigDecimal calculateLandTypePercentage(BigDecimal totalLandSize, BigDecimal landTypeSize) {
        return landTypeSize.divide(totalLandSize, 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
    }

    /**
     * check if LandTypeAnalytics already exist
     * @param locationType
     * @param locationId
     * @return
     */
    private UserLandAnalytics checkLandTypeAnalyticsExist(String locationType, String locationId) {
        String searchLocationKey = getLocationKey(locationType);
        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)).and("locationType").is(locationType));
        return mongoTemplate.findOne(query, UserLandAnalytics.class);
    }

    /**
     * get location key for User Land Details
     * @param locationType
     * @return
     */
    public static String getLocationKeyLandDetails(String locationType) {
        String searchLocationKey;
        switch (locationType) {
            case "STATE":
                searchLocationKey = "state.$id";
                break;
            case "DISTRICT":
                searchLocationKey = "city.$id";
                break;
            case "TEHSIL":
                searchLocationKey = "tehsil.$id";
                break;
            case "BLOCK":
                searchLocationKey = "block.$id";
                break;
            case "VILLAGE":
                searchLocationKey = "villageModel.$id";
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return searchLocationKey;
    }

    @Override
    public String storeUserLandAnalyticsAll(String locationType) {
        // store Land Type Analytics according to locationType
        if(locationType.equals(CommissionRate.LocationType.STATE.toString())){
            List<State> stateList = mongoTemplate.findAll(State.class);
            for (State state : stateList){
                System.out.println("=======state name========="+state.getName());
                storeUserLandAnalyticsByLocation(locationType,state.getId());
            }
        } else if(locationType.equals(DISTRICT.toString())){
            List<City> cityList = mongoTemplate.findAll(City.class);
            for (City city : cityList){
                System.out.println("=======city name========="+city.getName());
                storeUserLandAnalyticsByLocation(locationType,city.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.TEHSIL.toString())){
            List<Tehsil> tehsilList = mongoTemplate.findAll(Tehsil.class);
            for (Tehsil tehsil : tehsilList){
                System.out.println("=======tehsil name========="+tehsil.getName());
                storeUserLandAnalyticsByLocation(locationType,tehsil.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.BLOCK.toString())){
            List<Block> blocks = mongoTemplate.findAll(Block.class);
            for (Block block : blocks){
                System.out.println("=======block name========="+block.getName());
                storeUserLandAnalyticsByLocation(locationType,block.getId());
            }
        } else if(locationType.equals(CommissionRate.LocationType.VILLAGE.toString())){
            List<Village> villageList = mongoTemplate.findAll(Village.class);
            for (Village village : villageList){
                System.out.println("=======village name========="+village.getName());
                storeUserLandAnalyticsByLocation(locationType,village.getId());
            }
        }

        return "Success";
    }

    @Override
    public List<UserLandAnalytics> getUserLandAnalyticsByLocation(String type, String locationType, String locationId) {
        Criteria  criteria = getQueryCriteriaForAnalytics(locationType,locationId);

        Query query = new Query(criteria);
        if(type.equals("LandType")){
            query.fields().include("landTypeAnalytics");
        } else if(type.equals("OwnershipType")){
            query.fields().include("khasraOwnershipAnalytics");
        }
        query.fields().include("fieldSizeType").include("address").include("locationType");
        return mongoTemplate.find(query, UserLandAnalytics.class);
    }

}
