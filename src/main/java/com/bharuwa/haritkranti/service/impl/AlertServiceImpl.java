package com.bharuwa.haritkranti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.newmodels.Alert;
import com.bharuwa.haritkranti.service.AlertService;
import com.bharuwa.haritkranti.service.UserService;

@Service
public class AlertServiceImpl implements AlertService{

	private final MongoTemplate mongoTemplate;
	@Autowired
	private UserService userService;
	
	public AlertServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<Alert> getAlert(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
		String city = userExist.getAddressModel().getCity().getName();
    	Criteria criteria = new Criteria("district").is(city);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	//List<Alert> alertList = mongoTemplate.find(query, Alert.class);
    	List<Alert> alertList = mongoTemplate.findAll(Alert.class);
		return alertList;
	}
}
