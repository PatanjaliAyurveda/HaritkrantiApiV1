package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.payments.*;
import com.bharuwa.haritkranti.models.responseModels.SalaryResponse;
import com.bharuwa.haritkranti.service.CommissionService;
import com.bharuwa.haritkranti.service.EmployeeHistoryService;
import com.bharuwa.haritkranti.service.RoleService;
import com.bharuwa.haritkranti.service.RateService;
import com.bharuwa.haritkranti.service.SalaryService;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bharuwa.haritkranti.models.payments.SoilTestPayment.Status.Approved;
import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author anuragdhunna
 */
@Service
public class SalaryServiceImpl implements SalaryService {
    private final MongoTemplate mongoTemplate;

    public SalaryServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private RateService rateService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserSalary storeSalary(UserSalary salary) {
        UserSalary currentSalary = getCurrentSalary(salary.getUser().getId());
        if (currentSalary != null && currentSalary.getSalary().compareTo(salary.getSalary()) > 0) {
            salary.setPromotionType(UserSalary.PromotionType.Decrement);
        }
        return mongoTemplate.save(salary);
    }

    @Override
    public UserSalary getCurrentSalary(String userId) {
        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query(Criteria.where("user.$id").is(new ObjectId(userId)))
                .with(Sort.by(Sort.Direction.DESC, "creationDate")).limit(1);

        return mongoTemplate.findOne(query, UserSalary.class);
    }

    @Override
    public List<UserSalary> getUserSalaryHistory(String userId) {
        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query(Criteria.where("user").is(user)).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        return mongoTemplate.find(query, UserSalary.class);
    }

    @Override
    public BigDecimal calculateAgentSalary(String userId, String fromDate, String toDate, String paymentCycleId, String locationType, String locationId) throws ParseException {

        Criteria criteria = Criteria.where("agentId").is(userId);
        Date from;
        Date to;
        if (!StringUtils.isEmpty(fromDate)) {
            System.out.println("===fromDate=====");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            from = dateFormat.parse(fromDate);
            to = dateFormat.parse(toDate);
            criteria = criteria.and("creationDate").gte(from).lt(to);
        }
        if (!StringUtils.isEmpty(paymentCycleId)) {
            System.out.println("===paymentCycleId=====");
            PaymentCycle paymentCycle = genericMongoTemplate.findById(paymentCycleId, PaymentCycle.class);
            if (paymentCycle == null) {
                throw new ResourceNotFoundException("Payment Cycle not found.");
            }
            from = paymentCycle.getFromDate();
            to = paymentCycle.getToDate();
            criteria = criteria.and("creationDate").gte(from).lt(to);
        }

        if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
            String searchLocationKey = getLocationKey(locationType);
            if (!StringUtils.isEmpty(searchLocationKey)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        criteria = criteria.and("status").is(Approved);

        Query query = new Query(criteria);
        List<SoilTestPayment> soilTests = mongoTemplate.find(query, SoilTestPayment.class);

        BigDecimal salary = BigDecimal.ZERO;
        for (SoilTestPayment soilTestPayment : soilTests) {
            salary = salary.add(soilTestPayment.getKhasraRate());
        }
        return salary;
    }

    @Override
    public SalaryResponse calculateManagerSalary(String userId, String fromDate, String toDate) throws ParseException {

        SalaryResponse salaryResponse;

        User manager = genericMongoTemplate.findById(userId, User.class);
        if (manager == null) {
            throw new ResourceNotFoundException("Manager not found.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);
        final Date to = dateFormat.parse(toDate);

        // get days in current month
        int daysInMonth = getNumberOfDaysInMonth(from);

        // get current salary
//        UserSalary userSalary = getCurrentSalary(userId);
//        if (userSalary == null) {
//            throw new CustomException("Manager's Salary not found");
//        }
//        BigDecimal salary = userSalary.getSalary();
        BigDecimal salary = getUserSalary(userId,from);

        // TODO:
        // Calculate Salary till date according to Current Salary
        // Calculate Number of Days Worked in current Month
        int workingDays = 0;

        // Get Employee Status List of Current Month
        List<EmployeeHistory> employeeHistoryList = employeeHistoryService.getEmployeeHistoryByDate(userId,fromDate,toDate);

        if(!employeeHistoryList.isEmpty()){
            Date activeDate = null;
            Date notActiveDate = null;
            for(EmployeeHistory employeeHistory : employeeHistoryList) {

                if (employeeHistory.getStatus().equals(EmployeeHistory.Status.ACTIVE)) {

                    // get Date of Current Month when user is Active
                    if (activeDate == null) {
                        activeDate = employeeHistory.getCreationDate();
                        // if only one History for ACTIVE till date
                        if(employeeHistoryList.size() == 1){
                            long diffInMillies = Math.abs(to.getTime() - activeDate.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            workingDays = workingDays + (int) diff;
                        }
                    } else {
                        // if multiple Active records
                        if (activeDate.before(employeeHistory.getCreationDate()) && notActiveDate != null) {
                            EmployeeHistory historyAfterCurrentDate = employeeHistoryService.getEmployeeHistoryAfterDate(userId,dateFormat.format(employeeHistory.getCreationDate()));
                            // check if another Active history includes after current date and before to date
                            // if true then update active Date
                            if(historyAfterCurrentDate != null && historyAfterCurrentDate.getStatus().equals(EmployeeHistory.Status.INACTIVE) &&historyAfterCurrentDate.getCreationDate().before(to)) {
                                activeDate = employeeHistory.getCreationDate();
                            }
                            else {
                                // add working days till to date
                                long diffInMillies = Math.abs(to.getTime() - employeeHistory.getCreationDate().getTime());
                                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                workingDays = workingDays + (int) diff;
                                activeDate = employeeHistory.getCreationDate();
                            }
                        }
                    }
                } else {
                    if (notActiveDate == null) {
                        notActiveDate = employeeHistory.getCreationDate();

                        // if only one History for INACTIVE till date
                        if(employeeHistoryList.size() == 1){
                            EmployeeHistory employeeHistoryBeforeCurrentDate = employeeHistoryService.getEmployeeHistoryBeforeDate(userId, dateFormat.format(notActiveDate));
                            if (employeeHistoryBeforeCurrentDate != null && employeeHistoryBeforeCurrentDate.getStatus().equals(EmployeeHistory.Status.ACTIVE) ) {

                                long diffInMillies = Math.abs(notActiveDate.getTime() - from.getTime());
                                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                workingDays = workingDays + (int) diff;
                                System.out.println("=========only one not Active=============="+workingDays);

                            }
                        } else if(activeDate != null){
                            // if Active in current month
                            long diffInMillies = Math.abs(notActiveDate.getTime() - activeDate.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            workingDays = workingDays + (int) diff;
                            System.out.println("=========only one Active===and one not active==========="+workingDays);

                        }
                    } else {
                        // if multiple INACTIVE records
                        if (notActiveDate.before(employeeHistory.getCreationDate())) {
                            long diffInMillies = Math.abs(employeeHistory.getCreationDate().getTime() - activeDate.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            workingDays = workingDays + (int) diff;
                            notActiveDate = employeeHistory.getCreationDate();
                            System.out.println("========= multiple not Active=============="+workingDays);

                        }
                    }
                }
            }
        } else {
            // Check if the Employee is Terminated in current Month
            EmployeeHistory recentEmployeeHistory = employeeHistoryService.getRecentEmployeeHistory(userId);
            if (recentEmployeeHistory.getStatus().equals(EmployeeHistory.Status.ACTIVE)) {
                workingDays = daysInMonth;
            }
        }

        System.out.println("==============="+daysInMonth);


        double averageWorking  = (double)workingDays/(double)daysInMonth;

        System.out.println("====averageWorking===="+averageWorking);

        BigDecimal salaryByWorkingDays = salary.multiply(BigDecimal.valueOf(averageWorking));

        System.out.println("===========total working days=============="+workingDays);
        System.out.println("===========Salary =============="+salary);
        System.out.println("===========Salary by working days=============="+salaryByWorkingDays);


        // Calculate Commission only for Block Manager
        Role agentManager = roleService.roleName(ROLE_AGENT_MANAGER);
        if (!manager.getRoles().contains(agentManager)) {
            salaryResponse = new SalaryResponse();
            salaryResponse.setBasicSalary(salaryByWorkingDays);
            salaryResponse.setTotalSalary(salaryByWorkingDays);
            return salaryResponse;
        }

        Criteria criteria = Criteria.where("managerId").is(userId);
        if (!StringUtils.isEmpty(fromDate)) {
            criteria.and("creationDate").gte(from).lt(to);
        }

        Query query = new Query(criteria);
        List<SoilTestPayment> soilTestPayments = mongoTemplate.find(query, SoilTestPayment.class);

        BigDecimal commission = BigDecimal.ZERO;
        for (SoilTestPayment soilTestPayment : soilTestPayments) {
            commission = commission.add(soilTestPayment.getCommissionRate());
        }
        BigDecimal totalSalary = salaryByWorkingDays.add(commission);
        System.out.println("===========total=====commission===="+commission);
        System.out.println("===========total=====commission===="+ totalSalary);

        // calculate commission on the basis of Soil Test done by an Agent

        salaryResponse = new SalaryResponse();
        salaryResponse.setBasicSalary(salaryByWorkingDays);
        salaryResponse.setCommission(commission);
        salaryResponse.setTotalSalary(totalSalary);

        return salaryResponse;
    }

    private int getNumberOfDaysInMonth(Date from) {
        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(from.getYear(), from.getMonth(), 1);
        // Get the number of days in that month
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private BigDecimal getUserSalary(String userId, Date from) {

        BigDecimal totalSalary = BigDecimal.ZERO;
        UserSalary userSalary = getCurrentSalary(userId);
        if (userSalary == null) {
            throw new CustomException("Manager's Salary not found");
        }
        // if salary updated after month's starting date then consider last salary
        if(!userSalary.getCreationDate().before(from)){
            UserSalary salary = getUserSalaryBeforeDate(userId,from);
            if(salary != null) {
                totalSalary = salary.getSalary();
            } else {
                totalSalary = userSalary.getSalary();
            }
        } else {
            totalSalary = userSalary.getSalary();
        }
        return totalSalary;
    }

    private UserSalary getUserSalaryBeforeDate(String userId, Date from) {
        User user = genericMongoTemplate.findById(userId, User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query(Criteria.where("user").is(user).and("creationDate").lt(from)).with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.findOne(query, UserSalary.class);
    }

}
