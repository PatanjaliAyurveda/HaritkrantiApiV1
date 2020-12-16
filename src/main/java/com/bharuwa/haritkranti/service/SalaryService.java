package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.payments.UserSalary;
import com.bharuwa.haritkranti.models.responseModels.SalaryResponse;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface SalaryService {

    UserSalary storeSalary(UserSalary salary);
    UserSalary getCurrentSalary(String userId);
    List<UserSalary> getUserSalaryHistory(String userId);

    BigDecimal calculateAgentSalary(String userId, String from, String to, String paymentCycleId, String locationType, String locationId) throws ParseException;

    SalaryResponse calculateManagerSalary(String userId, String fromDate, String toDate) throws ParseException;
}
