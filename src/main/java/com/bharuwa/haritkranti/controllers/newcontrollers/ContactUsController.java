package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.newmodels.Alert;
import com.bharuwa.haritkranti.models.newmodels.ContactUs;
import com.bharuwa.haritkranti.models.newmodels.NewsAndFeeds;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ContactUsController extends BaseController{
	@RequestMapping(value = "/getContactUsList",method = RequestMethod.GET)
	public List<ContactUs> getContactUsList(@RequestParam String phoneNumber){
    	return contactUsService.getContactUsList(phoneNumber);
    }
}
