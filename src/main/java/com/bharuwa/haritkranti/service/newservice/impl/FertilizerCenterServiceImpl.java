package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.FertilizerCenter;
import com.bharuwa.haritkranti.service.UserService;
import com.bharuwa.haritkranti.service.newservice.FertilizerCenterService;

@Service
public class FertilizerCenterServiceImpl implements FertilizerCenterService{
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
    private UserService userService;
	
	public FertilizerCenterServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<FertilizerCenter> getFertilizerCenterList(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	String stateName = userExist.getAddressModel().getState().getName();
    	
    	Criteria stateAndDistrictcriteria = new Criteria("billingDistrict").is(districtName).and("billingState").is(stateName);
    	Query stateAndDistrictQuery = new Query();
    	stateAndDistrictQuery.addCriteria(stateAndDistrictcriteria);
    	
    	Criteria stateCriteria = new Criteria("billingState").is(stateName);
    	Query stateQuery = new Query();
    	stateQuery.addCriteria(stateCriteria);
    	List<FertilizerCenter> list = mongoTemplate.find(stateAndDistrictQuery,FertilizerCenter.class);
    	if(list.isEmpty()) {
    		list = mongoTemplate.find(stateQuery,FertilizerCenter.class);
    	}
    	return list;
	}
}
