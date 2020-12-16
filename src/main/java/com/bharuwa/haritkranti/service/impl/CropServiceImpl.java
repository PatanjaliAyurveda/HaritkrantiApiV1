package com.bharuwa.haritkranti.service.impl;

import com.amazonaws.services.dlm.model.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.crops.FruitVariety;
import com.bharuwa.haritkranti.models.crops.FruitVarietyLocation;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;
import com.bharuwa.haritkranti.models.requestModels.CropGroupReq;
import com.bharuwa.haritkranti.service.CropService;
import com.bharuwa.haritkranti.service.LocationServices;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author anuragdhunna
 */
@Service
public class CropServiceImpl implements CropService {

    private final MongoTemplate mongoTemplate;

    public CropServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Autowired
    private LocationServices locationServices;

    @Override
    public CropGroup addCropGroup(CropGroupReq req) {
        Query query = new Query(Criteria.where("name").is(req.getName()).and("type").is(req.getType().toString()));
        CropGroup value = mongoTemplate.findOne(query, CropGroup.class);

        if (value !=  null) {
            return value;
        }
        CropGroup group = new CropGroup();
        group.setName(req.getName());
        group.setType(req.getType());
        return mongoTemplate.save(group);
    }

    @Override
    public List<CropGroup> getCropGroupsByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return mongoTemplate.findAll(CropGroup.class);
        }
        Query query = new Query(Criteria.where("type").is(type));
        return mongoTemplate.find(query, CropGroup.class);
    }

    @Override
    public FruitVariety addFruitVariety(FruitVariety fruitVariety) {
        Query query = new Query(Criteria.where("name").is(fruitVariety.getName()));
        FruitVariety value = mongoTemplate.findOne(query, FruitVariety.class);

        System.out.println("==========fruitVariety=="+fruitVariety.getName());
        System.out.println("==========valiue=="+value);
        if (value ==  null) {
            System.out.println("==========null=="+value);
            mongoTemplate.save(fruitVariety);
        } else {
            System.out.println("else"+value);
            value.setName(fruitVariety.getName());
            value.setCropGroup(fruitVariety.getCropGroup());
            value.setFruitVarietyLocations(fruitVariety.getFruitVarietyLocations());
            mongoTemplate.save(value);
        }

        return fruitVariety;
    }

    @Override
    public List<FruitVarietyLocation> getFruitVarietyLocationByState(String stateId, String cropGroupId) {
        Query query = new Query(Criteria.where("fruitVarietyLocations.state.$id").is(new ObjectId(stateId)).and("cropGroup.$id").is(new ObjectId(cropGroupId)));
        return mongoTemplate.findDistinct(query, "fruitVarietyLocations", FruitVariety.class, FruitVarietyLocation.class);
    }

    @Override
    public List<FruitVariety> getFruitsByGroup(String cropGroupId) {
        Query query = new Query(Criteria.where("cropGroup.$id").is(new ObjectId(cropGroupId)));
        return mongoTemplate.find(query, FruitVariety.class);
    }

    @Override
    public CropGroup updateCropGroup(CropGroup cropGroup) {
        return mongoTemplate.save(cropGroup);
    }

    @Override
    public CropGroup getCropGroupsByNameAndType(String name, String type) {
        Query query = new Query(Criteria.where("type").is(type.trim()).and("name").is(name.trim()));
        return mongoTemplate.findOne(query, CropGroup.class);
    }

    @Override
    public Crop findCropByNameAndType(String cropGroupName, String cropGroupType, String cropName) {
        CropGroup cropGroup = getCropGroupsByNameAndType(cropGroupName,cropGroupType);
        if (cropGroup == null){
            throw new ResourceNotFoundException("croup group not found with name :"+cropGroupName+" and type : "+cropGroupType);
        }
        Query query = new Query(Criteria.where("cropGroup.$id").is(new ObjectId(cropGroup.getId())).and("cropName").is(cropName));
        return mongoTemplate.findOne(query,Crop.class);
    }
    
    @Override
    public void saveCropYield(List<CropYield> list) {
        for(CropYield yield:list) {
        	mongoTemplate.save(yield);
        }
    }
    
    @Override
    public void saveFarmCoordinate(List<FarmCordinate> list) {
        for(FarmCordinate coordinate:list) {
        	mongoTemplate.save(coordinate);
        }
    }
}
