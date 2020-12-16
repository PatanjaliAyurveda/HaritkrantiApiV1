package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.NewsAndFeeds;
import com.bharuwa.haritkranti.service.newservice.NewsAndFeedsService;

@Service
public class NewsAndFeedsServiceImpl implements NewsAndFeedsService{
	private final MongoTemplate mongoTemplate;
	
	public NewsAndFeedsServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<NewsAndFeeds> getNewsAndFeeds() {
		return mongoTemplate.findAll(NewsAndFeeds.class);
	}
}
