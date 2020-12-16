package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.payments.Rate;
import com.bharuwa.haritkranti.models.requestModels.RateReqBody;
import org.springframework.data.domain.Page;

import java.text.ParseException;

/**
 * @author anuragdhunna
 */
public interface RateService {

    Rate storeRate(RateReqBody rateReqBody);

    Rate getCurrentRate();

    Page<Rate> getRateList(int page, int size, String fromDate, String toDate, String locationType, String locationId) throws ParseException;

    Rate getCurrentRateByLocation(String locationType, String locationId);
}
