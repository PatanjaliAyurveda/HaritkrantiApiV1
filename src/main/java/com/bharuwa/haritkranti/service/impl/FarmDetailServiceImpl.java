package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.models.BeekeepingAreaMapping;
import com.bharuwa.haritkranti.models.BeekeepingDetails;
import com.bharuwa.haritkranti.models.MilkingAnimalDetails;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.repositories.BeekeepingAreaMappingRepo;
import com.bharuwa.haritkranti.repositories.BeekeepingDetailRepo;
import com.bharuwa.haritkranti.service.CountService;
import com.bharuwa.haritkranti.service.FarmDetailService;
import com.bharuwa.haritkranti.service.UserExtraService;
import com.bharuwa.haritkranti.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author harman
 */
@Service
public class FarmDetailServiceImpl implements FarmDetailService {

    private final MongoTemplate mongoTemplate;

    public FarmDetailServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private BeekeepingDetailRepo beekeepingDetailRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private BeekeepingAreaMappingRepo beekeepingAreaMappingRepo;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private CountService countService;

    @Override
    public BeekeepingDetails storeBeekeepingDetails(BeekeepingDetails beekeepingDetails) {
//        store beekeeping details
        beekeepingDetailRepo.save(beekeepingDetails);
//        store batch-code
        if(beekeepingDetails.getBatchCode() == null || StringUtils.isEmpty(beekeepingDetails.getBatchCode())){
            beekeepingDetails.setBatchCode(userExtraService.getBeekingBatchForFarmer(beekeepingDetails.getId()));
            beekeepingDetailRepo.save(beekeepingDetails);
        }

        //count set for Beekeeping Details
        CountAll countAll = countService.getCountAll();
        countAll.setTotalBeekeepingFarmers(countService.getBeekeepingFarmerCount());
        countService.storeCountAll(countAll);

        return beekeepingDetails;
    }

    @Override
    public List<BeekeepingDetails> getBeekeepingDetails(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return  mongoTemplate.find(query, BeekeepingDetails.class);
    }

    @Override
    public BeekeepingDetails getBeekeepingDetailById(String beekeepingId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(beekeepingId)));
        return  mongoTemplate.findOne(query, BeekeepingDetails.class);
    }

    @Override
    public BeekeepingAreaMapping storeBeekeepingAreaMapping(BeekeepingAreaMapping beekeepingAreaMapping) {
//        get if already exist & update
        BeekeepingAreaMapping value = getBeekeepingAreaMapping(beekeepingAreaMapping.getFarmerId(),beekeepingAreaMapping.getBeekeepingId());

        if (value == null) {
            beekeepingAreaMappingRepo.save(beekeepingAreaMapping);
        } else {
            value.setAreaImageUrl(beekeepingAreaMapping.getAreaImageUrl());
            value.setLocationPins(beekeepingAreaMapping.getLocationPins());
            beekeepingAreaMappingRepo.save(value);
        }
        return beekeepingAreaMapping;
    }

    @Override
    public BeekeepingAreaMapping getBeekeepingAreaMapping(String farmerId, String beekeepingId) {
        Query query = new Query(Criteria.where("farmerId").is(farmerId).and("beekeepingId").is(beekeepingId));
        return mongoTemplate.findOne(query, BeekeepingAreaMapping.class);
    }
}
