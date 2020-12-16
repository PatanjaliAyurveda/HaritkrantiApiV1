package com.bharuwa.haritkranti.service;

import java.util.List;
import com.bharuwa.haritkranti.models.newmodels.ContactUs;

public interface ContactUsService {
	public List<ContactUs> getContactUsList(String phoneNumber);
}
