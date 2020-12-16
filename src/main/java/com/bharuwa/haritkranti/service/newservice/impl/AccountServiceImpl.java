package com.bharuwa.haritkranti.service.newservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.models.newmodels.Account;
import com.bharuwa.haritkranti.models.newmodels.Employee;
import com.bharuwa.haritkranti.repositories.newrepositories.AccountRepo;
import com.bharuwa.haritkranti.service.newservice.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
    private AccountRepo repo;
	
	@Override
	public void addAccount(Account acc) {
		// TODO Auto-generated method stub
		repo.save(acc);
	}
	
	@Override
	public List<Account> findAllAccount() {
		List<Account> empList = null;
		try {
			//mongoTemplate.findAll(Employee.class);
			empList=repo.findAll();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return empList;
	}

}
