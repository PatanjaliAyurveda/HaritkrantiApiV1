package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.payments.PaymentCycle;
import com.bharuwa.haritkranti.models.payments.SoilTestPayment;
import com.bharuwa.haritkranti.service.PaymentCycleService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport.convertDate;
import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;

/**
 * @author anuragdhunna
 */
@Service
public class PaymentCycleServiceImpl implements PaymentCycleService {

    private final MongoTemplate mongoTemplate;

    public PaymentCycleServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Override
    public PaymentCycle storePaymentCycle(PaymentCycle paymentCycle) {
        String customFromDate = convertDate(paymentCycle.getFromDate());
        String customToDate = convertDate(paymentCycle.getToDate());

        String paymentCycleStr =
                paymentCycle.getCode() + "-" + paymentCycle.getName() + "-" + customFromDate.substring(0,5)
                        + "-" + customToDate.substring(0,5) + "-" + paymentCycle.getNoOfDays();

        paymentCycle.setPaymentCycle(paymentCycleStr);
        paymentCycle.setNoOfDays(paymentCycle.getNoOfDays());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateFormat.format(paymentCycle.getToDate()));
        LocalDateTime endOfToDate = localDate.atTime(LocalTime.MAX);
        Date toDateEnd = Date.from(endOfToDate.atZone(ZoneId.systemDefault()).toInstant());
        paymentCycle.setToDate(toDateEnd);
        System.out.println("=======toDateEnd======="+toDateEnd);

        // TODO: Get latest payment cycle in order to no day is missing between
//        PaymentCycle latestPaymentCycle = getLatestPaymentCycle();
//        if (latestPaymentCycle != null) {
//            Date toDate = latestPaymentCycle.getToDate();
//            Date fromDate = new Date(toDate.getTime() + (1000 * 60 * 60 * 24));
//            paymentCycle.setFromDate(fromDate);
//        }
        return mongoTemplate.save(paymentCycle);
    }

    @Override
    public PaymentCycle getLatestPaymentCycle() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "toDate")).limit(1);
        return mongoTemplate.findOne(query, PaymentCycle.class);
    }

    @Override
    public Page<PaymentCycle> getPaymentCyclesByDate(String fromDate, String toDate, Integer page, Integer size) throws ParseException {

        Criteria criteria = new Criteria();
        Date from;
        Date to;
        if (!StringUtils.isEmpty(fromDate)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            from = dateFormat.parse(fromDate);
            to = dateFormat.parse(toDate);
            criteria = criteria.and("fromDate").gte(from).and("toDate").lte(to);
        }
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return genericMongoTemplate.paginationWithQuery(page, size, query, PaymentCycle.class);
    }

    @Override
    public PaymentCycle getCurrentPaymentCycle() {
        Date now = new Date();
        Query query = new Query(Criteria.where("fromDate").lte(now).and("toDate").gte(now));
        return mongoTemplate.findOne(query, PaymentCycle.class);
    }

    @Override
    public PaymentCycle getPaymentCycle(String paymentCycle) {
        return genericMongoTemplate.findByKey("paymentCycle", paymentCycle, PaymentCycle.class);
    }

    @Override
    public Page<SoilTestPayment> getSoilTestPaymentByFilters(String paymentCycleId, String agentPhoneNumber, String locationType, String locationId, String paymentStatus, String status, int page, int size) {

        Criteria criteria = new Criteria();

        if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
            String searchLocationKey = getLocationKey(locationType);
            if (!StringUtils.isEmpty(searchLocationKey)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        if(!StringUtils.isEmpty(paymentStatus)){
            criteria = criteria.and("paymentStatus").is(paymentStatus);
        }

        if(!StringUtils.isEmpty(status)){
            criteria = criteria.and("status").is(status);
        }

        if(!StringUtils.isEmpty(agentPhoneNumber)){
            User agent = genericMongoTemplate.findByKey("primaryPhone",agentPhoneNumber,User.class);
            if(agent != null) {
                criteria = criteria.and("agent.$id").is(new ObjectId(agent.getId()));
            }
        }

        criteria = criteria.and("paymentCycle.$id").is(new ObjectId(paymentCycleId));

        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return genericMongoTemplate.paginationWithQuery(page, size, query, SoilTestPayment.class);
    }

    @Override
    public SoilTestPayment storeSoilTestPayment(SoilTestPayment soilTestPayment) {
        return mongoTemplate.save(soilTestPayment);
    }

    @Override
    public SoilTestPayment getSoilTestPayment(String soilTestPaymentId) {
        return genericMongoTemplate.findById(soilTestPaymentId,SoilTestPayment.class);
    }

    @Override
    public SoilTestPayment searchSoilTestPayment(String villageId, String khasraNo) {
        Query query = new Query(Criteria.where("address.villageModel.$id").is(new ObjectId(villageId)).and("khasraNo").is(khasraNo))
                .with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);
        return mongoTemplate.findOne(query, SoilTestPayment.class);
    }
}
