package com.bharuwa.haritkranti.repositories.impl;

import com.bharuwa.haritkranti.repositories.FamilyMemberHealthRecordRepo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import static com.bharuwa.haritkranti.service.impl.CountServiceImpl.getQueryByReportType;

/**
 * @author anuragdhunna
 */
@Service
public class FamilyMemberHealthRecordRepoImpl implements FamilyMemberHealthRecordRepo {

    private final MongoTemplate mongoTemplate;

    public FamilyMemberHealthRecordRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Cacheable("getUserLandDetailList")
    public <T> List<T> getUserLandDetailList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("userId").include("khasraNo").include("landSize").include("landSizeType").include("ownershipType").include("farmType")
                .include("ownerName").include("soil").include("irrigationSource").include("creationDate");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getFarmerList")
    public <T> List<T> getFarmerList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("firstName").include("middleName").include("lastName").include("fatherName").include("primaryPhone").include("religion").include("category").include("dateOfBirth").include("qualification").include("userCode") .include("addressModel"). include("createdByUserId");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getFamilyMemberList")
    public <T> List<T> getFamilyMemberList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getMilkingAnimalDetailsList")
    public <T> List<T> getMilkingAnimalDetailsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getUserSubsidyList")
    public <T> List<T> getUserSubsidyList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getUserList")
    public <T> List<T> getUserList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("firstName").include("middleName").include("lastName").include("primaryPhone").include("isActive")
                .include("userCode") .include("addressModel").include("roles"). include("createdByUserId");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getFamilyMemberHealthRecordList")
    public <T> List<T> getFamilyMemberHealthRecordList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getUserEquipmentsList")
    public <T> List<T> getUserEquipmentsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("userId").include("moveableEquipments").include("immoveableEquipments").include("creationDate");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getEmployeeAssignmentHistoryList")
    public <T> List<T> getEmployeeAssignmentHistoryList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getVillageAssets")
    public <T> List<T> getVillageAssets(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getBeekeepingDetailsList")
    public <T> List<T> getBeekeepingDetailsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("userId").include("income").include("quantity").include("unit").include("productionOutput").include("unitProductionOutput").include("honeyFlavour")
                .include("fromDate").include("toDate").include("batchCode").include("season").include("creationDate");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getHorticultureList")
    public <T> List<T> getHorticultureList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("userId").include("khasraNo").include("noOfTrees").include("treeType").include("ageOfTree").include("income").include("incomePeriod").include("creationDate");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }

    @Override
    @Cacheable("getUserCrops")
    public <T> List<T> getUserCrops(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException {
        System.out.println("=====start==form=query====================="+ Calendar.getInstance().getTime());
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        query.fields().include("userId").include("address").include("khasraNo").include("cropYield").include("cropYieldUnit").include("landSize")
                .include("landSizeType").include("crop").include("userLandDetailId").include("seedQuantity").include("fertilizerQuantity").include("numberOfManPower").include("yearOfSowing");
        System.out.println("=========end form query===================="+Calendar.getInstance().getTime());
        return mongoTemplate.find(query, clazz);
    }
}