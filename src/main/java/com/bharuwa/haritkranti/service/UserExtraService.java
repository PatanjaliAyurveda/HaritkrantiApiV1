package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.UserCrop;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.responseModels.BeekeepingQRCodeResponse;
import com.bharuwa.haritkranti.models.responseModels.OCQRCodeResponse;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.google.zxing.WriterException;
import com.itextpdf.text.BadElementException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface UserExtraService {

    UserCrop addUserCrop(UserCrop userCrop);

    Aadhar addAadhar(Aadhar aadhar);
    Aadhar getAadharByUserId(String userId);
    MessageResponse uploadAadharPic(@RequestParam("file") MultipartFile file, @RequestParam String userId);

    void getGenerateQRCodeImageForOC(String userId, String khasraNos, String certificateCode) throws IOException, WriterException;

    OCQRCodeResponse getOCQRResponse(String userId, String khasraNos, String certificateCode);

    String generateUniqueCodeForFarmer(User user);

    boolean checkUserLandOCEligible(String userId, String khasraNo);

    List<UserCrop> getUserCropListReadyForSale(String phoneNumber);

    GeoLocationMapping storeGeoLocationMapping(GeoLocationMapping geoLocationMapping);

    GeoLocationMapping getGeoLocationMappingByType(String userId, String khasraNo);

    String getBeekingBatchForFarmer(String beekeepingId);

    User updateUserParent(String userId, String parentId);

    BeekeepingQRCodeResponse getBeekeepingQRCodeResponse(String beekeepingId);

    byte[] generateQRCodeForBeekeeping(String beekeepingId) throws IOException, WriterException, BadElementException;

    String getSoilAndFertilizerReportNumber(String reportHistoryId) ;

    KhasrasOC storeKhasrasOC(KhasrasOC khasrasOC);

    KhasrasOC getKhasrasOC(String userId, String landDetailId);

    MessageResponse updateStateAbbreviationForFarmerCode(String stateName);
    
    public UserCrop getUserCropById(String userId, String id,String khasraNumber);
    
}
