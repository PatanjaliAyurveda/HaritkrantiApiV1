package com.bharuwa.haritkranti.service.impl;

import com.amazonaws.util.IOUtils;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.UserCrop;
import com.bharuwa.haritkranti.aws.AwsS3;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.responseModels.*;
import com.bharuwa.haritkranti.repositories.AadharRepo;
import com.bharuwa.haritkranti.repositories.GeoLocationMappingRepo;
import com.bharuwa.haritkranti.repositories.UserCropRepo;
import com.bharuwa.haritkranti.repositories.UserRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.google.zxing.WriterException;
import com.itextpdf.text.BadElementException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport.convertDate;
import static com.bharuwa.haritkranti.qrCode.GenerateQRCode.QR_CODE_IMAGE_PATH;
import static com.bharuwa.haritkranti.qrCode.GenerateQRCode.generateQRCodeImage;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.getEmployeeRelationshipByToUserType;

/**
 * @author anuragdhunna
 */
@Service
public class UserExtraServiceImpl implements UserExtraService {

    private final MongoTemplate mongoTemplate;
    private AwsS3 awsS3;

    @Autowired
    private AadharRepo aadharRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserCropRepo userCropRepo;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private GeoLocationMappingRepo geoLocationMappingRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private FarmDetailService farmDetailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private LocationServices locationServices;

    @Value("${qrcodeApiUrl}")
    private String qrcodeApiUrl;

    @Value("${beekeepigQRCodeApiUrl}")
    private String beekeepigQRCodeApiUrl;

    public UserExtraServiceImpl(MongoTemplate mongoTemplate, AwsS3 awsS3) {
        this.mongoTemplate = mongoTemplate;
        this.awsS3 = awsS3;
    }

    @Override
    public UserCrop addUserCrop(UserCrop userCrop) {
        User user = userService.findByid(userCrop.getUserId());
        if(user == null){
            throw new ResourceNotFoundException("user not found");
        }
        userCrop.setAddress(user.getAddressModel());
        return userCropRepo.save(userCrop);
    }

    @Override
    public UserCrop getUserCropById(String userId, String id,String khasraNumber) {
        User userExist = userService.findByid(userId);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId).and("id").is(id).and("khasraNo").is(khasraNumber));
        return mongoTemplate.findOne(query, UserCrop.class);
    }
    
    @Override
    public Aadhar addAadhar(Aadhar aadhar) {
        if (!userRepo.existsById(aadhar.getUserId())) {
            throw new ResourceNotFoundException("User not found");
        }
        return aadharRepo.save(aadhar);
    }

    @Override
    public Aadhar getAadharByUserId(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Aadhar.class);
    }

    @Override
    public MessageResponse uploadAadharPic(@RequestParam("file") MultipartFile file, @RequestParam String userId) {
        User user = userRepo.findByid(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Aadhar aadhar = getAadharByUserId(userId);

        String url = this.awsS3.uploadFile(file);
        aadhar.setAadharPicUrl(url);
        aadharRepo.save(aadhar);
        MessageResponse response = new MessageResponse();
        response.setMessage(url);
        return response;
    }

    @Override
    public void getGenerateQRCodeImageForOC(String userId, String khasraNos, String certificateCode) throws IOException, WriterException {
        String khasraList = khasraNos.replaceAll(" ", "");
        String apiUrl = qrcodeApiUrl + "?userId=" + userId + "&khasraNos=" + khasraList + "&certificateCode=" + certificateCode;
        generateQRCodeImage(apiUrl, 350, 350, QR_CODE_IMAGE_PATH);

    }

    @Override
    public OCQRCodeResponse getOCQRResponse(String userId, String khasraNos, String certificateCode) {
        User user = userRepo.findByid(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        OCQRCodeResponse ocqrCodeResponse = new OCQRCodeResponse();
        List<String> khasraList = Arrays.asList(khasraNos.split("\\s*,\\s*"));

        List<ReportHistory> reportHistoryList = new ArrayList<>();
        for (String khasra : khasraList) {
            ReportHistory reportHistory = reportHistoryService.getOrganicReportByKhasraAndUserId(khasra, userId);
            reportHistoryList.add(reportHistory);
        }

//        user's details
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
        userResponse.setFatherName(user.getFatherName());
        userResponse.setPrimaryPhone(user.getPrimaryPhone());
        userResponse.setState(user.getAddressModel().getState().getName());
        userResponse.setDistrict(user.getAddressModel().getCity().getName());
        userResponse.setTehsil(user.getAddressModel().getTehsil().getName());
        userResponse.setBlock(user.getAddressModel().getBlock().getName());
        userResponse.setVillage(user.getAddressModel().getVillageModel().getName());
        userResponse.setAddress(user.getAddressModel().getAddress());
        userResponse.setOrganicCertificateCode(certificateCode);

//      organic fertilizer list by khasra
        List<OCFertilizerResponse> ocFertilizerResponseList = new ArrayList<>();

        for (ReportHistory reportHistory : reportHistoryList) {

            OCFertilizerResponse ocFertilizerResponse = new OCFertilizerResponse();
            ocFertilizerResponse.setKhasraNo(reportHistory.getNpkRecommendation().getKhasraNo());
            ocFertilizerResponse.setLandArea(reportHistory.getNpkRecommendation().getFieldSize() + " " + reportHistory.getNpkRecommendation().getFieldSizeType());
            ocFertilizerResponse.setPomFertCals(reportHistory.getOrganicReqFert().getPomFertCals());
            ocFertilizerResponseList.add(ocFertilizerResponse);
        }

//        set details for OC-QRCode Response
        ocqrCodeResponse.setUserResponse(userResponse);
        ocqrCodeResponse.setOcFertilizerResponses(ocFertilizerResponseList);
        return ocqrCodeResponse;
    }

    /**
     * Generate Unique Code for Famers
     *
     * @param user
     * @return Unique String 'DKDF-AG-MH-37117793'
     */
    @Override
    public String generateUniqueCodeForFarmer(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String stateAbbreviation = null;
        if (user.getAddressModel() != null && user.getAddressModel().getState() != null) {
            stateAbbreviation = user.getAddressModel().getState().getStateAbbreviation();
        }
        // 8 digit random number
        Random rnd = new Random();
        String positiveRandomNumber = String.format("%08d", rnd.nextInt(100000000));

        if (StringUtils.isEmpty(lastName)) {
            return ("DKDF-" + firstName.charAt(0) + firstName.charAt(0) + "-" + stateAbbreviation + "-" + positiveRandomNumber).toUpperCase();
        }
        return ("DKDF-" + firstName.charAt(0) + lastName.charAt(0) + "-" + stateAbbreviation + "-" + positiveRandomNumber).toUpperCase();
    }

    @Override
    public boolean checkUserLandOCEligible(String userId, String khasraNo) {

        UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(khasraNo, userId);
        if (userLandDetail == null) {
            throw new ResourceNotFoundException("No khasra Found");
        }

        return userLandDetail.isOrganicLandEligible();
    }

    @Override
    public List<UserCrop> getUserCropListReadyForSale(String phoneNumber) {
        User userExist = userService.getUserByPhoneNum(phoneNumber);
        if (userExist == null) {
            throw new ResourceNotFoundException("You are not an DKD user.");
        }

        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userExist.getId()).and("readyToSell").ne(false).exists(true));
        return mongoTemplate.find(query, UserCrop.class);
    }

    @Override
    public GeoLocationMapping storeGeoLocationMapping(GeoLocationMapping geoLocationMapping) {
        return geoLocationMappingRepo.save(geoLocationMapping);
    }

    @Override
    public GeoLocationMapping getGeoLocationMappingByType(String userId, String khasraNo) {
        User userExist = userRepo.findByid(userId);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not exists");
        }

        Query query;
        
       /* if(!StringUtils.isEmpty(khasraNo)) {
            query = new Query(Criteria.where("userId").is(userId).and("mappedLocationType").is(GeoLocationMapping.MappedLocationType.User_Land).and("typeId").is(typeId));
        } else {
            query = new Query(Criteria.where("userId").is(userId).and("mappedLocationType").is(mappedLocationType.trim()).and("typeId").is(typeId));
        }*/
        
        if(!StringUtils.isEmpty(khasraNo)) {
            query = new Query(Criteria.where("userId").is(userId).and("khasraNo").is(khasraNo));
        } else {
            query = new Query(Criteria.where("userId").is(userId));
        }
        
        return mongoTemplate.findOne(query, GeoLocationMapping.class);
    }

    public String getBeekingBatchForFarmer(String beekeepingId) {

        String batchCode = "";
        BeekeepingDetails beekeepingDetails = farmDetailService.getBeekeepingDetailById(beekeepingId);

        if (beekeepingDetails != null) {
            User user = userService.findByid(beekeepingDetails.getUserId());
            String firstName = user.getFirstName();
            String lastName = user.getLastName();

            String stateAbbreviation = null;
            if (user.getAddressModel() != null && user.getAddressModel().getState() != null) {
                stateAbbreviation = user.getAddressModel().getState().getStateAbbreviation();
            }

            String honeyFlavour = beekeepingDetails.getHoneyFlavour().toString();

            Random rnd = new Random();
            int positiveRandomNumber = Math.abs(rnd.nextInt(10000));

            String fromDate = convertDate(beekeepingDetails.getFromDate());

            System.out.println("==============from============" + fromDate);

            if (StringUtils.isEmpty(lastName)) {
                batchCode = (stateAbbreviation + "-" + firstName.charAt(0) + firstName.charAt(0) + "-" + fromDate.substring(3, 5) + fromDate.substring(8, 10) + "-" + honeyFlavour.substring(0, 3) + "-" + positiveRandomNumber).toUpperCase();
            } else {
                batchCode = (stateAbbreviation + "-" + firstName.charAt(0) + lastName.charAt(0) + "-" + fromDate.substring(3, 5) + fromDate.substring(8, 10) + "-" + honeyFlavour.substring(0, 3) + "-" + positiveRandomNumber).toUpperCase();
            }
        }
        return batchCode;
    }

    public User updateUserParent(String userId, String parentId) {
        User user = userRepo.findByid(userId);
        User parent = userRepo.findByid(parentId);

        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);

        if (user == null || parent == null) {
            throw new ResourceNotFoundException("Either User or Parent not Exist");
        }
        user.setCreatedByUserId(parentId);
        userService.saveUser(user);

        EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
        employeeAssignmentHistory.setToUser(user);
        employeeAssignmentHistory.setFromUser(parent);

        // get relationship between parent and user
        String userPrimaryRoleName = null;
        if(user.getRoles().size() == 1){
            userPrimaryRoleName = user.getRoles().get(0).getRoleName();
        } else {
            if(user.getRoles().contains(nationalManager)){
                userPrimaryRoleName = ROLE_NATIONAL_MANAGER;
            } else if (user.getRoles().contains(stateManager)){
                userPrimaryRoleName = ROLE_STATE_MANAGER;
            } else if (user.getRoles().contains(districtManager)){
                userPrimaryRoleName = ROLE_DISTRICT_MANAGER;
            } else if (user.getRoles().contains(blockManager)){
                userPrimaryRoleName = ROLE_AGENT_MANAGER;
            }
        }
        employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(userPrimaryRoleName, parent,nationalManager,stateManager,districtManager,blockManager));
        employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);

        return user;
    }

    @Override
    public BeekeepingQRCodeResponse getBeekeepingQRCodeResponse(String beekeepingId) {

        BeekeepingDetails beekeepingDetails = farmDetailService.getBeekeepingDetailById(beekeepingId);
        if (beekeepingDetails == null) {
            throw new ResourceNotFoundException("Beekeeping details not found.");
        }
        User user = userService.findByid(beekeepingDetails.getUserId());

        String userName = "";
        if (!StringUtils.isEmpty(user.getMiddleName()) && !StringUtils.isEmpty(user.getLastName())) {
            userName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
        } else {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        BeekeepingQRCodeResponse beekeepingQRCodeResponse = new BeekeepingQRCodeResponse();
        if (!StringUtils.isEmpty(beekeepingDetails.getBatchCode())) {
            beekeepingQRCodeResponse.setBatchCode(beekeepingDetails.getBatchCode());
        }

        beekeepingQRCodeResponse.setFarmerName(userName);
        beekeepingQRCodeResponse.setHoneyFlavour(beekeepingDetails.getHoneyFlavour().toString());
        if (beekeepingDetails.getSeason() != null && !StringUtils.isEmpty(beekeepingDetails.getSeason().toString())) {
            beekeepingQRCodeResponse.setSeason(beekeepingDetails.getSeason().toString());
        }

        String fromDate = convertDate(beekeepingDetails.getFromDate());
        String toDate = convertDate(beekeepingDetails.getToDate());
        beekeepingQRCodeResponse.setFromDate(fromDate);
        beekeepingQRCodeResponse.setToDate(toDate);

        if (user.getAddressModel() != null) {
            beekeepingQRCodeResponse.setState(user.getAddressModel().getState().getName());
            beekeepingQRCodeResponse.setDistrict(user.getAddressModel().getCity().getName());
            beekeepingQRCodeResponse.setTehsil(user.getAddressModel().getTehsil().getName());
            beekeepingQRCodeResponse.setBlock(user.getAddressModel().getBlock().getName());
            beekeepingQRCodeResponse.setVillage(user.getAddressModel().getVillageModel().getName());
        }

        String appLink = "https://play.google.com/store/apps/details?id=com.patanjali.dhartikadoctor";
        beekeepingQRCodeResponse.setAppLink(appLink);

        return beekeepingQRCodeResponse;
    }

    @Override
    public byte[] generateQRCodeForBeekeeping(String beekeepingId) throws IOException, WriterException, BadElementException {
        BeekeepingDetails beekeepingDetails = farmDetailService.getBeekeepingDetailById(beekeepingId);
        if(beekeepingDetails == null){
            throw new ResourceNotFoundException("Beekeeping Details Not Found.");
        }

        String apiUrl = beekeepigQRCodeApiUrl + "?beekeepingId=" + beekeepingId;
        generateQRCodeImage(apiUrl, 350, 350, BK_QR_CODE_IMAGE_PATH);

        if(!beekeepingDetails.isQrCodeGenerated()) {
            beekeepingDetails.setQrCodeGenerated(true);
            beekeepingDetails.setQrCodeResponseURL(apiUrl);
            farmDetailService.storeBeekeepingDetails(beekeepingDetails);
        }

        File file = ResourceUtils.getFile("./BKQRCode.png");
        InputStream in = new FileInputStream(file);

        return IOUtils.toByteArray(in);
    }

    @Override
    public String getSoilAndFertilizerReportNumber(String reportHistoryId) {
        String reportNumber = "";
        ReportHistory reportDetail = reportHistoryService.getReportDetail(reportHistoryId);

        if (reportDetail != null){
            User agent = userService.getAgentByUserId(reportDetail.getUserId());

            String stateAbbreviation = null;
            if (agent.getAddressModel() != null && agent.getAddressModel().getState() != null) {
                stateAbbreviation = agent.getAddressModel().getState().getStateAbbreviation();
            }

            Random rnd = new Random();
            String positiveRandomNumber = String.format("%05d", rnd.nextInt(100000));

            reportNumber = ("STR" + "/" + stateAbbreviation + "/" + agent.getUserCode() + "/" + positiveRandomNumber).toUpperCase();
        }
        return reportNumber;
    }

    @Override
    public KhasrasOC storeKhasrasOC(KhasrasOC khasrasOC) {
        return mongoTemplate.save(khasrasOC);
    }

    @Override
    public KhasrasOC getKhasrasOC(String userId, String landDetailId) {
        Query query =  new Query(Criteria.where("userId").is(userId).and("userLandDetailList.id").is(landDetailId));
        return mongoTemplate.findOne(query,KhasrasOC.class);
    }

    @Override
    public MessageResponse updateStateAbbreviationForFarmerCode(String stateName) {
        State state = locationServices.findStateByName(stateName);
        if(state == null){
            throw new ResourceNotFoundException("State not found for input state name: "+stateName);
        }
        String abbreviation = state.getStateAbbreviation();

        Query query = new Query(Criteria.where("addressModel.state.$id").is(new ObjectId(state.getId())).and("roles.roleName").is(ROLE_USER));
        List<User> userList = mongoTemplate.find(query,User.class);
        if(!userList.isEmpty()) {
            for (User user : userList) {
                System.out.println("============phoneNumber============"+user.getPrimaryPhone()+"====userCode======"+user.getUserCode());
                if(user.getUserCode().startsWith("DKDF") && user.getUserCode().contains("NULL")){
                    user.setUserCode(user.getUserCode().replace("NULL",abbreviation));
                    mongoTemplate.save(user);
                    System.out.println("=========updatedCode==========="+user.getUserCode());
                }
            }
        }
        return null;
    }


}
