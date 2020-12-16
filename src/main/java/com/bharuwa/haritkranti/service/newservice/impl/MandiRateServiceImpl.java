package com.bharuwa.haritkranti.service.newservice.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bharuwa.haritkranti.aws.AwsS3;
import com.bharuwa.haritkranti.models.FarmDetails;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.repositories.newrepositories.MandiRateRepo;
import com.bharuwa.haritkranti.service.UserService;
import com.bharuwa.haritkranti.service.newservice.MandiRateService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.models.newmodels.MandiRateBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

@Service
public class MandiRateServiceImpl implements MandiRateService{

	@Autowired
    private MandiRateRepo mandiRateRepo;
	
	@Autowired
    private UserService userService;
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
    private GenericMongoTemplate genericMongoTemplate;
	
	public MandiRateServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	 
	@Override
	public void saveMandiRateRecords(MandiRateRecord rate) {
		mandiRateRepo.save(rate);
	}
	
	@Override
	public List<MandiRateRecord> getMandiRateForAnnadata(String district) {
		String districtName = district;
		
		Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> arrivalDateList = mongoTemplate.findDistinct(query,"arrival_date",MandiRateRecord.class,String.class);
    	List<Date> dateList = new ArrayList<Date>();
    	try {
    		for(String str:arrivalDateList) {
        		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(str);
        		dateList.add(date1);
        	}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	Collections.sort(dateList);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateList.get(dateList.size()-1));
    	
    	String currDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+(cal.get(Calendar.MONTH)+1)+"/";
    	currDate = currDate+cal.get(Calendar.YEAR);
    	currDate.trim();
    	
    	cal.setTime(dateList.get(dateList.size()-2));
    	String prevDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		prevDate = prevDate+"0";
    	prevDate = prevDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		prevDate = prevDate+"0";
    	prevDate = prevDate+(cal.get(Calendar.MONTH)+1)+"/";
    	prevDate = prevDate+cal.get(Calendar.YEAR);
    	prevDate.trim();
    	
    	Query currentQuery = new Query();
    	Query previousQuery = new Query();
    
    	currentQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(currDate));
    	previousQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(prevDate));
        List<MandiRateRecord> currentList=mongoTemplate.find(currentQuery, MandiRateRecord.class);
        List<MandiRateRecord> previousList=mongoTemplate.find(previousQuery, MandiRateRecord.class);
        Map<String,String> rateMap = new HashMap<String,String>();
        for(MandiRateRecord rate:previousList) {
        	rateMap.put(rate.getMarket()+rate.getCommodity()+rate.getVariety(), rate.getModal_price());
        }
        for(MandiRateRecord rate:currentList) {
        	String prevRate = rateMap.get(rate.getMarket()+rate.getCommodity()+rate.getVariety());
        	if(prevRate==null)
        		rate.setDifferenceInRate("0");
        	if(prevRate!=null && rate.getModal_price() != null) {
        		int diff = 0;
        		if(Integer.parseInt(rate.getModal_price()) > Integer.parseInt(prevRate)) {
        			diff = Integer.parseInt(rate.getModal_price()) - Integer.parseInt(prevRate);
        			rate.setDifferenceInRate("+"+diff);
        		}else if(Integer.parseInt(rate.getModal_price()) < Integer.parseInt(prevRate)) {
        			diff = Integer.parseInt(prevRate) - Integer.parseInt(rate.getModal_price());
        			rate.setDifferenceInRate("-"+diff);
        		}else if(Integer.parseInt(rate.getModal_price()) == Integer.parseInt(prevRate)) {
        			rate.setDifferenceInRate("0");
        		}
        	}
        }
        
		return currentList;
	}
	
	@Override
	public List<MandiRateRecord> getMandiRate(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	
    	Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> arrivalDateList = mongoTemplate.findDistinct(query,"arrival_date",MandiRateRecord.class,String.class);
    	List<Date> dateList = new ArrayList<Date>();
    	try {
    		for(String str:arrivalDateList) {
        		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(str);
        		dateList.add(date1);
        	}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	Collections.sort(dateList);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateList.get(dateList.size()-1));
    	
    	String currDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+(cal.get(Calendar.MONTH)+1)+"/";
    	currDate = currDate+cal.get(Calendar.YEAR);
    	currDate.trim();
    	
    	cal.setTime(dateList.get(dateList.size()-2));
    	String prevDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		prevDate = prevDate+"0";
    	prevDate = prevDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		prevDate = prevDate+"0";
    	prevDate = prevDate+(cal.get(Calendar.MONTH)+1)+"/";
    	prevDate = prevDate+cal.get(Calendar.YEAR);
    	prevDate.trim();
    	
    	Query currentQuery = new Query();
    	Query previousQuery = new Query();
    
    	currentQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(currDate));
    	previousQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(prevDate));
        List<MandiRateRecord> currentList=mongoTemplate.find(currentQuery, MandiRateRecord.class);
        List<MandiRateRecord> previousList=mongoTemplate.find(previousQuery, MandiRateRecord.class);
        Map<String,String> rateMap = new HashMap<String,String>();
        for(MandiRateRecord rate:previousList) {
        	rateMap.put(rate.getMarket()+rate.getCommodity()+rate.getVariety(), rate.getModal_price());
        }
        for(MandiRateRecord rate:currentList) {
        	String prevRate = rateMap.get(rate.getMarket()+rate.getCommodity()+rate.getVariety());
        	if(prevRate==null)
        		rate.setDifferenceInRate("0");
        	if(prevRate!=null && rate.getModal_price() != null) {
        		int diff = 0;
        		if(Integer.parseInt(rate.getModal_price()) > Integer.parseInt(prevRate)) {
        			diff = Integer.parseInt(rate.getModal_price()) - Integer.parseInt(prevRate);
        			rate.setDifferenceInRate("+"+diff);
        		}else if(Integer.parseInt(rate.getModal_price()) < Integer.parseInt(prevRate)) {
        			diff = Integer.parseInt(prevRate) - Integer.parseInt(rate.getModal_price());
        			rate.setDifferenceInRate("-"+diff);
        		}else if(Integer.parseInt(rate.getModal_price()) == Integer.parseInt(prevRate)) {
        			rate.setDifferenceInRate("0");
        		}
        	}
        }
        
		return currentList;
	}
	
	@Override
	public List<MandiRateRecord> getMandiRateData(String phoneNumber,String commodity) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	
    	Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> arrivalDateList = mongoTemplate.findDistinct(query,"arrival_date",MandiRateRecord.class,String.class);
    	List<Date> dateList = new ArrayList<Date>();
    	try {
    		for(String str:arrivalDateList) {
        		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(str);
        		dateList.add(date1);
        	}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	Collections.sort(dateList);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateList.get(dateList.size()-1));
    	
    	String currDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+(cal.get(Calendar.MONTH)+1)+"/";
    	currDate = currDate+cal.get(Calendar.YEAR);
    	currDate.trim();
    	
    	Query currentQuery = new Query();
    	currentQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(currDate).and("commodity").is(commodity));
        List<MandiRateRecord> currentList=mongoTemplate.find(currentQuery, MandiRateRecord.class);
        
		return currentList;
	}
	
	@Override
	public List<String> getCommodityList(String phoneNumber) {
		phoneNumber = phoneNumber.substring(0,10);
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> commodityList = mongoTemplate.findDistinct(query,"commodity",MandiRateRecord.class,String.class);
		return commodityList;
	}
	
	@Override
	public List<String> getVarietyList(String commodity,String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	Criteria distCriteria = new Criteria("district").is(districtName);
    	Criteria commodityCriteria = new Criteria("commodity").is(commodity);
    	Query query = new Query();
    	query.addCriteria(distCriteria);
    	query.addCriteria(commodityCriteria);
    	List<String> commodityList = mongoTemplate.findDistinct(query,"variety",MandiRateRecord.class,String.class);
		return commodityList;
	}
	
	@Override
	public List<String> getMarketList(String phoneNumber) {
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	Criteria distCriteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(distCriteria);
    	List<String> commodityList = mongoTemplate.findDistinct(query,"market",MandiRateRecord.class,String.class);
		return commodityList;
	}
	
	public List<MandiRateRecord> getFilteredMandiRateData(String phoneNumber,String commodity,String variety,String dobParam){
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	String dob=null;
    	if(dobParam!=null)
    		dob = dobParam.substring(8,10)+"/"+dobParam.substring(5,7)+"/"+dobParam.substring(0,4);    	
    	Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	
    	List<String> arrivalDateList = mongoTemplate.findDistinct(query,"arrival_date",MandiRateRecord.class,String.class);
    	List<Date> dateList = new ArrayList<Date>();
    	try {
    		for(String str:arrivalDateList) {
        		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(str);
        		dateList.add(date1);
        	}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	Collections.sort(dateList);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateList.get(dateList.size()-1));
    	
    	String currDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+(cal.get(Calendar.MONTH)+1)+"/";
    	currDate = currDate+cal.get(Calendar.YEAR);
    	currDate.trim();
    	if(dobParam!=null)
    		currDate = dob;
    	
    	Query currentQuery = new Query();
    	currentQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(currDate));
    	if(commodity!=null)
    		currentQuery.addCriteria(new Criteria("commodity").is(commodity));
    	if(variety!=null)
    		currentQuery.addCriteria(new Criteria("variety").is(variety));
    	List<MandiRateRecord> currentList=mongoTemplate.find(currentQuery, MandiRateRecord.class);
        
		return currentList;
	}
	
	public List<MandiRateRecord> getFilteredMarketRateData(String phoneNumber,String commodity,String variety,String market,String dobParam){
		User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String districtName = userExist.getAddressModel().getCity().getName();
    	String dob=null;
    	if(dobParam!=null)
    		dob = dobParam.substring(8,10)+"/"+dobParam.substring(5,7)+"/"+dobParam.substring(0,4);    	
    	Criteria criteria = new Criteria("district").is(districtName);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	
    	List<String> arrivalDateList = mongoTemplate.findDistinct(query,"arrival_date",MandiRateRecord.class,String.class);
    	List<Date> dateList = new ArrayList<Date>();
    	try {
    		for(String str:arrivalDateList) {
        		Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(str);
        		dateList.add(date1);
        	}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	Collections.sort(dateList);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateList.get(dateList.size()-1));
    	
    	String currDate="";
    	if(cal.get(Calendar.DATE)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+cal.get(Calendar.DATE)+"/";
    	if(cal.get(Calendar.MONTH)+1<10)
    		currDate = currDate+"0";
    	currDate = currDate+(cal.get(Calendar.MONTH)+1)+"/";
    	currDate = currDate+cal.get(Calendar.YEAR);
    	currDate.trim();
    	if(dobParam!=null)
    		currDate = dob;
    	
    	Query currentQuery = new Query();
    	currentQuery.addCriteria(Criteria.where("district").is(districtName).and("arrival_date").is(currDate));
    	if(commodity!=null)
    		currentQuery.addCriteria(new Criteria("commodity").is(commodity));
    	if(variety!=null)
    		currentQuery.addCriteria(new Criteria("variety").is(variety));
    	if(market!=null)
    		currentQuery.addCriteria(new Criteria("market").is(market));
    	List<MandiRateRecord> currentList=mongoTemplate.find(currentQuery, MandiRateRecord.class);
        
		return currentList;
	}
	
}
