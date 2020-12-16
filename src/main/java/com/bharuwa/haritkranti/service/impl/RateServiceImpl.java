package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.payments.Rate;
import com.bharuwa.haritkranti.models.requestModels.RateReqBody;
import com.bharuwa.haritkranti.service.RateService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;

/**
 * @author anuragdhunna
 */
@Service
public class RateServiceImpl implements RateService {

    private final MongoTemplate mongoTemplate;

    public RateServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;


    @Override
    public Rate storeRate(RateReqBody rateReqBody) {
        Rate rate = new Rate();
        Address address = new Address();
        switch (rateReqBody.getLocationType().toString()) {
            case "STATE":
                State state = genericMongoTemplate.findById(rateReqBody.getLocationId(),State.class);
                address.setState(state);
                rate.setAddress(address);
                rate.setLocationType(rateReqBody.getLocationType());
                break;
            case "DISTRICT":
                City city = genericMongoTemplate.findById(rateReqBody.getLocationId(),City.class);
                address.setState(city.getState());
                address.setCity(city);
                rate.setAddress(address);
                rate.setLocationType(rateReqBody.getLocationType());
                break;
            case "TEHSIL":
                Tehsil tehsil = genericMongoTemplate.findById(rateReqBody.getLocationId(),Tehsil.class);
                address.setState(tehsil.getState());
                address.setCity(tehsil.getCity());
                address.setTehsil(tehsil);
                rate.setAddress(address);
                rate.setLocationType(rateReqBody.getLocationType());
                break;
            case "BLOCK":
                Block block = genericMongoTemplate.findById(rateReqBody.getLocationId(),Block.class);
                address.setState(block.getState());
                address.setCity(block.getCity());
                address.setTehsil(block.getTehsil());
                address.setBlock(block);
                rate.setAddress(address);
                rate.setLocationType(rateReqBody.getLocationType());
                break;
            case "VILLAGE":
                Village village = genericMongoTemplate.findById(rateReqBody.getLocationId(),Village.class);
                address.setState(village.getState());
                address.setCity(village.getCity());
                address.setTehsil(village.getTehsil());
                address.setBlock(village.getBlock());
                address.setVillageModel(village);
                rate.setAddress(address);
                rate.setLocationType(rateReqBody.getLocationType());
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        rate.setNewFarmerKhasraRate(rateReqBody.getNewFarmerKhasraRate());
        rate.setNormalKhasraRate(rateReqBody.getNormalKhasraRate());
        rate.setModifiedByUserId(rateReqBody.getModifiedByUserId());
        rate.setBasicDetailsRate(rateReqBody.getBasicDetailsRate());
        return mongoTemplate.save(rate);
    }

    @Override
    public Rate getCurrentRate() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);
        return mongoTemplate.findOne(query, Rate.class);
    }

    @Override
    public Page<Rate> getRateList(int page, int size, String fromDate, String toDate, String locationType, String locationId) throws ParseException {
        Criteria criteria = new Criteria();

        if(!StringUtils.isEmpty(locationType) && !StringUtils.isEmpty(locationId)){
            String searchLocationKey = getLocationKey(locationType);
            criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
        }

        if (!StringUtils.isEmpty(fromDate)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            final Date from = dateFormat.parse(fromDate);
            final Date to = dateFormat.parse(toDate);

            criteria = criteria.and("creationDate").gte(from).lt(to);

        }
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return genericMongoTemplate.paginationWithQuery(page, size, query, Rate.class);
    }

    @Override
    public Rate getCurrentRateByLocation(String locationType, String locationId) {

        String searchLocationKey = getLocationKey(locationType);

        Query query = new Query(Criteria.where(searchLocationKey).is(new ObjectId(locationId)).and("locationType").is(locationType))
                .with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);
        return mongoTemplate.findOne(query, Rate.class);
    }
}
