package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.newmodels.AppLoginTable;
import com.bharuwa.haritkranti.models.requestModels.ResetPassReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserStatus;
import com.bharuwa.haritkranti.models.responseModels.AssignLocation;
import com.bharuwa.haritkranti.models.responseModels.CropArea;
import com.bharuwa.haritkranti.models.responseModels.CropPercentage;
import com.bharuwa.haritkranti.models.responseModels.LandTypePercentage;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public interface UserService {

    User getUser(String username);
    User saveUser(User user);
    public RegistrationOtp save(RegistrationOtp otp);

    User getUserByEmail(String email);
    User getUserByPhoneNum(String phoneNumber);

    User findByid(String id);
    boolean exist(String id);

    Set<String> getLandKhasraNos(String userId);

//    Set<String> getVillageList(String userId);

//    Set<String> getTehsilList(String userId);

//    User addFamilyMember(String userId, User user);
//    List<User> getFamilyMembers(String userId);

    Farm addFarmDetail(Farm farm);

    List<User> getAgentList();

    Page<User> getAllUsers(int page, int size);

    FamilyMemberHealthRecord addFamilyMemberHealthDetails(FamilyMemberHealthRecord familyMemberHealthRecord);

    FamilyMemberHealthRecord getFamilyMemberHealthDetails(String userId);

    List<FamilyMemberHealthRecord> getAllFamilyMembersHealthDetails(String headUserId);

    UserIdentificationDetails addUserIdentificationDetails(UserIdentificationDetails userIdentificationDetails);

    UserIdentificationDetails getUserIdentificationDetails(String userId);

    UserEquipments addUserEquipment(UserEquipments userEquipments);

    UserEquipments getUserEquipments(String userId);

    MessageResponse uploadCropImages(MultipartFile file, String cropId,String khasraNo, String userId, String agentId);

    FarmDetails addFarmDetails(FarmDetails farmDetails);

    List<FarmDetails> getFarmDetails(String userId, String animalType);

    UserSubsidy storeUserSubsidy(UserSubsidy userSubsidy);

    List<Subsidy> getSubsidies();

    UserSubsidy getUserSubsidyDetails(String userId);

    void storeSubsidy(Subsidy subsidy);

    List<UserSubsidy> getUserSubsidies(String userId);

    FamilyMember addFamilyMemberNew(FamilyMember familyMember);

    List<FamilyMember> getFamilyMembersNew(String userId);

    FamilyMember getFamilyMemberById(String id);

    void organiseFamilyMember(String phoneNumber, String stateName, String cityName);

    UserIdentificationDetails getFamilyMemberIdentificationDetails(String memberId);

    void organiseUserCrop();

    FarmDetails getFarmDetailById(String farmDetailId);

    void removeUserAccountOfAgent(String phoneNumber);

    void addLandDetailInUserCrop();

    List<CropArea> getCropsAreaByState(String stateId, String season);

    List<CropArea> getCropsAreaCountry(String season);

    List<CropArea> getCropsAreaByDistrict(String districtId, String season);

    BigDecimal remainingAreaForUserCrop(String userId, String khasraNo);

    List<CropPercentage> getCropsPercentageCountry(String season);

    List<CropPercentage> getCropsPercentageByState(String stateId, String season);

    List<CropPercentage> getCropsPercentageByDistrict(String districtId, String season);

    LandTypePercentage getLandTypesAreaPercentageCountry();

    LandTypePercentage getLandTypesAreaPercentageStates(String stateId);

    LandTypePercentage getLandTypesAreaPercentageDistricts(String districtId);

    MilkingAnimalDetails addMilkingAnimalDetails(MilkingAnimalDetails milkingAnimalDetails);

    List<MilkingAnimalDetails> getMilkingAnimalDetails(String userId, String animalType);

    MilkingAnimalDetails getMilkingAnimalDetailById(String id);

       List<User> getFarmersByDistrict(String districtId);

//    List<User> getDairyFarmerListByDistrict(String districtId);

    List<User> getDairyFarmerListByLocation(String locationType, String locationId);

    long getDairyFarmerCountByLocation(String locationType, String locationId);

    void addCropSeasonInUserCrop();

    String uploadImageS3(MultipartFile file);

    String getCropImages(String cropId, String khasraNo, String userId);

    String getCropImageByUserId(String userId);

    void organiseUserAddress();

    String uploadUserProfilePic(MultipartFile file, String userId);

    BigDecimal getRemainingLandSize(String userId, String khasraNo);

    MilkingAnimalDetails getAnimalDetail(String userId, String animalType, String fromDate, String toDate) throws ParseException;

    FarmDetails getFarmAnimalDetail(String userId, String animalType, String fromDate, String toDate) throws ParseException;

    User getAgentByUserId(String userId);

    MessageResponse resetPassword(ResetPassReqBody resetPassReqBody);

    Page<User> getManagers(String managerType, String userId, int page, int size);

    FarmerExtraDetails storeFarmerExtraDetail(FarmerExtraDetails farmerExtraDetails);

    FarmerExtraDetails getFarmerExtraDetail(String farmerId);

    String setUserStatus(UserStatus userStatus, String loggedInUserName);

    UserSchemes saveUserScheme(UserSchemes userSchemes);

    List<UserSchemes> getUserSchemesList(String userId);

    MessageResponse assignAgentRoleToManagers();

    MessageResponse updateManagerPassword();

    UserCertificate saveUserCertificate(UserCertificate userCertificate);

    UserCertificate getUserCertificate(String userId, String khasraNo);

    AssignLocation setManagersLocationStatus(String assignLocationId, boolean status);

    String assignUserCodeToUsers();

    List<User> getAdminUsers();

    List<FamilyMemberHealthRecord> getFamilyMemberHealthDetail(String userId);

    FamilyMemberHealthRecord getHealthDetailById(String id);

    Page<User> getFarmersByBlock(String blockId, int page, int size);

    Subsidy getUserSubsidyByName(String name);
    
    public RegistrationOtp getRegisteredOtp(String phoneNumber,String regOtp);
    
    public MessageResponse updateFarmerPassword(User user);

    public void saveAppLoginOtp(AppLoginTable loginOtp);
    
    public AppLoginTable getAppLoginOtp(String phoneNumber);
    
    public List<String> getKhasraList(String state,String district,String tehsil,String block,String village,String language);
    
//    void organiseUserLandDetails();5d288c6d6511224ceb89c067
    
    public List<String> getCropName(String khasraNumber);

}
