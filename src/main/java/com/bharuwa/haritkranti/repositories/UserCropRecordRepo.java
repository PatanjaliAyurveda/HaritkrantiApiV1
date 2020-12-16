package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.UserCrop;

import java.text.ParseException;
import java.util.List;

/**
 * @author harman
 */
public interface UserCropRecordRepo {

    List<UserCrop> getUserCropRecordListByFilters(String fromDate, String toDate, String locationType, String locationId, String  reportType) throws ParseException;
}
