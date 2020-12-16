package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.utils.MessageResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface CsvServices {

    State saveState(State state);

    List<String> uploadManagerCsv(MultipartFile file, String userId) throws Exception;

    void uploadGovernmentSchemesCsv(MultipartFile file) throws IOException;

    void uploadDairyFarmerCsv(MultipartFile file, String userId) throws IOException, ParseException;

    MessageResponse uploadProductCategoryCsv(MultipartFile file) throws Exception;

    MessageResponse uploadProductCategoryRollupCsv(MultipartFile file) throws Exception;

    MessageResponse uploadProductCategoryMemberCsv(MultipartFile file) throws Exception;

    MessageResponse uploadProductCsv(MultipartFile file) throws Exception;

    void readFruitVarietyExcel(MultipartFile file) throws IOException, InvalidFormatException;

    List<String> uploadFarmerCsv(MultipartFile file, String agentId) throws IOException, ParseException;

    List<String> LandDetailCsv(MultipartFile file) throws IOException;

    List<String> FarmerHealthDetailCsv(MultipartFile file) throws IOException;

    List<String> FarmerFamilyDetailCsv(MultipartFile file) throws IOException;

    List<String> FarmerLiveStockDetailCsv(MultipartFile file) throws IOException;

    List<String> FarmerCropDetailCsv(MultipartFile file) throws IOException, ParseException;

    List<String> FarmerEquipmentsCsv(MultipartFile file) throws IOException;

    List<String> FarmerSubsidyCsv(MultipartFile file) throws IOException;

    List<String> FarmerBeekepingDetailCsv(MultipartFile file) throws IOException, ParseException;

    List<String> FarmerHorticultureDetailCsv(MultipartFile file) throws IOException;

    List<String> VillageAssetDetailCsv(MultipartFile file) throws IOException;

    List<String> GovernmentSchemeDetailCsv(MultipartFile file) throws IOException;

    List<String> FinancialDetailCsv(MultipartFile file) throws IOException;
    
    public List<String> getAllCategories(String language);
    
    public List<String> getSubCategoryListByPrimaryCategoryName(String primaryCategoryName,String state,String district,String language);
    
    public List<String> getVarietyList(String primaryCategoryName, String subCategoryName,String language);
    
}
