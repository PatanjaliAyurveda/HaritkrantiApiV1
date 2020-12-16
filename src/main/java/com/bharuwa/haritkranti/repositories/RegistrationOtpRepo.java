package com.bharuwa.haritkranti.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bharuwa.haritkranti.models.RegistrationOtp;

public interface RegistrationOtpRepo extends MongoRepository<RegistrationOtp,String> {
	@Query("{ 'phoneNumber' : ?0,'otp' : ?1 }")
	RegistrationOtp findByMobileNumber(String phoneNumber,String regOtp);
}
