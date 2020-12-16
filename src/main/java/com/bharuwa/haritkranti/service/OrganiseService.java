package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.User;

import java.util.List;

/**
 * @author anuragdhunna
 */
public interface OrganiseService {

    void assignAgentsToManager(String phoneNumber, String stateName, String cityName);

    void createdViaSignUpToAgent();

    void addCropGrouptoUserCrop();

    String organiseBeekeepingDetailsFromFarmDetails();

    String assignUserCodeFarmers();

    String removeAgentFarmer(String agentId);

    String removeFarmerById(String agentId, String farmerId);

    String organiseCreatedByUserIdforAgentFarmers(String agentId);

    List<User> getAgentFarmersNotHavingAgentId(String agentId);

    void organiseUserSchemes();

    void organiseHorticultureData();

    void organiseUserCrop();

    void organiseFarmDetail();

    void organiseMilkingAnimalDetail();

    void organiseUserSubsidy();

    void organiseHealthRecords();

    void organiseUserEquipment();

    void storeAddressInSoilTest();

    void storeSoilTestFromReportHistory();

    void organiseVillageIdReportHistory();

    void organiseFertilizerTypeAndCategoryTypeInReportHistory(String farmingType);

    void addLandMapIdInLandDetails();

    void updateCreatedByUserInUserAssignmentHistory();

    void removeEmployeeAssignmentHistory(String userId);

    void updateVillageIdToVillageModelInVillageAsset();

    void storeAddressInFamilyMember();

}
