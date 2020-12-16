package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.payments.CommissionRate;
import com.bharuwa.haritkranti.models.requestModels.CommissionReqBody;
import com.bharuwa.haritkranti.service.CommissionService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;

/**
 * @author anuragdhunna
 */
@Service
public class CommissionServiceImpl implements CommissionService {

    private final MongoTemplate mongoTemplate;

    public CommissionServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    GenericMongoTemplate genericMongoTemplate;

    @Override
    public CommissionRate storeCommissionRate(CommissionReqBody commissionReqBody) {
        CommissionRate commissionRate = new CommissionRate();
        Address address = new Address();
        switch (commissionReqBody.getLocationType().toString()) {
            case "STATE":
                State state = genericMongoTemplate.findById(commissionReqBody.getLocationId(),State.class);
                address.setState(state);
                commissionRate.setAddress(address);
                commissionRate.setLocationType(commissionReqBody.getLocationType());
                break;
            case "DISTRICT":
                City city = genericMongoTemplate.findById(commissionReqBody.getLocationId(),City.class);
                address.setState(city.getState());
                address.setCity(city);
                commissionRate.setAddress(address);
                commissionRate.setLocationType(commissionReqBody.getLocationType());
                break;
            case "TEHSIL":
                Tehsil tehsil = genericMongoTemplate.findById(commissionReqBody.getLocationId(),Tehsil.class);
                address.setState(tehsil.getState());
                address.setCity(tehsil.getCity());
                address.setTehsil(tehsil);
                commissionRate.setAddress(address);
                commissionRate.setLocationType(commissionReqBody.getLocationType());
                break;
            case "BLOCK":
                Block block = genericMongoTemplate.findById(commissionReqBody.getLocationId(),Block.class);
                address.setState(block.getState());
                address.setCity(block.getCity());
                address.setTehsil(block.getTehsil());
                address.setBlock(block);
                commissionRate.setAddress(address);
                commissionRate.setLocationType(commissionReqBody.getLocationType());
                break;
            case "VILLAGE":
                Village village = genericMongoTemplate.findById(commissionReqBody.getLocationId(),Village.class);
                address.setState(village.getState());
                address.setCity(village.getCity());
                address.setTehsil(village.getTehsil());
                address.setBlock(village.getBlock());
                address.setVillageModel(village);
                commissionRate.setAddress(address);
                commissionRate.setLocationType(commissionReqBody.getLocationType());
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        commissionRate.setRate(commissionReqBody.getRate());
        return mongoTemplate.save(commissionRate);
    }

    @Override
    public CommissionRate getCurrentCommissionRateByLocation(String locationType, String locationId) {

        String searchLocationKey = getLocationKey(locationType);

        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)).and("locationType").is(locationType))
                .with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);
        return mongoTemplate.findOne(query, CommissionRate.class);
    }

    @Override
    public Page<CommissionRate> getCommissionRateHistoryByLocation(String locationType, String locationId, String fromDate, String toDate, Integer page, Integer size) throws ParseException {
        Criteria criteria = new Criteria();

        Date from;
        Date to;
        if (!StringUtils.isEmpty(fromDate)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            from = dateFormat.parse(fromDate);
            to = dateFormat.parse(toDate);
            criteria = criteria.and("creationDate").gte(from).lt(to);
        }

        if(!StringUtils.isEmpty(locationType) && !StringUtils.isEmpty(locationId)){
            String searchLocationKey = getLocationKey(locationType);

            criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
        }

        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return genericMongoTemplate.paginationWithQuery(page, size, query, CommissionRate.class);
    }

}