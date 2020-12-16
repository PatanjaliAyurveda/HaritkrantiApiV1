package com.bharuwa.haritkranti.config;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.repositories.BeekeepingDetailRepo;
import com.bharuwa.haritkranti.repositories.OrganicCertificateRepo;
import com.bharuwa.haritkranti.repositories.ReportHistoryRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author anuragdhunna
 */
@Component
public class Migration {

    private static final Logger logger = LoggerFactory.getLogger(Migration.class);

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportHistoryRepo reportHistoryRepo;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrganicCertificateRepo organicCertificateRepo;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private BeekeepingDetailRepo beekeepingDetailRepo;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CountService countService;

    private final MongoTemplate mongoTemplate;


    public Migration(GenericMongoTemplate genericMongoTemplate, MongoTemplate mongoTemplate) {
        this.genericMongoTemplate = genericMongoTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public void run() {

        logger.info("Migration Script Loaded");
        // Check app version

        // TODO: find from DB
        App app = genericMongoTemplate.findByKey("name", APP_NAME, App.class);

        if (app == null) {
            app = new App();
            app.setName(APP_NAME);
            app.setVersion(1);
            app = mongoTemplate.save(app);
        }

        int version = app.getVersion();

        if (app.getVersion() <= 1) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addReadyToSellField();
            version = 2;
        }

        if (app.getVersion() <= 2) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addActiveAgentField();
            version = 3;
        }

        if (app.getVersion() <= 3) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addCreatedByUserIdFieldFarmers();
            version = 4;
        }

        if (app.getVersion() <= 4) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            assignFarmerToAdminUser();
            version = 5;
        }
//        if (app.getVersion() <= 5) {
//            logger.info("Migration Script Loaded for Version: "+app.getVersion());
//            addCreatedByUserIdFieldFarmers();
//            setOCLandEligibleForLandDetails();
//            version = 6;
//        }

//        if (app.getVersion() <= 6) {
//            logger.info("Migration Script Loaded for Version: "+app.getVersion());
//            generateBeekeepingBatchCode();
//            version = 7;
//        }

        if (app.getVersion() <= 6) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            generateBeekeepingBatchCode();
            version = 7;
        }

        if (app.getVersion() <= 7) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addGenderFieldForFamilyMember();
            version = 8;
        }

        if (app.getVersion() <= 8) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            convertVillageIdToVillageModel();
            version = 9;
        }

        if (app.getVersion() <=9) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addHasUserFieldInUser();
            version = 10;
        }

        if (app.getVersion() <=10) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
//            addlandMapIdInLandDetails();
            version = 11;
        }


        if (app.getVersion() <=11) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            organiseUserFinancialDetail();
            version = 12;
        }

        if (app.getVersion() <=12) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            organiseEmployeeHistory();
            version = 13;
        }

        if (app.getVersion() <=13) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            organiseFinancialDetails();
            version = 14;
        }

        if (app.getVersion() <=14) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            addUserTypeInFinancialDetails();
            version = 15;
        }

        if (app.getVersion() <=15) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            cropTypeToCropGroup();
            version = 16;
        }

        if (app.getVersion() <=16) {
            logger.info("Migration Script Loaded for Version: " + app.getVersion());
            organiseCountAll();
            version = 17;
        }

//        if (app.getVersion() <=17) {
//            logger.info("Migration Script Loaded for Version: " + app.getVersion());
//            storeAddressInSoilTest();
//            version = 18;
//        }
//
//        if (app.getVersion() <=18) {
//            logger.info("Migration Script Loaded for Version: " + app.getVersion());
//            storeSoilTestFromReportHistory();
//            version = 19;
//        }

        if (app.getVersion() < version) {
            app.setVersion(version);
            mongoTemplate.save(app);
        }

    }


    /**
     * add readyToSellField in UserCrop
     */
    private void addReadyToSellField() {
        System.out.println("==addReadyToSellField==:::::::=");
        //LOGIC for Script
        List<UserCrop> userCrops = genericMongoTemplate.findAll(UserCrop.class);
        for (UserCrop userCrop : userCrops) {
            userCrop.setReadyToSell(false);
            mongoTemplate.save(userCrop);
        }

    }

    /**
     * add  isAgentActive in Agent
     */
    private void addActiveAgentField() {
        Query query;
        List<User> all = genericMongoTemplate.findAll(User.class);

        for (User agent : all) {
            query = new Query(Criteria.where("agentId").is(agent.getId()));
            UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
            if (userAgent != null && !userAgent.getUsers().isEmpty()) {
                agent.setHasFarmers(true);
                userService.saveUser(agent);
            } else {
                agent.setHasFarmers(false);
                userService.saveUser(agent);
            }
        }
    }

    private void addCreatedByUserIdFieldFarmers() {
        List<User> all = genericMongoTemplate.findAll(User.class);

        for (User user : all) {
            if (StringUtils.isEmpty(user.getCreatedByUserId()) || user.getCreatedByUserId() == null) {
                User agent = userService.getAgentByUserId(user.getId());
                if (agent == null) {
                    User admin = userService.getUserByPhoneNum("1212121212");
                    user.setCreatedByUserId(admin.getId());
                } else {
                    user.setCreatedByUserId(agent.getId());
                }
                userService.saveUser(user);
            }
        }
    }

    private void assignFarmerToAdminUser() {

        // Get Admin User
        User admin = userService.getUserByPhoneNum("1212121212");

        Query query = new Query(Criteria.where("createdByUserId").is(null));
        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(user -> {
            user.setCreatedByUserId(admin.getId());
            userService.saveUser(user);
        });
    }

    private void setOCLandEligibleForLandDetails() {
        Query query = new Query(Criteria.where("organicLandEligible").ne(false).exists(true));
        List<OrganicCertificate> organicCertificateList = mongoTemplate.find(query, OrganicCertificate.class);

        for (OrganicCertificate organicCertificate : organicCertificateList) {
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(organicCertificate.getKhasraNo(), organicCertificate.getUserId());
            if (userLandDetail != null) {
                userLandDetail.setOrganicLandEligible(organicCertificate.isOrganicLandEligible());
                agentService.addUserLandDetail(userLandDetail);
            }
        }
    }

    private void generateBeekeepingBatchCode() {
        List<BeekeepingDetails> beekeepingDetailsList = beekeepingDetailRepo.findAll();
        for (BeekeepingDetails beekeepingDetails : beekeepingDetailsList) {
            if (beekeepingDetails.getBatchCode() == null || StringUtils.isEmpty(beekeepingDetails.getBatchCode()) || beekeepingDetails.getBatchCode().equals(beekeepingDetails.getId())) {
                beekeepingDetails.setBatchCode(userExtraService.getBeekingBatchForFarmer(beekeepingDetails.getId()));
                beekeepingDetailRepo.save(beekeepingDetails);
            }
        }
    }

    private void storeUserCreatedBy() {
        List<User> all = genericMongoTemplate.findAll(User.class);

        for (User user : all) {
            if (!StringUtils.isEmpty(user.getCreatedByUserId())) {
                UserCreatedBy createdBy = new UserCreatedBy();
                createdBy.setUserId(user.getId());
                createdBy.setCreatedByUserId(user.getCreatedByUserId());
                mongoTemplate.save(createdBy);
            }
        }
    }

    private void restoreUserCreatedBy() {
        List<UserCreatedBy> all = genericMongoTemplate.findAll(UserCreatedBy.class);

        all.forEach(userCreatedBy -> {

            User user = genericMongoTemplate.findById(userCreatedBy.getUserId(), User.class);
            user.setCreatedByUserId(userCreatedBy.getCreatedByUserId());

            mongoTemplate.save(user);
        });
    }

    /**
     * add soilReportNumber in Report History
     */
    private void generateSoilAndFertilizerReportNumber() {
        Query query = new Query(Criteria.where("soilReportNumber").is(null));
        List<ReportHistory> reportHistoryList = mongoTemplate.find(query, ReportHistory.class);
        System.out.println("==========reportHistoryList.size()========" + reportHistoryList.size());
        for (ReportHistory reportHistory : reportHistoryList) {
            reportHistory.setSoilReportNumber(userExtraService.getSoilAndFertilizerReportNumber(reportHistory.getId()));
            reportHistoryRepo.save(reportHistory);
        }
    }

    /**
     * add GenderField in Report FamilyMember
     */
    private void addGenderFieldForFamilyMember() {
        final List<FamilyMember> familyMembers = mongoTemplate.findAll(FamilyMember.class);

        List<User.Relationship> femaleRelations = new ArrayList<>();
        femaleRelations.add(User.Relationship.GrandMother);
        femaleRelations.add(User.Relationship.Mother);
        femaleRelations.add(User.Relationship.Sister);
        femaleRelations.add(User.Relationship.Wife);
        femaleRelations.add(User.Relationship.Daughter);
        femaleRelations.add(User.Relationship.GrandDaughter);

        for (FamilyMember familyMember : familyMembers) {
            familyMember.setGender(User.Gender.MALE);

            if (femaleRelations.contains(familyMember.getRelationship())) {
                familyMember.setGender(User.Gender.FEMALE);
            }
            mongoTemplate.save(familyMember);
        }
    }

    /**
     * add Village Model in VillageAsset
     */
    private void convertVillageIdToVillageModel() {
        List<VillageAsset> villageAssets = mongoTemplate.findAll(VillageAsset.class);
        for (VillageAsset villageAsset : villageAssets){
            if (!StringUtils.isEmpty(villageAsset.getVillageId())) {
                Village village = genericMongoTemplate.findById(villageAsset.getVillageId(), Village.class);
                villageAsset.setVillage(village);
                mongoTemplate.save(villageAsset);
            }
        }
    }

    /**
     * add hasUser Field in User
     */
    private void addHasUserFieldInUser() {
        List<User> all = mongoTemplate.findAll(User.class);
        for (User user: all) {
            Query query = new Query(Criteria.where("createdByUserId").is(user.getId()));
            long userList = mongoTemplate.count(query, User.class);
            if ( userList > 0) {
                user.setHasUser(true);
            }
            userService.saveUser(user);
        }
    }

    /**
     * add UserLandMapping Id Field in UserLandDetails
     */
//    private void addlandMapIdInLandDetails() {
//        List<UserLandDetail> all = mongoTemplate.findAll(UserLandDetail.class);
//        for (UserLandDetail userLandDetail: all) {
//            UserLandMapping landMapping = agentService.getUserLandMapping(userLandDetail.getUserId(),userLandDetail.getKhasraNo());
//            if(landMapping != null) {
//                userLandDetail.setLandMapId(landMapping.getId());
//                mongoTemplate.save(userLandDetail);
//            }
//        }
//    }

    /**
     * add UserIdentificationDetail in FinancialDetails
     */
    private void organiseUserFinancialDetail() {
        List<User> userList = mongoTemplate.findAll(User.class);
        for (User user : userList){
            UserIdentificationDetails userIdentificationDetail  = userService.getUserIdentificationDetails(user.getId());

            if(userIdentificationDetail != null){
                Query query = new Query(Criteria.where("userId").is(userIdentificationDetail.getUserId()).and("accountNumber").is(userIdentificationDetail.getBankAccountNumber()));
                FinancialDetails financialDetail = mongoTemplate.findOne(query,FinancialDetails.class);
                if (financialDetail == null) {
                    financialDetail =  new FinancialDetails();
                }
                financialDetail.setUserId(userIdentificationDetail.getUserId());
                financialDetail.setAadhaarCardNumber(userIdentificationDetail.getAadhaarCardNumber());
                financialDetail.setAadharCardImage(userIdentificationDetail.getAadharCardImage());
                financialDetail.setAccountNumber(userIdentificationDetail.getBankAccountNumber());
                financialDetail.setBankPassBookImage(userIdentificationDetail.getBankPassBookImage());
                financialDetail.setIfscCode(userIdentificationDetail.getIfscCode());
                mongoTemplate.save(financialDetail);
            }
        }
    }

    private void setLocationReportHistory() {

        Date now = new Date();
        Date from = new DateTime(now).minusDays(7).toDate();
        Query query = new Query(Criteria.where("creationDate").gte(from).lte(now));
        List<ReportHistory> reportHistories = mongoTemplate.find(query, ReportHistory.class);

        System.out.println("================size=====" + reportHistories.size());
        reportHistories.forEach(reportHistory -> {
            NPKRecommendation npkRecommendation = reportHistory.getNpkRecommendation();

            UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(npkRecommendation.getKhasraNo(), reportHistory.getUserId());
            if (landDetail == null) {
                throw new ResourceNotFoundException("Land Details not found");
            }
            npkRecommendation.setCityId(landDetail.getCity().getId());
            npkRecommendation.setStateId(landDetail.getState().getId());
            if (landDetail.getTehsil() != null) {
             //   npkRecommendation.setTehsilId(landDetail.getTehsil().getId()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
            }
            if (landDetail.getBlock() != null) {
             //   npkRecommendation.setBlockId(landDetail.getBlock().getId());  THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
            }

            // store soil and fertilizer report Number
            if (reportHistory.getSoilReportNumber() == null || StringUtils.isEmpty(reportHistory.getSoilReportNumber())) {
                reportHistory.setSoilReportNumber(userExtraService.getSoilAndFertilizerReportNumber(reportHistory.getId()));
            }

            mongoTemplate.save(reportHistory);
        });
    }
    /**
     * add Status in Employee History
     */
    private void organiseEmployeeHistory() {
        List<User> all = mongoTemplate.findAll(User.class);
        for (User user: all) {
            EmployeeHistory employeeHistory = new EmployeeHistory();
            employeeHistory.setUser(user);
            employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
            employeeHistory.setCreatedByUserId(user.getCreatedByUserId());
            employeeHistory.setCreationDate(user.getCreationDate());
            employeeHistoryService.storeEmployeeHistory(employeeHistory);
        }
    }

    /**
     * store user instead of userId and set account verified false
     */
    private void organiseFinancialDetails() {
        List<FinancialDetails> all = mongoTemplate.findAll(FinancialDetails.class);
        for (FinancialDetails financialDetails: all) {
            User user = userService.findByid(financialDetails.getUserId());
            if(user != null) {
                financialDetails.setUser(user);
            }
            financialDetails.setAccountVerified(false);
            if(!financialDetails.isPrimaryAccount()){
                financialDetails.setPrimaryAccount(false);
            }
            mongoTemplate.save(financialDetails);
        }
    }

    /**
     * store Employee Assigment History
     */
//    private void addEmployeeAssignmentHistory() {
//
//        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
//        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
//        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
//        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);
//
//        List<User> all = mongoTemplate.findAll(User.class);
//        for (User user: all) {
////            System.out.println("=======primaryPhone==========="+user.getPrimaryPhone());
//            EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
//            if(!StringUtils.isEmpty(user.getCreatedByUserId())) {
//                User fromUser = userService.findByid(user.getCreatedByUserId());
//                if(fromUser != null){
//                    employeeAssignmentHistory.setFromUser(fromUser);
//                    employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationship(user, fromUser,nationalManager,stateManager,districtManager,blockManager));
//                    employeeAssignmentHistory.setToUser(user);
//                    employeeAssignmentHistory.setAssignmentDate(user.getCreationDate());
//                    employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);
//                }
//            }
//        }
//    }

    /**
     * store user instead of userId and set account verified false
     */
    private void addUserTypeInFinancialDetails() {
        Role farmer = roleService.roleName(ROLE_USER);
        Role agent = roleService.roleName(ROLE_AGENT);
        List<FinancialDetails> all = mongoTemplate.findAll(FinancialDetails.class);
        for (FinancialDetails financialDetails : all) {
            if(financialDetails.getUser() != null) {
                System.out.println("==bnak=====" + financialDetails.getId() + "===userId==========" + financialDetails.getUserId());
                if (financialDetails.getUser().getRoles().contains(farmer)) {
                    financialDetails.setUserType(FinancialDetails.UserType.Farmer);
                } else if (financialDetails.getUser().getRoles().size() == 1 && financialDetails.getUser().getRoles().contains(agent)) {
                    financialDetails.setUserType(FinancialDetails.UserType.Agent);
                } else {
                    financialDetails.setUserType(FinancialDetails.UserType.Manager);
                }
                mongoTemplate.save(financialDetails);
            }
        }
    }

    private void cropTypeToCropGroup() {
        List<Crop> crops = genericMongoTemplate.findAll(Crop.class);
        List<String> typeList = new ArrayList<>();
        crops.forEach(crop -> {
            Crop.CropType type = crop.getCropType();
            // find crop group
            CropGroup cropGroup = genericMongoTemplate.findByKey("name", type.toString(), CropGroup.class);
            if (cropGroup == null) {
                typeList.add(type.toString());
                System.out.println("====================================null====================================");
            } else {
                System.out.println("=============type==========" + type);
                System.out.println("==========cropGroup=============" + cropGroup.getName());
                crop.setCropGroup(cropGroup);
            }
            mongoTemplate.save(crop);
        });

        System.out.println(typeList.size());
        System.out.println(typeList);
    }

    private void organiseCountAll() {
        CountAll countAll = new CountAll();
        countAll.setTotalUsers(countService.getTotalUserCount());
        countAll.setTotalActiveStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,true));
        countAll.setTotalStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,false));
        countAll.setTotalActiveDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,true));
        countAll.setTotalDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,false));
        countAll.setTotalActiveAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,true));
        countAll.setTotalAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,false));
        countAll.setTotalNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,false));
        countAll.setTotalActiveNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,true));
        countAll.setTotalActiveAgents(countService.getTotalAgentCount(true));
        countAll.setTotalAgents(countService.getTotalAgentCount(false));
        countAll.setTotalFarmers(countService.getTotalFarmerCount());
        countAll.setTotalUserSignUps(countService.getTotalUserSignUpCount());
        countAll.setTotalKhasras(countService.getTotalKhasraCount());
        countAll.setTotalFemaleFarmers(countService.getTotalFemaleFarmerCount());
        countAll.setTotalMaleFarmers(countService.getTotalMaleFarmerCount());
        countAll.setTotalDairyFarmers(countService.getDairyFarmerCount());
        countAll.setTotalBeekeepingFarmers(countService.getBeekeepingFarmerCount());
        countAll.setTotalSericultureFarmers(countService.getSericultureFarmerCount());
        countAll.setTotalHorticultureFarmers(countService.getHorticultureFarmerCount());
        countAll.setTotalCowss(countService.getTotalCowsCount());
        countAll.setTotalSheeps(countService.getTotalSheepCount());
        countAll.setTotalGoats(countService.getTotalGoatCount());
        countAll.setTotalBuffalos(countService.getTotalBuffaloCount());
        mongoTemplate.save(countAll);
    }

    /**
     * store Address model in SoilTest
     */
    private void storeAddressInSoilTest() {
        List<SoilTest> soilTestList = mongoTemplate.findAll(SoilTest.class);
        System.out.println("==========reportHistoryList.size()========" + soilTestList.size());
        for (SoilTest soilTest : soilTestList) {
            if(soilTest.getUserCrop() != null && !StringUtils.isEmpty(soilTest.getUserCrop().getUserLandDetailId())) {
                UserLandDetail landDetail = genericMongoTemplate.findById(soilTest.getUserCrop().getUserLandDetailId(), UserLandDetail.class);
                if (landDetail != null) {
                    Address address = new Address();
                    address.setState(landDetail.getState());
                    address.setCity(landDetail.getCity());
                 //   address.setTehsil(landDetail.getTehsil()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
                 //   address.setBlock(landDetail.getBlock()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
                 //   address.setVillageModel(landDetail.getVillageModel());
                    address.setVillage(landDetail.getVillage());
                    soilTest.setAddress(address);
                }
                mongoTemplate.save(soilTest);
            }
        }
    }

    /**
     * add Soil Test in SoilTest Collection from Report History
     */
    private void storeSoilTestFromReportHistory() {
        List<ReportHistory> reportHistoryList = mongoTemplate.findAll(ReportHistory.class);
        System.out.println("==========reportHistoryList.size()========" + reportHistoryList.size());
        for (ReportHistory reportHistory : reportHistoryList) {
            SoilTest soilTest = reportHistory.getNpkRecommendation().getSoilTest();
            if(soilTest != null && !StringUtils.isEmpty(reportHistory.getNpkRecommendation().getKhasraNo()) && !StringUtils.isEmpty(reportHistory.getNpkRecommendation().getUserId())) {
                UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(reportHistory.getNpkRecommendation().getKhasraNo(),reportHistory.getNpkRecommendation().getUserId());
                if (landDetail != null) {
                    Address address = new Address();
                    address.setState(landDetail.getState());
                    address.setCity(landDetail.getCity());
      //              address.setTehsil(landDetail.getTehsil());
      //              address.setBlock(landDetail.getBlock());
                    address.setVillage(landDetail.getVillage());
                    soilTest.setAddress(address);
                    mongoTemplate.save(soilTest);
                }
            }
        }
    }

}
