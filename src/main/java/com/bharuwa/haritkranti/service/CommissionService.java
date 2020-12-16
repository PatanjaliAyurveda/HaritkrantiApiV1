package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.payments.CommissionRate;
import com.bharuwa.haritkranti.models.requestModels.CommissionReqBody;
import org.springframework.data.domain.Page;

import java.text.ParseException;

/**
 * @author anuragdhunna
 */
public interface CommissionService {

    CommissionRate storeCommissionRate(CommissionReqBody commissionReqBody);
    CommissionRate getCurrentCommissionRateByLocation(String locationType, String locationId);

    Page<CommissionRate> getCommissionRateHistoryByLocation(String locationType, String locationId, String fromDate, String toDate, Integer page, Integer size) throws ParseException;
}
