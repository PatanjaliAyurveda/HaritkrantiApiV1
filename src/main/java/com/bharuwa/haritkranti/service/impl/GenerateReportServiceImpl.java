package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.repositories.FamilyMemberHealthRecordRepo;
import com.bharuwa.haritkranti.repositories.UserRepo;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.models.requestModels.GeoLocation;
import com.bharuwa.haritkranti.models.responseModels.OCEligibleUsersAndCount;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.responseModels.FertilizerResponse;
import com.bharuwa.haritkranti.models.responseModels.POMFertCal;
import com.bharuwa.haritkranti.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;

import static com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport.convertDate;
import static com.bharuwa.haritkranti.service.impl.CountServiceImpl.getQueryByReportType;
import static com.bharuwa.haritkranti.service.impl.ReportHistoryServiceImpl.getManagerRoleName;
import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author harman
 */
@Service
public class GenerateReportServiceImpl implements GenerateReportService {

    private final MongoTemplate mongoTemplate;

    public GenerateReportServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private LandService landService;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private CountService countService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private FamilyMemberHealthRecordRepo familyMemberHealthRecordRepo;

    /**
     * @param farmerList Farmer List that are added by the agent
     * @param fromDate   Date from which agent want reports
     * @param toDate     Date upto which agent want reports
     * @return Farm details data in excel format
     * @throws IOException
     */
    @Override
    public ByteArrayInputStream generateFarmDetailExcel(List<User> farmerList, String fromDate, String toDate) throws IOException, ParseException {
        String[] COLUMNs = new String[0];

        COLUMNs = new String[]{"Farmer-Name", "Mobile-Number", "No-of-Cows", "No-of-Buffalos", "No-of-Goats", "No-of-Hens", "No-of-Sheeps", "fish Production(Pond-Size)", "Prawn Production(Pond-Size)", "Crab Production(Pond-Size)", "Beekeeping(Total Hives)", "Sericulture(Total Pupa)"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CreationHelper createHelper = workbook.getCreationHelper();

        Sheet sheet = workbook.createSheet("FarmDetailReports");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        for (User farmer : farmerList) {

            Row row = sheet.createRow(rowIdx++);

            if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
            } else {
                row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
            }

            row.createCell(1).setCellValue(farmer.getPrimaryPhone());
            row.createCell(2).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Cow, fromDate, toDate));
            row.createCell(3).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Buffalo, fromDate, toDate));
            row.createCell(4).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Goat, fromDate, toDate));
            row.createCell(5).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Hen, fromDate, toDate));
            row.createCell(6).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Sheep, fromDate, toDate));

            FarmDetails fishDetail = userService.getFarmAnimalDetail(farmer.getId(), String.valueOf(FarmDetails.AnimalType.Fish), fromDate, toDate);
            if (fishDetail != null) {
                row.createCell(7).setCellValue(fishDetail.getProductionOutput() + "( " + fishDetail.getPondSize() + " " + fishDetail.getPondSizeType() + " )");
            } else {
                row.createCell(7).setCellValue(" ");
            }
            FarmDetails prawnDetail = userService.getFarmAnimalDetail(farmer.getId(), String.valueOf(FarmDetails.AnimalType.Prawn), fromDate, toDate);
            if (prawnDetail != null) {
                row.createCell(8).setCellValue(prawnDetail.getProductionOutput() + "( " + prawnDetail.getPondSize() + " " + prawnDetail.getPondSizeType() + " )");
            } else {
                row.createCell(8).setCellValue(" ");
            }

            FarmDetails crabDetail = userService.getFarmAnimalDetail(farmer.getId(), String.valueOf(FarmDetails.AnimalType.Crab), fromDate, toDate);
            if (crabDetail != null) {
                row.createCell(9).setCellValue(crabDetail.getProductionOutput() + "( " + crabDetail.getPondSize() + " " + crabDetail.getPondSizeType() + " )");
            } else {
                row.createCell(9).setCellValue(" ");
            }
            row.createCell(10).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Hive, fromDate, toDate));
            row.createCell(11).setCellValue(getAnimalDetail(farmer.getId(), FarmDetails.AnimalType.Pupa, fromDate, toDate));
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private int getAnimalDetail(String userId, FarmDetails.AnimalType animalType, String fromDate, String toDate) throws ParseException {
        int count = 0;
        List<FarmDetails.AnimalType> milkingAnimalTypeList = new ArrayList<>();
        milkingAnimalTypeList.add(FarmDetails.AnimalType.Cow);
        milkingAnimalTypeList.add(FarmDetails.AnimalType.Buffalo);
        milkingAnimalTypeList.add(FarmDetails.AnimalType.Goat);
        if (milkingAnimalTypeList.contains(animalType)) {
            MilkingAnimalDetails animalDetail = userService.getAnimalDetail(userId, String.valueOf(animalType), fromDate, toDate);
            if (animalDetail != null) {
                count = animalDetail.getTotalNoOfAnimals();
            }
        } else {
            FarmDetails farmDetails = userService.getFarmAnimalDetail(userId, String.valueOf(animalType), fromDate, toDate);
            if (farmDetails != null) {
                count = farmDetails.getQuantity();
            }
        }
        return count;
    }

    /**
     * @param farmerList Farmer List that are added by the agent
     * @param fromDate   Date from which agent want reports
     * @param toDate     Date upto which agent want reports
     * @return Horticulture details data in excel format
     * @throws IOException
     */

    @Override
    public ByteArrayInputStream generateHorticultureExcel(List<User> farmerList, String fromDate, String toDate) throws IOException {

        String[] COLUMNs = new String[]{"Farmer-Name", "Mobile-Number", "khasraNo", "TreeType", "Age", "Number", "Income"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("HorticultureReports");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        for (User farmer : farmerList) {
            List<Horticulture> horticultureList = landService.getHorticultureDetails(farmer.getId());

            for (Horticulture horticulture : horticultureList) {
                Row row = sheet.createRow(rowIdx++);

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(1).setCellValue(farmer.getPrimaryPhone());
                row.createCell(2).setCellValue(horticulture.getKhasraNo());
                row.createCell(3).setCellValue(horticulture.getTreeType().toString());
                row.createCell(4).setCellValue(horticulture.getAgeOfTree());
                row.createCell(5).setCellValue(horticulture.getNoOfTrees());
                row.createCell(6).setCellValue(horticulture.getIncome().toString() + " " + horticulture.getIncomePeriod().toString());
            }
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * @param farmerList Farmer List that are added by the agent
     * @param fromDate   Date from which agent want reports
     * @param toDate     Date upto which agent want reports
     * @return Financial details data in excel format
     * @throws IOException
     */

    @Override
    public ByteArrayInputStream generateFinancialDetailExcel(List<User> farmerList, String fromDate, String toDate) throws IOException {

        String[] COLUMNs = new String[]{"Farmer-Name", "Mobile-Number", "Bank-Name", "IFSC code", "Account-Number", "Account-Type"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("FinancialReports");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < COLUMNs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(COLUMNs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        for (User farmer : farmerList) {
            List<FinancialDetails> financialDetailsList = userBankService.getFinancialDetails(farmer.getId());

            for (FinancialDetails financialDetails : financialDetailsList) {
                Row row = sheet.createRow(rowIdx++);

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(0).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(1).setCellValue(farmer.getPrimaryPhone());
                row.createCell(2).setCellValue(financialDetails.getBankName());
                row.createCell(3).setCellValue(financialDetails.getIfscCode());
                row.createCell(4).setCellValue(financialDetails.getAccountNumber());
                row.createCell(5).setCellValue(financialDetails.getAccountType().toString());
            }
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

//    @Override
//    public ByteArrayInputStream generatePersonalDetailsExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
//        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Farmer-Name", "Mobile-Number", "Father-Name", "State", "District", "Tehsil",
//                "Block", "Village", "Address", "DOB", "Religion", "Category", "Aadhar-Number", "Qualification", "Creation-Date"};
//
//        Workbook workbook = new XSSFWorkbook();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        Sheet sheet = workbook.createSheet("Personal Details Reports");
//
//        Font headerFont = workbook.createFont();
//        headerFont.setBold(true);
//        headerFont.setColor(IndexedColors.BLUE.getIndex());
//
//        CellStyle headerCellStyle = workbook.createCellStyle();
//        headerCellStyle.setFont(headerFont);
//
//        // Row for Header
//        Row headerRow = sheet.createRow(0);
//
//        // Header
//        for (int col = 0; col < columns.length; col++) {
//            Cell cell = headerRow.createCell(col);
//            cell.setCellValue(columns[col]);
//            cell.setCellStyle(headerCellStyle);
//        }
//
//        // get Query according to report type
////        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, PERSONAL_DETAILS);
////        List<User> farmerList = mongoTemplate.find(query, User.class);
//
//        System.out.println("=============start================"+Calendar.getInstance().getTime());
//        List<User> farmerList = familyMemberHealthRecordRepo.getFarmerList(fromDate, toDate, locationType, locationId, PERSONAL_DETAILS, User.class);
//        System.out.println("=============end================"+Calendar.getInstance().getTime());
//        System.out.println("========size======"+farmerList.size());
//        int rowIdx = 1;
//
//        for (User farmer : farmerList) {
//            Row row = sheet.createRow(rowIdx++);
//            System.out.println("==========farmerId============="+farmer.getId());
//
//            if(!StringUtils.isEmpty(farmer.getCreatedByUserId())) {
//
//                User agent = userService.findByid(farmer.getCreatedByUserId());
//                if (agent != null) {
//                    // get manager
////                User manager = employeeHistoryService.getAssignedUserByDate(agent, farmer.getCreationDate());
//                    if (!StringUtils.isEmpty(agent.getCreatedByUserId())) {
//
//                        User manager = userService.findByid(agent.getCreatedByUserId());
//
//                        if (manager != null) {
//                            row.createCell(0).setCellValue(getUserMainRole(manager));
//                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
//                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
//                        }
//                    }
//                    String agentName;
//                    if (!StringUtils.isEmpty(agent.getMiddleName())) {
//                        agentName = agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName();
//                    } else {
//                        agentName = agent.getFirstName() + " " + agent.getLastName();
//                    }
//                    row.createCell(3).setCellValue(agentName);
//                }
//            }
//
//            if (!StringUtils.isEmpty(farmer.getMiddleName())) {
//                row.createCell(4).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
//            } else {
//                row.createCell(4).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
//            }
//
//            row.createCell(5).setCellValue(farmer.getPrimaryPhone());
//            row.createCell(6).setCellValue(farmer.getFatherName());
//
//            row.createCell(7).setCellValue(getStateName(farmer,null, "state"));
//            row.createCell(8).setCellValue(getStateName(farmer,null,  "city"));
//            row.createCell(9).setCellValue(getStateName(farmer,null, "tehsil"));
//            row.createCell(10).setCellValue(getStateName(farmer,null, "block"));
//            row.createCell(11).setCellValue(getStateName(farmer,null, "village"));
//            row.createCell(12).setCellValue(getStateName(farmer,null, "address"));
//
//            row.createCell(13).setCellValue(farmer.getDateOfBirth() != null ?convertDate(farmer.getDateOfBirth()) : "");
//            row.createCell(14).setCellValue(!StringUtils.isEmpty(farmer.getReligion()) ? farmer.getReligion() : "");
//            row.createCell(15).setCellValue(!StringUtils.isEmpty(farmer.getCategory()) ? farmer.getCategory().toString() : "");
//            UserIdentificationDetails userIdentificationDetails = userService.getUserIdentificationDetails(farmer.getId());
//            if (userIdentificationDetails != null) {
//                row.createCell(16).setCellValue(userIdentificationDetails.getAadhaarCardNumber());
//            }
//            row.createCell(17).setCellValue(!StringUtils.isEmpty(farmer.getQualification()) ? farmer.getQualification().toString() : "");
//            row.createCell(18).setCellValue(convertDate(farmer.getCreationDate()));
//        }
//
//        workbook.write(out);
//        return new ByteArrayInputStream(out.toByteArray());
//    }

    @Override
    public ByteArrayInputStream generatePersonalDetailsExcelNew(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Agent-Mobile", "Farmer-Name", "Farmer-Code", "Mobile-Number", "Father-Name", "State", "District", "Tehsil",
                "Block", "Village", "Address", "DOB", "Religion", "Category", "Aadhar-Number", "Qualification", "Creation-Date"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Personal Details");
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
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        List<User> farmerList = familyMemberHealthRecordRepo.getFarmerList(fromDate, toDate, locationType, locationId, PERSONAL_DETAILS, User.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());
        System.out.println("========size======"+farmerList.size());
        int rowIdx = 1;

        Iterator<User> i = farmerList.iterator();
        System.out.println("==========farmerId============="+farmerList.get(0).getId());
        while (i.hasNext()) {
            User farmer = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);
            System.out.println("==========farmerId============="+farmer.getId());

            if(!StringUtils.isEmpty(farmer.getCreatedByUserId())) {

                User agent = userService.findByid(farmer.getCreatedByUserId());
                if (agent != null) {
                    // get manager
//                User manager = employeeHistoryService.getAssignedUserByDate(agent, farmer.getCreationDate());
                    if (!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        User manager = userService.findByid(agent.getCreatedByUserId());

                        if (manager != null) {
                            row.createCell(0).setCellValue(getUserMainRole(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }
                    String agentName;
                    if (!StringUtils.isEmpty(agent.getMiddleName())) {
                        agentName = agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName();
                    } else {
                        agentName = agent.getFirstName() + " " + agent.getLastName();
                    }
                    row.createCell(3).setCellValue(agentName);
                    row.createCell(4).setCellValue(agent.getUserCode());
                    row.createCell(5).setCellValue(agent.getPrimaryPhone());
                }
            }

            if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                row.createCell(6).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
            } else {
                row.createCell(6).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
            }

            row.createCell(7).setCellValue(farmer.getUserCode());
            row.createCell(8).setCellValue(farmer.getPrimaryPhone());
            row.createCell(9).setCellValue(farmer.getFatherName());

            row.createCell(10).setCellValue(getStateName(farmer,null, "state"));
            row.createCell(11).setCellValue(getStateName(farmer,null,  "city"));
            row.createCell(12).setCellValue(getStateName(farmer,null, "tehsil"));
            row.createCell(13).setCellValue(getStateName(farmer,null, "block"));
            row.createCell(14).setCellValue(getStateName(farmer,null, "village"));
            row.createCell(15).setCellValue(getStateName(farmer,null, "address"));

            row.createCell(16).setCellValue(farmer.getDateOfBirth() != null ?convertDate(farmer.getDateOfBirth()) : "");
            row.createCell(17).setCellValue(!StringUtils.isEmpty(farmer.getReligion()) ? farmer.getReligion() : "");
            row.createCell(18).setCellValue(!StringUtils.isEmpty(farmer.getCategory()) ? farmer.getCategory().toString() : "");
            UserIdentificationDetails userIdentificationDetails = userService.getUserIdentificationDetails(farmer.getId());
            if (userIdentificationDetails != null) {
                row.createCell(19).setCellValue(userIdentificationDetails.getAadhaarCardNumber());
            }
            row.createCell(20).setCellValue(!StringUtils.isEmpty(farmer.getQualification()) ? farmer.getQualification().toString() : "");
            row.createCell(21).setCellValue(convertDate(farmer.getCreationDate()));
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateFamilyDetailsExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "Member-Name", "Dependent", "Same Address", "Relation", "Age", "State", "District", "Tehsil",
                "Block", "Village", "Address", "Creation-Date"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Family Detail Report");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, FAMILY_DETAILS);
//        List<FamilyMember> familyMemberList = mongoTemplate.find(query, FamilyMember.class);

        List<FamilyMember> familyMemberList = familyMemberHealthRecordRepo.getFamilyMemberList(fromDate, toDate, locationType, locationId, FAMILY_DETAILS, FamilyMember.class);
        int rowIdx = 1;

        Iterator<FamilyMember> i = familyMemberList.iterator();
        while (i.hasNext()) {
            FamilyMember familyMember = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User agent = userService.findByid(familyMember.getAgentId());
            if (agent != null) {
                if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                    // get manager
//                User manager = employeeHistoryService.getAssignedUserByDate(agent, familyMember.getCreationDate());
                    User manager = userService.findByid(agent.getCreatedByUserId());
                    if (manager != null) {
                        row.createCell(0).setCellValue(getUserMainRole(manager));
                        row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                        row.createCell(2).setCellValue(manager.getPrimaryPhone());
                    }
                }

                String agentName;
                if (!StringUtils.isEmpty(agent.getMiddleName())) {
                    agentName = agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName();
                } else {
                    agentName = agent.getFirstName() + " " + agent.getLastName();
                }
                row.createCell(3).setCellValue(agentName);
                row.createCell(4).setCellValue(agent.getUserCode());

            }

            User farmer = userService.findByid(familyMember.getFarmerId());
            if (farmer != null) {
                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());
            }

            if (!StringUtils.isEmpty(familyMember.getMiddleName())) {
                row.createCell(8).setCellValue(familyMember.getFirstName() + " " + familyMember.getMiddleName() + " " + familyMember.getLastName());
            } else {
                row.createCell(8).setCellValue(familyMember.getFirstName() + " " + familyMember.getLastName());
            }
            row.createCell(9).setCellValue(String.valueOf(familyMember.isDependent()));
            row.createCell(10).setCellValue(String.valueOf(familyMember.isSameAddress()));
            row.createCell(11).setCellValue(familyMember.getRelationship().toString());
            row.createCell(12).setCellValue(familyMember.getAge());

            row.createCell(13).setCellValue(getStateName(null, familyMember, "state"));
            row.createCell(14).setCellValue(getStateName(null, familyMember, "city"));
            row.createCell(15).setCellValue(getStateName(null, familyMember, "tehsil"));
            row.createCell(16).setCellValue(getStateName(null, familyMember, "block"));
            row.createCell(17).setCellValue(getStateName(null, familyMember, "village"));
            row.createCell(18).setCellValue(getStateName(null, familyMember, "address"));

            row.createCell(19).setCellValue(convertDate(familyMember.getCreationDate()));
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    /**
     * @param farmerList Farmer List that are added by the agent
     * @param fromDate   Date from which agent want reports
     * @param toDate     Date upto which agent want reports
     * @param agentId    Agent Id
     * @return Insurance details data in excel format
     * @throws IOException
     */
    @Override
    public ByteArrayInputStream generateInsuranceDetailsExcel(List<User> farmerList, String fromDate, String toDate, String agentId) throws IOException {

        String[] columns = new String[]{"Agent-Name", "Farmer-Name", "Mobile-Number", "InsuranceType", "Amount"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Insurance Detail Report");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }

        User agent = userService.findByid(agentId);
        String agentName;

        if (!StringUtils.isEmpty(agent.getMiddleName())) {
            agentName = agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName();
        } else {
            agentName = agent.getFirstName() + " " + agent.getLastName();
        }
        int rowIdx = 1;
        for (User farmer : farmerList) {
            List<InsuranceDetails> insuranceDetailList = userBankService.getInsuranceDetails(farmer.getId());
            for (InsuranceDetails insuranceDetails : insuranceDetailList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(agentName);

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(1).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(1).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }
                row.createCell(2).setCellValue(farmer.getPrimaryPhone());
                row.createCell(3).setCellValue(insuranceDetails.getInsuranceType().toString());
                row.createCell(4).setCellValue(String.valueOf(insuranceDetails.getAmount()));
            }
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * Generate CSV for Agent's, Farmer's Loan Details
     *
     * @param farmerList Farmer List that are added by the agent
     * @param fromDate   Date from which agent want reports
     * @param toDate     Date upto which agent want reports
     * @param agentId    Agent Id
     * @return Loan details data in excel format
     * @throws IOException
     */
    @Override
    public ByteArrayInputStream generateLoanDetailsExcel(List<User> farmerList, String fromDate, String toDate, String agentId) throws IOException {

        String[] columns = new String[]{"Agent-Name", "Farmer-Name", "Mobile-Number", "loanType", "bankName", "totalAmount"
                , "emi", "amountPaid", "timePeriod"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Loan Detail Report");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }

        User agent = userService.findByid(agentId);
        String agentName;
        if (!StringUtils.isEmpty(agent.getMiddleName())) {
            agentName = agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName();
        } else {
            agentName = agent.getFirstName() + " " + agent.getLastName();
        }
        int rowIdx = 1;
        for (User farmer : farmerList) {
            List<LoanDetails> loanDetailsList = userBankService.getLoanDetails(farmer.getId());
            for (LoanDetails loanDetails : loanDetailsList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(agentName);

                if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                    row.createCell(1).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else {
                    row.createCell(1).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                }
                row.createCell(2).setCellValue(farmer.getPrimaryPhone());
                row.createCell(3).setCellValue(loanDetails.getLoanType().toString());
                row.createCell(4).setCellValue(loanDetails.getBankName());
                row.createCell(5).setCellValue(String.valueOf(loanDetails.getTotalAmount()));
                row.createCell(6).setCellValue(String.valueOf(loanDetails.getEmi()));
                row.createCell(7).setCellValue(String.valueOf(loanDetails.getAmountPaid()));
                row.createCell(8).setCellValue(loanDetails.getTimePeriod());
            }
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }


    @Override
    public ByteArrayInputStream generateDairyFarmDetailExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {

        String[] COLUMNs = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent_Name", "Agent_Code", "Farmer_Name", "Farmer_Code", "Mobile_No", "State", "District", "Tehsil", "Block", "Village",
                "Creation-Date", "Milking-Animal-Type", " No. Of Animals", " No. Of Milking-Animals", "Milk-Production", "Milk-Production-Unit", "Home-Usage-Milk", "Milk-Quantity-Sold", "Milk-Sold-Unit", "Income(Rs)", "Income_duration", "Location-1"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Dairy Farm Detail Report");
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

        // get Query according to report type
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, DAIRY_FARM_DETAIL);
//        List<MilkingAnimalDetails> milkingAnimalDetailsList = mongoTemplate.find(query, MilkingAnimalDetails.class);
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        List<MilkingAnimalDetails> milkingAnimalDetailsList = familyMemberHealthRecordRepo.getMilkingAnimalDetailsList(fromDate, toDate, locationType, locationId, DAIRY_FARM_DETAIL, MilkingAnimalDetails.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());

        int maxHeaders = 0;
        int rowIdx = 1;

        Iterator<MilkingAnimalDetails> i = milkingAnimalDetailsList.iterator();
        while (i.hasNext()) {
            MilkingAnimalDetails milkingAnimalDetail = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            System.out.println("=========milkid==============="+milkingAnimalDetail.getId());
            User farmer = userRepo.findByid(milkingAnimalDetail.getUserId());
            if(!StringUtils.isEmpty(farmer.getCreatedByUserId())) {

                User agent = userService.findByid(farmer.getCreatedByUserId());
//                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);


                if (agent != null) {
                    if (!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, milkingAnimalDetail.getCreationDate());
                        User manager = genericMongoTemplate.findById(agent.getCreatedByUserId(), User.class);
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
            row.createCell(13).setCellValue(convertDate(milkingAnimalDetail.getCreationDate()));
            row.createCell(14).setCellValue(milkingAnimalDetail.getAnimalType().toString());
            row.createCell(15).setCellValue(milkingAnimalDetail.getTotalNoOfAnimals());
            row.createCell(16).setCellValue(milkingAnimalDetail.getMilkingCondition());
            row.createCell(17).setCellValue(String.valueOf(milkingAnimalDetail.getMilkProductionOutput()));
            row.createCell(18).setCellValue(String.valueOf(milkingAnimalDetail.getUnitProductionOutput()));
            row.createCell(19).setCellValue(String.valueOf(milkingAnimalDetail.getHomeUsage()));
            row.createCell(20).setCellValue(String.valueOf(milkingAnimalDetail.getSale()));
            row.createCell(21).setCellValue(String.valueOf(milkingAnimalDetail.getUnitProductionOutput()));

            row.createCell(22).setCellValue(String.valueOf(milkingAnimalDetail.getIncome()));
            row.createCell(23).setCellValue(String.valueOf(milkingAnimalDetail.getIncomePeriod()));

  //          GeoLocationMapping milkingAnimalMapping = userExtraService.getGeoLocationMappingByType(milkingAnimalDetail.getUserId(), GeoLocationMapping.MappedLocationType.Milking_Animal_Shed.toString(), milkingAnimalDetail.getId(), "");
            GeoLocationMapping milkingAnimalMapping = userExtraService.getGeoLocationMappingByType(milkingAnimalDetail.getUserId(), "");

            if (milkingAnimalMapping != null) {
                List<GeoLocation> locationPinsList = milkingAnimalMapping.getLocationPins();
                if (!locationPinsList.isEmpty()) {

                    // Create Dynamic Header
                    int column = 24;
                    if (maxHeaders < locationPinsList.size()) {
                        for (int col = 0; col < locationPinsList.size(); col++) {
                            Cell cell = headerRow.createCell(column);
                            cell.setCellValue("Location-" + (column - 23));
                            cell.setCellStyle(headerCellStyle);
                            column++;
                        }
                        maxHeaders = locationPinsList.size();
                    }
                    int columnIndex = 24;
                    for (GeoLocation location : locationPinsList) {
                        row.createCell(columnIndex).setCellValue(location.getLattitude() + "," + location.getLongitude());
                        columnIndex++;
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
    public ByteArrayInputStream getAgentUniqueCode() throws IOException {
        String[] columns = new String[]{"Agent-Code", "Mobile-Number", "Agent-Name", "State", "District", "Tehsil", "Block"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Agent Unique Code");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Row for Header
        Row headerRow = sheet.createRow(0);

        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }

        Query query = new Query();
        query = query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT));
        final List<User> userList = mongoTemplate.find(query, User.class);

        int rowIdx = 1;
        for (User user : userList) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(user.getUserCode());
            row.createCell(1).setCellValue(user.getPrimaryPhone());

            if (!StringUtils.isEmpty(user.getMiddleName()) && !StringUtils.isEmpty(user.getLastName())) {
                row.createCell(2).setCellValue(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
            } else if (!StringUtils.isEmpty(user.getLastName())) {
                row.createCell(2).setCellValue(user.getFirstName() + " " + user.getLastName());
            } else {
                row.createCell(2).setCellValue(user.getFirstName());
            }

            row.createCell(3).setCellValue(getStateName(user, null, "state"));
            row.createCell(4).setCellValue(getStateName(user, null, "city"));
            row.createCell(5).setCellValue(getStateName(user, null, "tehsil"));
            row.createCell(6).setCellValue(getStateName(user, null, "block"));
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }


    /**
     * Between farmer or familyMember one object MUST Be NULL
     *
     * @param farmer
     * @param familyMember
     * @param type
     * @return location name
     */
    public static String getStateName(User farmer, FamilyMember familyMember, String type) {
        if (farmer == null && familyMember == null) {
            return "error";
        }
        switch (type) {
            case "state":
                if (farmer != null && farmer.getAddressModel() != null && farmer.getAddressModel().getState() != null && !StringUtils.isEmpty(farmer.getAddressModel().getState().getName())) {
                    return farmer.getAddressModel().getState().getName();
                }
                if (familyMember != null && familyMember.getAddress() != null && familyMember.getAddress().getState() != null && !StringUtils.isEmpty(familyMember.getAddress().getState().getName())) {
                    return familyMember.getAddress().getState().getName();
                }
                return "";

            case "city":
                if (farmer != null && farmer.getAddressModel() != null && farmer.getAddressModel().getCity() != null && !StringUtils.isEmpty(farmer.getAddressModel().getCity().getName())) {
                    return farmer.getAddressModel().getCity().getName();
                }
                if (familyMember != null && familyMember.getAddress() != null && familyMember.getAddress().getCity() != null && !StringUtils.isEmpty(familyMember.getAddress().getCity().getName())) {
                    return familyMember.getAddress().getCity().getName();
                }
                return "";

            case "tehsil":
                if (farmer != null && farmer.getAddressModel() != null && farmer.getAddressModel().getTehsil() != null && !StringUtils.isEmpty(farmer.getAddressModel().getTehsil().getName())) {
                    return farmer.getAddressModel().getTehsil().getName();
                }
                if (familyMember != null && familyMember.getAddress() != null && familyMember.getAddress().getTehsil() != null && !StringUtils.isEmpty(familyMember.getAddress().getTehsil().getName())) {
                    return familyMember.getAddress().getTehsil().getName();
                }
                return "";

            case "block":
                if (farmer != null && farmer.getAddressModel() != null && farmer.getAddressModel().getBlock() != null && !StringUtils.isEmpty(farmer.getAddressModel().getBlock().getName())) {
                    return farmer.getAddressModel().getBlock().getName();
                }
                if (familyMember != null && familyMember.getAddress() != null && familyMember.getAddress().getBlock() != null && !StringUtils.isEmpty(familyMember.getAddress().getBlock().getName())) {
                    return familyMember.getAddress().getBlock().getName();
                }
                return "";

            case "village":
                if (farmer != null && farmer.getAddressModel() != null && farmer.getAddressModel().getVillageModel() != null && !StringUtils.isEmpty(farmer.getAddressModel().getVillageModel().getName())) {
                    return farmer.getAddressModel().getVillageModel().getName();
                } else if (farmer != null && farmer.getAddressModel() != null && !StringUtils.isEmpty(farmer.getAddressModel().getVillage())) {
                    return farmer.getAddressModel().getVillage();
                }

                if (familyMember != null && familyMember.getAddress() != null && familyMember.getAddress().getVillageModel() != null) {
                    return  familyMember.getAddress().getVillageModel().getName();
                }
                return "";

            case "address":
                if (farmer != null && farmer.getAddressModel() != null && !StringUtils.isEmpty(farmer.getAddressModel().getAddress())) {
                    return farmer.getAddressModel().getAddress();
                }

                if (familyMember != null && familyMember.getAddress() != null) {
                    return familyMember.getAddress().getAddress();
                }

                return "";

        }
        return "";
    }

    public ByteArrayInputStream reportsToExcel(List<ReportHistory> reportHistoryList, String farmingType) throws IOException {

        String[] COLUMNs = new String[0];

        if (farmingType.equals(FarmingType.Type.Chemical.toString()) || farmingType.equals(FarmingType.Type.Organic.toString())) {
            COLUMNs = new String[]{"FarmingType", "Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "Khasra-No", "Khasra-Size", "CropName", "Farm-Type", "nValue", "pValue", "kValue", "Organic-Carbon", "pHValue", "Orgainic-Fertilizers", "Chemical-Fertilizers", "Creation-Date", "State", "District", "Tehsil",
                    "Block", "Village", "Location 1"};
        } else if (farmingType.equals(FarmingType.Type.INM_MIX.toString())) {
            COLUMNs = new String[]{"FarmingType", "Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "Khasra-No", "Khasra-Size", "CropName", "Farm-Type", "nValue", "pValue", "kValue", "Organic-Carbon", "pHValue", "Orgainic-Fertilizers", "Chemical-Fertilizers", "Creation-Date", "State", "District", "Tehsil",
                    "Block", "Village", "Location"};
        }

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("FertilizerRecommendationReports");
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

        int rowIdx = 1;
        int maxHeaders = 0;


        Iterator<ReportHistory> i = reportHistoryList.iterator();
        while (i.hasNext()) {
            ReportHistory history = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            if (history.getNpkRecommendation() != null) {
                NPKRecommendation recommendation = history.getNpkRecommendation();
                final User user = userService.findByid(history.getUserId());
                UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(recommendation.getKhasraNo(), history.getUserId());
                row.createCell(0).setCellValue(recommendation.getFarmingType().toString());

                if (user != null) {
                    User agent = StringUtils.isEmpty(user.getCreatedByUserId()) ? userService.getAgentByUserId(user.getId()) : genericMongoTemplate.findById(user.getCreatedByUserId(), User.class);

                    if (agent != null) {
                        row.createCell(4).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                        row.createCell(5).setCellValue(agent.getUserCode());

                        // get manager
//                        User manager = employeeHistoryService.getAssignedUserByDate(agent, history.getCreationDate());

                        User manager = genericMongoTemplate.findById(agent.getCreatedByUserId(), User.class);

                        if (manager != null) {
                            row.createCell(1).setCellValue(getUserMainRole(manager));
                            row.createCell(2).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(3).setCellValue(manager.getPrimaryPhone());
                        }
                    }

                    row.createCell(6).setCellValue(user.getFirstName() + " " + user.getLastName());
                    row.createCell(7).setCellValue(user.getUserCode());
                    row.createCell(8).setCellValue(user.getPrimaryPhone());
                }
                row.createCell(9).setCellValue(recommendation.getKhasraNo());

                // get Crop Name
                if (!StringUtils.isEmpty(recommendation.getCropId())) {
                    Crop crop = landService.getCrop(recommendation.getCropId());
                    if (crop != null) {
                        row.createCell(11).setCellValue(crop.getCropName());
                    }
                }

                SoilTest soilTest = recommendation.getSoilTest();
                if (soilTest != null) {
                    row.createCell(13).setCellValue(soilTest.getnValue());
                    row.createCell(14).setCellValue(soilTest.getpValue());
                    row.createCell(15).setCellValue(soilTest.getkValue());
                    row.createCell(16).setCellValue(soilTest.getOrganicCarbon());
                    row.createCell(17).setCellValue(soilTest.getpHValue());
                }
                if (farmingType.equals(FarmingType.Type.INM_MIX.toString())) {

                    List<POMFertCal> pomFertCalList = history.getMixReqFert().getOrganicReqFert().getPomFertCals();
                    //get List of Organic FfarmingTypeertilizer in List of String
                    List<String> organicFertilizerList = new ArrayList<>();
                    for (POMFertCal pomFertCal : pomFertCalList) {
                        String requirment = pomFertCal.getRequiredFert().toBigInteger().toString();
                        organicFertilizerList.add(pomFertCal.getFertilizerName() + " (" + pomFertCal.getFertilizerType() + "-" + pomFertCal.getCategoryType() + ") : " + requirment + " " + pomFertCal.getUnit());
                    }

                    //get List of Chemical Fertilizer in List of String
                    Set<FertilizerResponse> fertilizerResponses = history.getMixReqFert().getFertilizerResponseList();
                    List<String> chemicalFertilizerList = new ArrayList<>();
                    for (FertilizerResponse response : fertilizerResponses) {
                        int requirment = 0;
                        requirment = (int) response.getRequirement();
                        chemicalFertilizerList.add(response.getFertilizerName() + " (" + response.getFertylizerType() + ") : " + requirment + " " + response.getUnit());
                    }

                    row.createCell(18).setCellValue(organicFertilizerList.toString());

                    row.createCell(19).setCellValue(chemicalFertilizerList.toString());

                } else if (farmingType.equals(FarmingType.Type.Chemical.toString())) {
                    List<FertilizerResponse> responseList = history.getFertilizerResponse();

                    //get List of Chemical Fertilizer in List of String
                    List<String> chemicalFertilizerList = new ArrayList<>();
                    for (FertilizerResponse response : responseList) {
                        int requirment = 0;
                        requirment = (int) response.getRequirement();
                        chemicalFertilizerList.add(response.getFertilizerName() + " (" + response.getFertylizerType() + ") : " + requirment + " " + response.getUnit());
                    }

                    row.createCell(19).setCellValue(chemicalFertilizerList.toString());
                } else if (farmingType.equals(FarmingType.Type.Organic.toString())) {
                    List<POMFertCal> pomFertCalList = history.getOrganicReqFert().getPomFertCals();

                    //get List of Organic Fertilizer in List of String
                    List<String> organicFertilizerList = new ArrayList<>();
                    for (POMFertCal pomFertCal : pomFertCalList) {
                        String requirment = pomFertCal.getRequiredFert().toBigInteger().toString();
                        organicFertilizerList.add(pomFertCal.getFertilizerName() + " (" + pomFertCal.getFertilizerType() + "-" + pomFertCal.getCategoryType() + ") : " + requirment + " " + pomFertCal.getUnit());
                    }

                    row.createCell(18).setCellValue(organicFertilizerList.toString());
                }

                if (userLandDetail != null) {

                    row.createCell(10).setCellValue(userLandDetail.getLandSize() + " " + userLandDetail.getLandSizeType());
                    row.createCell(12).setCellValue(userLandDetail.getFarmType().toString());

                    row.createCell(20).setCellValue(convertDate(history.getCreationDate()));
                    row.createCell(21).setCellValue(userLandDetail.getState().getName());
                    row.createCell(22).setCellValue(userLandDetail.getCity().getName());
        //            row.createCell(23).setCellValue(userLandDetail.getTehsil().getName());
       //             row.createCell(24).setCellValue(userLandDetail.getBlock().getName());
       //             row.createCell(25).setCellValue(userLandDetail.getVillageModel().getName());


                    if (!StringUtils.isEmpty(userLandDetail.getLandMapId())) {
                        UserLandMapping userLandMapping = agentService.getUserLandMappingById(userLandDetail.getLandMapId());

                        if (userLandMapping != null) {
                            List<GeoLocation> locationPinsList = userLandMapping.getLocationPins();
                            if (!locationPinsList.isEmpty()) {


                                // Create Dynamic Header
                                int column = 26;
                                if (maxHeaders < locationPinsList.size()) {
                                    for (int col = 0; col < locationPinsList.size(); col++) {
                                        Cell cell = headerRow.createCell(column);
                                        cell.setCellValue("Location" + (column - 25));
                                        cell.setCellStyle(headerCellStyle);
                                        column++;
                                    }
                                    maxHeaders = locationPinsList.size();
                                }
                                int columnIndex = 26;
                                for (GeoLocation location : locationPinsList) {
                                    row.createCell(columnIndex).setCellValue(location.getLattitude() + "," + location.getLongitude());
                                    columnIndex++;
                                }

                            }
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
    public ByteArrayInputStream getSubsidyDetailsExcels(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {

        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "State", "District", "Tehsil", "Block", "Village", "Subsidy-Type", "Subsidy-Item", "Subsidy-Quantity", "Subsidy-Amount", "Entry-Date"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet(" Subsidy and Insurance Detail Report");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, SUBSIDY_DETAIL);
//        List<UserSubsidy> userSubsidyList = mongoTemplate.find(query, UserSubsidy.class);
        List<UserSubsidy> userSubsidyList = familyMemberHealthRecordRepo.getUserSubsidyList(fromDate, toDate, locationType, locationId, SUBSIDY_DETAIL, UserSubsidy.class);

        int rowIdx = 1;

        Iterator<UserSubsidy> i = userSubsidyList.iterator();
        while (i.hasNext()) {
            UserSubsidy userSubsidy = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User farmer = userService.findByid(userSubsidy.getUserId());
            if (farmer != null) {
                if(!StringUtils.isEmpty(farmer.getCreatedByUserId())) {

                    User agent = userService.findByid(farmer.getCreatedByUserId());
                    if (agent != null) {
                        System.out.println("========agentId========="+agent.getId());
                        if (!StringUtils.isEmpty(agent.getCreatedByUserId())) {


                            // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, userSubsidy.getCreationDate());
                            User manager = userService.findByid(agent.getCreatedByUserId());
                            if (manager != null) {
                                row.createCell(0).setCellValue(getManagerRoleName(manager));
                                row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                                row.createCell(2).setCellValue(manager.getPrimaryPhone());
                            }
                        }
                        if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                            row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                        } else if (!StringUtils.isEmpty(agent.getLastName())) {
                            row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                        } else {
                            row.createCell(3).setCellValue(agent.getFirstName());
                        }
                        row.createCell(4).setCellValue(agent.getUserCode());
                    }
                }

                if (!StringUtils.isEmpty(farmer.getMiddleName()) && !StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else if (!StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());

                row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                row.createCell(12).setCellValue(getStateName(farmer,null, "village"));

                row.createCell(13).setCellValue(userSubsidy.getSubsidy().getName());
                row.createCell(14).setCellValue(userSubsidy.getItem());
                row.createCell(15).setCellValue(userSubsidy.getQuantity());
                row.createCell(16).setCellValue(userSubsidy.getAmount().doubleValue());
                row.createCell(17).setCellValue(convertDate(userSubsidy.getCreationDate()));
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    @Override
    public ByteArrayInputStream getOrganicCertificateDetails(String fromDate, String toDate) throws IOException, ParseException {

        String[] columns = new String[]{"Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-code", "Farmer-MobileNumber", "FarmingType", "Khasra-No", "Khasra-Size", "Crop-Name",
                "Farm-Type", "nValue", "pValue", "kValue", "pH_Value", "Organic_Carbon", "Fertilizers", "State", "District", "Tehsil", "Block", "Village"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(" Organic Certificate Detail Report");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sheet.createRow(0);
        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        Query query;
        int rowIdx = 1;

        OCEligibleUsersAndCount ocEligibleUsersAndCount = countService.getOCEligibleUsers(fromDate, toDate);
        System.out.println("=====ocEligibleUsersAndCount====" + ocEligibleUsersAndCount);
        final Set<User> userSet = ocEligibleUsersAndCount.getUsers();
        for (User user : userSet) {
            User agent = StringUtils.isEmpty(user.getCreatedByUserId()) ? userService.getAgentByUserId(user.getId()) : genericMongoTemplate.findById(user.getCreatedByUserId(), User.class);

            query = new Query(Criteria.where("userId").is(user.getId()).and("organicLandEligible").exists(true).ne(false));
            List<UserLandDetail> userLandDetailList = mongoTemplate.find(query, UserLandDetail.class);
            for (UserLandDetail userLandDetail : userLandDetailList) {

                Row row = sheet.createRow(rowIdx++);
                if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                    row.createCell(0).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                } else if (!StringUtils.isEmpty(agent.getLastName())) {
                    row.createCell(0).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                } else {
                    row.createCell(0).setCellValue(agent.getFirstName());
                }
                row.createCell(1).setCellValue(agent.getUserCode());
                if (!StringUtils.isEmpty(user.getMiddleName()) && !StringUtils.isEmpty(user.getLastName())) {
                    row.createCell(2).setCellValue(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                } else if (!StringUtils.isEmpty(user.getLastName())) {
                    row.createCell(2).setCellValue(user.getFirstName() + " " + user.getLastName());
                } else {
                    row.createCell(2).setCellValue(user.getFirstName());
                }
                row.createCell(3).setCellValue(user.getUserCode());
                row.createCell(4).setCellValue(user.getPrimaryPhone());
                row.createCell(6).setCellValue(userLandDetail.getKhasraNo());
                row.createCell(7).setCellValue(userLandDetail.getLandSize() + "" + userLandDetail.getLandSizeType());
                row.createCell(15).setCellValue(user.getAddressModel().getState().getName());
                row.createCell(16).setCellValue(user.getAddressModel().getCity().getName());
                row.createCell(17).setCellValue(user.getAddressModel().getTehsil().getName());
                row.createCell(18).setCellValue(user.getAddressModel().getBlock().getName());
                row.createCell(19).setCellValue(user.getAddressModel().getVillageModel().getName());

                row.createCell(16).setCellValue(user.getAddressModel().getState().getName());
                row.createCell(17).setCellValue(user.getAddressModel().getCity().getName());
                row.createCell(18).setCellValue(user.getAddressModel().getTehsil().getName());
                row.createCell(19).setCellValue(user.getAddressModel().getBlock().getName());
                row.createCell(20).setCellValue(user.getAddressModel().getVillageModel().getName());

//                get Oranic Recommendation Report for UserLandDetails
                query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
                query = query.addCriteria(Criteria.where("userId").is(user.getId()).and("npkRecommendation.khasraNo").is(userLandDetail.getKhasraNo()).and("npkRecommendation.farmingType").is(FarmingType.Type.Organic));
                ReportHistory organicReport = mongoTemplate.findOne(query, ReportHistory.class);
                if (organicReport != null) {
                    NPKRecommendation npkRecommendation = organicReport.getNpkRecommendation();
                    List<POMFertCal> pomFertCalList = organicReport.getOrganicReqFert().getPomFertCals();
                    row.createCell(5).setCellValue(npkRecommendation.getFarmingType().toString());
                    row.createCell(6).setCellValue(userLandDetail.getKhasraNo());
                    row.createCell(7).setCellValue(userLandDetail.getLandSize() + "" + userLandDetail.getLandSizeType());
//                get Crop from Organic Report
                    query = new Query(Criteria.where("_id").is(new ObjectId(npkRecommendation.getCropId())));
                    Crop crop = mongoTemplate.findOne(query, Crop.class);
                    if (crop != null) {
                        row.createCell(8).setCellValue(crop.getCropName());
                    }
                    row.createCell(9).setCellValue(npkRecommendation.getSoilTest().getnValue());
                    row.createCell(10).setCellValue(npkRecommendation.getSoilTest().getkValue());
                    row.createCell(11).setCellValue(npkRecommendation.getSoilTest().getpValue());
                    row.createCell(12).setCellValue(npkRecommendation.getSoilTest().getOrganicCarbon());
                    row.createCell(13).setCellValue(npkRecommendation.getSoilTest().getpHValue());

                    row.createCell(9).setCellValue(userLandDetail.getFarmType().toString());
                    row.createCell(10).setCellValue(npkRecommendation.getSoilTest().getnValue());
                    row.createCell(11).setCellValue(npkRecommendation.getSoilTest().getkValue());
                    row.createCell(12).setCellValue(npkRecommendation.getSoilTest().getpValue());
                    row.createCell(13).setCellValue(npkRecommendation.getSoilTest().getOrganicCarbon());
                    row.createCell(14).setCellValue(npkRecommendation.getSoilTest().getpHValue());

                    //get List of Organic Fertilizer in List of String
                    List<String> organicFertilizerList = new ArrayList<>();
                    for (POMFertCal pomFertCal : pomFertCalList) {
                        String requirment = pomFertCal.getRequiredFert().toBigInteger().toString();
                        organicFertilizerList.add(pomFertCal.getFertilizerName() + " (" + pomFertCal.getFertilizerType() + "-" + pomFertCal.getCategoryType() + ") : " + requirment + " " + pomFertCal.getUnit());
                    }
                    row.createCell(14).setCellValue(organicFertilizerList.toString());
                    row.createCell(15).setCellValue(organicFertilizerList.toString());

                    row.createCell(16).setCellValue(user.getAddressModel().getState().getName());
                    row.createCell(17).setCellValue(user.getAddressModel().getCity().getName());
                    row.createCell(18).setCellValue(user.getAddressModel().getTehsil().getName());
                    row.createCell(19).setCellValue(user.getAddressModel().getBlock().getName());
                    row.createCell(20).setCellValue(user.getAddressModel().getVillageModel().getName());
                }
            }
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream getLoanAndBankDetailsExcel() throws IOException, ParseException {
        String[] columns = new String[]{"Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Farmer'-MobileNumber", "State",
                "District", "Tehsil", "Block", "Village", "Bank-Name", "IFSC code", "Account-Type", "LoanType",
                "Loan-Bank-Name", "Loan-amount", "EMI", "AmountPaid", "Duration(in Years)"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet(" LoanAndBank Detail Report");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Row for Header
        Row headerRow = sheet.createRow(0);
        // Header
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        int rowIdx = 1;
        List<User> agentList = userService.getAgentList();
        for (User agent : agentList) {
            List<User> farmerList = agentService.getAgentUsersList(agent.getId());
            for (User farmer : farmerList) {
                List<LoanDetails> loanDetailsList = userBankService.getLoanDetails(farmer.getId());
                List<FinancialDetails> financialDetailsList = userBankService.getFinancialDetails(farmer.getId());
                if (loanDetailsList.isEmpty() && financialDetailsList.isEmpty()) {
                    continue;
                }
                Row row = sheet.createRow(rowIdx++);
                if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                    row.createCell(0).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                } else if (!StringUtils.isEmpty(agent.getLastName())) {
                    row.createCell(0).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                } else {
                    row.createCell(0).setCellValue(agent.getFirstName());
                }
                row.createCell(1).setCellValue(agent.getUserCode());
                if (!StringUtils.isEmpty(farmer.getMiddleName()) && !StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(2).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else if (!StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(2).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                } else {
                    row.createCell(2).setCellValue(farmer.getFirstName());
                }
                row.createCell(3).setCellValue(farmer.getUserCode());
                row.createCell(4).setCellValue(farmer.getPrimaryPhone());
                row.createCell(5).setCellValue(farmer.getAddressModel().getState().getName());
                row.createCell(6).setCellValue(farmer.getAddressModel().getCity().getName());
                row.createCell(7).setCellValue(farmer.getAddressModel().getTehsil().getName());
                row.createCell(8).setCellValue(farmer.getAddressModel().getBlock().getName());
                row.createCell(9).setCellValue(farmer.getAddressModel().getVillageModel().getName());

                // TODO: It will show only last Financial Detail. How to show all financial details??
                for (FinancialDetails financialDetails : financialDetailsList) {
                    row.createCell(10).setCellValue(financialDetails.getBankName());
                    row.createCell(11).setCellValue(financialDetails.getIfscCode());
                    row.createCell(12).setCellValue(financialDetails.getAccountType().toString());
                }
                // TODO: It will show only last Loan Detail. How to show all loan details??
                for (LoanDetails loanDetails : loanDetailsList) {
                    row.createCell(13).setCellValue(loanDetails.getLoanType().toString());
                    row.createCell(14).setCellValue(loanDetails.getBankName());
                    row.createCell(15).setCellValue(String.valueOf(loanDetails.getTotalAmount()));
                    row.createCell(16).setCellValue(String.valueOf(loanDetails.getEmi()));
                    row.createCell(17).setCellValue(String.valueOf(loanDetails.getAmountPaid()));
                    row.createCell(18).setCellValue(loanDetails.getTimePeriod());
                }
            }
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateKhasraLocationCoordinatesExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        List<String> COLUMNs = Arrays.asList("Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-code", "Farmer-Name", "Farmer-code", "Mobile-Number", "State", "District", "Tehsil",
                "Block", "Village", "Khasra-Number", "Khasra-size", "Khasra-Unit", "Soil-Type", "Ownership-Type", "Ownership-Name", "Irrigation-Type", "Irrigation-Source", "Timestamp", "Location 1");

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Khasra Location Coordinates");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, KHASRA_LOC_COORDINATES);
//        List<UserLandDetail> userLandDetailList = mongoTemplate.find(query, UserLandDetail.class);

        System.out.println("========start===================="+Calendar.getInstance().getTime());
        List<UserLandDetail> userLandDetailList = familyMemberHealthRecordRepo.getUserLandDetailList(fromDate, toDate, locationType, locationId, KHASRA_LOC_COORDINATES, UserLandDetail.class);
        System.out.println("========end===================="+Calendar.getInstance().getTime());

        System.out.println("========size======"+userLandDetailList.size());

        Iterator<UserLandDetail> i = userLandDetailList.iterator();
        while (i.hasNext()) {
            UserLandDetail userLandDetail = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User farmer = userRepo.findByid(userLandDetail.getUserId());
            if (farmer != null) {

                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);
                if (agent != null) {
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, userLandDetail.getCreationDate());

                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getUserMainRole(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }

                    if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else if (!StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName());
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


                row.createCell(13).setCellValue(userLandDetail.getKhasraNo());
                row.createCell(14).setCellValue(userLandDetail.getLandSize().toString());
                row.createCell(15).setCellValue(userLandDetail.getLandSizeType().toString());
                row.createCell(16).setCellValue(userLandDetail.getSoil().getSoilName());
                row.createCell(17).setCellValue(userLandDetail.getOwnershipType().toString());
                row.createCell(18).setCellValue(userLandDetail.getOwnerName());
                row.createCell(19).setCellValue(userLandDetail.getFarmType().toString());
                row.createCell(20).setCellValue(userLandDetail.getIrrigationSource().toString());


                row.createCell(21).setCellValue(convertDate(userLandDetail.getCreationDate()));

                //check if CurrentCrop is true then create row and fill details
                UserLandMapping userLandMapping = agentService.getUserLandMapping(userLandDetail.getUserId(), userLandDetail.getKhasraNo());
                if (userLandMapping != null) {

                    List<GeoLocation> locationPinsList = userLandMapping.getLocationPins();
                    if (!locationPinsList.isEmpty()) {

                        // Create Dynamic Header
                        int column = 22;
                        if (maxHeaders < locationPinsList.size()) {
                            for (int col = 0; col < locationPinsList.size(); col++) {
                                Cell cell = headerRow.createCell(column);
                                cell.setCellValue("location" + (column - 21));
                                cell.setCellStyle(headerCellStyle);
                                column++;
                            }
                            maxHeaders = locationPinsList.size();
                        }
                        int columnIndex = 22;
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
    public ByteArrayInputStream getAllUsersStatusData(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"User-Name", "User-Code", "Mobile-Number", "User-Role", "Assigned-to-Name", "Assigned-to-Role", "Assigned-to-Code", "Assigned-to-Mobile", "State", "District", "Tehsil",
                "Block", "Village", "Address", "Creation-Date", "Status"};
        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("User Status Reports");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, USER_STATUS);
//        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        List<User> userList = familyMemberHealthRecordRepo.getUserList(fromDate, toDate, locationType, locationId, USER_STATUS, User.class);
        System.out.println("===============end=============="+Calendar.getInstance().getTime());

        int rowIdx = 1;


        Iterator<User> i = userList.iterator();
        while (i.hasNext()) {
                User user = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            System.out.println("=======no========="+user.getPrimaryPhone());

            if (!StringUtils.isEmpty(user.getMiddleName())) {
                row.createCell(0).setCellValue(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
            } else {
                row.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());
            }
            row.createCell(1).setCellValue(user.getUserCode());
            row.createCell(2).setCellValue(user.getPrimaryPhone());
            row.createCell(3).setCellValue(getUserMainRole(user));

            if(!StringUtils.isEmpty(user.getCreatedByUserId())) {
                User assignedToUser = userService.findByid(user.getCreatedByUserId());
                if (assignedToUser != null) {
                    if (!StringUtils.isEmpty(assignedToUser.getMiddleName())) {
                        row.createCell(4).setCellValue(assignedToUser.getFirstName() + " " + assignedToUser.getMiddleName() + " " + assignedToUser.getLastName());
                    } else {
                        row.createCell(4).setCellValue(assignedToUser.getFirstName() + " " + assignedToUser.getLastName());
                    }

                    row.createCell(5).setCellValue(getUserMainRole(assignedToUser));
                    row.createCell(6).setCellValue(assignedToUser.getUserCode());
                    row.createCell(7).setCellValue(assignedToUser.getPrimaryPhone());
                }
            }

            row.createCell(8).setCellValue(getStateName(user,null, "state"));
            row.createCell(9).setCellValue(getStateName(user,null,  "city"));
            row.createCell(10).setCellValue(getStateName(user,null, "tehsil"));
            row.createCell(11).setCellValue(getStateName(user,null, "block"));
            row.createCell(12).setCellValue(getStateName(user,null, "village"));
            row.createCell(13).setCellValue(getStateName(user,null, "address"));
            row.createCell(14).setCellValue(convertDate(user.getCreationDate()));
            row.createCell(15).setCellValue(String.valueOf(user.isActive()));
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateHealthDetailExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "State", "District", "Tehsil", "Block", "Village", "Disability", "Medical-Problem", "Expense", "Entry-Date"};
        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Health Details");
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
        List<FamilyMemberHealthRecord> familyMemberHealthRecordList = familyMemberHealthRecordRepo.getFamilyMemberHealthRecordList(fromDate, toDate, locationType, locationId, HEALTH_DETAIL, FamilyMemberHealthRecord.class);
        int rowIdx = 1;

        Iterator<FamilyMemberHealthRecord> i = familyMemberHealthRecordList.iterator();
        while (i.hasNext()) {
            FamilyMemberHealthRecord familyMemberHealthRecord = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User farmer = userService.findByid(familyMemberHealthRecord.getUserId());
            if (farmer != null) {

                User agent = userService.findByid(farmer.getCreatedByUserId());
                if (agent != null) {
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                    User manager = employeeHistoryService.getAssignedUserByDate(agent, familyMemberHealthRecord.getCreationDate());
                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getManagerRoleName(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }
                    if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else if (!StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName());
                    }
                    row.createCell(4).setCellValue(agent.getUserCode());
                }


                if (!StringUtils.isEmpty(farmer.getMiddleName()) && !StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else if (!StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());

                row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                row.createCell(12).setCellValue(getStateName(farmer,null, "village"));


                row.createCell(13).setCellValue(familyMemberHealthRecord.getDisability());
                row.createCell(14).setCellValue(familyMemberHealthRecord.getMedicalProblem());
                row.createCell(15).setCellValue(familyMemberHealthRecord.getMedicalExpense().toString());
                row.createCell(16).setCellValue(convertDate(familyMemberHealthRecord.getCreationDate()));
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    @Override
    public ByteArrayInputStream generateFarmEquipmentExcel(String fromDate, String toDate, String locationType, String locationId) throws ParseException, IOException {
        String[] columns = new String[]{"Manager-Type", "Manager-Name", "Manager-Number", "Agent-Name", "Agent-Code", "Farmer-Name", "Farmer-Code", "Mobile-Number", "State", "District", "Tehsil", "Block", "Village", "MoveAble-Equipments", "Immovable-Equipments", "Entry-Date"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("Farm Equipment Details");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, FARM_EQUIPMENT);
//        List<UserEquipments> userEquipmentsList = mongoTemplate.find(query, UserEquipments.class);

        System.out.println("=====start======================="+ Calendar.getInstance().getTime());

        List<UserEquipments> userEquipmentsList = familyMemberHealthRecordRepo.getUserEquipmentsList(fromDate, toDate, locationType, locationId, FARM_EQUIPMENT, UserEquipments.class);
        System.out.println("=====end========================="+ Calendar.getInstance().getTime());

        int rowIdx = 1;

        Iterator<UserEquipments> i = userEquipmentsList.iterator();
        while (i.hasNext()) {
            UserEquipments userEquipments = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User farmer = userService.findByid(userEquipments.getUserId());
            if (farmer != null) {

                User agent = userService.findByid(farmer.getCreatedByUserId());
                if (agent != null) {
                    System.out.println("======managerId=============="+agent.getCreatedByUserId());
                    if(!StringUtils.isEmpty(agent.getCreatedByUserId())) {

                        // get manager
//                        User manager = employeeHistoryService.getAssignedUserByDate(agent, userEquipments.getCreationDate());
                        User manager = userService.findByid(agent.getCreatedByUserId());
                        if (manager != null) {
                            row.createCell(0).setCellValue(getUserMainRole(manager));
                            row.createCell(1).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                            row.createCell(2).setCellValue(manager.getPrimaryPhone());
                        }
                    }

                    if (!StringUtils.isEmpty(agent.getMiddleName()) && !StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else if (!StringUtils.isEmpty(agent.getLastName())) {
                        row.createCell(3).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    } else {
                        row.createCell(3).setCellValue(agent.getFirstName());
                    }
                    row.createCell(4).setCellValue(agent.getUserCode());
                }


                if (!StringUtils.isEmpty(farmer.getMiddleName()) && !StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                } else if (!StringUtils.isEmpty(farmer.getLastName())) {
                    row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                } else {
                    row.createCell(5).setCellValue(farmer.getFirstName());
                }

                row.createCell(6).setCellValue(farmer.getUserCode());
                row.createCell(7).setCellValue(farmer.getPrimaryPhone());

                row.createCell(8).setCellValue(getStateName(farmer,null, "state"));
                row.createCell(9).setCellValue(getStateName(farmer,null,  "city"));
                row.createCell(10).setCellValue(getStateName(farmer,null, "tehsil"));
                row.createCell(11).setCellValue(getStateName(farmer,null, "block"));
                row.createCell(12).setCellValue(getStateName(farmer,null, "village"));

                if(!userEquipments.getMoveableEquipments().isEmpty()) {
                    List<String> moveableEquipments = new ArrayList<>();
                    for (Equipment equipment : userEquipments.getMoveableEquipments()) {
                        moveableEquipments.add(equipment.getName());
                    }
                    row.createCell(13).setCellValue(moveableEquipments.toString());
                }
                if(!userEquipments.getImmoveableEquipments().isEmpty()) {
                    List<String> immoveableEquipments = new ArrayList<>();
                    for (Equipment equipment : userEquipments.getImmoveableEquipments()) {
                        immoveableEquipments.add(equipment.getName());
                    }
                    row.createCell(14).setCellValue(immoveableEquipments.toString());
                }
                row.createCell(15).setCellValue(convertDate(userEquipments.getCreationDate()));
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateUserAssignmentExcel(String fromDate, String toDate, String locationType, String locationId) throws IOException, ParseException {
        String[] columns = new String[]{"User-Name", "User-Code", "User-Mobile-Number", "User-Role", "State", "District", "Tehsil", "Block", "Village", "Assignment-Date", "Assigned-to-Name", "Assigned-to-Code", "Assigned-to-Mobile", "Assigned-to-Role", "Modified-By-Name", "Modified-By-Code", "Modified-By-Number", "Modified-By-Role", "Entry-Date"};

        SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
        wb.setCompressTempFiles(true);

        SXSSFSheet sh = wb.createSheet("User Assignment Details");
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
//        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, USER_ASSIGNMENT_HISTORY);
//        List<EmployeeAssignmentHistory> employeeAssignmentHistoryList = mongoTemplate.find(query, EmployeeAssignmentHistory.class);
        System.out.println("=============start================"+Calendar.getInstance().getTime());
        List<EmployeeAssignmentHistory> employeeAssignmentHistoryList = familyMemberHealthRecordRepo.getEmployeeAssignmentHistoryList(fromDate, toDate, locationType, locationId, USER_ASSIGNMENT_HISTORY, EmployeeAssignmentHistory.class);
        System.out.println("=============end================"+Calendar.getInstance().getTime());
        int rowIdx = 1;

        Iterator<EmployeeAssignmentHistory> i = employeeAssignmentHistoryList.iterator();
        while (i.hasNext()) {
            EmployeeAssignmentHistory employeeAssignmentHistory = i.next();
            SXSSFRow row = sh.createRow(rowIdx++);

            User toUser = employeeAssignmentHistory.getToUser();
            if (toUser != null) {

                if (!StringUtils.isEmpty(toUser.getMiddleName()) && !StringUtils.isEmpty(toUser.getLastName())) {
                    row.createCell(0).setCellValue(toUser.getFirstName() + " " + toUser.getMiddleName() + " " + toUser.getLastName());
                } else if (!StringUtils.isEmpty(toUser.getLastName())) {
                    row.createCell(0).setCellValue(toUser.getFirstName() + " " + toUser.getLastName());
                } else {
                    row.createCell(0).setCellValue(toUser.getFirstName());
                }

                row.createCell(1).setCellValue(toUser.getUserCode());
                row.createCell(2).setCellValue(toUser.getPrimaryPhone());

                row.createCell(3).setCellValue(getUserMainRole(toUser));

                row.createCell(4).setCellValue(getStateName(toUser, null, "state"));
                row.createCell(5).setCellValue(getStateName(toUser, null, "city"));
                row.createCell(6).setCellValue(getStateName(toUser, null, "tehsil"));
                row.createCell(7).setCellValue(getStateName(toUser, null, "block"));
                row.createCell(8).setCellValue(getStateName(toUser, null, "village"));

                row.createCell(9).setCellValue(convertDate(employeeAssignmentHistory.getAssignmentDate()));

                User fromUser = employeeAssignmentHistory.getFromUser();
                if (fromUser != null) {
                    if (!StringUtils.isEmpty(fromUser.getMiddleName()) && !StringUtils.isEmpty(fromUser.getLastName())) {
                        row.createCell(10).setCellValue(fromUser.getFirstName() + " " + fromUser.getMiddleName() + " " + fromUser.getLastName());
                    } else if (!StringUtils.isEmpty(fromUser.getLastName())) {
                        row.createCell(10).setCellValue(fromUser.getFirstName() + " " + fromUser.getLastName());
                    } else {
                        row.createCell(10).setCellValue(fromUser.getFirstName());
                    }
                    row.createCell(11).setCellValue(fromUser.getUserCode());
                    row.createCell(12).setCellValue(fromUser.getPrimaryPhone());
                    row.createCell(13).setCellValue(getUserMainRole(fromUser));

                }

                if (employeeAssignmentHistory.getCreatedByUser() != null) {
                    User modifiedByUser = userService.getUserByPhoneNum(employeeAssignmentHistory.getCreatedByUser());

                    if (modifiedByUser != null) {
                        if (!StringUtils.isEmpty(modifiedByUser.getMiddleName()) && !StringUtils.isEmpty(modifiedByUser.getLastName())) {
                            row.createCell(14).setCellValue(modifiedByUser.getFirstName() + " " + modifiedByUser.getMiddleName() + " " + modifiedByUser.getLastName());
                        } else if (!StringUtils.isEmpty(modifiedByUser.getLastName())) {
                            row.createCell(14).setCellValue(modifiedByUser.getFirstName() + " " + modifiedByUser.getLastName());
                        } else {
                            row.createCell(14).setCellValue(modifiedByUser.getFirstName());
                        }
                        row.createCell(15).setCellValue(modifiedByUser.getUserCode());
                        row.createCell(16).setCellValue(modifiedByUser.getPrimaryPhone());
                        row.createCell(17).setCellValue(getUserMainRole(modifiedByUser));
                    } else {
                        row.createCell(14).setCellValue("Modified By System");
                    }
                }
                row.createCell(18).setCellValue(convertDate(employeeAssignmentHistory.getCreationDate()));
            }
            i.remove();
        }
        wb.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * return User Main Role (eg. if any manager has two roles like National Manager and Agent
     * then it will return National Manager as main role
     *
     * @param user
     * @return
     */
    public static String getUserMainRole(User user) {
        if (user.getRoles().size() > 1) {
            for (Role role : user.getRoles()) {
                if (role.getRoleName().contains("_MANAGER")) {
                    return role.getRoleName();
                }
            }
        }
        return user.getRoles().get(0).getRoleName();
    }

}
