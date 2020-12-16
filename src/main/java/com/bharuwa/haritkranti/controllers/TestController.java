package com.bharuwa.haritkranti.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bharuwa.haritkranti.models.Employee;
import com.bharuwa.haritkranti.models.crops.CropGroup;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TestController {
	
	@GetMapping(value="/getEmployeeList",produces = MediaType.APPLICATION_ATOM_XML_VALUE)
	@ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeeList() {
		List<Employee> empList = new ArrayList<Employee>();
		empList.add(new Employee("1","Sonu"));
		empList.add(new Employee("2","Vimal"));
        return empList;
    }
	
	@GetMapping("/getEmployee")
	public Employee getEmployee(@RequestParam long id) {
		return new Employee("1","Sonu");
	}
}
