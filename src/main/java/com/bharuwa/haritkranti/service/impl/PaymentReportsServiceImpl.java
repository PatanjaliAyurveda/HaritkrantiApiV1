package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.FinancialDetails;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.payments.PaymentCycle;
import com.bharuwa.haritkranti.models.payments.SoilTestPayment;
import com.bharuwa.haritkranti.models.responseModels.SalaryResponse;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.opencsv.CSVReader;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport.convertDate;
import static com.bharuwa.haritkranti.models.payments.SoilTestPayment.Status.Approved;
import static com.bharuwa.haritkranti.service.impl.GenerateReportServiceImpl.getStateName;
import static com.bharuwa.haritkranti.service.impl.GenerateReportServiceImpl.getUserMainRole;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.getCellValueAsString;

/**
 * @author anuragdhunna
 */
@Service
public class PaymentReportsServiceImpl implements PaymentReportsService {

    private final MongoTemplate mongoTemplate;

    public PaymentReportsServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private LocationServices locationServices;

    @Override
    public ByteArrayInputStream getPaymentCycle(String fromDate, String toDate) throws ParseException, IOException {
        String[] columns = new String[]{"Payment_cycle_S-no.", "Payment-cycle-Code", "From Date", "To Date", "No of Days"};
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Payment Detail Reports");
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
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);
        int rowIdx = 1;
        Query query = new Query().with(Sort.by(Sort.Direction.ASC, "creationDate"));
        query.addCriteria(Criteria.where("creationDate").gte(from).lt(to));
        List<PaymentCycle> paymentCycleList = mongoTemplate.find(query, PaymentCycle.class);
        for (PaymentCycle paymentCycle :paymentCycleList){
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(paymentCycle.getPaymentCycle());
            row.createCell(1).setCellValue(paymentCycle.getCode());
            row.createCell(2).setCellValue(paymentCycle.getFromDate());
            row.createCell(3).setCellValue(paymentCycle.getToDate());
            row.createCell(4).setCellValue(paymentCycle.getNoOfDays());
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream getPaymentDetail(String paymentCycleId, String locationType, String locationId, String paymentStatus, String status) throws  IOException {
        String[] columns = new String[]{"Soil-Test-Payment-Id", "Payment-Cycle-Code", "Agent-Name", "Agent-Code", "Agent-Mobile", "Farmer-Name", "Farmer-Code", "Farmer-Mobile", "Farmer-State", "Farmer-District", "Farmer-Tehsil",
                "Farmer-Block", "Farmer-Village", "Khasra-No", "Crop", "Area", "Season","Soil-Test-Date", "N", "P", "K", "Oc", "pH", "Agent-Bank-Name", "A/c-Holders-Name",
                "Agent-Account-No.", "IFSC-code", "Payment-Status", "Khasra-Rate", "Khasra-Rate-Location","Commission-Rate", "Commission-Rate-Location"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Payment Detail Reports");

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

        int maxHeaders = 0;
        int rowIdx = 1;

        Criteria criteria = new Criteria();

        if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
            String searchLocationKey = getLocationKey(locationType);
            if (!StringUtils.isEmpty(searchLocationKey)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        if(!StringUtils.isEmpty(paymentStatus)){
            criteria = criteria.and("paymentStatus").is(paymentStatus);
        }

        if(!StringUtils.isEmpty(status)){
            criteria = criteria.and("status").is(status);
        }

        criteria = criteria.and("paymentCycle.$id").is(new ObjectId(paymentCycleId));

        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        System.out.println("===== String paymentStatus // default value = paymentStatus  0========query======"+query);
        List<SoilTestPayment> soilTestPaymentList = mongoTemplate.find(query, SoilTestPayment.class);

        System.out.println("=============soilTestPaymentListsize======"+soilTestPaymentList.size());

        for (SoilTestPayment soilTestPayment : soilTestPaymentList) {
            if (soilTestPayment == null) {
                continue;
            }
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(soilTestPayment.getId());

            row.createCell(1).setCellValue(soilTestPayment.getPaymentCycle().getPaymentCycle());

            User farmer = genericMongoTemplate.findById(soilTestPayment.getSoilTest().getUserCrop().getUserId(), User.class);
            if (farmer != null) {
                User agent = StringUtils.isEmpty(farmer.getCreatedByUserId()) ? userService.getAgentByUserId(farmer.getId()) : genericMongoTemplate.findById(farmer.getCreatedByUserId(), User.class);
                if (agent != null) {
                    if (!StringUtils.isEmpty(agent.getMiddleName())) {
                        row.createCell(2).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
                    } else {
                        row.createCell(2).setCellValue(agent.getFirstName() + " " + agent.getLastName());
                    }
                    row.createCell(3).setCellValue(agent.getUserCode());
                    row.createCell(4).setCellValue(agent.getPrimaryPhone());
                    if (!StringUtils.isEmpty(farmer.getMiddleName())) {
                        row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getMiddleName() + " " + farmer.getLastName());
                    } else {
                        row.createCell(5).setCellValue(farmer.getFirstName() + " " + farmer.getLastName());
                    }
                    row.createCell(6).setCellValue(farmer.getUserCode());
                    row.createCell(7).setCellValue(farmer.getPrimaryPhone());
                    row.createCell(8).setCellValue(getStateName(farmer, null, "state"));
                    row.createCell(9).setCellValue(getStateName(farmer, null, "city"));
                    row.createCell(10).setCellValue(getStateName(farmer, null, "tehsil"));
                    row.createCell(11).setCellValue(getStateName(farmer, null, "block"));
                    row.createCell(12).setCellValue(getStateName(farmer, null, "village"));
                    row.createCell(13).setCellValue(soilTestPayment.getSoilTest().getUserCrop().getKhasraNo());
                    row.createCell(14).setCellValue(soilTestPayment.getSoilTest().getUserCrop().getCrop().getCropName());
                    row.createCell(15).setCellValue(soilTestPayment.getSoilTest().getUserCrop().getLandSize().toString()+ " "+soilTestPayment.getSoilTest().getUserCrop().getLandSizeType()); //user crop area
                    row.createCell(16).setCellValue(soilTestPayment.getSoilTest().getUserCrop().getCropSeason().toString());
                    row.createCell(17).setCellValue(convertDate(soilTestPayment.getSoilTest().getCreationDate()));
                    row.createCell(18).setCellValue(soilTestPayment.getSoilTest().getnValue());
                    row.createCell(19).setCellValue(soilTestPayment.getSoilTest().getpValue());
                    row.createCell(20).setCellValue(soilTestPayment.getSoilTest().getkValue());
                    row.createCell(21).setCellValue(soilTestPayment.getSoilTest().getOrganicCarbon());
                    row.createCell(22).setCellValue(soilTestPayment.getSoilTest().getpHValue());

                    FinancialDetails financialDetails = userBankService.getPrimaryBankDetails(agent.getId());

                    System.out.println("===============agent.getId()==============="+agent.getId());
                    System.out.println("===============financialDetails==============="+financialDetails);
                    if (financialDetails != null) {
                        row.createCell(23).setCellValue(financialDetails.getBankName());
                        row.createCell(24).setCellValue(financialDetails.getAccountHolderName());
                        row.createCell(25).setCellValue(financialDetails.getAccountNumber());
                        row.createCell(26).setCellValue(financialDetails.getIfscCode());
                    }

                    row.createCell(27).setCellValue(soilTestPayment.getPaymentStatus().toString());
                    row.createCell(28).setCellValue(soilTestPayment.getKhasraRate().toString());
                    row.createCell(29).setCellValue(soilTestPayment.getKhasraRateLocationType().toString());
                    row.createCell(30).setCellValue(soilTestPayment.getCommissionRate().toString());
                    row.createCell(31).setCellValue(soilTestPayment.getCommissionRateLocationType().toString());


                }
            }
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream getPaymentSummaryReportAgents(String paymentCycleId, String locationType, String locationId, String paymentStatus, String status) throws IOException, ParseException {
        String[] columns = new String[]{"Vendor-Code","Bill-No","Bill-Date","Bill-Amount","Payment-Cycle-Code","Start-Date","End-Date","ST-Location", "Agent-Name", "Agent-Code", "Agent-Mobile", "Assigned-State", "Assigned-District", "Assigned-Tehsil",
                "Assigned-Block", "Assigned-Village", "Total-Soil-Test", "Agent-Bank-Name", "A/c-Holders-Name", "Agent-Account-No.", "IFSC-code", "Total-Payment",
                "Transaction Id", "Payment Received Date", "Payment Status", "Payment Remarks" };

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Agent's Payment Summary Report");

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

        int maxHeaders = 0;
        int rowIdx = 1;

        Criteria criteria = new Criteria();
        String searchLocationKey = null;
        if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
            searchLocationKey = getLocationKey(locationType);
            if (!StringUtils.isEmpty(searchLocationKey)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        // get location Name
        String locationName = getLocationName(locationType,locationId);


        if(!StringUtils.isEmpty(paymentStatus)){
            criteria = criteria.and("paymentStatus").is(paymentStatus);
        }

        if(!StringUtils.isEmpty(status)){
            criteria = criteria.and("status").is(status);
        }

        criteria = criteria.and("paymentCycle.$id").is(new ObjectId(paymentCycleId));
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        List<User> userList = mongoTemplate.findDistinct(query, "agent", SoilTestPayment.class, User.class);

        PaymentCycle paymentCycle = mongoTemplate.findById(paymentCycleId, PaymentCycle.class);
        for (User agent : userList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(agent.getVendorCode() != null ? agent.getVendorCode().trim() : " ");

            if(paymentCycle != null) {
                row.createCell(1).setCellValue(paymentCycle.getPaymentCycle().trim()+"-"+agent.getUserCode());
                row.createCell(2).setCellValue(convertDate(Calendar.getInstance().getTime()));
                row.createCell(4).setCellValue(paymentCycle.getPaymentCycle().trim());
                row.createCell(5).setCellValue(convertDate(paymentCycle.getFromDate()));
                row.createCell(6).setCellValue(convertDate(paymentCycle.getToDate()));
                row.createCell(7).setCellValue(locationType+":"+locationName+"("+locationId+")");


            }
            if (!StringUtils.isEmpty(agent.getMiddleName())) {
                row.createCell(8).setCellValue(agent.getFirstName() + " " + agent.getMiddleName() + " " + agent.getLastName());
            } else {
                row.createCell(8).setCellValue(agent.getFirstName() + " " + agent.getLastName());
            }
            row.createCell(9).setCellValue(agent.getUserCode());
            row.createCell(10).setCellValue(agent.getPrimaryPhone());
            row.createCell(11).setCellValue(getStateName(agent, null, "state"));
            row.createCell(12).setCellValue(getStateName(agent, null, "city"));
            row.createCell(13).setCellValue(getStateName(agent, null, "tehsil"));
            row.createCell(14).setCellValue(getStateName(agent, null, "block"));
            row.createCell(15).setCellValue(getStateName(agent, null, "village"));

            query = new Query(Criteria.where("paymentCycle.$id").is(new ObjectId(paymentCycleId)).and("agent.$id").is(new ObjectId(agent.getId())).and(searchLocationKey).is(new ObjectId(locationId)).and("status").is(Approved));
            long strCount = mongoTemplate.count(query, SoilTestPayment.class);

            row.createCell(16).setCellValue(strCount);

            FinancialDetails financialDetails = userBankService.getPrimaryBankDetails(agent.getId());

            if(financialDetails != null) {
                row.createCell(17).setCellValue(financialDetails.getBankName());
                row.createCell(18).setCellValue(financialDetails.getAccountHolderName());
                row.createCell(19).setCellValue(financialDetails.getAccountNumber());
                row.createCell(20).setCellValue(financialDetails.getIfscCode());
            }

            BigDecimal totalPayment = salaryService.calculateAgentSalary(agent.getId(),null,null,paymentCycleId,locationType,locationId);

            row.createCell(3).setCellValue(String.valueOf(totalPayment));
            row.createCell(21).setCellValue(String.valueOf(totalPayment));

        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream getPaymentSummaryReportManagers(String fromDate, String toDate, String locationType, String locationId, String paymentStatus, String status) throws ParseException, IOException {
        String[] columns = new String[]{"Manager-Name", "Manager-Code", "Manager-Mobile", "Manager-Bank-Name", "A/c-Holders-Name", "Manager-Account-No.", "IFSC-code", "Basic-Salary", "Commission", "Total-Payment"};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Manager's Payment Summary Report");

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

        Calendar start = Calendar.getInstance();
        start.setTime(from);
        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthStartDate = start.getTime();
        System.out.println(monthStartDate);

        Calendar end = Calendar.getInstance();
        end.setTime(from);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthEndDate = end.getTime();
        System.out.println(monthEndDate);

        int rowIdx = 1;

        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
            String searchLocationKey = getLocationKey(locationType);
            if (!StringUtils.isEmpty(searchLocationKey)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        if(!StringUtils.isEmpty(paymentStatus)){
            criteria = criteria.and("paymentStatus").is(paymentStatus).and("paymentStatus").is(SoilTestPayment.PaymentStatus.Pending);
        }

        if(!StringUtils.isEmpty(status)){
            criteria = criteria.and("status").is(status);
        }

        criteria = criteria.and("creationDate").gte(monthStartDate).lt(monthEndDate);
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        List<String> managerList = mongoTemplate.findDistinct(query, "managerId", SoilTestPayment.class, String.class);

        for (String managerId : managerList) {
            Row row = sheet.createRow(rowIdx++);

            User manager = userService.findByid(managerId);
            if(manager != null) {
                if (!StringUtils.isEmpty(manager.getMiddleName())) {
                    row.createCell(0).setCellValue(manager.getFirstName() + " " + manager.getMiddleName() + " " + manager.getLastName());
                } else {
                    row.createCell(0).setCellValue(manager.getFirstName() + " " + manager.getLastName());
                }
                row.createCell(1).setCellValue(manager.getUserCode());
                row.createCell(2).setCellValue(manager.getPrimaryPhone());

                FinancialDetails financialDetails = userBankService.getPrimaryBankDetails(manager.getId());

                if (financialDetails != null) {
                    row.createCell(3).setCellValue(financialDetails.getBankName());
                    row.createCell(4).setCellValue(financialDetails.getAccountHolderName());
                    row.createCell(5).setCellValue(financialDetails.getAccountNumber());
                    row.createCell(6).setCellValue(financialDetails.getIfscCode());
                }

                // convert date back to string
                String startDate = dateFormat.format(monthStartDate);
                String endDate = dateFormat.format(monthEndDate);

                SalaryResponse salaryResponse = salaryService.calculateManagerSalary(manager.getId(), startDate, endDate);

                row.createCell(7).setCellValue(String.valueOf(salaryResponse.getBasicSalary()));
                row.createCell(8).setCellValue(String.valueOf(salaryResponse.getCommission()));
                row.createCell(9).setCellValue(String.valueOf(salaryResponse.getTotalSalary()));

            }
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * get location key
     * @param locationType
     * @return
     */
    public static String getLocationKey(String locationType) {
        String searchLocationKey;
        switch (locationType) {
            case "STATE":
                searchLocationKey = "address.state.$id";
                break;
            case "DISTRICT":
                searchLocationKey = "address.city.$id";
                break;
            case "TEHSIL":
                searchLocationKey = "address.tehsil.$id";
                break;
            case "BLOCK":
                searchLocationKey = "address.block.$id";
                break;
            case "VILLAGE":
                searchLocationKey = "address.villageModel.$id";
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return searchLocationKey;
    }


    private String getLocationName(String locationType, String locationId) {
        String locationName;
        switch (locationType) {
            case "STATE":
                State state = locationServices.getState(locationId);
                locationName = state.getName();
                break;
            case "DISTRICT":
                City city = locationServices.getCity(locationId);
                locationName = city.getName();
                break;
            case "TEHSIL":
                Tehsil tehsil = locationServices.getTehsil(locationId);
                locationName = tehsil.getName();
                break;
            case "BLOCK":
                Block block = locationServices.getBlock(locationId);
                locationName = block.getName();
                break;
            case "VILLAGE":
                Village village = locationServices.getVillage(locationId);
                locationName = village.getName();
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return locationName;
    }


    @Override
    public List<String> uploadPaymentSummaryExcel(MultipartFile file) throws IOException, ParseException {
        // add log messages
        List<String> messages = new ArrayList<>();

        // to check error line
        int line = 1;

        // Read Excel File
        XSSFWorkbook myWorkBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet paymentSummarySheet = myWorkBook.getSheetAt(0);

        //iterating over excel file
        Iterator<Row> itr = paymentSummarySheet.iterator();
        itr.next();

        while (itr.hasNext()){
            Row row = itr.next();

            line++;

            Criteria criteria = new Criteria();
            if (StringUtils.isEmpty(getCellValueAsString(row.getCell(0)))) {
                continue;
            }

            String paymentCycleCode = getCellValueAsString(row.getCell(0)).trim();
            PaymentCycle paymentCycle = genericMongoTemplate.findByKey("paymentCycle", paymentCycleCode, PaymentCycle.class);
            if (paymentCycle != null) {
                criteria = criteria.and("paymentCycle.$id").is(new ObjectId(paymentCycle.getId()));
            } else {
                messages.add("Payment Cycle not found for code :"+getCellValueAsString(row.getCell(0)).trim()+ "(line:"+line+")");
                continue;
            }

            User agent = userService.getUserByPhoneNum(getCellValueAsString(row.getCell(6)).trim());
            if(agent != null){
                criteria = criteria.and("agentId").is(agent.getId());
            } else {
                messages.add("Agent not found with phone number : "+getCellValueAsString(row.getCell(6)).trim()+ "(line:"+line+")");
                continue;
            }

            // get locaion filter
            String soilTestLocation = getCellValueAsString(row.getCell(3)).trim();
            String locationType = soilTestLocation.substring(0, soilTestLocation.indexOf(':'));
            String locationId = soilTestLocation.substring(soilTestLocation.indexOf('(')+1, soilTestLocation.indexOf(')'));

            if (!StringUtils.isEmpty(locationId) && !StringUtils.isEmpty(locationType)) {
                String searchLocationKey = getLocationKey(locationType);
                if (!StringUtils.isEmpty(searchLocationKey)) {
                    criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
                }
            }

            criteria = criteria.and("status").is(Approved).and("paymentStatus").is(SoilTestPayment.PaymentStatus.Pending);

            Query query = new Query(criteria);
            List<SoilTestPayment> soilTestPaymentList = mongoTemplate.find(query, SoilTestPayment.class);

            SoilTestPayment.PaymentStatus paymentStatus = null;
            for (SoilTestPayment.PaymentStatus status : SoilTestPayment.PaymentStatus.values()) {
                if (status.toString().equalsIgnoreCase(getCellValueAsString(row.getCell(20)).trim())) {
                    paymentStatus = status;
                }
            }

            if(paymentStatus == null){
                messages.add("Payment Status can only be one of these: Received, Pending, Hold"+ "(line:"+line+")");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date paymentDate = dateFormat.parse(getCellValueAsString(row.getCell(19)));

            for(SoilTestPayment soilTestPayment : soilTestPaymentList){

                soilTestPayment.setTransactionId(getCellValueAsString(row.getCell(18)).trim());
                soilTestPayment.setPaymentReceivedDate(paymentDate);
                soilTestPayment.setPaymentStatus(paymentStatus);
                soilTestPayment.setPaymentRemarks(getCellValueAsString(row.getCell(21)).trim());
                mongoTemplate.save(soilTestPayment);
            }

        }

        messages.add("CSV Uploaded Successfully");
        return  messages;
    }

    @Override
    public ByteArrayInputStream getVendorBankDetialReportforERP(String fromDate, String toDate) throws ParseException, IOException {
        String[] columns = new String[]{"Name", "User-Code", "Mobile-Number", "User-Role", "Aadhar-Number", "Creation-Date", "State", "District", "Tehsil",
                "Block", "Village", "Address", "Bank-Name", "Account-Number", "IFSC code", "Status", "Vendor-Code"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Vendor Bank Details Report");

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

        List<User> userList = getAgentsAndManagers(fromDate, toDate);

        int rowIdx = 1;
        for (User user : userList) {
            System.out.println("=======no========="+user.getPrimaryPhone());

            Row row = sheet.createRow(rowIdx++);

            if (!StringUtils.isEmpty(user.getMiddleName())) {
                row.createCell(0).setCellValue(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
            } else {
                row.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());
            }
            row.createCell(1).setCellValue(user.getUserCode());
            row.createCell(2).setCellValue(user.getPrimaryPhone());
            row.createCell(3).setCellValue(getUserMainRole(user));
            FinancialDetails financialDetails = userBankService.getPrimaryBankDetails(user.getId());
            if(financialDetails != null){
                row.createCell(4).setCellValue(financialDetails.getAadhaarCardNumber());
                row.createCell(12).setCellValue(financialDetails.getBankName());
                row.createCell(13).setCellValue(financialDetails.getAccountNumber());
                row.createCell(14).setCellValue(financialDetails.getIfscCode());
                row.createCell(15).setCellValue(String.valueOf(financialDetails.isAccountVerified()));
            } else {
                row.createCell(12).setCellValue("No Data Available.");
            }
            row.createCell(5).setCellValue(convertDate(user.getCreationDate()));
            row.createCell(6).setCellValue(getStateName(user,null, "state"));
            row.createCell(7).setCellValue(getStateName(user,null,  "city"));
            row.createCell(8).setCellValue(getStateName(user,null, "tehsil"));
            row.createCell(9).setCellValue(getStateName(user,null, "block"));
            row.createCell(10).setCellValue(getStateName(user,null, "village"));
            row.createCell(11).setCellValue(getStateName(user,null, "address"));
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public List<User> getAgentsAndManagers(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        Criteria criteria = new Criteria();
        criteria = criteria.and("creationDate").gte(from).lt(to);

        List<String> roleList = new ArrayList<>();
        roleList.add(ROLE_NATIONAL_MANAGER);
        roleList.add(ROLE_STATE_MANAGER);
        roleList.add(ROLE_DISTRICT_MANAGER);
        roleList.add(ROLE_AGENT_MANAGER);
        roleList.add(ROLE_AGENT);


        // only include Agents and Managers
        criteria = criteria.and("roles.roleName").in(roleList);


        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "creationDate"));
        query.fields().include("addressModel").include("firstName").include("middleName").include("lastName")
                .include("primaryPhone").include("roles").include("crationDate").include("userCode");
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<String> updateVendorCode(MultipartFile file) throws IOException {
        // add log messages
        List<String> messages = new ArrayList<>();

        // to check error line
        int line = 1;

        // Read Excel File
        XSSFWorkbook myWorkBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet paymentSummarySheet = myWorkBook.getSheetAt(0);

        //iterating over excel file
        Iterator<Row> itr = paymentSummarySheet.iterator();
        itr.next();

        while (itr.hasNext()){
            Row row = itr.next();

            if (StringUtils.isEmpty(getCellValueAsString(row.getCell(2)).trim())) {
                messages.add("Mobile Number empty at " + "(line:" + line + ")");
                continue;
            }
            // check if User Exist
            User exist = userService.getUserByPhoneNum(getCellValueAsString(row.getCell(2)).trim());
            if (exist == null) {
                messages.add("User not found with mobile no " + getCellValueAsString(row.getCell(2)).trim() + "(line:" + line + ")");
            } else {
                // check if vendor code exist or not
                if(!StringUtils.isEmpty(exist.getVendorCode())){
                    messages.add("Vendor Code already generated for mobile no  " + getCellValueAsString(row.getCell(2)).trim()  + " (line:" + line + ")");
                } else {
                    // check vendor code exist in file
                    if (StringUtils.isEmpty(getCellValueAsString(row.getCell(16)).trim())) {
                        messages.add("Vendor Code Empty for mobile no  " + getCellValueAsString(row.getCell(2)).trim() + " (line:" + line + ")");
                    } else {
                        exist.setVendorCode(getCellValueAsString(row.getCell(16)).trim());
                        userService.saveUser(exist);
                    }
                }
            }
        }
        return messages;
    }
}















