package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.responseModels.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface CountService {

    Long getTotalUserCount();

    Long getTotalAgentCount(boolean active);

    Long getTotalFarmerCount();

    Long getTotalUserSignUpCount();

    List<FarmerCountStateDistrict> getTotalFarmerCountByStates();

    List<FarmerCountStateDistrict> getTotalFarmerCountByDistrict(String stateId);

    Long getTotalKhasraCount();

    Long getKhasraCountByState(String stateId);

    Long getKhasraCountByDistrict(String districtId);

    Long getTotalFemaleFarmerCount();

    Long getTotalMaleFarmerCount();

    LandTypeSize getLandTypesAreaCountCountry();

    List<LandTypeSize> getLandTypesAreaCountStates();

    List<LandTypeSize> getLandTypesAreaCountDistricts(String stateId);

    int getDairyFarmerCount();

    Long getBeekeepingFarmerCount();

    int getSericultureFarmerCount();

    int getHorticultureFarmerCount();

    int getTotalCowsCount();

    int getTotalSheepCount();

    int getTotalGoatCount();

    int getTotalBuffaloCount();

    List<FarmerCountStateDistrict> getDairyFarmerCountByStates();

    List<FarmerCountStateDistrict> getDairyFarmerCountByDistrict(String stateId);

    Long getUserTypeCount(String roleName, boolean active);

    ManagerFarmerCount getManagerFarmerCount(String userId);

    CountAll getCountAll();

    OCEligibleUsersAndCount getOCEligibleUsers(String fromDate , String toDate) throws ParseException;

    List<FarmerCountStateDistrict> getOCEligibleUsersCountByStates() throws ParseException;

    List<FarmerCountStateDistrict> getSTRCountByStates(String farmingType, String fromDate, String toDate) throws ParseException;

    List<FarmerCountStateDistrict> getSTRCountByDistrict(String stateId, String farmingType, String fromDate, String toDate) throws ParseException;

    List<FarmerCountStateDistrict> getSTRCountByTehsil(String districtId, String farmingType, String fromDate, String toDate) throws ParseException;

    List<FarmerCountStateDistrict> getSTRCountByBlock(String tehsilId, String farmingType, String fromDate, String toDate) throws ParseException;

    List<FarmerCountStateDistrict> getTotalFarmerCountByTehsil(String districtId);

    List<FarmerCountStateDistrict> getTotalFarmerCountByBlock(String tehsilId);

    List<FarmerCountStateDistrict> getDairyFarmerCountByTehsil(String districtId);

    List<FarmerCountStateDistrict> getDairyFarmerCountByBlock(String tehsilId);

    long getAllUsersCountByDate(String fromDate, String toDate, String locationType, String locationId) throws ParseException;

    List<User> getAllUserListByDate(String fromDate, String toDate, String locationType, String locationId) throws ParseException;

    CountAll storeCountAll(CountAll countAll);

    long getCountByReportType(String reportType, String fromDate, String toDate, String locationType, String locationId, String farmingType) throws ParseException;
}
