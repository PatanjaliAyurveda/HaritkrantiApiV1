package com.bharuwa.haritkranti.service.newservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.Employee;
import com.bharuwa.haritkranti.repositories.newrepositories.EmployeeRepo;
import com.bharuwa.haritkranti.service.newservice.EmployeeService;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	//private MongoTemplate mongoTemplate;
	
	@Autowired
    private EmployeeRepo repo;
	
	@Override
	public void saveEmployeeRecords(Employee emp) {
		try {
			repo.save(emp);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public List<Employee> findAllEmployee() {
		List<Employee> empList = null;
		try {
			//mongoTemplate.findAll(Employee.class);
			empList=repo.findAll();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return empList;
	}
	
	

}
