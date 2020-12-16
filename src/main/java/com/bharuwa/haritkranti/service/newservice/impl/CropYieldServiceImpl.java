package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FertilizerCenter;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.service.UserService;
import com.bharuwa.haritkranti.service.newservice.CropYieldService;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;

@Service
public class CropYieldServiceImpl implements CropYieldService{
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
    private UserService userService;
	
	public CropYieldServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<CropYield> getCropYieldList() {
	//	User userExist = userService.getUserByPhoneNum(phoneNumber);
    //	String districtName = userExist.getAddressModel().getCity().getName();
    	
    //	Criteria criteria = new Criteria("billingDistrict").is(districtName);
    //	Query query = new Query();
    //	query.addCriteria(criteria);
		return mongoTemplate.findAll(CropYield.class);
	}
	
	@Override
	public List<String> getTehsilList(String districtName) {
	    Criteria criteria = new Criteria("District").is(districtName);
	    Query query = new Query();
	    query.addCriteria(criteria);
		List<String> tehsilList = mongoTemplate.findDistinct(query,"Tehsil",CropYield.class,String.class);
		return tehsilList;
	}
	
	@Override
	public List<String> getBlockList(String tehsilName) {
		Criteria criteria = new Criteria("Tehsil").is(tehsilName);
	    Query query = new Query();
	    query.addCriteria(criteria);
		List<String> blockList = mongoTemplate.findDistinct(query,"Block",CropYield.class,String.class);
		return blockList;
	}
	
	@Override
	public List<String> getVillageList(String blockName) {
		Criteria criteria = new Criteria("Block").is(blockName);
	    Query query = new Query();
	    query.addCriteria(criteria);
		List<String> villageList = mongoTemplate.findDistinct(query,"Village",CropYield.class,String.class);
		return villageList;
	}
	
	@Override
	public List<String> getKhasraList(String villageName) {
		Criteria criteria = new Criteria("Village").is(villageName);
	    Query query = new Query();
	    query.addCriteria(criteria);
	    List<String> list=new ArrayList<String>();
		List<Object> villageList = mongoTemplate.findDistinct(query,"KhasraNumber",CropYield.class,Object.class);
		for(Object obj:villageList) {
			list.add(obj.toString());
		}
		return list;
	}
	
	@Override
	public CropYield getCropYield(String blockName,String villageName,String khasraNumber) {
		Criteria criteria = new Criteria("Village").is(villageName).and("Block").is(blockName).and("KhasraNumber").is(khasraNumber);
	    Query query = new Query();
	    query.addCriteria(criteria); 
	    CropYield cropYield=mongoTemplate.findOne(query, CropYield.class);
		return cropYield;
	}
	
	@Override
	public FarmCordinate getFarmCordinate(String blockName,String villageName,String khasraNumber) {
		Criteria criteria = new Criteria("Village").is(villageName).and("Block").is(blockName).and("KhasraNumber").is(khasraNumber);
	    Query query = new Query();
	    query.addCriteria(criteria); 
	    CropYield cropYield=mongoTemplate.findOne(query, CropYield.class);
	    FarmCordinate farmCordinate = null;
	    if(cropYield!=null && cropYield.getFarmId()!=null) {
	    	Criteria farmCordinatecriteria = new Criteria("farmId").is(cropYield.getFarmId().intValue());
		    Query farmCordinateQuery = new Query();
		    farmCordinateQuery.addCriteria(farmCordinatecriteria); 
		    farmCordinate=mongoTemplate.findOne(farmCordinateQuery, FarmCordinate.class);
	    }
	    
		return farmCordinate;
	}
}
