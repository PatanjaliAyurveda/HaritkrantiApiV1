package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport;
import com.bharuwa.haritkranti.generatePDFReport.OrganicCertificatePDFReport;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.responseModels.ReportHistoryResponse;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author anuragdhunna
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportHistoryController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ReportHistoryController.class);

    @PreAuthorize("hasAnyRole('USER','AGENT')")
    @RequestMapping(value = "/storeReport", method = RequestMethod.POST)
    public ReportHistory storeReport(@RequestBody ReportHistory reportHistory) {
        return reportHistoryService.saveReport(reportHistory);
    }

    @RequestMapping(value = "/getUserReports", method = RequestMethod.GET)
    public List<ReportHistory> getUserReports(@RequestParam String userId) {
        return reportHistoryService.getUserReports(userId);
    }
    
    @RequestMapping(value = "/getUserReportByKhsraNoAndCropId", method = RequestMethod.GET)
    public ReportHistory getUserReportByKhsraNoAndCropId(@RequestParam String userId,@RequestParam String khasraNo,@RequestParam String cropId) {
        return reportHistoryService.getUserReportByKhsraNoAndCropId(userId,khasraNo,cropId);
    }

    @RequestMapping(value = "/getReportDetail", method = RequestMethod.GET)
    public ReportHistory getReportDetail(@RequestParam String reportId) {
        return reportHistoryService.getReportDetail(reportId);
    }

    @RequestMapping(value = "/getReportPdf", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getReportPdf(@RequestParam String reportId,
                                                            @Nullable @RequestParam String locale) throws IOException, DocumentException {

        ReportHistory reportHistory = getReportDetail(reportId);
        if(reportHistory == null){
            throw new ResourceNotFoundException("Invalid Report");
        }
        User user = userService.findByid(reportHistory.getUserId());
        if(user == null){
            throw new ResourceNotFoundException("Invalid UserId");
        }

        if (reportHistory.getNpkRecommendation() == null) {
            logger.info("getNpkRecommendation is null");

            
            throw new CustomException("Recommendation not found.");
        }
        Crop crop = landService.getCrop(reportHistory.getNpkRecommendation().getCropId());
        if(crop == null){
            throw new ResourceNotFoundException("Crop not entered");
        }

        User agent = userService.getAgentByUserId(user.getId());

        String landImage = null;
        String villageImage = null;

        UserLandMapping userLandMapping = agentService.getUserLandMapping(user.getId(),reportHistory.getNpkRecommendation().getKhasraNo());
        if(userLandMapping != null) {
            landImage = userLandMapping.getLandMapImageUrl();
        }

        VillageMapping villageMapping = null;
        if(user.getAddressModel().getVillageModel()!=null)
        	villageMapping = agentService.getVillageMapping(agent.getId(),user.getAddressModel().getVillageModel().getId());
        else {
        	Village village =locationServices.getVillageByNameBlockTehsilCityState(user.getAddressModel().getVillage());
        	villageMapping = agentService.getVillageMapping(agent.getId(),village.getId());
        }
        if(villageMapping != null) {
            villageImage = villageMapping.getVillageMapImageUrl();
        }
        ByteArrayInputStream bis = GeneratePdfReport.pdfReport(reportHistory, user, crop, landImage, villageImage);
        
//        switch (locale) {
//            case "Eng":
//                 bis = GeneratePdfReport.pdfReport(reportHistory, user, crop, landImage, villageImage);
//                break;
//            case "Hin":
//                 bis = GeneratePdfReport.pdfReportHindi(reportHistory, user, crop, landImage, villageImage);
//                break;
//            default:
//                throw new CustomException("Locale value false: " + locale);
//        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/generateReportExcel", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateReportExcel(@RequestParam String farmingType,
                                                                   @Nullable @RequestParam String locationType,
                                                                   @Nullable @RequestParam String locationId,
                                                                   @RequestParam String fromDate,
                                                                   @RequestParam String toDate) throws IOException, ParseException {
        List<ReportHistory> reportHistoryList = reportHistoryService.getReportListByFarmingTypeAndLocation(farmingType,locationType,locationId,fromDate,toDate);

        ByteArrayInputStream in = generateReportService.reportsToExcel(reportHistoryList,farmingType);
        // return IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @RequestMapping(value = "/generateCertificate", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generateCertificate(@RequestParam String userId,@RequestParam String khasraNos) throws IOException, DocumentException, WriterException {

        User user = userService.findByid(userId);
        if(user == null){
            throw new ResourceNotFoundException("Invalid UserId");
        }

        if(StringUtils.isEmpty(khasraNos)){
            throw new ResourceNotFoundException("No kharsa(s) selected");
        }

        BigDecimal totalArea = BigDecimal.ZERO;

        List<String> khasraList = Arrays.asList(khasraNos.split("\\s*,\\s*"));

        List<UserLandDetail> userLandDetailList = new ArrayList<>();
        for(String khasra : khasraList){
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(khasra,user.getId());
            // set status for AlreadyGeneratedOC
            if(!userLandDetail.isAlreadyGeneratedOc()) {
                userLandDetail.setAlreadyGeneratedOc(true);
                agentService.addUserLandDetail(userLandDetail);
            }
            userLandDetailList.add(userLandDetail);
        }

        // check if OC already generated for these khasras or not
        KhasrasOC khasrasOC = userExtraService.getKhasrasOC(userId,userLandDetailList.get(0).getId());

        // if null then generate new OC and save khasras whose OC generated together
        if(khasrasOC == null){
            khasrasOC = new KhasrasOC();
            khasrasOC.setUserId(userId);
            khasrasOC.setUserLandDetailList(userLandDetailList);
            userExtraService.storeKhasrasOC(khasrasOC);
        } else {
            userLandDetailList.clear();
            userLandDetailList = khasrasOC.getUserLandDetailList();
        }

        // create new list of khasras which generated together
        List<String> klist = new ArrayList<>();

        for (UserLandDetail userLandDetail : userLandDetailList){
            klist.add(userLandDetail.getKhasraNo().trim());
            totalArea = totalArea.add(userLandDetail.getLandSize());
        }

        String kString = String.join(",", klist);
        System.out.println(">>>>>>>>>>>klist>>>>>>>>>>>"+klist);

//        get Agent By UserId
        User agent = userService.findByid(user.getCreatedByUserId());
        if (agent == null){
            throw new ResourceNotFoundException("Agent not found for user with phone number "+user.getPrimaryPhone());
        }
        String certificateCode = agentService.generateCertificateCode(agent.getId());

        // generate qr-code image
        userExtraService.getGenerateQRCodeImageForOC(user.getId(),kString,certificateCode);

        ByteArrayInputStream bis = OrganicCertificatePDFReport.getCertificate(user,kString,totalArea,certificateCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=organicCertificate.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

//    // api to generate Excel for Data
//    @RequestMapping(value = "/generateFarmDetailExcel", method = RequestMethod.GET)
//    public ResponseEntity<InputStreamResource> generateFarmDetailExcel(@RequestParam String agentId,
//                                                                       @RequestParam String reportType,
//                                                                       @Nullable @RequestParam String fromDate,
//                                                                       @Nullable @RequestParam String toDate,
//                                                                       @RequestParam(value = "page", defaultValue = "0")int page,
//                                                                       @RequestParam(value = "size", defaultValue = "20")int size) throws Exception {
//        ByteArrayInputStream in = null;
//        final List<User> farmerList = agentService.getAgentUsers(agentId);
//
//        switch (reportType) {
//            case FARM_DETAIL:
//                in = generateReportService.generateFarmDetailExcel(farmerList, fromDate, toDate);
//                break;
//            case HORTICULTURE:
//                in = generateReportService.generateHorticultureExcel(farmerList, fromDate, toDate);
//                break;
//            case FINANCIAL_DETAILS:
//                in = generateReportService.generateFinancialDetailExcel(farmerList, fromDate, toDate);
//                break;
//            case PERSONAL_DETAILS:
//                // get farmers by pagination
//                final Page<User> farmerPage = agentService.getAgentUsersByPagination(agentId,page,size);
//                List<User> farmers = farmerPage.getContent();
//                // generate personal detail excel for farmers by pagination
////                in = generateReportService.generatePersonalDetailsExcel(farmers, fromDate, toDate, agentId);
//                break;
//            case FAMILY_DETAILS:
////                in = generateReportService.generateFamilyDetailsExcel(farmerList, fromDate, toDate, agentId);
//                break;
//            case INSURANCE_DETAILS:
//                in = generateReportService.generateInsuranceDetailsExcel(farmerList, fromDate, toDate, agentId);
//                break;
//            case LOAN_DETAILS:
//                in = generateReportService.generateLoanDetailsExcel(farmerList, fromDate, toDate, agentId);
//                break;
//            default:
//                throw new CustomException("Report Type is not correct.");
//        }
//        // return IOUtils.toByteArray(in);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=report.xlsx");
//
//        assert in != null;
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(new InputStreamResource(in));
//    }

    @RequestMapping(value = "/getOrganicReportByKhasraAndUserId",method = RequestMethod.GET)
    public ReportHistory getOrganicReportByKhasraAndUserId(@RequestParam String khasraNo, @RequestParam String userId){
        return reportHistoryService.getOrganicReportByKhasraAndUserId(khasraNo, userId);
    }


    @RequestMapping(value = "/getReportHistoryCount", method = RequestMethod.GET)
    public long getReportHistoryCount(@RequestParam String farmingType,
                                      @Nullable @RequestParam String locationType,
                                      @Nullable @RequestParam String locationId,
                                      @RequestParam String fromDate,
                                      @RequestParam String toDate) throws IOException, ParseException {
        return reportHistoryService.getReportHistoryCount(farmingType, locationType, locationId,fromDate,toDate);
    }

    // api to get unique code for every agent and return list in excel format
    @RequestMapping(value = "/getAgentUniqueCode",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAgentUniqueCode() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=AgentUniqueCode.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(generateReportService.getAgentUniqueCode()));
    }

    @ApiOperation(value = "Download CSV Sheet for all Farmer's Current Crop")
    @RequestMapping(value = "/getAllKhasraLocCoordinates",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAllKhasraLocCoordinates(@RequestParam String fromDate,
                                                                          @RequestParam String toDate,
                                                                          @Nullable @RequestParam String locationType,
                                                                          @Nullable @RequestParam String locationId) throws IOException,ParseException{

        ByteArrayInputStream byteArrayInputStream = generateReportService.generateKhasraLocationCoordinatesExcel(fromDate,toDate,locationType,locationId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=All Farmer's Khasra's Location Coordinates.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation(value = "Download Excel Sheet for all Farmer's with their insurance and subsidies details for a particular agent")
    @RequestMapping(value = "/getSubsidyDetailsExcels",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getSubsidyDetailsExcels(@RequestParam String fromDate,
                                                                       @RequestParam String toDate,
                                                                       @Nullable @RequestParam String locationType,
                                                                       @Nullable @RequestParam String locationId) throws IOException, ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Farmer's Insurance and Subsidy Details Excels.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(generateReportService.getSubsidyDetailsExcels(fromDate, toDate, locationType, locationId)));
    }


    @ApiOperation(value = "Download CSV Sheet for all Farmer's Government Data ")
    @RequestMapping(value = "/getFarmerGovernmentSchemeData",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFarmerGovernmentSchemeData(@RequestParam String fromDate,
                                                                             @RequestParam String toDate,
                                                                             @Nullable @RequestParam String locationType,
                                                                             @Nullable @RequestParam String locationId) throws IOException, ParseException {

        ByteArrayInputStream byteArrayInputStream = reportHistoryService.generateFarmerGovernmentSchemeDataExcel(fromDate , toDate, locationType, locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=FarmerGovernmentSchemeData.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation(value = "Download CSV Sheet for all Farmer's Beekeeping Data ")
    @RequestMapping(value = "/getFarmerBeekeepingData",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFarmerBeekeepingData(@RequestParam String fromDate,
                                                                       @RequestParam String toDate,
                                                                       @Nullable @RequestParam String locationType,
                                                                       @Nullable @RequestParam String locationId) throws IOException, ParseException {

        ByteArrayInputStream byteArrayInputStream = reportHistoryService.generateFarmerBeekeepingDataExcel(fromDate,toDate,locationType,locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=FarmerBeekeepingData.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation(value = "Download CSV Sheet for all Farmer's Horticulture Data ")
    @RequestMapping(value = "/getAllFarmersHorticultureData",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAllFarmersHorticultureData(@RequestParam String fromDate,
                                                                             @RequestParam String toDate,
                                                                             @Nullable @RequestParam String locationType,
                                                                             @Nullable @RequestParam String locationId) throws IOException, ParseException {

        ByteArrayInputStream byteArrayInputStream = reportHistoryService.generateFarmersHorticultureDataExcel(fromDate, toDate,locationType,locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Agent's Farmer's Horticulture.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation(value = "Download Excel Sheet for all Farmer's Crop-Expense details ")
    @RequestMapping(value = "/getCropExpensesDetailsExcels",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getCropExpensesDetailsExcels(@RequestParam String fromDate,
                                                                            @RequestParam String toDate,
                                                                            @Nullable @RequestParam String locationType,
                                                                            @Nullable @RequestParam String locationId) throws IOException,ParseException {
        ByteArrayInputStream byteArrayInputStream = reportHistoryService.getCropExpensesDetailsExcels(fromDate, toDate,locationType,locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Agent's Farmer's CropExpensesDetail.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation(value = "Download Excel Sheet for all Farmer's with their Loan and Bank details for a particular agent")
    @RequestMapping(value = "/getLoanAndBankDetailsExcel",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getLoanAndBankDetailsExcel() throws IOException,ParseException{

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Farmer's Loan and Bank Details Excels.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(generateReportService.getLoanAndBankDetailsExcel()));
    }

    @ApiOperation(value = "Download Excel Sheet for all Farmer's Organic Certificate details ")
    @RequestMapping(value = "/getOCDetailsExcels",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getOCDetailsExcels(@RequestParam String fromDate ,@RequestParam String toDate) throws IOException, ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Agent's Farmer's OCDetailsExcels.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(generateReportService.getOrganicCertificateDetails(fromDate, toDate)));
    }

    @ApiOperation(value = "Download Excel Sheet for all Farmer's Village Assets and Village Mapping details")
    @RequestMapping(value = "/getVillageAssetAndMappingDetailsExcel",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getVillageAssetAndMappingDetailsExcel(@RequestParam String fromDate ,
                                                                                     @RequestParam String toDate,
                                                                                     @Nullable @RequestParam String locationId,
                                                                                     @Nullable @RequestParam String locationType) throws IOException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Agent's Farmer's villageAssetAndMapping.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(reportHistoryService.getVillageAssetAndMappingDetailsExcel(fromDate, toDate, locationId,locationType)));
    }

    @RequestMapping(value = "/getUserReportHistoryResponseListByKhasra", method = RequestMethod.GET)
    public List<ReportHistoryResponse> getUserReportHistoryResponseListByKhasra(@RequestParam String userId,@RequestParam String khasraNo) {
        return reportHistoryService.getUserReportHistoryResponseListByKhasra(userId,khasraNo);
    }

    @ApiOperation(value = "Download CSV Sheet for all Farmer's Dairy Farm Data ")
    @RequestMapping(value = "/getFarmerDairyFarmData",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFarmerDairyFarmData(@RequestParam String fromDate,
                                                                      @RequestParam String toDate,
                                                                      @Nullable @RequestParam String locationType,
                                                                      @Nullable @RequestParam String locationId) throws IOException, ParseException {

        ByteArrayInputStream byteArrayInputStream = generateReportService.generateDairyFarmDetailExcel(fromDate,toDate,locationType,locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=FarmerDairyFarmData.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }


    @ApiOperation(value = "Download CSV Sheet for all User's Status Data")
    @RequestMapping(value = "/getAllUsersStatusData",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAllUsersStatusData(@RequestParam String fromDate,
                                                                     @RequestParam String toDate,
                                                                     @Nullable @RequestParam String locationType,
                                                                     @Nullable @RequestParam String locationId) throws IOException, ParseException {

        ByteArrayInputStream byteArrayInputStream = generateReportService.getAllUsersStatusData(fromDate,toDate,locationType,locationId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ManagersStatusData.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @ApiOperation("Generate Excel for All Type of Reports according to Report Type")
    @RequestMapping(value = "/generateExcelReportByReportType", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> generateExcelReportByReportType(
            @RequestParam String reportType,
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @Nullable @RequestParam String farmingType,
            @Nullable @RequestParam String locationType,
            @Nullable @RequestParam String locationId) throws Exception {
        ByteArrayInputStream in = null;

        switch (reportType) {
            case PERSONAL_DETAILS:
                in = generateReportService.generatePersonalDetailsExcelNew(fromDate, toDate, locationType, locationId);
                break;
            case HORTICULTURE:
                in = reportHistoryService.generateFarmersHorticultureDataExcel(fromDate , toDate, locationType, locationId);
                break;
            case GOVERNMENT_SCHEME:
                in = reportHistoryService.generateFarmerGovernmentSchemeDataExcel(fromDate , toDate, locationType, locationId);
                break;
            case KHASRA_LOC_COORDINATES:
                in = generateReportService.generateKhasraLocationCoordinatesExcel(fromDate, toDate, locationType, locationId);
                break;
            case BEEKEEPING_DETAIL:
                in =  reportHistoryService.generateFarmerBeekeepingDataExcel(fromDate, toDate, locationType, locationId);
                break;
            case DAIRY_FARM_DETAIL:
                in = generateReportService.generateDairyFarmDetailExcel(fromDate, toDate, locationType, locationId);
                break;
            case CROP_EXPENSE_DETAIL:
                in = reportHistoryService.getCropExpensesDetailsExcels(fromDate, toDate, locationType, locationId);
                break;
            case FAMILY_DETAILS:
                in = generateReportService.generateFamilyDetailsExcel(fromDate, toDate, locationType, locationId);
                break;
            case SUBSIDY_DETAIL:
                in = generateReportService.getSubsidyDetailsExcels(fromDate, toDate, locationType, locationId);
                break;
            case VILLAGE_ASSET:
                in = reportHistoryService.getVillageAssetAndMappingDetailsExcel(fromDate, toDate, locationType, locationId);
                break;
            case HEALTH_DETAIL:
                in = generateReportService.generateHealthDetailExcel(fromDate, toDate, locationType, locationId);
                break;
            case FARM_EQUIPMENT:
                in = generateReportService.generateFarmEquipmentExcel(fromDate, toDate, locationType, locationId);
                break;
            case USER_ASSIGNMENT_HISTORY:
                in = generateReportService.generateUserAssignmentExcel(fromDate, toDate, locationType, locationId);
                break;
            case USER_STATUS:
                in = generateReportService.getAllUsersStatusData(fromDate, toDate, locationType, locationId);
                break;
            case RECOMMENDATION_REPORT:
                List<ReportHistory> reportHistoryList = reportHistoryService.getReportListByFarmingTypeAndLocation(farmingType,locationType,locationId,fromDate,toDate);
                in = generateReportService.reportsToExcel(reportHistoryList,farmingType);
                break;

            default:
                throw new CustomException("Report Type is not correct.");
        }
        // return IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename="+reportType+".xlsx");

        assert in != null;
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

}
