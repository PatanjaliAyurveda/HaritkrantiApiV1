package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.GovtSchemes;
import com.bharuwa.haritkranti.service.newservice.GovtSchemesService;

@Service
public class GovtSchemesServiceImpl implements GovtSchemesService{
	
	private final MongoTemplate mongoTemplate;
	
	public GovtSchemesServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<GovtSchemes> getGovSchemes() {
		return mongoTemplate.findAll(GovtSchemes.class);
	}

}
