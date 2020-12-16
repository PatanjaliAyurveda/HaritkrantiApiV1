package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.ReportHistory;
import com.bharuwa.haritkranti.models.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author harman
 */
public interface GenerateReportService {

    ByteArrayInputStream reportsToExcel(List<ReportHistory> reportHistoryList, String farmingType) throws IOException;

    ByteArrayInputStream generateFarmDetailExcel(List<User> farmerList, String fromDate, String toDate) throws IOException, ParseException;

    ByteArrayInputStream generateHorticultureExcel(List<User> farmerList, String fromDate, String toDate) throws IOException;

    ByteArrayInputStream generateFinancialDetailExcel(List<User> farmerList, String fromDate, String toDate) throws IOException;

//    ByteArrayInputStream generatePersonalDetailsExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;
    ByteArrayInputStream generatePersonalDetailsExcelNew(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateFamilyDetailsExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateInsuranceDetailsExcel(List<User> farmerList, String fromDate, String toDate, String agentId) throws IOException;

    ByteArrayInputStream generateLoanDetailsExcel(List<User> farmerList, String fromDate, String toDate, String agentId) throws IOException;

    ByteArrayInputStream generateDairyFarmDetailExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream getAgentUniqueCode() throws IOException;

    ByteArrayInputStream getSubsidyDetailsExcels(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream getLoanAndBankDetailsExcel() throws IOException, ParseException;

    ByteArrayInputStream getOrganicCertificateDetails(String fromDate, String toDate) throws IOException, ParseException;

    ByteArrayInputStream generateKhasraLocationCoordinatesExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream getAllUsersStatusData(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateHealthDetailExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateFarmEquipmentExcel(String fromDate, String toDate, String locationType, String locationId) throws ParseException, IOException;

    ByteArrayInputStream generateUserAssignmentExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;
}
