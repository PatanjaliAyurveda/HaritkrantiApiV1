package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.Role;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.service.EmployeeHistoryService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.utils.MessageResponse;
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
import java.util.List;

import static com.bharuwa.haritkranti.service.impl.AnalyticsServiceImpl.getLocationKeyLandDetails;
import static com.bharuwa.haritkranti.service.impl.CountServiceImpl.getLocationKeyFromUser;
import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;
import static com.bharuwa.haritkranti.service.impl.ReportHistoryServiceImpl.getLocationKeyVillageAsset;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.Constants.ROLE_AGENT_MANAGER;
import static com.bharuwa.haritkranti.utils.HelperMethods.*;

/**
 * @author anuragdhunna
 */
@Service
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

    private final MongoTemplate mongoTemplate;

    public EmployeeHistoryServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Override
    public EmployeeHistory storeEmployeeHistory(EmployeeHistory employeeHistory) {
        return mongoTemplate.save(employeeHistory);
    }

    @Override
    public Page<EmployeeHistory> getEmployeeHistory(String userId, int page, int size, String fromDate, String toDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);
        final Date to = dateFormat.parse(toDate);

        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        Criteria criteria = Criteria.where("user").is(user);
        Query query;
        if (!StringUtils.isEmpty(fromDate)) {
            query = new Query(criteria
                    .and("creationDate").gte(from).lt(to));
            return genericMongoTemplate.paginationWithQuery(page, size, query, EmployeeHistory.class);
        }

        query = new Query(criteria);
        return genericMongoTemplate.paginationWithQuery(page, size, query, EmployeeHistory.class);
    }

    @Override
    public List<EmployeeHistory> getEmployeeHistoryByDate(String userId, String fromDate, String toDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);
        final Date to = dateFormat.parse(toDate);

        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        Query query = new Query(Criteria.where("user").is(user).and("creationDate").gte(from).lt(to)).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        return mongoTemplate.find(query, EmployeeHistory.class);
    }

    @Override
    public EmployeeHistory getRecentEmployeeHistory(String userId) {
        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        Query query = new Query(Criteria.where("user").is(user)).with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);
        return mongoTemplate.findOne(query, EmployeeHistory.class);
    }

    @Override
    public EmployeeHistory getEmployeeHistoryBeforeDate(String userId, String fromDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);

        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        Query query = new Query(Criteria.where("user").is(user).and("creationDate").lt(from)).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.findOne(query, EmployeeHistory.class);
    }

    @Override
    public EmployeeHistory getEmployeeHistoryAfterDate(String userId, String fromDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);
        Date newDate = addDays(from, 1);


        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        Query query = new Query(Criteria.where("user").is(user).and("creationDate").gte(newDate)).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        return mongoTemplate.findOne(query, EmployeeHistory.class);
    }

    @Override
    public EmployeeAssignmentHistory storeEmployeeAssignmentHistory(EmployeeAssignmentHistory employeeAssignmentHistory) {
        User toUser = mongoTemplate.findById(employeeAssignmentHistory.getToUser().getId(),User.class);
        if(toUser == null){
            throw new ResourceNotFoundException("User not found");
        }
        employeeAssignmentHistory.setAddress(toUser.getAddressModel());
        return mongoTemplate.save(employeeAssignmentHistory);
    }

    @Override
    public List<EmployeeAssignmentHistory> getEmployeeAssignmentHistory(String userId) {
        Query query = new Query(Criteria.where("toUser.$id").is(new ObjectId(userId))).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.find(query, EmployeeAssignmentHistory.class);
    }

    @Override
    public MessageResponse organiseEmployeeAssignmentHistoryByUserType(String userRoleName, String locationType, String locationId) {
        System.out.println("=======userRoleName==========="+userRoleName);
        Role nationalManager = genericMongoTemplate.findByKey("roleName",ROLE_NATIONAL_MANAGER,Role.class);
        Role stateManager = genericMongoTemplate.findByKey("roleName",ROLE_STATE_MANAGER,Role.class);
        Role districtManager = genericMongoTemplate.findByKey("roleName",ROLE_DISTRICT_MANAGER,Role.class);
        Role blockManager = genericMongoTemplate.findByKey("roleName",ROLE_AGENT_MANAGER,Role.class);

        Criteria criteria = new Criteria();
        if(userRoleName.equals(ROLE_AGENT)){
            criteria = criteria.and("roles").size(1);
        }

        if(!StringUtils.isEmpty(locationType) && !StringUtils.isEmpty(locationId)){
            // get search location key from user collection
            String searchLocationKey = getLocationKeyFromUser(locationType);
            criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
        }


        criteria = criteria.and("roles.roleName").is(userRoleName);
        Query query = new Query(criteria);
        List<User> all = mongoTemplate.find(query,User.class);

        System.out.println("=======size==========="+all.size());
        for (User user: all) {
            System.out.println("=======primaryPhone==========="+user.getPrimaryPhone());
            EmployeeAssignmentHistory employeeAssignmentHistory = checkEmployeeAssignmentHistoryExist(user);
            if(employeeAssignmentHistory == null) {
                employeeAssignmentHistory = new EmployeeAssignmentHistory();
                if(!StringUtils.isEmpty(user.getCreatedByUserId())) {
                    User fromUser = genericMongoTemplate.findById(user.getCreatedByUserId(),User.class);
                    if(fromUser != null){
                        employeeAssignmentHistory.setFromUser(fromUser);
                        employeeAssignmentHistory.setToUser(user);
                        // address stored for search
                        employeeAssignmentHistory.setAddress(user.getAddressModel());
                        employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(userRoleName, fromUser,nationalManager,stateManager,districtManager,blockManager));
                        employeeAssignmentHistory.setAssignmentDate(user.getCreationDate());
                        storeEmployeeAssignmentHistory(employeeAssignmentHistory);
                    }
                }
            } else {
                // update Address in existing Object
                employeeAssignmentHistory.setAddress(user.getAddressModel());
                storeEmployeeAssignmentHistory(employeeAssignmentHistory);

            }

        }
        MessageResponse response = new MessageResponse();
        response.setMessage("Success");
        return response;
    }

    private EmployeeAssignmentHistory checkEmployeeAssignmentHistoryExist(User user) {
        if(!StringUtils.isEmpty(user.getCreatedByUserId())) {
            User fromUser = mongoTemplate.findById(user.getCreatedByUserId(), User.class);
            Query query = new Query(Criteria.where("toUser").is(user).and("fromUser").is(fromUser));
            return mongoTemplate.findOne(query, EmployeeAssignmentHistory.class);
        }
        return null;
    }

    @Override
    public User getAssignedUserByDate(User user, Date date) {
        Query query;
        query = new Query(Criteria.where("toUser").is(user).and("assignmentDate").lte(date)).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        EmployeeAssignmentHistory employeeAssignmentHistoryBeforeDate = mongoTemplate.findOne(query, EmployeeAssignmentHistory.class);

        if(employeeAssignmentHistoryBeforeDate == null){

            query = new Query(Criteria.where("toUser").is(user).and("assignmentDate").gte(date)).with(Sort.by(Sort.Direction.ASC, "creationDate"));
            EmployeeAssignmentHistory employeeAssignmentHistoryAfterDate = mongoTemplate.findOne(query, EmployeeAssignmentHistory.class);

            if(employeeAssignmentHistoryAfterDate == null){
                return null;
            } else {
                return employeeAssignmentHistoryAfterDate.getFromUser();
            }
        }
        return employeeAssignmentHistoryBeforeDate.getFromUser();
    }
}
