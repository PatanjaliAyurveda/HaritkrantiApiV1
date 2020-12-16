package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.responseModels.BeekeepingQRCodeResponse;
import com.bharuwa.haritkranti.models.responseModels.OCQRCodeResponse;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.google.zxing.WriterException;
import com.itextpdf.text.BadElementException;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserExtraController extends BaseController {

    @ApiOperation(value = "Store Khasra History for Crop and Fruit")
    @RequestMapping(value = "/addUserCrop", method = RequestMethod.POST)
    public UserCrop addUserCrop(@RequestBody UserCrop userCrop) {
        return userExtraService.addUserCrop(userCrop);
    }

    @ApiOperation(value = "Get List of Khasra History by Crop or Fruit")
    @RequestMapping(value = "/getUserCrops", method = RequestMethod.GET)
    public List<UserCrop> getUserCrops(@RequestParam String userId, @Nullable @RequestParam String cropGrouptype) {
        return agentService.getUserCrops(userId, cropGrouptype);
    }
    
    @RequestMapping(value = "/getUserCropByIdAndKhasraNumber", method = RequestMethod.GET)
    public UserCrop getUserCropById(@RequestParam String userId,@RequestParam @Nullable String id,@RequestParam @Nullable String khasraNumber) {
        return userExtraService.getUserCropById(userId, id,khasraNumber);
    }

    @RequestMapping(value = "/addAadhar", method = RequestMethod.POST)
    public Aadhar addAadhar(@RequestBody Aadhar aadhar) {
        return userExtraService.addAadhar(aadhar);
    }

    @RequestMapping(value = "/getAadhar", method = RequestMethod.GET)
    public Aadhar getAadhar(@RequestBody String userId) {
        return userExtraService.getAadharByUserId(userId);
    }

    @RequestMapping(value = "/uploadAadharPic", method = RequestMethod.POST)
    @ResponseBody
    public MessageResponse uploadAadharPic(@RequestParam(value = "file") MultipartFile file, @RequestParam String userId) throws Exception {
        return userExtraService.uploadAadharPic(file, userId);
    }

    @RequestMapping(value = "/addFarmDetail", method = RequestMethod.POST)
    public Farm addFarmDetail(@RequestBody Farm farm) {
        return userService.addFarmDetail(farm);
    }

    @RequestMapping(value = "/getUserLandDetails", method = RequestMethod.GET)
    public List<UserLandDetail> getUserLandDetails(@RequestParam String userId) {
        return agentService.getUserLandDetails(userId);
    }

    @RequestMapping(value = "/getAgentUsersList", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAgentUsersList(@RequestParam String agentId) {
        return agentService.getAgentUsersList(agentId);
    }

    @RequestMapping(value = "/addEquipment", method = RequestMethod.POST)
    public Equipment addEquipment(@RequestBody Equipment equipment) {
        return equipmentService.addEquipment(equipment);
    }

    @RequestMapping(value = "/createEquipment", method = RequestMethod.POST)
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @RequestMapping(value = "/getMoveableEquipments", method = RequestMethod.GET)
    public List<Equipment> getMoveableEquipments() {
        return equipmentService.getMoveableEquipments();
    }

    @RequestMapping(value = "/getImmoveableEquipments", method = RequestMethod.GET)
    public List<Equipment> getImmoveableEquipments() {
        return equipmentService.getImmoveableEquipments();
    }

    @RequestMapping(value = "/getGenerateQRCodeImageForOC", method = RequestMethod.GET)
    public void getGenerateQRCodeImageForOC(@RequestParam String userId,
                                            @RequestParam String khasraNos,
                                            @RequestParam String certificateCode) throws IOException, WriterException {
        userExtraService.getGenerateQRCodeImageForOC(userId, khasraNos, certificateCode);
    }

    @RequestMapping(value = "/getOCQRResponse", method = RequestMethod.GET)
    public OCQRCodeResponse getOCQRResponse(@RequestParam String userId,
                                            @RequestParam String khasraNos,
                                            @RequestParam String certificateCode) {
        return userExtraService.getOCQRResponse(userId, khasraNos, certificateCode);
    }

    @RequestMapping(value = "/checkUserLandOCEligible", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUserLandOCEligible(@RequestParam String userId,
                                           @RequestParam String khasraNo) {
        return userExtraService.checkUserLandOCEligible(userId, khasraNo);

    }

    /**
     * returns UserCrop List ready to sell by Phone Number
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/getUserCropListReadyForSale", method = RequestMethod.GET)
    public List<UserCrop> getUserCropListReadyForSale(@RequestParam String phoneNumber) {
        return userExtraService.getUserCropListReadyForSale(phoneNumber);
    }


    @RequestMapping(value = "/storeGeoLocationMapping", method = RequestMethod.POST)
    public GeoLocationMapping storeGeoLocationMapping(@RequestBody GeoLocationMapping geoLocationMapping) {
        return userExtraService.storeGeoLocationMapping(geoLocationMapping);
    }

    @RequestMapping(value = "/getGeoLocationMappingByType", method = RequestMethod.GET)
    public GeoLocationMapping getGeoLocationMappingByType(@RequestParam String userId,
                                                         
                                                          @Nullable @RequestParam String khasraNo) {
        return userExtraService.getGeoLocationMappingByType(userId,khasraNo);
    }

    @RequestMapping(value = "/getBeekingBatchForFarmer", method = RequestMethod.GET)
    public String getBeekingBatchForFarmer(@RequestParam String beekeepingId) {
        return userExtraService.getBeekingBatchForFarmer(beekeepingId);
    }

    @RequestMapping(value = "/generateQRCodeForBeekeeping", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] generateQRCodeForBeekeeping(@RequestParam String beekeepingId) throws IOException, WriterException, BadElementException {
        return userExtraService.generateQRCodeForBeekeeping(beekeepingId);
    }

    @RequestMapping(value = "/getBeekeepingQRCodeResponse", method = RequestMethod.GET)
    public BeekeepingQRCodeResponse getBeekeepingQRCodeResponse(@RequestParam String beekeepingId) {
        return userExtraService.getBeekeepingQRCodeResponse(beekeepingId);
    }

    @RequestMapping(value = "/updateUserParent", method = RequestMethod.PUT)
    public User updateUserParent(@RequestParam String userId, @RequestParam String parentId) {
        return userExtraService.updateUserParent(userId, parentId);
    }

    @RequestMapping(value = "/getSoilAndFertilizerReportNumber", method = RequestMethod.GET)
    public String getSoilAndFertilizerReportNumber(@RequestParam String reportHistoryId) {
        return userExtraService.getSoilAndFertilizerReportNumber(reportHistoryId);
    }


    @RequestMapping(value = "/storeKhasrasOC", method = RequestMethod.POST)
    public KhasrasOC storeKhasrasOC(@RequestBody KhasrasOC khasrasOC) {
        return userExtraService.storeKhasrasOC(khasrasOC);
    }

    @RequestMapping(value = "/getKhasrasOC", method = RequestMethod.GET)
    public KhasrasOC getKhasrasOC(@RequestParam String userId,
                                  @RequestParam String landDetailId) {
        return userExtraService.getKhasrasOC(userId, landDetailId);
    }

    @RequestMapping(value = "/updateStateAbbreviationForFarmerCode", method = RequestMethod.PUT)
    public MessageResponse updateStateAbbreviationForFarmerCode(@RequestParam String stateName) {
        return userExtraService.updateStateAbbreviationForFarmerCode(stateName);
    }
}

