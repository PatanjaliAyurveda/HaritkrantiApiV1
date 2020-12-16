package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.payments.PaymentCycle;
import com.bharuwa.haritkranti.models.payments.SoilTestPayment;
import org.springframework.data.domain.Page;

import java.text.ParseException;

/**
 * @author anuragdhunna
 */
public interface PaymentCycleService {

    PaymentCycle storePaymentCycle(PaymentCycle paymentCycle);
    PaymentCycle getLatestPaymentCycle();
    Page<PaymentCycle> getPaymentCyclesByDate(String fromDate, String toDate, Integer page, Integer size) throws ParseException;
    PaymentCycle getCurrentPaymentCycle();

    PaymentCycle getPaymentCycle(String paymentCycle);

    Page<SoilTestPayment> getSoilTestPaymentByFilters(String paymentCycleId, String agentPhoneNumber, String locationType, String locationId, String paymentStatus, String status, int page, int size);

    SoilTestPayment storeSoilTestPayment(SoilTestPayment soilTestPayment);

    SoilTestPayment getSoilTestPayment(String soilTestPaymentId);

    SoilTestPayment searchSoilTestPayment(String villageId, String khasraNo);
}
