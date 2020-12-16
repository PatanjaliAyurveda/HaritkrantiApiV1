/*
 * package com.bharuwa.haritkranti.controllers.payments;
 * 
 * import com.bharuwa.haritkranti.controllers.BaseController; import
 * com.bharuwa.haritkranti.models.User; import
 * com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory; import
 * com.bharuwa.haritkranti.models.payments.EmployeeHistory; import
 * com.bharuwa.haritkranti.utils.MessageResponse; import
 * org.springframework.data.domain.Page; import
 * org.springframework.lang.Nullable; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.text.ParseException; import java.util.Date; import
 * java.util.List;
 * 
 *//**
	 * @author anuragdhunna
	 *//*
		 * @CrossOrigin(origins = "*", maxAge = 3600)
		 * 
		 * @RestController
		 * 
		 * @RequestMapping("/api") public class EmployeeHistoryController extends
		 * BaseController {
		 * 
		 * @RequestMapping(value = "/storeEmployeeHistory", method = RequestMethod.POST)
		 * public EmployeeHistory storeEmployeeHistory(@RequestBody EmployeeHistory
		 * employeeHistory) { return
		 * employeeHistoryService.storeEmployeeHistory(employeeHistory); }
		 * 
		 * @RequestMapping(value = "/getEmployeeHistoryByDate", method =
		 * RequestMethod.GET) public List<EmployeeHistory>
		 * getEmployeeHistoryByDate(@RequestParam String userId,
		 * 
		 * @Nullable @RequestParam String fromDate,
		 * 
		 * @Nullable @RequestParam String toDate) throws ParseException { return
		 * employeeHistoryService.getEmployeeHistoryByDate(userId, fromDate, toDate); }
		 * 
		 * @RequestMapping(value = "/getEmployeeHistory", method = RequestMethod.GET)
		 * public Page<EmployeeHistory> getEmployeeHistory(@RequestParam String userId,
		 * 
		 * @RequestParam(value = "page",defaultValue = "0")int page,
		 * 
		 * @RequestParam(value = "size",defaultValue = "20")int size,
		 * 
		 * @Nullable @RequestParam String fromDate,
		 * 
		 * @Nullable @RequestParam String toDate) throws ParseException { return
		 * employeeHistoryService.getEmployeeHistory(userId, page, size, fromDate,
		 * toDate); }
		 * 
		 * @RequestMapping(value = "/getEmployeeHistoryBeforeDate", method =
		 * RequestMethod.GET) public EmployeeHistory
		 * getEmployeeHistoryBeforeDate(@RequestParam String userId,
		 * 
		 * @RequestParam String fromDate) throws ParseException { return
		 * employeeHistoryService.getEmployeeHistoryBeforeDate(userId, fromDate); }
		 * 
		 * @RequestMapping(value = "/getEmployeeHistoryAfterDate", method =
		 * RequestMethod.GET) public EmployeeHistory
		 * getEmployeeHistoryAfterDate(@RequestParam String userId,
		 * 
		 * @RequestParam String fromDate) throws ParseException { return
		 * employeeHistoryService.getEmployeeHistoryAfterDate(userId, fromDate); }
		 * 
		 * @RequestMapping(value = "/storeEmployeeAssignmentHistory", method =
		 * RequestMethod.POST) public EmployeeAssignmentHistory
		 * storeEmployeeAssignmentHistory(@RequestBody EmployeeAssignmentHistory
		 * employeeAssignmentHistory) { return
		 * employeeHistoryService.storeEmployeeAssignmentHistory(
		 * employeeAssignmentHistory); }
		 * 
		 * @RequestMapping(value = "/getEmployeeAssignmentHistory", method =
		 * RequestMethod.GET) public List<EmployeeAssignmentHistory>
		 * getEmployeeAssignmentHistory(@RequestParam String userId) throws
		 * ParseException { return
		 * employeeHistoryService.getEmployeeAssignmentHistory(userId); }
		 * 
		 * @RequestMapping(value = "/organiseEmployeeAssignmentHistoryByUserType",
		 * method = RequestMethod.POST) public MessageResponse
		 * organiseEmployeeAssignmentHistoryByUserType(@RequestParam String
		 * userRoleName,
		 * 
		 * @Nullable @RequestParam String locationType,
		 * 
		 * @Nullable @RequestParam String locationId) { return
		 * employeeHistoryService.organiseEmployeeAssignmentHistoryByUserType(
		 * userRoleName,locationType,locationId); }
		 * 
		 * @RequestMapping(value = "/getAssignedUserByDate", method = RequestMethod.GET)
		 * public User getAssignedUserByDate(@RequestParam User user,
		 * 
		 * @RequestParam Date date) throws ParseException { return
		 * employeeHistoryService.getAssignedUserByDate(user,date); } }
		 */