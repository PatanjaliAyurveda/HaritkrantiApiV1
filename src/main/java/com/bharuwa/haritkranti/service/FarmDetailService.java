package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.BeekeepingAreaMapping;
import com.bharuwa.haritkranti.models.BeekeepingDetails;

import java.util.List;

/**
 * @author harman
 */
public interface FarmDetailService {

    BeekeepingDetails storeBeekeepingDetails(BeekeepingDetails beekeepingDetails);

    List<BeekeepingDetails> getBeekeepingDetails(String userId);

    BeekeepingDetails getBeekeepingDetailById(String beekeepingId);

    BeekeepingAreaMapping storeBeekeepingAreaMapping(BeekeepingAreaMapping beekeepingAreaMapping);

    BeekeepingAreaMapping getBeekeepingAreaMapping(String farmerId, String beekeepingId);
}
