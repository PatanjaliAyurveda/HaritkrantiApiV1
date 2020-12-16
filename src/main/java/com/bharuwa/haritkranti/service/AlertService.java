package com.bharuwa.haritkranti.service;

import java.util.List;
import com.bharuwa.haritkranti.models.newmodels.Alert;

public interface AlertService {
	public List<Alert> getAlert(String phoneNumber);
}
