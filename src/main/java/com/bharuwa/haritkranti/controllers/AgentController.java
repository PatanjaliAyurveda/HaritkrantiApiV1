package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.payments.ChangeAssignmentReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.responseModels.FormChecks;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import io.swagger.annotations.ApiOperation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author harmanpreet
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AgentController extends BaseController {

    @RequestMapping(value = "/addUserForAgent", method = RequestMethod.POST)
    @ResponseBody
    public User addUserForAgent(@RequestParam String agentId, @RequestBody UserReqBody userReqBody) {
        return agentService.addUserForAgent(agentId, userReqBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/getAgentList", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAgentList() {
        return userService.getAgentList();
    }

    @RequestMapping(value = "/addUserLandDetail", method = RequestMethod.POST)
    public UserLandDetail addUserLandDetail(@RequestBody UserLandDetail userLandDetail) {
        return agentService.addUserLandDetail(userLandDetail);
    }

    @RequestMapping(value = "/getUserLandDetailByKhasraNo", method = RequestMethod.GET)
    @ResponseBody
    public UserLandDetail getUserLandDetailByKhasraNo(@RequestParam String khasraNo,
                                                      @RequestParam String userId) {
        return agentService.getUserLandDetailByKhasraNo(khasraNo, userId);
    }

    @RequestMapping(value = "/getUserCropByKhasraNo", method = RequestMethod.GET)
    @ResponseBody
    public UserCrop getUserCropByKhasraNo(@RequestParam String khasraNo) {
        return agentService.getUserCropByKhasraNo(khasraNo);
    }

    @RequestMapping(value = "/getUserCropListByKhasraNo", method = RequestMethod.GET)
    @ResponseBody
    public List<UserCrop> getUserCropListByKhasraNo(@RequestParam String khasraNo,
                                                    @RequestParam String userId) {
        return agentService.getUserCropListByKhasraNo(khasraNo, userId);
    }

    @RequestMapping(value = "/addFamilyMemberHealthDetails", method = RequestMethod.POST)
    public FamilyMemberHealthRecord addFamilyMemberHealthDetails(@RequestBody FamilyMemberHealthRecord familyMemberHealthRecord) {
        return userService.addFamilyMemberHealthDetails(familyMemberHealthRecord);
    }

    @RequestMapping(value = "/getFamilyMemberHealthDetails", method = RequestMethod.GET)
    public FamilyMemberHealthRecord getFamilyMemberHealthDetails(@RequestParam String userId) {
        return userService.getFamilyMemberHealthDetails(userId);
    }

//    @RequestMapping(value = "/getFamilyMembers", method = RequestMethod.GET)
//    public List<User> getFamilyMembers(@RequestParam String userId){
//        return userService.getFamilyMembers(userId);
//    }

    @RequestMapping(value = "/getAllFamilyMembersHealthDetails", method = RequestMethod.GET)
    public List<FamilyMemberHealthRecord> getAllFamilyMembersHealthDetails(@RequestParam String headUserId) {
        return userService.getAllFamilyMembersHealthDetails(headUserId);
    }

    @RequestMapping(value = "/getMedicalProblemList", method = RequestMethod.GET)
    public List<FamilyMemberHealthRecord.MedicalProblem> getMedicalProblemList() {
        return Arrays.asList(FamilyMemberHealthRecord.MedicalProblem.values());
    }

    @RequestMapping(value = "/getDisabilityList", method = RequestMethod.GET)
    public List<FamilyMemberHealthRecord.Disability> getDisabilityList() {
        return Arrays.asList(FamilyMemberHealthRecord.Disability.values());
    }

    @RequestMapping(value = "/addUserIdentificationDetails", method = RequestMethod.POST)
    public UserIdentificationDetails addUserIdentificationDetails(@RequestBody UserIdentificationDetails userIdentificationDetails) {
        return userService.addUserIdentificationDetails(userIdentificationDetails);
    }

    @RequestMapping(value = "/getUserIdentificationDetails", method = RequestMethod.GET)
    public UserIdentificationDetails getUserIdentificationDetails(@RequestParam String userId) {
        return userService.getUserIdentificationDetails(userId);
    }

    @RequestMapping(value = "/getFamilyMemberIdentificationDetails", method = RequestMethod.GET)
    public UserIdentificationDetails getFamilyMemberIdentificationDetails(@RequestParam String memberId) {
        return userService.getFamilyMemberIdentificationDetails(memberId);
    }

    @RequestMapping(value = "/userFormChecks", method = RequestMethod.GET)
    public FormChecks userFormChecks(@RequestParam String userId) {
        return agentService.userFormChecks(userId);
    }

    @RequestMapping(value = "/addUserEquipment", method = RequestMethod.POST)
    public UserEquipments addUserEquipment(@RequestBody UserEquipments userEquipments) {
        return userService.addUserEquipment(userEquipments);
    }

    @RequestMapping(value = "/getUserEquipments", method = RequestMethod.GET)
    public UserEquipments getUserEquipments(@RequestParam String userId) {
        return userService.getUserEquipments(userId);
    }

    @RequestMapping(value = "/addFarmDetails", method = RequestMethod.POST)
    public FarmDetails addFarmDetails(@RequestBody FarmDetails farmDetails) {
        return userService.addFarmDetails(farmDetails);
    }

    @RequestMapping(value = "/getFarmDetails", method = RequestMethod.GET)
    public List<FarmDetails> getFarmDetails(@RequestParam String userId, @RequestParam String animalType) {
        return userService.getFarmDetails(userId, animalType);
    }

    @RequestMapping(value = "/getFarmAnimalDetail", method = RequestMethod.GET)
    public FarmDetails getFarmAnimalDetail(@RequestParam String userId,
                                           @RequestParam String animalType,
                                           @RequestParam String fromDate,
                                           @RequestParam String toDate) throws ParseException {
        return userService.getFarmAnimalDetail(userId, animalType, fromDate, toDate);
    }

    @RequestMapping(value = "/getFarmDetailById", method = RequestMethod.GET)
    public FarmDetails getFarmDetailById(@RequestParam String farmDetailId) {
        return userService.getFarmDetailById(farmDetailId);
    }

    @RequestMapping(value = "/getSubsidies", method = RequestMethod.GET)
    public List<Subsidy> getSubsidies() {
        return userService.getSubsidies();
    }

    @RequestMapping(value = "/storeUserSubsidy", method = RequestMethod.POST)
    public UserSubsidy storeUserSubsidy(@RequestBody UserSubsidy userSubsidy) {
        return userService.storeUserSubsidy(userSubsidy);
    }

    @RequestMapping(value = "/getUserSubsidyDetails", method = RequestMethod.GET)
    public UserSubsidy getUserSubsidyDetails(@RequestParam String id) {
        return userService.getUserSubsidyDetails(id);
    }

    @RequestMapping(value = "/getUserSubsidies", method = RequestMethod.GET)
    public List<UserSubsidy> getUserSubsidies(@RequestParam String userId) {
        return userService.getUserSubsidies(userId);
    }

    @RequestMapping(value = "/addFamilyMemberNew", method = RequestMethod.POST)
    public FamilyMember addFamilyMemberNew(@RequestBody FamilyMember familyMember) {
        return userService.addFamilyMemberNew(familyMember);
    }

    @RequestMapping(value = "/getFamilyMembersNew", method = RequestMethod.GET)
    public List<FamilyMember> getFamilyMembersNew(@RequestParam String userId) {
        return userService.getFamilyMembersNew(userId);
    }

    @RequestMapping(value = "/getFamilyMemberById", method = RequestMethod.GET)
    public FamilyMember getFamilyMemberById(@RequestParam String id) {
        return userService.getFamilyMemberById(id);
    }

    // TODO: Need to implement the complete logic for checking the eligibility of the Khasra
    @RequestMapping(value = "/storeOrganicCertificate", method = RequestMethod.POST)
    public MessageResponse storeOrganicCertificate(@RequestBody OrganicCertificate organicCertificate) {
        return agentService.storeOrganicCertificate(organicCertificate);
    }

    @RequestMapping(value = "/getOrganicCertificate", method = RequestMethod.GET)
    public OrganicCertificate getOrganicCertificate(@RequestParam String userId,
                                                    @RequestParam String khasraNo) {
        return agentService.getOrganicCertificate(userId,khasraNo);
    }

    @RequestMapping(value = "/getOrganicCertificateById", method = RequestMethod.GET)
    public OrganicCertificate getOrganicCertificateById(@RequestParam String certificateId) {
        return agentService.getOrganicCertificateById(certificateId);
    }
    
    @RequestMapping(value = "/getOrganicCertificateList", method = RequestMethod.GET)
    public List<OrganicCertificate> getOrganicCertificateList(@RequestParam String userId) {
        return agentService.getOrganicCertificateList(userId);
    }
    
    //TODO:Find All Users Whose Khasra Land is/are Certified For Organic Farming
    @RequestMapping(value = "/getOrganicCertifiedKhasra" , method = RequestMethod.GET)
    public List<UserLandDetail> getOrganicCertifiedKhasra(@RequestParam String userId){
        return agentService.getOrganicCertifiedKhasra(userId);
    }

    @RequestMapping(value = "/addUserCropHistory", method = RequestMethod.POST)
    public UserCropHistory addUserCropHistory(@RequestBody UserCropHistory userCropHistory) {
        return agentService.addUserCropHistory(userCropHistory);
    }

    @RequestMapping(value = "/getUserCropHistory", method = RequestMethod.GET)
    public List<UserCropHistory> getUserCropHistory(@RequestParam String userId) {
        return agentService.getUserCropHistory(userId);
    }

    @RequestMapping(value = "/getUserCropHistoryById", method = RequestMethod.GET)
    public UserCropHistory getUserCropHistoryById(@RequestParam String id) {
        return agentService.getUserCropHistoryById(id);
    }

    @RequestMapping(value = "/getUserCropById", method = RequestMethod.GET)
    public UserCrop getUserCropById(@RequestParam String id) {
        return agentService.getUserCropById(id);
    }

    @RequestMapping(value = "/getSourceOfIrrigation", method = RequestMethod.GET)
    public List<UserLandDetail.SourceOfIrrigation> getSourceOfIrrigation() {
        return Arrays.asList(UserLandDetail.SourceOfIrrigation.values());
    }

    @RequestMapping(value = "/storeUserLandMapping", method = RequestMethod.POST)
    public UserLandMapping storeUserLandMapping(@RequestBody UserLandMapping userLandMapping) {
        return agentService.storeUserLandMapping(userLandMapping);
    }

    @RequestMapping(value = "/getUserLandMapping", method = RequestMethod.GET)
    public UserLandMapping getUserLandMapping(@RequestParam String userId, @RequestParam String KhasraNo) {
        return agentService.getUserLandMapping(userId, KhasraNo);
    }

    @RequestMapping(value = "/getUserLandMappingList", method = RequestMethod.GET)
    public List<UserLandMapping> getUserLandMappingList(@RequestParam String userId) {
        return agentService.getUserLandMappingList(userId);
    }

    @RequestMapping(value = "/getUserLandMappingById", method = RequestMethod.GET)
    public UserLandMapping getUserLandMappingById(@RequestParam String landMappingId) {
        return agentService.getUserLandMappingById(landMappingId);
    }

    @RequestMapping(value = "/remainingAreaForUserCrop", method = RequestMethod.GET)
    public BigDecimal remainingAreaForUserCrop(@RequestParam String userId, @RequestParam String KhasraNo) {
        return userService.remainingAreaForUserCrop(userId, KhasraNo);
    }

    @RequestMapping(value = "/addMilkingAnimalDetails", method = RequestMethod.POST)
    public MilkingAnimalDetails addMilkingAnimalDetails(@RequestBody MilkingAnimalDetails milkingAnimalDetails) {
        return userService.addMilkingAnimalDetails(milkingAnimalDetails);
    }

    @RequestMapping(value = "/getMilkingAnimalDetails", method = RequestMethod.GET)
    public List<MilkingAnimalDetails> getMilkingAnimalDetails(@RequestParam String userId, @RequestParam String animalType) {
        return userService.getMilkingAnimalDetails(userId, animalType);
    }

    @RequestMapping(value = "/getAnimalDetail", method = RequestMethod.GET)
    public MilkingAnimalDetails getAnimalDetail(@RequestParam String userId,
                                                @RequestParam String animalType,
                                                @RequestParam String fromDate,
                                                @RequestParam String toDate) throws ParseException {
        return userService.getAnimalDetail(userId, animalType, fromDate, toDate);
    }

    @RequestMapping(value = "/getMilkingAnimalDetailById", method = RequestMethod.GET)
    public MilkingAnimalDetails getMilkingAnimalDetailById(@RequestParam String id) {
        return userService.getMilkingAnimalDetailById(id);
    }

    @RequestMapping(value = "/getMilkingAnimalList", method = RequestMethod.GET)
    public static List<MilkingAnimalDetails.MilkingAnimalType> getMilkingAnimalList() {
        return Arrays.asList(MilkingAnimalDetails.MilkingAnimalType.values());
    }
    @RequestMapping(value = "/getMilkingAnimalBreedList", method = RequestMethod.GET)
    public List<MilkingAnimalDetails.AnimalBreed> getMilkingAnimalBreedList() {
        return Arrays.asList(MilkingAnimalDetails.AnimalBreed.values());
    }

    @RequestMapping(value = "/getCropSeasonsList", method = RequestMethod.GET)
    public List<Crop.CropSeason> getCropSeasonsList() {
        return Arrays.asList(Crop.CropSeason.values());
    }

    @RequestMapping(value = "/getRemainingLandSize", method = RequestMethod.GET)
    public BigDecimal getRemainingLandSize(@RequestParam String userId,
                                           @RequestParam String khasraNo) {
        return userService.getRemainingLandSize(userId,khasraNo);
    }

    @RequestMapping(value = "/storeVillageAsset", method = RequestMethod.POST)
    public VillageAsset storeVillageAsset(@RequestBody VillageAsset villageAsset) {
        return agentService.storeVillageAsset(villageAsset);
    }

    @RequestMapping(value = "/getVillageAssetbyId",method = RequestMethod.GET)
    public VillageAsset getVillageAssetbyId(@RequestParam String agentId,@RequestParam String villageId) {
        return agentService.getVillageAssetbyId(agentId,villageId);
    }

    @RequestMapping(value = "/getOrganicLandUserList",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getOrganicLandUserList (@RequestParam String agentId){
        return agentService.getOrganicLandUserList(agentId);
    }

    @RequestMapping(value = "/generateCertificateCode",method = RequestMethod.GET)
    @ResponseBody
    public String generateCertificateCode (@RequestParam String agentId){
        return agentService.generateCertificateCode(agentId);
    }

    @RequestMapping(value = "/storeVillageMapping", method = RequestMethod.POST)
    public VillageMapping storeVillageMapping(@RequestBody VillageMapping villageMapping) {
        return agentService.storeVillageMapping(villageMapping);
    }

    @RequestMapping(value = "/getVillageMapping", method = RequestMethod.GET)
    public VillageMapping getVillageMapping(@RequestParam String agentId, @RequestParam String villageId) {
        return agentService.getVillageMapping(agentId, villageId);
    }

    @RequestMapping(value = "/getHoneyFlavoursList", method = RequestMethod.GET)
    public List<FarmDetails.HoneyFlavour> getHoneyFlavoursList() {
        return Arrays.asList(FarmDetails.HoneyFlavour.values());
    }

    // Get List of farmers under a particular Agent & Village
    @RequestMapping (value = "/getFarmerListOfVillage" , method = RequestMethod.GET)
    public List<User> getFarmerListOfVillage(@RequestParam String agentId , @RequestParam String villageId) throws IOException {
        return agentService.getFarmerListOfVillage(agentId , villageId);
    }

    @RequestMapping(value = "/getFamilyMemberHealthDetail", method = RequestMethod.GET)
    public List<FamilyMemberHealthRecord> getFamilyMemberHealthDetail(@RequestParam String userId) {
        return userService.getFamilyMemberHealthDetail(userId);
    }

    @RequestMapping(value = "/getHealthDetailById" , method = RequestMethod.GET)
    public FamilyMemberHealthRecord getHealthDetailById(@RequestParam String id) {
        return userService.getHealthDetailById(id);
    }

    @ApiOperation(value = "Enable or Disable Manager's Login for Agent Role")
    @RequestMapping(value = "/enableAgentRole", method = RequestMethod.GET)
    @ResponseBody
    public User enableAgentRole(@RequestParam String userId, boolean status) {
        return agentService.enableAgentRole(userId, status);
    }

    /**
     * change the createdByUserId from fromUserId to toUserId
     * @param changeAssignmentReqBody
     * @return
     */
    @RequestMapping(value = "/changeUserAssignment",method = RequestMethod.PUT)
    @ResponseBody
    public String changeUserAssignment(@RequestBody ChangeAssignmentReqBody changeAssignmentReqBody){
        return agentService.changeUserAssignment(changeAssignmentReqBody.getFromUserId(),changeAssignmentReqBody.getToUserId());
    }

    /**
     * Get All the Users Under Your deactivated Users By User-Type
     * @param userId
     * @param userType
     * @param managerType
     * @return
     */
    @RequestMapping(value = "/getUsersUnderDeactivatedUsersByMasterUserId",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersUnderDeactivatedUsersByMasterUserId(@RequestParam String userId,
                                                                  @RequestParam String userType,
                                                                  @Nullable @RequestParam String managerType){
        return agentService.getUsersUnderDeactivatedUsersByMasterUserId( userId, userType, managerType);
    }

    /**
     * search By VillageId and KhasraNo
     * @param khasraNo
     * @param locationId is villageId
     * @return
     */
    @RequestMapping(value = "/searchLandDetail",method = RequestMethod.GET)
    public List<UserLandDetail> searchLandDetail (@RequestParam String locationId, @RequestParam String khasraNo) {
        return agentService.searchLandDetail(locationId,khasraNo);
    }
    
    @RequestMapping(value = "/getFarmerAllLandDetail",method = RequestMethod.GET)
    public List<UserLandDetail> getFarmerAllLandDetail (@RequestParam String userId) {
        return agentService.getFarmerAllLandDetail(userId);
    }

    @RequestMapping(value = "/getUserSubsidyByName", method = RequestMethod.GET)
    public Subsidy getUserSubsidyByName(@RequestParam String name) {
        return userService.getUserSubsidyByName(name);
    }
    
    @RequestMapping(value = "/addAnndataCropCategory", method = RequestMethod.GET)
    public void addAnndataCropCategory() {
    	try {
			FileInputStream file = new FileInputStream(new File("D:/ExcelSheetForUploading/Annadata_files_hindi.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            PrimaryCategoryInHindi primaryCategory = new PrimaryCategoryInHindi();
            Category category = new Category();
            Variety variety;
            List<PrimaryCategoryInHindi> primaryCategoryList = new ArrayList<PrimaryCategoryInHindi>();
            List<Category> categoryList; 
            List<Variety> varietyList;
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                BasicDBObject document = new BasicDBObject(); 
                if(row.getCell(1).toString()!="") {
                	primaryCategory = new PrimaryCategoryInHindi();
                	primaryCategory.setPrimaryCategoryName(row.getCell(1).toString().trim());
                	categoryList = new ArrayList<Category>();
                	primaryCategory.setCategoryList(categoryList);
                	primaryCategoryList.add(primaryCategory);
                }
                if(row.getCell(3).toString()!="") {
                	category = new Category();
                	category.setCategoryName(row.getCell(3).toString().trim());
                	varietyList = new ArrayList<Variety>();
                	category.setVarietyList(varietyList);
                	primaryCategory.getCategoryList().add(category);
                }
                if(row.getCell(5).toString()!="") {
                	variety = new Variety();
                	variety.setVarietyName(row.getCell(5).toString().trim());
                	category.getVarietyList().add(variety);
                }         
                
            }
            for(PrimaryCategoryInHindi hindi:primaryCategoryList) {
            	agentService.save(hindi);
            }
            System.out.println("Data is inserted "+primaryCategoryList.size()+"  "+primaryCategoryList.get(0).getCategoryList().size()+"");
            file.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
    
}
