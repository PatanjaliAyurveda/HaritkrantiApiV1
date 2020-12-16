package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.FamilyMemberHealthRecord;

import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface FamilyMemberHealthRecordRepo {

    <T> List<T> getUserLandDetailList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getFarmerList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getFamilyMemberList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getMilkingAnimalDetailsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getUserSubsidyList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getUserList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getFamilyMemberHealthRecordList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getUserEquipmentsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getEmployeeAssignmentHistoryList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getVillageAssets(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getBeekeepingDetailsList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getHorticultureList(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;
    <T> List<T> getUserCrops(String fromDate, String toDate, String locationType, String locationId, String reportType, Class<T> clazz) throws ParseException;

}
