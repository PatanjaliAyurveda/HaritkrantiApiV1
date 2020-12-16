package com.bharuwa.haritkranti.service.newservice;

import java.util.List;

import com.bharuwa.haritkranti.models.newmodels.Employee;

public interface EmployeeService {
	public void saveEmployeeRecords(Employee emp);
	public List<Employee> findAllEmployee();
}
