package com.bharuwa.haritkranti.service.newservice;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.MandiRateBean;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;


public interface MandiRateService {
	
	public void saveMandiRateRecords(MandiRateRecord rate);
	
	public List<MandiRateRecord>  getMandiRate(String phoneNumber);
	
//	public List<MandiRateRecord> getMandiRateData(MandiRateBean bean);
	
	public List<MandiRateRecord> getMandiRateData(String phoneNumber,String commodity);
	
	public List<String> getCommodityList(String phoneNumber);
	
	public List<String> getVarietyList(String commodity,String phoneNumber);
	
//	public List<MandiRateRecord> getFilteredMandiRateData(MandiRateBean bean);
	
	public List<String> getMarketList(String phoneNumber);
	
	public List<MandiRateRecord> getFilteredMarketRateData(String phoneNumber,String commodity,String variety,String market,String dobParam);
	
	public List<MandiRateRecord> getFilteredMandiRateData(String phoneNumber,String commodity,String variety,String dobParam);
	
	public List<MandiRateRecord> getMandiRateForAnnadata(String district);
	
}
