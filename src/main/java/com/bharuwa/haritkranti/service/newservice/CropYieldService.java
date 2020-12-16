package com.bharuwa.haritkranti.service.newservice;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;

public interface CropYieldService {
	public List<CropYield> getCropYieldList();
	public List<String> getTehsilList(String districtName);
	public List<String> getBlockList(String tehsilName);
	public List<String> getVillageList(String blockName);
	public List<String> getKhasraList(String villageName);
	public CropYield getCropYield(String blockName,String villageName,String khasraNumber);
	public FarmCordinate getFarmCordinate(String blockName,String villageName,String khasraNumber);
}
