package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.repositories.DealerRepo;
import com.bharuwa.haritkranti.models.Dealer;
import com.bharuwa.haritkranti.service.DealerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerServiceImpl implements DealerService {

    private final MongoTemplate mongoTemplate;

    public DealerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private DealerRepo dealerRepo;


    @Override
    public void createDealer(Dealer dealer) {
        Query query = new Query();
        query.addCriteria(Criteria.where("nameOfFarm").is(dealer.getNameOfFarm()).and("address").is(dealer.getAddress().trim()).and("phoneNumber").is(dealer.getPhoneNumber()));
        Dealer value = mongoTemplate.findOne(query, Dealer.class);

        if (value == null) {
            dealerRepo.save(dealer);
        }else {
            value.setNameOfFarm(dealer.getNameOfFarm());
            value.setCity(dealer.getCity());
            value.setState(dealer.getState());
            value.setAddress(dealer.getAddress());
            value.setLocation(dealer.getLocation());
            value.setTelephone(dealer.getTelephone());
            value.setPhoneNumber(dealer.getPhoneNumber());
            value.setFertLicenseNo(dealer.getFertLicenseNo());
            value.setContactPerson(dealer.getContactPerson());
            value.setOfficerName(dealer.getOfficerName());
            value.setPincode(dealer.getPincode());
            dealerRepo.save(value);
        }

    }

    @Override
    public List<Dealer> getDealersByState(String stateId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("state._id").is(new ObjectId(stateId)));
        return mongoTemplate.find(query, Dealer.class);
    }

}
