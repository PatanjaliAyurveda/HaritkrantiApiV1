package com.bharuwa.haritkranti.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.AgroClimaticZone;
import com.bharuwa.haritkranti.models.AgroClimaticZoneInHindi;
import com.bharuwa.haritkranti.models.CropDisease;
import com.bharuwa.haritkranti.models.CropDiseaseInHindi;
import com.bharuwa.haritkranti.models.CropPesticides;
import com.bharuwa.haritkranti.models.CropPesticidesInHindi;
import com.bharuwa.haritkranti.models.CropSeed;
import com.bharuwa.haritkranti.models.CropSeedInHindi;
import com.bharuwa.haritkranti.models.CropWeed;
import com.bharuwa.haritkranti.models.CropWeedInHindi;
import com.bharuwa.haritkranti.models.GovtMapCoordinate;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinaterResponse;
import com.bharuwa.haritkranti.models.newmodels.LocationPin;
import com.bharuwa.haritkranti.service.FarmerAppCropService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sunaina
 */
@Service
public class FarmerAppCropServiceImpl implements FarmerAppCropService {
    private final MongoTemplate mongoTemplate;

    public FarmerAppCropServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CropYield fetchCropNameFromCropYield(String khasraNo,String language) {
        /*Query query = new Query(Criteria.where("KhasraNumber").is(khasraNo));
        CropYield cropYield = mongoTemplate.findOne(query, CropYield.class);
        if (cropYield == null) {
            throw new ResourceNotFoundException("crop yield data not Exist for khasra number " + khasraNo);
        }
        return cropYield;*/
    	CropYield cropYield=null;
    	Query query1 = new Query();
        Criteria criteria = new Criteria("KhasraNumber").is(khasraNo);
        query1.addCriteria(criteria);
    	List<GovtMapCoordinate> list = mongoTemplate.find(query1, GovtMapCoordinate.class);
    	if(!list.isEmpty()) {
    		Query query2 = new Query(Criteria.where("lat").gte(list.get(0).getLatMin()).lte(list.get(0).getLatMax()).and("lon").gte(list.get(0).getLonMin()).lte(list.get(0).getLonMax()));
    		List<FarmCordinate> coordinates=mongoTemplate.find(query2, FarmCordinate.class);
    
    		System.out.println("Farm cordinate : "+list.size());
    		if(!coordinates.isEmpty()) {
    			Query query = new Query(Criteria.where("farmId").is(coordinates.get(0).getFarmId()));
    			cropYield = mongoTemplate.findOne(query, CropYield.class);
    			if (cropYield == null) {
    				throw new ResourceNotFoundException("crop yield data not Exist for khasra number " + khasraNo);
    			}
    		}
    	}
    	if(language.equals("English"))
    		cropYield.setCropDisplayName(cropYield.getCropName());
    	else if(language.equals("Hindi"))
    		cropYield.setCropDisplayName(cropYield.getCropNameInHindi());
        return cropYield;
    }

    @Override
    public CropDetail storeCropDetail(CropDetail cropDetail, String khasraNo, boolean status,String language) {
        //get crop name from cropYield table against the khasra number.
        CropYield cropYield = fetchCropNameFromCropYield(khasraNo,language);
        if (cropYield == null) {
            throw new ResourceNotFoundException("cropYield data does not exist for khasra number " + khasraNo);
        } else {
            cropDetail.setKhasraNumber(khasraNo);
            cropDetail.setState(cropYield.getState());
            cropDetail.setDistrict(cropYield.getDistrict());
            cropDetail.setTehsil(cropYield.getTehsil());
            cropDetail.setBlock(cropYield.getBlock());
            cropDetail.setVillage(cropYield.getVillage());
            cropDetail.setSatelliteCrop(cropYield.getCropName());
            cropDetail.setFarmId(cropYield.getFarmId());
        //    cropDetail.setFarmerName(cropYield.getFarmerName());
        //    cropDetail.setMobileNumber(cropYield.getMobileNumber());
            if (status == true) {
                cropDetail.setActualCrop(cropYield.getCropName());
                return mongoTemplate.save(cropDetail);
            }
            return mongoTemplate.save(cropDetail);
        }
    }


    @Override
    public CropDetail findByid(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, CropDetail.class);
    }

    @Override
    public CropDetail updateAlreadySownCropDetail(String cropDetailId,boolean status) {

        //get crop detail record from cropDetail table on the basis of Id.
        CropDetail cropDetail = findByid(cropDetailId);
        if (cropDetail == null ){
            throw new ResourceNotFoundException("crop detail data not Exist");
        }

        if (status == true) {
            cropDetail.setAlreadySown("Y");
        } else {
            cropDetail.setAlreadySown("N");
        }
        return mongoTemplate.save(cropDetail);
    }

    @Override
    public CropDetail updateCropDetail(String days, boolean status, String cropDetailId) {

        CropDetail cropDetail = findByid(cropDetailId);
        if (cropDetail == null ){
            throw new ResourceNotFoundException("crop detail data not Exist");
        }

        if (status == true) {
            cropDetail.setBeforeDays(days);
        } else {
            cropDetail.setAfterDays(days);
        }
        return mongoTemplate.save(cropDetail);
    }
    
    @Override
    public CropDetail updateCropName(String cropCategory,String cropName,String cropVariety, String cropDetailId) {

        CropDetail cropDetail = findByid(cropDetailId);
        if (cropDetail == null ){
            throw new ResourceNotFoundException("crop detail data not Exist");
        }
        cropDetail.setActualCropCategory(cropCategory);
        cropDetail.setActualCrop(cropName);
        cropDetail.setActualCropVariety(cropVariety);
        return mongoTemplate.save(cropDetail);
    }

    @Override
    public FarmCordinaterResponse getfarmCordinate(String khasraNo) {
       /* List<FarmCordinate> cordinates = new ArrayList<>();
        Query query = new Query(Criteria.where("KhasraNumber").is(khasraNo));
        List<CropYield> cropYieldList = mongoTemplate.find(query, CropYield.class);
        if (cropYieldList == null) {
            throw new ResourceNotFoundException("crop yield data not Exist for khasra number " + khasraNo);
        }

        for (CropYield cropYield : cropYieldList) {
            Query query1 = new Query(Criteria.where("farmId").is(cropYield.getFarmId()));
            List<FarmCordinate> farmCordinates = mongoTemplate.find(query1, FarmCordinate.class);
            if (farmCordinates == null) {
                throw new ResourceNotFoundException("farmCordinates data not Exist");
            }

            for (FarmCordinate farmCordinate : farmCordinates) {
                if (farmCordinate != null) {
                    cordinates.add(farmCordinate);
                }
            }
        }
        return cordinates; */
        
        Query query1 = new Query();
        Criteria criteria = new Criteria("KhasraNumber").is(khasraNo);
        query1.addCriteria(criteria);
    	List<GovtMapCoordinate> list = mongoTemplate.find(query1, GovtMapCoordinate.class);
    	
    	Query query2 = new Query(Criteria.where("lat").gte(list.get(0).getLatMin()).lte(list.get(0).getLatMax()).and("lon").gte(list.get(0).getLonMin()).lte(list.get(0).getLonMax()));
    	List<FarmCordinate> coordinates=mongoTemplate.find(query2, FarmCordinate.class);
    	FarmCordinaterResponse response = new FarmCordinaterResponse();
    	List<LocationPin> locationpins=new ArrayList<LocationPin>();
    	for(FarmCordinate cordinate:coordinates) {
    		LocationPin locationPin = new LocationPin();
    		locationPin.setLattitude(Float.toString(cordinate.getLat()));
    		locationPin.setLongitude(Float.toString(cordinate.getLon()));
    		locationpins.add(locationPin);
    		response.setFarmId(cordinate.getFarmId());
    	}
    	response.setLocationPins(locationpins);
    	return response;
    }

    @Override
    public List getCropPesticides(String cropName, String state, String district, String sowingTime,String language) {

        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZoneInHindi climaticZoneInHindi = mongoTemplate.findOne(query, AgroClimaticZoneInHindi.class);
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZoneInHindi.getReasonId()));
          //      query = new Query(Criteria.where("cropName").is("  "));
                return mongoTemplate.find(query, CropPesticidesInHindi.class);
          //      return mongoTemplate.findAll(CropPesticidesInHindi.class);

            case "English" :
                query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZone climaticZone = mongoTemplate.findOne(query, AgroClimaticZone.class);
          //      query = new Query(Criteria.where("cropName").is(cropName).and("agroClimaticZoneName").is(climaticZone.getReasonName()).and("sowingTime").is(sowingTime));
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZone.getReasonId()));
                return mongoTemplate.find(query, CropPesticides.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }
    @Override
    public List getCropDisease(String cropName, String state, String district, String sowingTime,String language) {
        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZoneInHindi climaticZoneInHindi = mongoTemplate.findOne(query, AgroClimaticZoneInHindi.class);
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZoneInHindi.getReasonId()));
                return mongoTemplate.find(query, CropDiseaseInHindi.class);

            case "English":
                query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZone climaticZone = mongoTemplate.findOne(query, AgroClimaticZone.class);
         //       query = new Query(Criteria.where("cropName").is(cropName).and("agroClimaticZoneName").is(climaticZone.getReasonName()).and("sowingTime").is(sowingTime));
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZone.getReasonId()));
                return mongoTemplate.find(query, CropDisease.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }
    @Override
    public List getCropWeed(String cropName, String state, String district, String sowingTime,String language) {

        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZoneInHindi climaticZoneInHindi = mongoTemplate.findOne(query,AgroClimaticZoneInHindi.class);
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZoneInHindi.getReasonId()));
                return mongoTemplate.find(query,CropWeedInHindi.class);

            case "English":
                query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZone climaticZone = mongoTemplate.findOne(query,AgroClimaticZone.class);
         //       query = new Query(Criteria.where("cropName").is(cropName).and("agroClimaticZone").is(climaticZone.getReasonName()).and("sowingTime").is(sowingTime));
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZone.getReasonId()));
                return mongoTemplate.find(query,CropWeed.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public List getCropSeed(String cropName, String state, String district, String sowingTime,String language) {

        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZoneInHindi climaticZoneInHindi = mongoTemplate.findOne(query,AgroClimaticZoneInHindi.class);
          //    query = new Query(Criteria.where("cropName").is(cropName.trim()));
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZoneInHindi.getReasonId()));
         //     query = new Query(Criteria.where("cropName").is(cropName).and("agroClimaticZone").is(climaticZoneInHindi.getReasonName()).and("sowingTime").is(sowingTime));
                return mongoTemplate.find(query,CropSeedInHindi.class);

            case "English":
                query = new Query(Criteria.where("state").is(state).and("district").is(district));
                AgroClimaticZone climaticZone = mongoTemplate.findOne(query,AgroClimaticZone.class);
         //     query = new Query(Criteria.where("cropName").is(cropName).and("agroClimaticZone").is(climaticZone.getReasonName()).and("sowingTime").is(sowingTime));
                query = new Query(Criteria.where("cropName").is(cropName.trim()).and("reasonId").is(climaticZone.getReasonId()));
                return mongoTemplate.find(query,CropSeed.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public CropSeedInHindi storeCropSeed(CropSeedInHindi seed) { return mongoTemplate.save(seed); }

    @Override
    public CropWeedInHindi storeCropWeed(CropWeedInHindi weed) { return mongoTemplate.save(weed); }

    @Override
    public CropDiseaseInHindi storeCropDisease(CropDiseaseInHindi disease) { return mongoTemplate.save(disease); }

    @Override
    public CropPesticidesInHindi storeCropPesticides(CropPesticidesInHindi pesticides) { return mongoTemplate.save(pesticides); }

    @Override
    public AgroClimaticZoneInHindi storeAgroClimaticZoneInHindi(AgroClimaticZoneInHindi agroClimaticZoneInHindi) { return mongoTemplate.save(agroClimaticZoneInHindi); }
}
