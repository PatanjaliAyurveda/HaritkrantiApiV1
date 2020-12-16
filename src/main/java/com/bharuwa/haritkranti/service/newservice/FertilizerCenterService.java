package com.bharuwa.haritkranti.service.newservice;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.FertilizerCenter;

public interface FertilizerCenterService {
	public List<FertilizerCenter> getFertilizerCenterList(String phoneNumber);
}
