package com.bharuwa.haritkranti.service.newservice;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.Account;


public interface AccountService {
	public void addAccount(Account acc);
	public List<Account> findAllAccount();
}
