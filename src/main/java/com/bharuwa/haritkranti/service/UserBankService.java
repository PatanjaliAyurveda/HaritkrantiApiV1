package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.payments.VerifyAccountReqBody;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author anuragdhunna
 */
public interface UserBankService {

    FinancialDetails addFinancialDetails(FinancialDetails financialDetails);

    List<FinancialDetails> getFinancialDetails(String userId);

    IncomeSources addIncomeSources(IncomeSources incomeSources);

    IncomeSources getIncomeSources(String userId);

    LoanDetails addLoanDetails(LoanDetails loanDetails);

    List<LoanDetails> getLoanDetails(String userId);

    InsuranceDetails addInsuranceDetails(InsuranceDetails insuranceDetails);

    List<InsuranceDetails> getInsuranceDetails(String userId);

    List<String> getUserBankList(String userId);

    FinancialDetails getBankDetailsById(String bankId);

    LoanDetails getLoanDetailsById(String loanId);

    InsuranceDetails getInsuranceDetailsById(String insuranceId);

    FinancialDetails getPrimaryBankDetails(String userId);

    String setPrimaryBank(String financialDetailId, String userId);

    FinancialDetails verifyBankAccount(VerifyAccountReqBody verifyAccountReqBody);

    Page<FinancialDetails> getBankAccountsByStatus(Boolean verified, int page, int size);
}
