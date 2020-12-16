package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.SeedCenter;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.FertilizerCenter;
import com.bharuwa.haritkranti.service.UserService;
import com.bharuwa.haritkranti.service.newservice.SeedCenterService;

@Service
public class SeedCenterServiceImpl implements SeedCenterService{
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
    private UserService userService;
	
	public SeedCenterServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<SeedCenter> getSeedCenterList(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	String stateName = userExist.getAddressModel().getState().getName();
    	
    	Criteria stateAndDistrictcriteria = new Criteria("district").is(districtName).and("state").is(stateName);
    	Query stateAndDistrictQuery = new Query();
    	stateAndDistrictQuery.addCriteria(stateAndDistrictcriteria);
    	
    	Criteria stateCriteria = new Criteria("state").is(stateName);
    	Query stateQuery = new Query();
    	stateQuery.addCriteria(stateCriteria);
    	
    	List<SeedCenter> list = mongoTemplate.find(stateAndDistrictQuery,SeedCenter.class);
    	if(list.isEmpty()) {
    		list = mongoTemplate.find(stateQuery,SeedCenter.class);
    	}
    	return list;
	}
}
