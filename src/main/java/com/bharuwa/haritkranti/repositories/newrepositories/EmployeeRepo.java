package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bharuwa.haritkranti.models.newmodels.Employee;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;

@Repository
public interface EmployeeRepo extends MongoRepository<Employee, Long>{

}
