package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.UserLandDetail;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import io.swagger.annotations.ApiOperation;
import com.bharuwa.haritkranti.models.ReportHistory;
import com.bharuwa.haritkranti.repositories.ReportHistoryRepo;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class OrganiseController extends BaseController {

    private final MongoTemplate mongoTemplate;

    @Autowired
    ReportHistoryRepo reportHistoryRepo;


    public OrganiseController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping(value = "/assignAgentsToManager",method = RequestMethod.GET)
    @ResponseBody
    public void assignAgentsToManager(@RequestParam String phoneNumber,
                                      @RequestParam String stateName,
                                      @RequestParam String cityName){
        organiseService.assignAgentsToManager(phoneNumber, stateName, cityName);
    }

    @RequestMapping(value = "createdViaSignUpToAgent",method = RequestMethod.GET)
    @ResponseBody
    public void createdViaSignUpToAgent(){
        organiseService.createdViaSignUpToAgent();
    }

    @ApiOperation(value = "Add CropGroup to UserCrop to Filter between Crop and Fruit")
    @RequestMapping(value = "addCropGrouptoUserCrop",method = RequestMethod.GET)
    @ResponseBody
    public void addCropGrouptoUserCrop(){
        organiseService.addCropGrouptoUserCrop();
    }

    @RequestMapping(value = "/organiseBeekeepingDetailsFromFarmDetails",method = RequestMethod.GET)
    @ResponseBody
    public String organiseBeekeepingDetailsFromFarmDetails(){
        return organiseService.organiseBeekeepingDetailsFromFarmDetails();
    }

    @RequestMapping(value = "/assignUserCodeFarmers",method = RequestMethod.GET)
    @ResponseBody
    public String assignUserCodeFarmers(){
        return organiseService.assignUserCodeFarmers();
    }

    /**
     * add soilReportNumber in Report History
     */
    @RequestMapping(value = "/generateSoilAndFertilizerReportNumber",method = RequestMethod.GET)
    @ResponseBody
    public MessageResponse generateSoilAndFertilizerReportNumber() {
        Query query = new Query(Criteria.where("soilReportNumber").is(null));
        List<ReportHistory> reportHistoryList = mongoTemplate.find(query, ReportHistory.class);
        System.out.println("==========reportHistoryList.size()========"+reportHistoryList.size());
        for (ReportHistory reportHistory : reportHistoryList){
            reportHistory.setSoilReportNumber(userExtraService.getSoilAndFertilizerReportNumber(reportHistory.getId()));
            reportHistoryRepo.save(reportHistory);
        }
        MessageResponse response = new MessageResponse();
        response.setMessage("Soil Report Number Updated");
        return response;
    }

    /**
     * Remove Agent's All Farmer
     * Every Detail of Farmer will be Removed from Database
     * @param agentId Agent Id
     * @return Success Message
     */
    @RequestMapping(value = "/removeAgentFarmer",method = RequestMethod.DELETE)
    @ResponseBody
    public String removeAgentFarmer(@RequestParam String agentId){
        return organiseService.removeAgentFarmer(agentId);
    }

    /**
     * Remove Farmer from system
     * @param agentId Agent Id
     * @param farmerId Farmer Id
     * @return Success Message
     */
    @RequestMapping(value = "/removeFarmer",method = RequestMethod.DELETE)
    @ResponseBody
    public String removeFarmer(@RequestParam String agentId,
                               @RequestParam String farmerId){
        return organiseService.removeFarmerById(agentId,farmerId);
    }

    /**
     * set CreatedByUserId to agentId for agent's farmer
     * @param agentId
     * @return
     */
    @RequestMapping(value = "/organiseCreatedByUserIdforAgentFarmers",method = RequestMethod.GET)
    @ResponseBody
    public String organiseCreatedByUserIdforAgentFarmers(@RequestParam String agentId){
        return organiseService.organiseCreatedByUserIdforAgentFarmers(agentId);
    }

    @RequestMapping(value = "/getAgentFarmersNotHavingAgentId",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAgentFarmersNotHavingAgentId(@RequestParam String agentId){
        return organiseService.getAgentFarmersNotHavingAgentId(agentId);
    }

    @RequestMapping(value = "/organiseLocationReportHistory",method = RequestMethod.GET)
    @ResponseBody
    public void organiseLocationReportHistory(){
        setLocationReportHistory();
    }

    private void setLocationReportHistory() {

        Map<String, String> userIdKhasraMap = new HashMap<>();

        Date now = new Date();
        Date from = new DateTime(now).minusDays(7).toDate();
        Query query = new Query(Criteria.where("creationDate").gte(from).lte(now));
        List<ReportHistory> reportHistories = mongoTemplate.find(query, ReportHistory.class);

        System.out.println("================size=====" + reportHistories.size());
        for (ReportHistory reportHistory : reportHistories) {
            NPKRecommendation npkRecommendation = reportHistory.getNpkRecommendation();

            System.out.println("=========getKhasraNo=====" + npkRecommendation.getKhasraNo());
            System.out.println("=======reportHistory.getUserId()=======" + reportHistory.getUserId());
            UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(npkRecommendation.getKhasraNo(), reportHistory.getUserId());
            if (landDetail == null) {
                userIdKhasraMap.put(reportHistory.getUserId(), npkRecommendation.getKhasraNo());
//                throw new ResourceNotFoundException("Land Details not found");
                continue;
            }
            npkRecommendation.setCityId(landDetail.getCity().getId());
            npkRecommendation.setStateId(landDetail.getState().getId());
            if (landDetail.getTehsil() != null) {
              //  npkRecommendation.setTehsilId(landDetail.getTehsil().getId()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
            }
            if (landDetail.getBlock() != null) {
              //  npkRecommendation.setBlockId(landDetail.getBlock().getId()); THIS LINE OF CODE IS COMENTED BY SONU ON 02/09/2020
            }

            // store soil and fertilizer report Number
            if (reportHistory.getSoilReportNumber() == null || StringUtils.isEmpty(reportHistory.getSoilReportNumber())) {
                reportHistory.setSoilReportNumber(userExtraService.getSoilAndFertilizerReportNumber(reportHistory.getId()));
            }


            mongoTemplate.save(reportHistory);
        }
        System.out.println(userIdKhasraMap);
        System.out.println(userIdKhasraMap.size());
    }

    @RequestMapping(value = "/organiseUserSchemes",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseUserSchemes(){
        organiseService.organiseUserSchemes();
        return "success";
    }

    @RequestMapping(value = "/organiseHorticultureData",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseHorticultureData(){
        organiseService.organiseHorticultureData();
        return "success";
    }

    @RequestMapping(value = "/organiseUserCrop",method = RequestMethod.PUT)
    @ResponseBody
    public String  organiseUserCrop(){
        organiseService.organiseUserCrop();
        return "success";
    }

    @RequestMapping(value = "/organiseFarmDetail",method = RequestMethod.PUT)
    @ResponseBody
    public String  organiseFarmDetail(){
        organiseService.organiseFarmDetail();
        return "success";
    }

    @RequestMapping(value = "/organiseMilkingAnimalDetail",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseMilkingAnimalDetail(){
        organiseService.organiseMilkingAnimalDetail();
        return "success";
    }

    @RequestMapping(value = "/organiseUserSubsidy",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseUserSubsidy(){
        organiseService.organiseUserSubsidy();
        return "success";
    }

    @RequestMapping(value = "/organiseHealthRecords",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseHealthRecords(){
        organiseService.organiseHealthRecords();
        return "success";
    }

    @RequestMapping(value = "/organiseUserEquipment",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseUserEquipment(){
        organiseService.organiseUserEquipment();
        return "success";
    }


    @RequestMapping(value = "/storeAddressInSoilTest",method = RequestMethod.PUT)
    @ResponseBody
    public String storeAddressInSoilTest(){
        organiseService.storeAddressInSoilTest();
        return "success";
    }

    @RequestMapping(value = "/storeSoilTestFromReportHistory",method = RequestMethod.PUT)
    @ResponseBody
    public String storeSoilTestFromReportHistory(){
        organiseService.storeSoilTestFromReportHistory();
        return "success";
    }

    /**
     * store villageId in Report History
     * @return
     */
    @RequestMapping(value = "/organiseVillageIdReportHistory",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseVillageIdReportHistory(){
        organiseService.organiseVillageIdReportHistory();
        return "success";
    }

    @RequestMapping(value = "/organiseFertilizerTypeAndCategoryTypeInReportHistory",method = RequestMethod.PUT)
    @ResponseBody
    public String organiseFertilizerTypeAndCategoryTypeInReportHistory(@RequestParam String farmingType){
        organiseService.organiseFertilizerTypeAndCategoryTypeInReportHistory(farmingType);
        return "success";
    }

    @RequestMapping(value = "/addLandMapIdInLandDetails",method = RequestMethod.PUT)
    @ResponseBody
    public String addLandMapIdInLandDetails(){
        organiseService.addLandMapIdInLandDetails();
        return "success";
    }

    @RequestMapping(value = "/updateCreatedByUserInUserAssignmentHistory",method = RequestMethod.PUT)
    @ResponseBody
    public String updateCreatedByUserInUserAssignmentHistory(){
        organiseService.updateCreatedByUserInUserAssignmentHistory();
        return "success";
    }

    @RequestMapping(value = "/removeEmployeeAssignmentHistory",method = RequestMethod.DELETE)
    @ResponseBody
    public String  removeEmployeeAssignmentHistory(String userId){
        organiseService.removeEmployeeAssignmentHistory(userId);
        return "success";
    }

    @RequestMapping(value = "/updateVillageIdToVillageModelInVillageAsset",method = RequestMethod.PUT)
    @ResponseBody
    public String updateVillageIdToVillageModelInVillageAsset(){
        organiseService.updateVillageIdToVillageModelInVillageAsset();
        return "success";
    }

    @RequestMapping(value = "/storeAddressInFamilyMember",method = RequestMethod.PUT)
    @ResponseBody
    public String storeAddressInFamilyMember(){
        organiseService.storeAddressInFamilyMember();
        return "success";
    }
}
