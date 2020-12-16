package com.bharuwa.haritkranti.service;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.CropSelection;

public interface CropAdvisoryService {
	public List<String> getCropList();
	public List<String> getCropVarietyList(String cropId);
	public List<CropSelection> getCropSelectionList(String cropId,String varietyId);
}
