package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.BeekeepingDetails;
import com.bharuwa.haritkranti.models.FarmDetails;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.UserCrop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.responseModels.POMFertCal;
import com.bharuwa.haritkranti.repositories.BeekeepingDetailRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bharuwa.haritkranti.controllers.AgentController.getMilkingAnimalList;
import static com.bharuwa.haritkranti.models.fertilizerModels.FarmingType.Type.*;
import static com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer.CategoryType.Manure;
import static com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer.CategoryType.PatanjaliManure;
import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author anuragdhunna
 */
@Service
public class OrganiseServiceImpl implements OrganiseService {

    private final MongoTemplate mongoTemplate;

    public OrganiseServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private LocationServices locationServices;

    @Autowired
    private FarmDetailService farmDetailService;

    @Autowired
    private BeekeepingDetailRepo  beekeepingDetailRepo;

    @Autowired
    private UserExtraService userExtraService;

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

    @Override
    public void assignAgentsToManager(String phoneNumber, String stateName, String cityName) {
        User adminUser = userService.getUserByPhoneNum("1212121212");
        User userByPhoneNum = userService.getUserByPhoneNum(phoneNumber);
        if (userByPhoneNum == null) {
            throw new ResourceNotFoundException("userByPhoneNum not found");
        }

        // get list of all agent managers
        State state = locationServices.findStateByName(stateName);
        City city = locationServices.getCityByName(cityName);

        List<String> roleList = new ArrayList<>();
        roleList.add(ROLE_AGENT_MANAGER);

        Query query = new Query(Criteria.where("addressModel.state").is(state).and("addressModel.city")
                .is(city).and("roles.roleName").in(roleList).and("createdByUserId").is(null));

        // check role

        List<User> users = mongoTemplate.find(query, User.class);

        for (User userExist: users) {

//            for (Role role: userExist.getRoles()) {
//                System.out.println("======role.getRoleName============userExist===" + role.getRoleName());
//            }
            userExist.setCreatedByUserId(userByPhoneNum.getId());

            userService.saveUser(userExist);
        }
    }

    @Override
    public void createdViaSignUpToAgent() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT).and("createdVia").is(User.CreatedVia.SIGNUP));
        List<User> users = mongoTemplate.find(query,User.class);
        for(User user : users) {
            user.setCreatedVia(User.CreatedVia.AGENT);
            userService.saveUser(user);
        }
    }

    @Override
    public void addCropGrouptoUserCrop() {
        Query query = new Query();
        List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);
        System.out.println("========userCrops======"+userCrops.size());

        for (UserCrop userCrop: userCrops) {
            userCrop.setType(CropGroup.Type.CROP);
            mongoTemplate.save(userCrop);
        }
    }

    @Override
    public String organiseBeekeepingDetailsFromFarmDetails() {
        Query query = new Query();
        query.addCriteria(Criteria.where("animalType").is(FarmDetails.AnimalType.Hive));
        List<FarmDetails> beekeepingFarmDetailList = mongoTemplate.find(query, FarmDetails.class);

        for (FarmDetails farmDetails : beekeepingFarmDetailList){
            BeekeepingDetails beekeepingDetails = new BeekeepingDetails();
            beekeepingDetails.setUserId(farmDetails.getUserId());
            beekeepingDetails.setAnimalType(farmDetails.getAnimalType());
            beekeepingDetails.setQuantity(farmDetails.getQuantity());
            beekeepingDetails.setUnit(farmDetails.getUnit());
            beekeepingDetails.setIncome(farmDetails.getIncome());
            beekeepingDetails.setProductionOutput(farmDetails.getProductionOutput());
            beekeepingDetails.setUnitProductionOutput(farmDetails.getUnitProductionOutput());
            beekeepingDetailRepo.save(beekeepingDetails);
        }
        return "Success";
    }

    @Override
    public String assignUserCodeFarmers() {
        Query query = new Query();
        query.addCriteria(Criteria.where("roles.roleName").is(ROLE_USER));
        List<User> userList = mongoTemplate.find(query, User.class);
        for(User user : userList){
            if(StringUtils.isEmpty(user.getUserCode())){
                user.setUserCode(userExtraService.generateUniqueCodeForFarmer(user));
                userService.saveUser(user);
            }
        }
        return "Success";
    }

    @Override
    public String removeAgentFarmer(String agentId) {

        Query query = new Query(Criteria.where("createdByUserId").is(agentId).and("roles.roleName").is(ROLE_USER));
        List<User> farmerList = mongoTemplate.find(query, User.class);
        for(User farmer : farmerList){
            removeFarmerById(agentId, farmer.getId());
        }
        query = new Query(Criteria.where("agentId").is(agentId.trim()));
        UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
        if (userAgent != null) {
            mongoTemplate.remove(userAgent);
        }
        return "Farmer's removed Successfully";
    }

    @Override
    public String removeFarmerById(String agentId, String farmerId) {

        Query query;
        User farmer = genericMongoTemplate.findById(farmerId,User.class);

        System.out.println("farmerId===================="+farmerId);

        //remove Family Member
        query = new Query(Criteria.where("farmerId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, FamilyMember.class);

        //remove Family Health Record
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, FamilyMemberHealthRecord.class);

        // farmer extra detail
        query = new Query(Criteria.where("farmerId").is(farmerId));
        mongoTemplate.findAndRemove(query,FarmerExtraDetails.class);

        //remove userLand Detail and Mapping ,OC and User-Certificate
        List<UserLandDetail> userLandDetailList = agentService.getUserLandDetails(farmerId);
        if(!userLandDetailList.isEmpty()){
            for (UserLandDetail userLandDetail : userLandDetailList){

                // remove OC detail
                query = new Query(Criteria.where("userId").is(farmerId).and("khasraNo").is(userLandDetail.getKhasraNo()));
                mongoTemplate.findAndRemove(query, OrganicCertificate.class);

                // remove UC
                query = new Query(Criteria.where("userId").is(farmerId).and("khasraNo").is(userLandDetail.getKhasraNo()));
                mongoTemplate.findAndRemove(query, UserCertificate.class);

                //  remove land map
                query = new Query(Criteria.where("khasraNo").is(userLandDetail.getKhasraNo()).and("userId").is(farmerId));
                mongoTemplate.findAndRemove(query, UserLandMapping.class);
                mongoTemplate.remove(userLandDetail);
            }
        }

        // remove milking animal detail
        List<MilkingAnimalDetails.MilkingAnimalType> milkingAnimalTypeList = getMilkingAnimalList();
        for (MilkingAnimalDetails.MilkingAnimalType milkingAnimalType : milkingAnimalTypeList) {
            List<MilkingAnimalDetails> milkingAnimalDetailsList = userService.getMilkingAnimalDetails(farmerId,milkingAnimalType.toString());
            if(!milkingAnimalDetailsList.isEmpty()){
                for ( MilkingAnimalDetails milkingAnimalDetails : milkingAnimalDetailsList){
                    //remove cattleshed mapping
                    query = new Query(Criteria.where("userId").is(farmerId).and("mappedLocationType").is( GeoLocationMapping.MappedLocationType.Milking_Animal_Shed.toString()).and("typeId").is(milkingAnimalDetails.getId()));
                    mongoTemplate.findAndRemove(query,GeoLocationMapping.class);
                    mongoTemplate.remove(milkingAnimalDetails);
                }
            }
        }

        // reomove farm detail
        List<FarmDetails.AnimalType> animalList = Arrays.asList(FarmDetails.AnimalType.values());
        for (FarmDetails.AnimalType animalType : animalList) {
            query = new Query(Criteria.where("userId").is(farmerId).and("animalType").is(animalType.toString()));
            mongoTemplate.findAndRemove(query, FarmDetails.class);
        }

        // removing beekeeping detail and mapping
        List<BeekeepingDetails> beekeepingDetailsList = farmDetailService.getBeekeepingDetails(farmerId);
        if(!beekeepingDetailsList.isEmpty()){
            for (BeekeepingDetails beekeepingDetails : beekeepingDetailsList){
                // remove beekeeeping mapping
                query = new Query(Criteria.where("farmerId").is(farmerId).and("beekeepingId").is(beekeepingDetails.getId()));
                mongoTemplate.findAndRemove(query, BeekeepingAreaMapping.class);

                query = new Query(Criteria.where("userId").is(farmerId).and("mappedLocationType").is( GeoLocationMapping.MappedLocationType.Beekeeping_Area.toString()).and("typeId").is(beekeepingDetails.getId()));
                mongoTemplate.findAndRemove(query,GeoLocationMapping.class);
                mongoTemplate.remove(beekeepingDetails);
            }
        }

        //removing UserCrop
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, UserCrop.class);

        //remove CropImage
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, CropImage.class);

        //remove horticulture detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, Horticulture.class);

        //remove financial detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, FinancialDetails.class);

        //remove Loan detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, LoanDetails.class);

        //remove Insurance detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, InsuranceDetails.class);

        //remove User Income detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAndRemove(query, IncomeSources.class);

        //remove User Govt Schemes
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, UserSchemes.class);

        //remove User Subsidy
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, UserSubsidy.class);

        //remove UserEquipments detail
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAndRemove(query, UserEquipments.class);

        //remove User Identification details
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAndRemove(query, UserIdentificationDetails.class);

        //remove Report History
        query = new Query(Criteria.where("userId").is(farmerId));
        mongoTemplate.findAllAndRemove(query, ReportHistory.class);

        //remove Employee Assignment History
        removeEmployeeAssignmentHistory(farmerId);


        query = new Query(Criteria.where("agentId").is(agentId.trim()));
        UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
        if (userAgent != null) {
            userAgent.removeUser(farmer);
            mongoTemplate.save(userAgent);
        }
        mongoTemplate.remove(farmer);

        return "Farmer removed successfully";
    }

    @Override
    public String organiseCreatedByUserIdforAgentFarmers(String agentId) {
        List<User> farmerList = agentService.getAgentUsers(agentId);
        for(User farmer :farmerList){
            farmer.setCreatedByUserId(agentId);
            userService.saveUser(farmer);
        }
        return "success";
    }

    @Override
    public List<User> getAgentFarmersNotHavingAgentId(String agentId) {
        List<User> userList = new ArrayList<>();
        List<User> farmerList = agentService.getAgentUsers(agentId);
        for(User farmer :farmerList){
            if(StringUtils.isEmpty(farmer.getCreatedByUserId()) ||!farmer.getCreatedByUserId().equals(agentId)){
                userList.add(farmer);
            }
        }
        return userList;
    }


    /**
     * store address
     */
    @Override
    public void organiseUserSchemes() {

        List<UserSchemes> userSchemesList = mongoTemplate.findAll(UserSchemes.class);
        System.out.println("==========userSchemesList==========="+userSchemesList.size());
        int counter = 0;

        for (UserSchemes userSchemes : userSchemesList) {
            User user = userService.findByid(userSchemes.getUserId());
            if(user != null){
                userSchemes.setAddress(user.getAddressModel());
                userSchemes.setSchemaVersion(userSchemes.getSchemaVersion() + 1);
                mongoTemplate.save(userSchemes);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseHorticultureData() {
        List<Horticulture> horticultureList = mongoTemplate.findAll(Horticulture.class);
        System.out.println("==========horticultureList==========="+horticultureList.size());
        int counter = 0;

        for (Horticulture horticulture : horticultureList) {
            User user = userService.findByid(horticulture.getUserId());
            if(user != null){
                horticulture.setAddress(user.getAddressModel());
                horticulture.setSchemaVersion(horticulture.getSchemaVersion() + 1);
                mongoTemplate.save(horticulture);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseUserCrop() {
        List<UserCrop> userCropList = mongoTemplate.findAll(UserCrop.class);
        System.out.println("==========userCropList==========="+userCropList.size());
        int counter = 0;
        for (UserCrop userCrop : userCropList) {
            // get Address from Land Details
            UserLandDetail landDetail = mongoTemplate.findById(userCrop.getUserLandDetailId(),UserLandDetail.class);
            if(landDetail != null){
                Address address = new Address();
                address.setState(landDetail.getState());
                address.setCity(landDetail.getCity());
        //        address.setTehsil(landDetail.getTehsil());
        //        address.setBlock(landDetail.getBlock());
        //        address.setVillageModel(landDetail.getVillageModel());
                userCrop.setAddress(address);
                userCrop.setSchemaVersion(userCrop.getSchemaVersion() + 1);
                mongoTemplate.save(userCrop);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseFarmDetail() {
        List<FarmDetails> farmDetailsList = mongoTemplate.findAll(FarmDetails.class);
        System.out.println("==========farmDetailsList==========="+farmDetailsList.size());
        int counter = 0;

        for (FarmDetails farmDetails : farmDetailsList) {
            User user = userService.findByid(farmDetails.getUserId());
            if(user != null){
                farmDetails.setAddress(user.getAddressModel());
                farmDetails.setSchemaVersion(farmDetails.getSchemaVersion() + 1);
                mongoTemplate.save(farmDetails);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseMilkingAnimalDetail() {
        List<MilkingAnimalDetails> milkingAnimalDetailsList = mongoTemplate.findAll(MilkingAnimalDetails.class);
        System.out.println("==========milkingAnimalDetailsList==========="+milkingAnimalDetailsList.size());
        int counter = 0;

        for (MilkingAnimalDetails milkingAnimalDetails : milkingAnimalDetailsList) {
            User user = userService.findByid(milkingAnimalDetails.getUserId());
            if(user != null){
                milkingAnimalDetails.setAddress(user.getAddressModel());
                milkingAnimalDetails.setSchemaVersion(milkingAnimalDetails.getSchemaVersion() + 1);
                mongoTemplate.save(milkingAnimalDetails);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseUserSubsidy() {
        List<UserSubsidy> userSubsidyList = mongoTemplate.findAll(UserSubsidy.class);
        System.out.println("==========userSubsidyList==========="+userSubsidyList.size());
        int counter = 0;

        for (UserSubsidy userSubsidy : userSubsidyList) {
            User user = userService.findByid(userSubsidy.getUserId());
            if(user != null){
                userSubsidy.setAddress(user.getAddressModel());
                userSubsidy.setSchemaVersion(userSubsidy.getSchemaVersion() + 1);
                mongoTemplate.save(userSubsidy);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseHealthRecords() {
        List<FamilyMemberHealthRecord> familyMemberHealthRecordList = mongoTemplate.findAll(FamilyMemberHealthRecord.class);
        System.out.println("==========familyMemberHealthRecordList==========="+familyMemberHealthRecordList.size());
        int counter = 0;

        for (FamilyMemberHealthRecord familyMemberHealthRecord : familyMemberHealthRecordList) {
            User user = userService.findByid(familyMemberHealthRecord.getHeadUserId());
            if(user != null){
                familyMemberHealthRecord.setAddress(user.getAddressModel());
                familyMemberHealthRecord.setSchemaVersion(familyMemberHealthRecord.getSchemaVersion() + 1);
                mongoTemplate.save(familyMemberHealthRecord);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store address
     */
    @Override
    public void organiseUserEquipment() {
        List<UserEquipments> userEquipmentsList = mongoTemplate.findAll(UserEquipments.class);
        System.out.println("==========userEquipmentsList==========="+userEquipmentsList.size());
        int counter = 0;

        for (UserEquipments userEquipments : userEquipmentsList) {
            User user = userService.findByid(userEquipments.getUserId());
            if(user != null){
                userEquipments.setAddress(user.getAddressModel());
                userEquipments.setSchemaVersion(userEquipments.getSchemaVersion() + 1);
                mongoTemplate.save(userEquipments);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * store Address model in SoilTest
     */
    @Override
    public void storeAddressInSoilTest() {
        List<SoilTest> soilTestList = mongoTemplate.findAll(SoilTest.class);
        System.out.println("==========soilTestList==========="+soilTestList.size());
        int counter = 0;

        for (SoilTest soilTest : soilTestList) {
            if(soilTest.getUserCrop() != null && !StringUtils.isEmpty(soilTest.getUserCrop().getUserLandDetailId())) {
                UserLandDetail landDetail = genericMongoTemplate.findById(soilTest.getUserCrop().getUserLandDetailId(), UserLandDetail.class);
                if (landDetail != null) {
                    Address address = new Address();
                    address.setState(landDetail.getState());
                    address.setCity(landDetail.getCity());
 //                   address.setTehsil(landDetail.getTehsil());
 //                   address.setBlock(landDetail.getBlock());
 //                   address.setVillageModel(landDetail.getVillageModel());
                    soilTest.setAddress(address);
                }
                mongoTemplate.save(soilTest);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }

    /**
     * add Soil Test in SoilTest Collection from Report History
     */
    @Override
    public void storeSoilTestFromReportHistory() {
        List<ReportHistory> reportHistoryList = mongoTemplate.findAll(ReportHistory.class);
        System.out.println("==========reportHistoryList.size()========" + reportHistoryList.size());
        int counter = 0;

        for (ReportHistory reportHistory : reportHistoryList) {
            SoilTest soilTest = reportHistory.getNpkRecommendation().getSoilTest();
            if(soilTest != null && !StringUtils.isEmpty(reportHistory.getNpkRecommendation().getKhasraNo()) && !StringUtils.isEmpty(reportHistory.getNpkRecommendation().getUserId())) {
                UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(reportHistory.getNpkRecommendation().getKhasraNo(),reportHistory.getNpkRecommendation().getUserId());
                if (landDetail != null) {
                    Address address = new Address();
                    address.setState(landDetail.getState());
                    address.setCity(landDetail.getCity());
  //                  address.setTehsil(landDetail.getTehsil());
  //                  address.setBlock(landDetail.getBlock());
  //                  address.setVillageModel(landDetail.getVillageModel());
                    soilTest.setAddress(address);
                    mongoTemplate.save(soilTest);
                    counter++;
                    System.out.println("=======Record============"+counter);
                }
            }
        }
    }

    @Override
    public void organiseVillageIdReportHistory() {
        List<ReportHistory> reportHistoryList = mongoTemplate.findAll(ReportHistory.class);
        System.out.println("==========reportHistoryList.size()========" + reportHistoryList.size());
        int counter = 0;

        for (ReportHistory reportHistory : reportHistoryList) {
            UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(reportHistory.getNpkRecommendation().getKhasraNo(),reportHistory.getNpkRecommendation().getUserId());
            if(landDetail != null) {
     //           reportHistory.getNpkRecommendation().setVillageId(landDetail.getVillageModel().getId());
                mongoTemplate.save(reportHistory);
                counter++;
                System.out.println("=======Record============" + counter);
            }
        }
    }

    @Override
    public void organiseFertilizerTypeAndCategoryTypeInReportHistory(String farmingType) {

        Criteria criteria = new Criteria();
        if(farmingType.equals("Organic")) {
            criteria = criteria.and("npkRecommendation.farmingType").is(Organic).and("organicReqFert.pomFertCals.categoryType").is(PatanjaliManure);
        } else if(farmingType.equals("INM_MIX")) {
            criteria = criteria.and("npkRecommendation.farmingType").is(INM_MIX).and("mixReqFert.organicReqFert.pomFertCals.categoryType").is(PatanjaliManure);
        }
        Query query = new Query(criteria);
        List<ReportHistory> reportHistoryList = mongoTemplate.find(query,ReportHistory.class);
        System.out.println("=======size============="+reportHistoryList.size());
        for(ReportHistory reportHistory : reportHistoryList){
            List<POMFertCal> pomFertCalList = new ArrayList<>();
            if(farmingType.equals("Organic")){
                pomFertCalList = reportHistory.getOrganicReqFert().getPomFertCals();
                for(POMFertCal pomFertCal : pomFertCalList){
                    if(pomFertCal.getCategoryType().equals(PatanjaliManure)){
                        pomFertCal.setCategoryType(Manure);
                    }
                }
            } else if(farmingType.equals("INM_MIX")){
                pomFertCalList = reportHistory.getMixReqFert().getOrganicReqFert().getPomFertCals();
                for(POMFertCal pomFertCal : pomFertCalList){
                    if(pomFertCal.getCategoryType().equals(PatanjaliManure)){
                        pomFertCal.setCategoryType(Manure);
                    }
                }
            }
            mongoTemplate.save(reportHistory);
        }
    }


    /**
     * add UserLandMapping Id Field in UserLandDetails
     */
    @Override
    public void addLandMapIdInLandDetails() {
        Query query = new Query(Criteria.where("landMapId").exists(false));
        List<UserLandDetail> all = mongoTemplate.find(query,UserLandDetail.class);
        System.out.println("=======all===size==========="+all.size());
        for (UserLandDetail userLandDetail: all) {
            UserLandMapping landMapping = agentService.getUserLandMapping(userLandDetail.getUserId(),userLandDetail.getKhasraNo());
            if(landMapping != null) {
                userLandDetail.setLandMapId(landMapping.getId());
                mongoTemplate.save(userLandDetail);
            }
        }
    }

    @Override
    public void updateCreatedByUserInUserAssignmentHistory() {
        Query query = new Query(Criteria.where("createdByUser").is("1212121212"));
        List<EmployeeAssignmentHistory> all = mongoTemplate.find(query,EmployeeAssignmentHistory.class);
        System.out.println("=======all===size==========="+all.size());
        int count = 0;
        for(EmployeeAssignmentHistory employeeAssignmentHistory :all){
            count++;
            employeeAssignmentHistory.setCreatedByUser("1001001001");
            mongoTemplate.save(employeeAssignmentHistory);
            System.out.println("=======count==========="+count);
        }
    }

    @Override
    public void removeEmployeeAssignmentHistory(String userId) {
        Query query = new Query(Criteria.where("toUser.$id").is(new ObjectId(userId)));
        mongoTemplate.findAllAndRemove(query, EmployeeAssignmentHistory.class);
    }

    @Override
    public void updateVillageIdToVillageModelInVillageAsset() {
        List<VillageAsset> villageAssetList = mongoTemplate.findAll(VillageAsset.class);
        System.out.println("==========villageAssetList.size()========" + villageAssetList.size());
        int counter = 0;

        for (VillageAsset villageAsset : villageAssetList) {
            if(!StringUtils.isEmpty(villageAsset.getVillageId())) {
                Village village = locationServices.getVillage(villageAsset.getVillageId());
                if (village != null) {
                    villageAsset.setVillage(village);
                    mongoTemplate.save(villageAsset);
                    counter++;
                    System.out.println("=======Record============" + counter);
                }
            }
        }
    }

    @Override
    public void storeAddressInFamilyMember() {
        Query query = new Query(Criteria.where("sameAddress").is(false).and("address").ne(false));
        List<FamilyMember> familyMemberList = mongoTemplate.find(query,FamilyMember.class);
        System.out.println("==========familyMemberHealthRecordList==========="+familyMemberList.size());
        int counter = 0;

        for (FamilyMember familyMember : familyMemberList) {
            User user = userService.findByid(familyMember.getFarmerId());
            if(user != null){
                familyMember.setSameAddress(true);
                familyMember.setAddress(user.getAddressModel());
                mongoTemplate.save(familyMember);
                counter++;
                System.out.println("=======Record============"+counter);
            }
        }
    }
}


