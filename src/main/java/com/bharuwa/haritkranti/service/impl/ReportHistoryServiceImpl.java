package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.requestModels.GeoLocation;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.responseModels.FertilizerResponse;
import com.bharuwa.haritkranti.models.responseModels.POMFertCal;
import com.bharuwa.haritkranti.models.responseModels.ReportHistoryResponse;
import com.bharuwa.haritkranti.msg91.Msg91Services;
import com.bharuwa.haritkranti.repositories.FamilyMemberHealthRecordRepo;
import com.bharuwa.haritkranti.repositories.ReportHistoryRepo;
import com.bharuwa.haritkranti.repositories.UserRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static com.bharuwa.haritkranti.service.impl.CountServiceImpl.getQueryByReportType;
import static com.bharuwa.haritkranti.service.impl.GenerateReportServiceImpl.getStateName;
import static com.bharuwa.haritkranti.service.impl.GenerateReportServiceImpl.getUserMainRole;
import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author anuragdhunna
 */
@Service
public class ReportHistoryServiceImpl implements ReportHistoryService {

    private final MongoTemplate mongoTemplate;

    public ReportHistoryServiceImpl(MongoTemplate mongoTemplate) throws IOException {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private ReportHistoryRepo reportHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserExtraService userExtraService;
    @Autowired
    private GovernmentSchemesService governmentSchemesService;

    @Autowired
    private FarmDetailService farmDetailService;

    @Autowired
    private LandService landService;

    @Autowired
    private FarmingService farmingService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private FamilyMemberHealthRecordRepo familyMemberHealthRecordRepo;

    @Override
    public ReportHistory saveReport(ReportHistory reportHistory) {

        Logger logger = Logger.getLogger(ReportHistoryServiceImpl.class.getName());

        //check khasra No and userId is present or not
        NPKRecommendation npkRecommendation = reportHistory.getNpkRecommendation();
        if (StringUtils.isEmpty(npkRecommendation.getKhasraNo()) || StringUtils.isEmpty(reportHistory.getUserId())) {
            throw new ResourceNotFoundException("KhasraNo or UserId empty");
        }

        //Store Land details
        UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(npkRecommendation.getKhasraNo(), reportHistory.getUserId());
        if (landDetail == null) {
            throw new ResourceNotFoundException("Land Details not found");
        }
        npkRecommendation.setCityId(landDetail.getCity().getId());
        npkRecommendation.setStateId(landDetail.getState().getId());
    //    npkRecommendation.setTehsilId(landDetail.getTehsil().getId()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
    //    npkRecommendation.setBlockId(landDetail.getBlock().getId());
    //    npkRecommendation.setVillageId(landDetail.getVillageModel().getId());

        reportHistoryRepo.save(reportHistory);

        // store soil and fertilizer report Number
        if (reportHistory.getSoilReportNumber() == null || StringUtils.isEmpty(reportHistory.getSoilReportNumber())) {
            reportHistory.setSoilReportNumber(userExtraService.getSoilAndFertilizerReportNumber(reportHistory.getId()));
        }

        // store report history
        ReportHistory storedHistory = reportHistoryRepo.save(reportHistory);

        Query query = new Query(Criteria.where("crop.$id").is(new ObjectId(npkRecommendation.getCropId())).and("userLandDetailId").is(landDetail.getId()));
        UserCrop userCrop = mongoTemplate.findOne(query,UserCrop.class);

        User user = userService.findByid(storedHistory.getUserId());
        if (user != null) {
            SoilTest soilTest = npkRecommendation.getSoilTest();
            // Send SMS for Soil Test
            String message = "Dear "+ user.getFirstName() + "\n" +
                    "Your Soil Test Report For Land Area : "+ npkRecommendation.getFieldSize() +" "+npkRecommendation.getFieldSizeType() +" With Khasra No. : " + landDetail.getKhasraNo() +" and "+ "CropName : " +  userCrop.getCrop().getCropName() + " , Season : " + userCrop.getCropSeason() +"\n" +
                    "Nitrogen: " + soilTest.getnValue() +"Kg"+ "\n" +
                    "Phosphorus: " + soilTest.getpValue()  +"Kg"+ "\n" +
                    "Potash: " + soilTest.getkValue() +"Kg"+ "\n" +
                    "Organic Carbon: " + soilTest.getOrganicCarbon() +"%"+ "\n" +
                    "pH: " + soilTest.getpHValue() + "\n" +
                    "\n" +
                    "Recommended Fertilizers are: " + "\n";


            StringBuilder stringBuilderMessage = new StringBuilder(message);
//            final DecimalFormat f = new DecimalFormat("##.00");

            switch (npkRecommendation.getFarmingType()){

                case Chemical:
                    for (FertilizerResponse response : storedHistory.getFertilizerResponse()) {
                        stringBuilderMessage.append(response.getFertilizerName()).append(": ").append(response.getRequirement()).append(response.getUnit()).append("\n");
                    }
                    break;

                case Organic:
                    for (POMFertCal response : storedHistory.getOrganicReqFert().getPomFertCals()) {
                        stringBuilderMessage.append(response.getFertilizerName()).append(": ").append(response.getRequiredFert()).append(response.getUnit()).append("\n");
                    }
                    break;

                case INM_MIX:
                    for (FertilizerResponse response : storedHistory.getMixReqFert().getFertilizerResponseList()) {
                        stringBuilderMessage.append(response.getFertilizerName()).append(": ").append(response.getRequirement()).append(response.getUnit()).append("\n");
                    }
                    for (POMFertCal response : storedHistory.getMixReqFert().getOrganicReqFert().getPomFertCals()) {
                        stringBuilderMessage.append(response.getFertilizerName()).append(": ").append(response.getRequiredFert()).append(response.getUnit()).append("\n");
                    }
                    break;

                default:
                    throw new CustomException("Invalid Farming Type");
            }

            try {
                Msg91Services.sendFarmerAppLinkMsg(String.valueOf(stringBuilderMessage), user.getPrimaryPhone());
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Soil Test SMS not Sent");
            }
        }
        return storedHistory;
    }



    @Override
    public List<ReportHistory> getUserReports(String userId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, ReportHistory.class);
    }
    
    @Override
    public ReportHistory getUserReportByKhsraNoAndCropId(String userId,String khasraNo,String cropId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId).and("npkRecommendation.cropId").is(cropId).and("npkRecommendation.khasraNo").is(khasraNo));
        List<ReportHistory> list = mongoTemplate.find(query, ReportHistory.class);
        return list.get(0);
    }

    @Override
    public ReportHistory getReportDetail(String reportId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(reportId)));
        return mongoTemplate.findOne(query, ReportHistory.class);
    }

    @Override
    public ReportHistory getReportHistoryByKhasraAndUserId(String khasraNo, String userId) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        query = query.addCriteria(Criteria.where("userId").is(userId).and("npkRecommendation.khasraNo").is(khasraNo));
        return mongoTemplate.findOne(query, ReportHistory.class);
    }

    @Override
    public List<ReportHistory> getReportHistoryListByKhasraAndUserId(String khasraNo, String userId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId).and("npkRecommendation.khasraNo").is(khasraNo));
        return mongoTemplate.find(query, ReportHistory.class);
    }

    @Override
    public List<ReportHistoryResponse> getUserReportHistoryResponseListByKhasra(String userId, String khasraNo) {
        List<ReportHistoryResponse> reportHistoryResponseList = new ArrayList<>();

        List<ReportHistory> reportHistoryList = getReportHistoryListByKhasraAndUserId(khasraNo, userId);
        for (ReportHistory reportHistory : reportHistoryList) {

            // remove fruit recommendation reports
            if(StringUtils.isEmpty(reportHistory.getNpkRecommendation().getCropId()) || !StringUtils.isEmpty(reportHistory.getNpkRecommendation().getFruitVarietyId())){
                continue;
            }

            ReportHistoryResponse response = new ReportHistoryResponse();
            response.setReportId(reportHistory.getId());
            response.setSoilReportNumber(reportHistory.getSoilReportNumber());
            response.setKhasraNo(reportHistory.getNpkRecommendation().getKhasraNo());
            response.setUserId(reportHistory.getUserId());
            response.setFarmingType(reportHistory.getNpkRecommendation().getFarmingType().toString());

            Query query = new Query(Criteria.where("crop.$id").is(new ObjectId(reportHistory.getNpkRecommendation().getCropId())).and("userId").is(userId).and("khasraNo").is(khasraNo));
            UserCrop userCrop = mongoTemplate.findOne(query, UserCrop.class);
            if (userCrop == null) {
                throw new ResourceNotFoundException("Crop data not found");
            }
            response.setCropName(userCrop.getCrop().getCropName());
            response.setCropType(userCrop.getCrop().getCropType().toString());
            response.setCropSeason(userCrop.getCropSeason().toString());
            response.setCropSowingDate(userCrop.getYearOfSowing());
            response.setCreationDate(reportHistory.getCreationDate());
            reportHistoryResponseList.add(response);
        }
        return reportHistoryResponseList;
    }



    @Override
    public ReportHistory getOrganicReportByKhasraAndUserId(String khasraNo, String userId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId).and("npkRecommendation.khasraNo").is(khasraNo).and("npkRecommendation.farmingType").is(FarmingType.Type.Organic));
        ReportHistory reportHistory = mongoTemplate.findOne(query, ReportHistory.class);
        if (reportHistory == null) {
            throw new ResourceNotFoundException("You need to obtain Organic Fertilizer Recommendation Report for this Khasra.");
        }
        return reportHistory;
    }

    @Override
    public List<ReportHistory> getReportListByFarmingTypeAndLocation(String farmingType, String locationType, String locationId, String fromDate, String toDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);
        // check if locationId and locationType are empty or not
        if (StringUtils.isEmpty(locationId) && StringUtils.isEmpty(locationType)) {
            return getReportListByFilters(farmingType, null, null, from, to);
        }

        // get location key for Query
        String searchLocationKey = getSearchLocationKey(locationType);

        return getReportListByFilters(farmingType, searchLocationKey, locationId, from, to);
    }

    public List<ReportHistory> getReportListByFilters(String farmingType, String searchLocationKey, String locationId, Date from, Date to) {
        // get Query depending upon filters
        Query query = getQueryForReportHistoryByLocationFilter(farmingType, searchLocationKey, locationId, from, to);
        query.fields().include("userId").include("npkRecommendation.khasraNo").include("npkRecommendation.farmingType").include("npkRecommendation.soilTest").include("npkRecommendation.cropId").include("mixReqFert.organicReqFert.pomFertCals")
        .include("mixReqFert.fertilizerResponseList").include("fertilizerResponse").include("organicReqFert.pomFertCals").include("creationDate");
        return mongoTemplate.find(query, ReportHistory.class);
    }

    @Override
    public long getReportHistoryCount(String farmingType, String locationType, String locationId, String fromDate, String toDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        // check if locationId and locationType are empty or not
        if (StringUtils.isEmpty(locationId) && StringUtils.isEmpty(locationType)) {
            return getReportHistoryCountByFilters(farmingType, null, null, from, to);
        }

        // get location key for Query
        String searchLocationKey = getSearchLocationKey(locationType);

        return getReportHistoryCountByFilters(farmingType, searchLocationKey, locationId, from, to);
    }

    public long getReportHistoryCountByFilters(String farmingType, String searchLocationKey, String locationId, Date from, Date to) {
        // get Query depending upon filters
        Query query = getQueryForReportHistoryByLocationFilter(farmingType, searchLocationKey, locationId, from, to);
        return mongoTemplate.count(query, ReportHistory.class);
    }

    /**
     * Return location key for query
     *
     * @param locationType
     * @return
     */
    private String getSearchLocationKey(String locationType) {
        String searchLocationKey;
        // Filter for locationTye
        switch (locationType) {
            case "STATE":
                searchLocationKey = "npkRecommendation.stateId";
                break;
            case "DISTRICT":
                searchLocationKey = "npkRecommendation.cityId";
                break;
            case "TEHSIL":
                searchLocationKey = "npkRecommendation.tehsilId";
                break;
            case "BLOCK":
                searchLocationKey = "npkRecommendation.blockId";
                break;
            case "VILLAGE":
                searchLocationKey = "npkRecommendation.villageId";
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return searchLocationKey;
    }

    /**
     * return Query By adding filters
     *
     * @param farmingType
     * @param searchLocationKey
     * @param locationId
     * @param from
     * @param to
     * @return
     */
    private Query getQueryForReportHistoryByLocationFilter(String farmingType, String searchLocationKey, String locationId, Date from, Date to) {
        Query query = new Query();

        // filter query based on input parameters
        if (!StringUtils.isEmpty(searchLocationKey) && !StringUtils.isEmpty(farmingType)) {
            query.addCriteria(Criteria.where("npkRecommendation.farmingType").is(farmingType).and(searchLocationKey).is(locationId).and("creationDate").gte(from).lte(to));
        } else if(StringUtils.isEmpty(searchLocationKey) && !StringUtils.isEmpty(farmingType)){
            query.addCriteria(Criteria.where("npkRecommendation.farmingType").is(farmingType).and("creationDate").gte(from).lte(to));
        } else if(!StringUtils.isEmpty(searchLocationKey) && StringUtils.isEmpty(farmingType)){
            query.addCriteria(Criteria.where(searchLocationKey).is(locationId).and("creationDate").gte(from).lte(to));
        } else {
            query.addCriteria(Criteria.where("creationDate").gte(from).lte(to));
        }
        return query;
    }

    @Override
    public ByteArrayInputStream generateFarmerGovernmentSchemeDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        List<String> COLUMNs = Arrays.asList("Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "State", "District", "Tehsil", "Block", "Village", "Govt-scheme 1");

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Government Schemes Details");
        sh.setRandomAccessWindowSize(100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sh.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.size(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs.get(col));
            cell.setCellStyle(headerCellStyle);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        int rowIdx = 1;
        int maxHeaders = 0;

        // get Query according to report type
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        System.out.println("=============start==form query=============="+Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, GOVERNMENT_SCHEME);
        System.out.println("=============end form query================"+Calendar.getInstance().getTime());
        List<User> distinctUserList = mongoTemplate.findDistinct(query,"user", UserSchemes.class,User.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());

        Iterator<User> i = distinctUserList.iterator();
        while (i.hasNext()) {
            User farmer = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            // get Agent
            User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);
            if(agent != null) {

                // get manager
//                User manager = employeeHistoryService.getAssignedUserByDate(agent, farmer.getCreationDate());
                if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                    User manager = userService.findByid(agent.getCreatedByUserId());
                    if (manager != null) {
                        row.createCell(0).setCellValue(getUserMainRole(manager));
                        row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                        row.createCell(2).setCellValue(manager.getPrimaryPhone());
                    }
                }
                if (!StringUtils.isEmpty(agent.getMiddleName())) {
                    row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                } else {
                    row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                }
                row.createCell(4).setCellValue(agent.getUserCode());
            }

            if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
            } else {
                row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
            }

            row.createCell(6).setCellValue(farmer.getUserCode());
            row.createCell(7).setCellValue(farmer.getPrimaryPhone());

            row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
            row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
            row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
            row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
            row.createCell(12).setCellValue(getStateName(farmer,null, "village"));

            // get user-adopted-schemes according to time
            query = new Query(Criteria.where("user.$id").is(new ObjectId(farmer.getId())).and("status").ne(false).and("creationDate").gte(from).lte(to));
            List<GovernmentSchemes> userAdoptedGovernmentSchemes = mongoTemplate.findDistinct(query,"governmentSchemes", UserSchemes.class,GovernmentSchemes.class);

            if (userAdoptedGovernmentSchemes.isEmpty()) {
                continue;
            }

            // Create Dynamic Header
            int column = 13;
            if (maxHeaders < userAdoptedGovernmentSchemes.size()) {
                for (int col = 0; col < userAdoptedGovernmentSchemes.size(); col++) {
                    Cell cell = headerRow.createCell(column);
                    cell.setCellValue("Govt-scheme " + (column - 12));
                    cell.setCellStyle(headerCellStyle);
                    column++;
                }
                maxHeaders = userAdoptedGovernmentSchemes.size();
            }
            int columnIndex = 13;
            for (GovernmentSchemes governmentSchemes : userAdoptedGovernmentSchemes) {
                row.createCell(columnIndex).setCellValue(governmentSchemes.getSchemeName());
                columnIndex++;
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateFarmerBeekeepingDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        List<String> COLUMNs = Arrays.asList("Manager-Type", "Manager-Name", "Manager-Number", "Agent_Name", "Agent_Code", "Farmer_Name", "Farmer_Code", "Mobile_No", "State", "District", "Tehsil", "Block", "Village", "Season", "From_date",
                "To_date", "No._of_boxes", "Quantity_honey", "Unit", "Flavour", "Total_income", "Batch_Code", "TimeStamp", "Location 1");

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Farmer's Beekeeping Data");
        sh.setRandomAccessWindowSize(100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sh.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.size(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs.get(col));
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        int maxHeaders = 0;

        // get Query according to report type
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, BEEKEEPING_DETAIL);
//        List<BeekeepingDetails> beekeepingDetailsList = mongoTemplate.find(query, BeekeepingDetails.class);
        System.out.println("=============start================"+ Calendar.getInstance().getTime());
        List<BeekeepingDetails> beekeepingDetailsList = familyMemberHealthRecordRepo.getBeekeepingDetailsList(fromDate, toDate, locationType, locationId, BEEKEEPING_DETAIL, BeekeepingDetails.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());
        System.out.println("========size======"+beekeepingDetailsList.size());

        if (beekeepingDetailsList.isEmpty()) {
            throw new ResourceNotFoundException("Beekeeping data is empty");
        }

        Iterator<BeekeepingDetails> i = beekeepingDetailsList.iterator();
        System.out.println("==========beekeepingId============="+beekeepingDetailsList.get(0).getId());
        while (i.hasNext()) {
            BeekeepingDetails beekeepingDetails = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);
            System.out.println("==========beekeepingId============="+beekeepingDetails.getId());

            User farmer = userService.findByid(beekeepingDetails.getUserId());
            if (farmer != null) {

                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);
                if (agent != null) {
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, beekeepingDetails.getCreationDate());
                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getUserMainRole(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }

                    if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    }
                    row.createCell(4).setCellValue(agent.getUserCode());
                }

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());
                if (farmer.getAddressModel() != null) {

                    row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                    row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                    row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                    row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                    row.createCell(12).setCellValue(getStateName(farmer,null, "village"));
                }

                if (beekeepingDetails.getSeason() != null) {
                    row.createCell(13).setCellValue(beekeepingDetails.getSeason().toString());
                }

                row.createCell(14).setCellValue(beekeepingDetails.getFromDate().toInstant().toString());
                row.createCell(15).setCellValue(beekeepingDetails.getToDate().toInstant().toString());
                row.createCell(16).setCellValue(beekeepingDetails.getQuantity());
                row.createCell(17).setCellValue(beekeepingDetails.getProductionOutput().toString());
                row.createCell(18).setCellValue(beekeepingDetails.getUnitProductionOutput().toString());
                row.createCell(19).setCellValue(beekeepingDetails.getHoneyFlavour().toString());
                row.createCell(20).setCellValue(beekeepingDetails.getIncome().doubleValue());
                row.createCell(21).setCellValue(beekeepingDetails.getBatchCode());
                row.createCell(22).setCellValue(beekeepingDetails.getCreationDate().toInstant().toString());

                BeekeepingAreaMapping beekeepingAreaMapping = farmDetailService.getBeekeepingAreaMapping(beekeepingDetails.getUserId(), beekeepingDetails.getId());

                if (beekeepingAreaMapping != null) {

                    List<GeoLocation> locationPinsList = beekeepingAreaMapping.getLocationPins();
                    if (!locationPinsList.isEmpty()) {

                        // Create Dynamic Header
                        int column = 23;
                        if (maxHeaders < locationPinsList.size()) {
                            for (int col = 0; col < locationPinsList.size(); col++) {
                                Cell cell = headerRow.createCell(column);
                                cell.setCellValue("location" + (column - 22));
                                cell.setCellStyle(headerCellStyle);
                                column++;
                            }
                            maxHeaders = locationPinsList.size();
                        }
                        int columnIndex = 23;
                        for (GeoLocation location : locationPinsList) {
                            row.createCell(columnIndex).setCellValue(location.getLattitude() + "," + location.getLongitude());
                            columnIndex++;
                        }
                    }
                }
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateFarmersHorticultureDataExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] COLUMNs = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent_Name", "Agent_Code", "Farmer_Name", "Farmer_Code", "Mobile_No", "State", "District", "Tehsil", "Block", "Village", "khasraNo", "TreeType", "Age", "Number", "Income", "TimeStamp"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Horticulture Details");
        sh.setRandomAccessWindowSize(100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sh.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        System.out.println("=============start================"+ Calendar.getInstance().getTime());
        List<Horticulture> horticultureList = familyMemberHealthRecordRepo.getHorticultureList(fromDate, toDate, locationType, locationId, HORTICULTURE, Horticulture.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());
        System.out.println("========size======"+horticultureList.size());
        int rowIdx = 1;


        Iterator<Horticulture> i = horticultureList.iterator();
        System.out.println("==========horticultureId============="+horticultureList.get(0).getId());
        while (i.hasNext()) {
            Horticulture horticulture = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);
            System.out.println("==========horticultureId============="+horticulture.getId());

            User farmer = userService.findByid(horticulture.getUserId());
            if (farmer != null) {
                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);
                if (agent != null) {

                    // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, horticulture.getCreationDate());
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getUserMainRole(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }
                    if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    }

                    row.createCell(4).setCellValue(agent.getUserCode());

                }

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());

                row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                row.createCell(12).setCellValue(getStateName(farmer,null, "village"));

                row.createCell(13).setCellValue(horticulture.getKhasraNo());
                row.createCell(14).setCellValue(horticulture.getTreeType().toString());
                row.createCell(15).setCellValue(horticulture.getAgeOfTree());
                row.createCell(16).setCellValue(horticulture.getNoOfTrees());
                row.createCell(17).setCellValue(horticulture.getIncome().toString() + " " + horticulture.getIncomePeriod().toString());
                row.createCell(18).setCellValue(horticulture.getCreationDate().toInstant().toString());
            }
            i.remove();
        }

        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * get Role name of any type of manager or admin
     * @param manager
     * @return
     */
    public static String getManagerRoleName(User manager) {
        String managerRoleName = "";
        if (manager.getRoles().size() == 1) {
            managerRoleName = manager.getRoles().get(0).getRoleName();
        } else {
            for (Role role : manager.getRoles()) {
                if (role.getRoleName().contains("_MANAGER")) {
                    managerRoleName = role.getRoleName();
                }
            }
        }
        return managerRoleName;
    }

    @Override
    public ByteArrayInputStream getCropExpensesDetailsExcels(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "State", "District", "Tehsil", "Block", "Village", "Khasra-No", "Khasra-Size", "Khasra-Unit", "Crop-Season", "Crop-Group-Type", "Crop-Group-Name", "Crop-Name", "Land-Size", "LandSize-Unit", "Crop-Yield", "Yield-Unit", "Seed_Quantity", " Fertilizer_Qunatity", "Number-of-Manpower", "Sowing_date", "Crop_status", "TimeStamp"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Crop_Expenses Detail Report");
        sh.setRandomAccessWindowSize(100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sh.createRow(0);

        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }

        // get Query according to report type
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, CROP_EXPENSE_DETAIL);
//        List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        List<UserCrop> userCrops = familyMemberHealthRecordRepo.getUserCrops(fromDate, toDate, locationType, locationId, CROP_EXPENSE_DETAIL, UserCrop.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());
        System.out.println("========size======"+userCrops.size());

        int rowIdx = 1;

        Iterator<UserCrop> i = userCrops.iterator();
        System.out.println("==========usercropId============="+userCrops.get(0).getId());
        while (i.hasNext()) {
            UserCrop userCrop = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);
            System.out.println("==========usercropId=============="+userCrop.getId());

            User farmer = userRepo.findByid(userCrop.getUserId());
            if (farmer != null) {
                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);

                if (agent != null) {
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, userCrop.getCreationDate());
                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getManagerRoleName(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }

                    if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    }
                    row.createCell(4).setCellValue(agent.getUserCode());
                }

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());

                row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                row.createCell(12).setCellValue(getStateName(farmer,null, "village"));
                row.createCell(13).setCellValue(userCrop.getKhasraNo());

                String userLandDetailId = userCrop.getUserLandDetailId();
                UserLandDetail userLandDetail = genericMongoTemplate.findById(userLandDetailId, UserLandDetail.class);
                if (userLandDetail != null) {
                    row.createCell(14).setCellValue(userLandDetail.getLandSize().toString());
                    row.createCell(15).setCellValue(userLandDetail.getLandSizeType().toString());
                }
                row.createCell(16).setCellValue(userCrop.getCropSeason().toString());
                if (userCrop.getCrop() != null) {
                    row.createCell(17).setCellValue(userCrop.getCrop().getCropGroup().getType().toString());
                    row.createCell(18).setCellValue(userCrop.getCrop().getCropGroup().getName());
                    row.createCell(19).setCellValue(userCrop.getCrop().getCropName());
                }
                row.createCell(20).setCellValue(String.valueOf(userCrop.getLandSize()));
                row.createCell(21).setCellValue(userCrop.getLandSizeType().toString());
                row.createCell(22).setCellValue(String.valueOf(userCrop.getCropYield()));
                row.createCell(23).setCellValue(userCrop.getCropYieldUnit().toString());
                row.createCell(24).setCellValue(userCrop.getSeedQuantity()+ " "+userCrop.getSeedQuantityUnit());
                row.createCell(25).setCellValue(userCrop.getFertilizerQuantity()+ " "+userCrop.getFertilizerQuantityUnit());
                row.createCell(26).setCellValue(userCrop.getNumberOfManPower()+ " "+userCrop.getNumberOfManPowerUnit());
                row.createCell(27).setCellValue(userCrop.getYearOfSowing());

                if (userCrop.isCurrentCrop()) {
                    row.createCell(28).setCellValue("Current");
                } else {
                    row.createCell(28).setCellValue("History");
                }
                row.createCell(29).setCellValue(userCrop.getCreationDate().toInstant().toString());
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream getVillageAssetAndMappingDetailsExcel(String fromDate, String toDate, String locationId, String locationType) throws IOException, ParseException {
        List<String> COLUMNs = Arrays.asList("Manager-Type", "Manager-Name", "Manager-Number", "Agent_Name", "Agent_Code", "Mobile-Number", "State", "District", "Tehsil", "BLock", "Village", "Aanganwadi Centre", "Atm", "Adult Education Centre", "Banks",
                "Common Pastures", "Community Bio Gas or Recycle Of Waste For Production Use", "Community Forest", "Community Ponds For Fisheries", "Cottage", "Csc", "Water shed Development Project",
                "Drainage Facilities", "Community Waste Disposal System", "Community Rain Water Harvesting System", "Internet Café", "Health Facility", "Fertilizer Shop", "Drip Irrigation",
                "Electricity For Domestic Use", "Electricity Supply To MSME Units", "High School", "Extension Facilities For Aquaculture", "Goatary Development Project", "Government Seed Centre", "Middle School",
                "Vocational Educational Centre", "Food Storage Warehouse", "Govt Degree College", "Village Connected To All Origin", "Veterinary Clinic Hospital", "Sub Centre PHC", "Spo", "Soil Testing Centres",
                "Senior Secondary School", "Self Help Groups", "Recreational Centre", "Railway Station", "Public Transport", "Public Library", "Public Information Board", "Primary School", "Panchayat Bhawan", "PDS",
                "Village coordinate 1");

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Village Asset and Village Mapping Data");
        sh.setRandomAccessWindowSize(100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sh.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.size(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs.get(col));
            cell.setCellStyle(headerCellStyle);
        }


        // get Query according to report type
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, VILLAGE_ASSET);
//        List<VillageAsset> villageAssets = mongoTemplate.find(query, VillageAsset.class);

        List<VillageAsset> villageAssets = familyMemberHealthRecordRepo.getVillageAssets(fromDate, toDate, locationType, locationId, VILLAGE_ASSET, VillageAsset.class);

        int rowIdx = 1;

        Iterator<VillageAsset> i = villageAssets.iterator();
        while (i.hasNext()) {
            VillageAsset villageAsset = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User agent = userRepo.findByid(villageAsset.getAgentId());
            if (agent != null) {

                if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                    // get manager
//                User manager = employeeHistoryService.getAssignedUserByDate(agent, villageAsset.getCreationDate());
                    User manager = userRepo.findByid(agent.getCreatedByUserId());
                    if (manager != null) {
                        row.createCell(0).setCellValue(getManagerRoleName(manager));
                        row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                        row.createCell(2).setCellValue(manager.getPrimaryPhone());
                    }
                }

                if (!StringUtils.isEmpty(agent.getMiddleName())) {
                    row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                } else {
                    row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                }
                row.createCell(4).setCellValue(agent.getUserCode());
                row.createCell(5).setCellValue(agent.getPrimaryPhone());
                if(villageAsset.getVillage() != null) {
                    row.createCell(6).setCellValue(villageAsset.getVillage().getState().getName());
                    row.createCell(7).setCellValue(villageAsset.getVillage().getCity().getName());
                    row.createCell(8).setCellValue(villageAsset.getVillage().getTehsil().getName());
                    row.createCell(9).setCellValue(villageAsset.getVillage().getBlock().getName());
                    row.createCell(10).setCellValue(villageAsset.getVillage().getName());
                }else {
                    if(!StringUtils.isEmpty(villageAsset.getVillageId())) {
                        Village village = genericMongoTemplate.findById(villageAsset.getVillageId(), Village.class);
                        if (village != null) {
                            row.createCell(6).setCellValue(village.getState().getName());
                            row.createCell(7).setCellValue(village.getCity().getName());
                            row.createCell(8).setCellValue(village.getTehsil().getName());
                            row.createCell(9).setCellValue(village.getBlock().getName());
                            row.createCell(10).setCellValue(village.getName());
                        }
                    }
                }
                row.createCell(11).setCellValue(villageAsset.getAanganwadiCentre());
                row.createCell(12).setCellValue(villageAsset.getAtm());
                row.createCell(13).setCellValue(villageAsset.getAdultEducationCentre());
                row.createCell(14).setCellValue(villageAsset.getBanks());
                row.createCell(15).setCellValue(villageAsset.getCommonPastures());
                row.createCell(16).setCellValue(villageAsset.getCommunityBioGasOrRecycleOfWasteForProductionUse());
                row.createCell(17).setCellValue(villageAsset.getCommunityForest());
                row.createCell(18).setCellValue(villageAsset.getCommunityPondsForFisheries());
                row.createCell(19).setCellValue(villageAsset.getCottage());
                row.createCell(20).setCellValue(villageAsset.getCsc());
                row.createCell(21).setCellValue(villageAsset.getWatershedDevelopmentProject());
                row.createCell(22).setCellValue(villageAsset.getDrainageFacilities());
                row.createCell(23).setCellValue(villageAsset.getCommunityWasteDisposalSystem());
                row.createCell(24).setCellValue(villageAsset.getCommunityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam());
                row.createCell(25).setCellValue(villageAsset.getInternetCafe());
                row.createCell(26).setCellValue(villageAsset.getHealthFacility());
                row.createCell(27).setCellValue(villageAsset.getFertilizerShop());
                row.createCell(28).setCellValue(villageAsset.getDripIrrigationOrsprinkler_Irrigation());
                row.createCell(29).setCellValue(villageAsset.getElectricityForDomesticUse());
                row.createCell(30).setCellValue(villageAsset.getElectricitySupplyToMSMEUnits());
                row.createCell(31).setCellValue(villageAsset.getHighSchool());
                row.createCell(32).setCellValue(villageAsset.getExtensionFacilitiesForAquaculture());
                row.createCell(33).setCellValue(villageAsset.getGoataryDevelopmentProject());
                row.createCell(34).setCellValue(villageAsset.getGovernmentSeedCentre());
                row.createCell(35).setCellValue(villageAsset.getMiddleSchool());
                row.createCell(36).setCellValue(villageAsset.getVocationalEducationalCentreOrITIOrRSETIOrDDUGKY());
                row.createCell(37).setCellValue(villageAsset.getFoodStorageWarehouse());
                row.createCell(38).setCellValue(villageAsset.getGovtDegreeCollege());
                row.createCell(39).setCellValue(villageAsset.getVillageConnectedToAllOrigin());
                row.createCell(40).setCellValue(villageAsset.getVeterinaryClinicHospital());
                row.createCell(41).setCellValue(villageAsset.getSubCentrePHCOrCHC());
                row.createCell(42).setCellValue(villageAsset.getSpoOrPo());
                row.createCell(43).setCellValue(villageAsset.getSoilTestingCentres());
                row.createCell(44).setCellValue(villageAsset.getSeniorSecondarySchool());
                row.createCell(45).setCellValue(villageAsset.getSelfHelpGroups());
                row.createCell(46).setCellValue(villageAsset.getRecreationalCentreOrSportsOrPlayground());
                row.createCell(47).setCellValue(villageAsset.getRailwayStation());
                row.createCell(48).setCellValue(villageAsset.getPublicTransport());
                row.createCell(49).setCellValue(villageAsset.getPublicLibrary());
                row.createCell(50).setCellValue(villageAsset.getPublicInformationBoard());
                row.createCell(51).setCellValue(villageAsset.getPrimarySchool());
                row.createCell(52).setCellValue(villageAsset.getPanchayatBhawan());
                row.createCell(53).setCellValue(villageAsset.getpDS());

                VillageMapping villageMapping = agentService.getVillageMapping(villageAsset.getAgentId(), villageAsset.getVillageId());
                if (villageMapping != null) {
                    int column = 54;
                    for (GeoLocation location : villageMapping.getLocationPins()) {
                        Cell cell = headerRow.createCell(column);
                        cell.setCellValue("Village coordinate " + (column - 53));
                        cell.setCellStyle(headerCellStyle);
                        row.createCell(column).setCellValue(location.getLattitude() + "," + location.getLongitude());
                        column++;
                    }
                }
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static String getLocationKeyVillageAsset(String locationType) {
        String searchLocationKey;
        // Filter for locationTye
        switch (locationType) {
            case "STATE":
                searchLocationKey = "village.state.$id";
                break;
            case "DISTRICT":
                searchLocationKey = "village.city.$id";
                break;
            case "TEHSIL":
                searchLocationKey = "village.tehsil.$id";
                break;
            case "BLOCK":
                searchLocationKey = "village.block.$id";
                break;
            case "VILLAGE":
                searchLocationKey = "village.$id";
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return searchLocationKey;
    }
}
