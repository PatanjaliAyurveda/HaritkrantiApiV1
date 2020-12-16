package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.requestModels.AssignLocationRequestBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.responseModels.AssignLocation;
import com.bharuwa.haritkranti.models.responseModels.FormChecks;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author harmanpreet
 */
public interface AgentService {

    void saveUserForAgent(UserAgent userAgent);

    UserLandDetail addUserLandDetail(UserLandDetail userLandDetail);

    UserLandDetail getUserLandDetailByKhasraNo(String khasraNo, String userId);

    UserCrop getUserCropByKhasraNo(String khasraNo);

    User addUserForAgent(String agentId, UserReqBody userReqBody);

    List<UserLandDetail> getUserLandDetails(String userId);

    List<UserCrop> getUserCrops(String userId, @Nullable String cropGrouptype);

    List<User> getAgentUsersList(String agentId);

    FormChecks userFormChecks(String userId);

    Page<User> getAllAgents(String userId, int page, int size);

    MessageResponse storeOrganicCertificate(OrganicCertificate organicCertificate);

    OrganicCertificate getOrganicCertificate(String userId, String khasraNo);

    List<User> getAgentUsers(String agentId);

    UserCropHistory addUserCropHistory(UserCropHistory userCropHistory);

    List<UserCropHistory> getUserCropHistory(String userId);

    UserCropHistory getUserCropHistoryById(String id);

    List<User> searchAgents(String text);

    Page<User> searchUsers(String userId, String searchText, String userType, String managerType, String locationId, String locationType, Boolean active, Boolean searchAllUser, int page, int size);

    List<User> searchFarmers(String text);

    List<UserCrop> getUserCropListByKhasraNo(String khasraNo, String userId);

    UserCrop getUserCropById(String id);

    UserLandMapping storeUserLandMapping(UserLandMapping userLandMapping);

    UserLandMapping getUserLandMapping(String userId, String khasraNo);

    List<UserLandMapping> getUserLandMappingList(String userId);

    UserLandMapping getUserLandMappingById(String landMappingId);

    OrganicCertificate getOrganicCertificateById(String certificateId);

    void sendAppLink();

    MessageResponse assignVillageToAgent(String agentId, String villageId);

    AgentVillage getAgentVillage(String id);

    List<Village> getAgentVillageList(String agentId);

    VillageAsset storeVillageAsset(VillageAsset villageAsset);

    List<UserLandDetail> getOrganicCertifiedKhasra(String userId);

    List<User> getOrganicLandUserList(String agentId);

    VillageAsset getVillageAssetbyId(String agentId, String villageId);

    String generateCertificateCode(String agentId);

    AssignLocation assignLocToManager(AssignLocationRequestBody assignLocation);

    List<AssignLocation> getAssignLocsOfManager(String userId);

    VillageMapping storeVillageMapping(VillageMapping villageMapping);

    VillageMapping getVillageMapping(String agentId, String villageId);

    Page<User> searchParentManager(String userId, String searchText, int page, int size);

    List<User> getFarmerListOfVillage(String agentId, String villageId);

    Page<User> getAgentUsersByPagination(String agentId, int page, int size);

    User enableAgentRole(String userId, boolean status);

    String changeUserAssignment(String fromUserId, String toUserId);

    List<User> getUsersUnderDeactivatedUsersByMasterUserId(String userId,String userType, String managerType);

    List<UserLandDetail> searchLandDetail(String locationId, String khasraNo);

    AgentVillage findAgentByAssignVillage(String agentId, String villageId);
    
    public List<UserLandDetail> getFarmerAllLandDetail(String userId);
    
    public List<OrganicCertificate> getOrganicCertificateList(String userId);
    
    public void save(PrimaryCategoryInHindi primaryCategoryInHindi);
    
}



