package com.bharuwa.haritkranti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.Alert;
import com.bharuwa.haritkranti.models.newmodels.ContactUs;
import com.bharuwa.haritkranti.service.ContactUsService;
import com.bharuwa.haritkranti.service.UserService;

@Service
public class ContactUsServiceImpl implements ContactUsService {
	private final MongoTemplate mongoTemplate;
	@Autowired
	private UserService userService;
	
	public ContactUsServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<ContactUs> getContactUsList(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
		String city = userExist.getAddressModel().getCity().getName();
    	Criteria criteria = new Criteria("district").is(city);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<ContactUs> contactUsList = mongoTemplate.find(query, ContactUs.class);
		return contactUsList;
	}
}
