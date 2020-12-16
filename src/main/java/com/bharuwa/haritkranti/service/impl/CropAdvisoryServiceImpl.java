package com.bharuwa.haritkranti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.CropSelection;
import com.bharuwa.haritkranti.service.CropAdvisoryService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;

@Service
public class CropAdvisoryServiceImpl implements CropAdvisoryService{
	
private final MongoTemplate mongoTemplate;
	
	@Autowired
    private GenericMongoTemplate genericMongoTemplate;
	
	public CropAdvisoryServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<String> getCropList() {
    	List<String> cropList = mongoTemplate.findDistinct("name",CropSelection.class,String.class);
		return cropList;
	}
	
	@Override
	public List<String> getCropVarietyList(String cropId) {
		Criteria criteria = new Criteria("name").is(cropId);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> cropVarietyList = mongoTemplate.findDistinct(query,"commercialVarieties",CropSelection.class,String.class);
		return cropVarietyList;
	}
	
	@Override
	public List<CropSelection> getCropSelectionList(String cropId,String varietyId) {
		Criteria criteria = new Criteria("name").is(cropId).and("commercialVarieties").is(varietyId);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<CropSelection> cropSelectionList = mongoTemplate.find(query,CropSelection.class);
		return cropSelectionList;
	}
}
