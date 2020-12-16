package com.bharuwa.haritkranti.controllers.newcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bharuwa.haritkranti.controllers.BaseController;
import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.Account;
import com.bharuwa.haritkranti.models.newmodels.Employee;
import com.bharuwa.haritkranti.models.newmodels.MandiRate;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class EmployeeController extends BaseController{
	
	@RequestMapping(value = "/saveEmployee",method = RequestMethod.GET)
    @ResponseBody
    public void saveEmployee(){
		Employee emp= new Employee();
		emp.setId(2);
		emp.setName("Saurav");
		Employee emp1= new Employee();
		emp.setId(1);
		emp.setName("Sahil");
		
		Account ad=new Account();
		ad.setId(1);
		ad.setAccountType("Saving");
		ad.setAccountNumber("12200000234512");
		emp.setAccount(ad);
		accountService.addAccount(ad);
	    employeeService.saveEmployeeRecords(emp);
    }
	
	@RequestMapping(value = "/getAllEmployee",method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> getAllEmployee(){
        return employeeService.findAllEmployee();
    }
	
	@RequestMapping(value = "/getAllAccounts",method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getAllAccounts(){
        return accountService.findAllAccount();
    }
}
