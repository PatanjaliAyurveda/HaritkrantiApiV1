package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface PaymentReportsService {

    ByteArrayInputStream getPaymentCycle(String fromDate, String toDate) throws ParseException, IOException;
    ByteArrayInputStream getPaymentDetail(String paymentCycleId, String locationType, String locationId, String paymentStatus, String status) throws IOException;

    ByteArrayInputStream getPaymentSummaryReportAgents(String paymentCycleId, String locationType, String locationId, String paymentStatus, String status) throws IOException, ParseException;

    ByteArrayInputStream getPaymentSummaryReportManagers(String fromDate, String toDate, String locationType, String locationId, String paymentStatus, String status) throws ParseException, IOException;

    List<String> uploadPaymentSummaryExcel(MultipartFile file) throws IOException, ParseException;

    ByteArrayInputStream getVendorBankDetialReportforERP(String fromDate, String toDate) throws ParseException, IOException;

    List<User> getAgentsAndManagers(String fromDate, String toDate) throws ParseException;

    List<String> updateVendorCode(MultipartFile file) throws IOException;
}
