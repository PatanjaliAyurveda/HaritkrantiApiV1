package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.AgentVillage;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.requestModels.AssignLocationRequestBody;
import com.bharuwa.haritkranti.models.requestModels.ResetPassReqBody;
import com.bharuwa.haritkranti.models.responseModels.*;
import com.bharuwa.haritkranti.models.ReportHistory;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.msg91.Msg91Services;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bharuwa.haritkranti.utils.Constants.FARMER_APP_LINK;

/**
 * @author harmanpreet
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AdminController extends BaseController {

    @RequestMapping(value = "/getAgentUsers",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAgentUsers(@RequestParam String agentId){
        return agentService.getAgentUsers(agentId);
    }

    @RequestMapping(value = "/getAgentUsersByPagination",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getAgentUsersByPagination(@RequestParam String agentId,
                                                @RequestParam(value = "page", defaultValue = "0")int page,
                                                @RequestParam(value = "size", defaultValue = "20")int size){
        return agentService.getAgentUsersByPagination(agentId,page,size);
    }

    @RequestMapping(value = "/getAllAgents",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getAllAgents(@RequestParam String userId,
                                   @RequestParam(value = "page", defaultValue = "0")int page,
                                   @RequestParam(value = "size", defaultValue = "20")int size) {
        return agentService.getAllAgents(userId, page, size);
    }

    @RequestMapping(value = "/cropIdtoCropModel",method = RequestMethod.GET)
    @ResponseBody
    public void cropIdtoCropModel(){
        landService.cropIdtoCropModel();
    }

    @ResponseBody
    public List<User> searchAgents(@RequestParam String text){
        return agentService.searchAgents(text);
    }

    @ResponseBody
    public List<User> searchFarmers(@RequestParam String text){
        return agentService.searchFarmers(text);
    }

    /**
     * searchUsers
     * @param userId: Login User Id
     * @param searchText : Text to be searched either phone number or name
     * @param userType :RoleName- Agent , Manager , Admin
     * @param locationId : Id of the required location (State/District/Block/Tehsil/Village)
     * @param locationType : Type can be STATE/DISTRICT/BLOCK/TEHSIL/VILLAGE
     * @return List of users will be returnedcriteria = criteria.and("status").is(Approved);
     */
    @RequestMapping(value = "/searchUsers",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> searchUsers(@RequestParam String userId,
                                  @RequestParam String searchText,
                                  @RequestParam String userType,
                                  @Nullable @RequestParam String managerType,
                                  @Nullable @RequestParam String locationId,
                                  @Nullable @RequestParam String locationType,
                                  @Nullable @RequestParam Boolean active,
                                  @Nullable @RequestParam Boolean searchAllUser, //TODO : need to remove after bug fix
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size){
        return agentService.searchUsers(userId, searchText, userType, managerType ,locationId, locationType, active,searchAllUser, page, size);
    }

    @RequestMapping(value = "/organiseUserCrop",method = RequestMethod.GET)
    @ResponseBody
    public void organiseUserCrop(){
        userService.organiseUserCrop();
    }

    @RequestMapping(value = "/addLandDetailInUserCrop",method = RequestMethod.GET)
    @ResponseBody
    public void addLandDetailInUserCrop(){
        userService.addLandDetailInUserCrop();
    }

    @RequestMapping(value = "/removeUserAccountOfAgent",method = RequestMethod.GET)
    @ResponseBody
    public void removeUserAccountOfAgent(@RequestParam String phoneNumber){
        userService.removeUserAccountOfAgent(phoneNumber);
    }

    @RequestMapping(value = "/getCropsAreaCountry",method = RequestMethod.GET)
    @ResponseBody
    public List<CropArea> getCropsAreaCountry(@Nullable @RequestParam String season){
        return userService.getCropsAreaCountry(season);
    }

    @RequestMapping(value = "/getCropsAreaByState",method = RequestMethod.GET)
    @ResponseBody
    public List<CropArea> getCropsAreaByState(@RequestParam String stateId,
                                              @Nullable @RequestParam String season){
        return userService.getCropsAreaByState(stateId,season);
    }

    @RequestMapping(value = "/getCropsAreaByDistrict",method = RequestMethod.GET)
    @ResponseBody
    public List<CropArea> getCropsAreaByDistrict(@RequestParam String districtId,
                                                 @Nullable @RequestParam String season){
        return userService.getCropsAreaByDistrict(districtId,season);
    }

    @RequestMapping(value = "/getCropsPercentageCountry",method = RequestMethod.GET)
    @ResponseBody
    public List<CropPercentage> getCropsPercentageCountry(@Nullable @RequestParam String season){
        return userService.getCropsPercentageCountry(season);
    }

    @RequestMapping(value = "/getCropsPercentageByState",method = RequestMethod.GET)
    @ResponseBody
    public List<CropPercentage> getCropsPercentageByState(@RequestParam String stateId,
                                                          @Nullable @RequestParam String season){
        return userService.getCropsPercentageByState(stateId,season);
    }

    @RequestMapping(value = "/getCropsPercentageByDistrict",method = RequestMethod.GET)
    @ResponseBody
    public List<CropPercentage> getCropsPercentageByDistrict(@RequestParam String districtId,
                                                             @Nullable @RequestParam String season){
        return userService.getCropsPercentageByDistrict(districtId,season);
    }

    @RequestMapping(value = "/getLandTypesAreaPercentageCountry",method = RequestMethod.GET)
    @ResponseBody
    public LandTypePercentage getLandTypesAreaPercentageCountry(){
        return userService.getLandTypesAreaPercentageCountry();
    }

    @RequestMapping(value = "/getLandTypesAreaPercentageStates",method = RequestMethod.GET)
    @ResponseBody
    public LandTypePercentage getLandTypesAreaPercentageStates(@RequestParam String stateId){
        return userService.getLandTypesAreaPercentageStates(stateId);
    }

    @RequestMapping(value = "/getLandTypesAreaPercentageDistricts",method = RequestMethod.GET)
    @ResponseBody
    public LandTypePercentage getLandTypesAreaPercentageDistricts(@RequestParam String districtId){
        return userService.getLandTypesAreaPercentageDistricts(districtId);
    }

    @RequestMapping(value = "/getFarmersByDistrict",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getFarmersByDistrict(@RequestParam String districtId){
        return userService.getFarmersByDistrict(districtId);
    }

    @RequestMapping(value = "/organiseCityCropSoil",method = RequestMethod.GET)
    @ResponseBody
    public void organiseCityCropSoil() {
        landService.organiseCityCropSoil();
    }

    @RequestMapping(value = "/testLink",method = RequestMethod.GET)
    @ResponseBody
    public String testLink(@RequestParam String phoneNumber){
        // Send Farmer App Link
        String appLinkMessage = "प�?रिय किसान, आपका अन�?नदाता �?प में स�?वागत है। \n" +
                "\n" +
                "कृपया नीचे दि�? ग�? लिंक पे क�?लिक करके अपने मोबाइल पर �?प इनस�?टॉल करें " + FARMER_APP_LINK;
        String response = null;
        try {
            response = Msg91Services.sendFarmerAppLinkMsg(appLinkMessage, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(response);
        }
        return response;
    }

    @RequestMapping(value = "/sendAppLink",method = RequestMethod.GET)
    @ResponseBody
    public void sendAppLink() {
        agentService.sendAppLink();
    }

    /**
     * get Dairy Farmers List BY Locations
     * @param locationType
     * @param locationId
     * @return
     */
    @RequestMapping(value = "/getDairyFarmerListByLocation",method = RequestMethod.GET)
    @ResponseBody
    List<User> getDairyFarmerListByLocation(@RequestParam String locationType,
                                          @RequestParam String locationId){
        return userService.getDairyFarmerListByLocation(locationType, locationId);
    }

    @RequestMapping(value = "/addCropSeasonInUserCrop",method = RequestMethod.POST)
    public void addCropSeasonInUserCrop(){
        userService.addCropSeasonInUserCrop();
    }

    @RequestMapping(value = "/getReportHistoryByKhasraAndUserId",method = RequestMethod.GET)
    ReportHistory getReportHistoryByKhasraAndUserId(@RequestParam String khasraNo, @RequestParam String userId){
        return reportHistoryService.getReportHistoryByKhasraAndUserId(khasraNo, userId);
    }

    @RequestMapping(value = "/getReportHistoryListByKhasraAndUserId",method = RequestMethod.GET)
    List<ReportHistory> getReportHistoryListByKhasraAndUserId(@RequestParam String khasraNo, @RequestParam String userId){
        return reportHistoryService.getReportHistoryListByKhasraAndUserId(khasraNo, userId);
    }

    @RequestMapping(value = "removeDuplicateRecordsCityCropSoil",method = RequestMethod.GET)
    public void removeDuplicateRecordsCityCropSoil(){
        landService.removeDuplicateRecordsCityCropSoil();
    }

    @RequestMapping(value = "/organiseUserAddress",method = RequestMethod.GET)
    @ResponseBody
    public void organiseUserAddress() {
        userService.organiseUserAddress();
    }

    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    @ResponseBody
    public MessageResponse resetPassword (@RequestBody ResetPassReqBody resetPassReqBody){
        return userService.resetPassword(resetPassReqBody);
    }

    /**
     * Get List of Managers
     * @param managerType roleName
     * @param userId logged in user
     * @param page page number
     * @param size page size
     * @return list of managers
     */
    @RequestMapping(value = "/getManagers",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getManagers(@Nullable @RequestParam String managerType,
                                  @RequestParam String userId,
                                  @RequestParam(value = "page",defaultValue = "0")int page,
                                  @RequestParam(value = "size",defaultValue = "20")int size){
        return userService.getManagers(managerType,userId,page,size);
    }

    @RequestMapping(value = "/assignVillageToAgent",method = RequestMethod.POST)
    @ResponseBody
    public MessageResponse assignVillageToAgent (@RequestParam String agentId,
                                                 @RequestParam String villageId){
        return agentService.assignVillageToAgent(agentId,villageId);
    }

    @RequestMapping(value = "/getAgentVillageList",method = RequestMethod.GET)
    @ResponseBody
    public List<Village> getAgentVillageList (@RequestParam String agentId){
        return agentService.getAgentVillageList(agentId);
    }

    @RequestMapping(value = "/getAgentVillage",method = RequestMethod.GET)
    @ResponseBody
    public AgentVillage getAgentVillage (@RequestParam String id){
        return agentService.getAgentVillage(id);
    }

    @RequestMapping(value = "/assignAgentRoleToManagers",method = RequestMethod.PUT)
    @ResponseBody
    public MessageResponse assignAgentRoleToManagers (){
        return userService.assignAgentRoleToManagers();
    }

    @RequestMapping(value = "/updateManagerPassword",method = RequestMethod.PUT)
    @ResponseBody
    public MessageResponse updateManagerPassword (){
        return userService.updateManagerPassword();
    }

    /**
     * Assign Location to Managers(State, District, Agent Manager)
     * @param assignLocationRequestBody
     * @return
     */
    @RequestMapping(value = "/assignLocToManager", method = RequestMethod.POST)
    @ResponseBody
    public AssignLocation assignLocToManager(@RequestBody AssignLocationRequestBody assignLocationRequestBody) {
        return agentService.assignLocToManager(assignLocationRequestBody);
    }

    /**
     * To Retrieve the List of Assigned Locations of Manager
     * @param userId is the ManagerId
     * @return List of Assigned Locations
     */
    @RequestMapping(value = "/getAssignLocsOfManager",method = RequestMethod.GET)
    @ResponseBody
    public List<AssignLocation> getAssignLocsOfManager(@RequestParam String userId){
        return agentService.getAssignLocsOfManager(userId);
    }

    @RequestMapping(value = "/setManagersLocationStatus", method = RequestMethod.PUT)
    @ResponseBody
    public AssignLocation setManagersLocationStatus(@RequestParam String assignLocationId, @RequestParam boolean status) {
        return userService.setManagersLocationStatus(assignLocationId,status);
    }

    @RequestMapping(value = "/organiseAgents", method = RequestMethod.GET)
    @ResponseBody
    public void organiseAgents(@RequestParam String phoneNumber, @RequestParam String stateName, @RequestParam String cityName){
        userService.organiseFamilyMember(phoneNumber, stateName, cityName);
    }

    @RequestMapping(value = "/assignUserCodeToUsers", method = RequestMethod.PUT)
    @ResponseBody
    public String assignUserCodeToUsers() {
        return userService.assignUserCodeToUsers();
    }

    @RequestMapping(value = "/searchParentManager",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> searchParentManager(@RequestParam String userId,
                                          @RequestParam String searchText,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "20") int size){
        return agentService.searchParentManager(userId, searchText, page, size);
    }

    @RequestMapping(value = "/getFarmersByBlock",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getFarmersByBlock(@RequestParam String blockId,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "20") int size){
        return userService.getFarmersByBlock(blockId,page,size);
    }

    @RequestMapping(value = "/findAgentByAssignVillage",method = RequestMethod.GET)
    @ResponseBody
    public AgentVillage findAgentByAssignVillage (@RequestParam String agentId,
                                                 @RequestParam String villageId){
        return agentService.findAgentByAssignVillage(agentId,villageId);
    }

}