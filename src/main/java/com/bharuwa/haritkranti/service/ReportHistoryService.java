package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.ReportHistory;
import com.bharuwa.haritkranti.models.responseModels.ReportHistoryResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface ReportHistoryService {

    ReportHistory saveReport(ReportHistory reportHistory);

    List<ReportHistory> getUserReports(String userId);

    ReportHistory getReportDetail(String reportId);

    ReportHistory getReportHistoryByKhasraAndUserId(String khasraNo, String userId);

    List<ReportHistory> getReportHistoryListByKhasraAndUserId(String khasraNo, String userId);

    ReportHistory getOrganicReportByKhasraAndUserId(String khasraNo, String userId);

    List<ReportHistory> getReportListByFarmingTypeAndLocation(String farmingType, String locationType, String locationId, String fromDate, String toDate) throws ParseException;

    long getReportHistoryCount(String farmingType, String locationType, String locationId, String fromDate, String toDate) throws ParseException;

    ByteArrayInputStream generateFarmerGovernmentSchemeDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateFarmerBeekeepingDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream generateFarmersHorticultureDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    ByteArrayInputStream getCropExpensesDetailsExcels(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException;

    List<ReportHistoryResponse> getUserReportHistoryResponseListByKhasra(String userId, String khasraNo);

    ByteArrayInputStream getVillageAssetAndMappingDetailsExcel(String fromDate, String toDate, String locationId, String locationType) throws IOException, ParseException;
    
    public ReportHistory getUserReportByKhsraNoAndCropId(String userId,String khasraNo,String cropId);
    

}
