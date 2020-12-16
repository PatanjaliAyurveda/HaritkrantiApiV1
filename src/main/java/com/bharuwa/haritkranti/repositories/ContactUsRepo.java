package com.bharuwa.haritkranti.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.ContactUs;

public interface ContactUsRepo extends MongoRepository<ContactUs, String>{

}
