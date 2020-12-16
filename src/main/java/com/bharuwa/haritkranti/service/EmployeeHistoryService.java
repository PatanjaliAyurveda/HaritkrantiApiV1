package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface EmployeeHistoryService {

    EmployeeHistory storeEmployeeHistory(EmployeeHistory employeeHistory);

    Page<EmployeeHistory> getEmployeeHistory(String userId, int page, int size, String fromDate, String toDate) throws ParseException;

    List<EmployeeHistory> getEmployeeHistoryByDate(String userId, String fromDate, String toDate) throws ParseException;

    EmployeeHistory getRecentEmployeeHistory(String userId);

    EmployeeHistory getEmployeeHistoryBeforeDate(String userId, String fromDate) throws ParseException;

    EmployeeHistory getEmployeeHistoryAfterDate(String userId, String fromDate) throws ParseException;

    EmployeeAssignmentHistory storeEmployeeAssignmentHistory(EmployeeAssignmentHistory employeeAssignmentHistory);

    List<EmployeeAssignmentHistory> getEmployeeAssignmentHistory(String userId);

    MessageResponse organiseEmployeeAssignmentHistoryByUserType(String userRoleName, String locationType, String locationId);

    User getAssignedUserByDate(User user, Date date);
}
