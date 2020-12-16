package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.payments.VerifyAccountReqBody;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserBankController extends BaseController {

    @RequestMapping(value = "/addFinancialDetails", method = RequestMethod.POST)
    public FinancialDetails addFinancialDetails(@RequestBody FinancialDetails financialDetails) {
        return userBankService.addFinancialDetails(financialDetails);
    }

    @RequestMapping(value = "/getFinancialDetails", method = RequestMethod.GET)
    public List<FinancialDetails> getFinancialDetails(@RequestParam String userId) {
        return userBankService.getFinancialDetails(userId);
    }

    @RequestMapping(value = "/addIncomeSources", method = RequestMethod.POST)
    public IncomeSources addIncomeSources(@RequestBody IncomeSources incomeSources) {
        return userBankService.addIncomeSources(incomeSources);
    }

    @RequestMapping(value = "/getIncomeSources", method = RequestMethod.GET)
    public IncomeSources getIncomeSources(@RequestParam String userId) {
        return userBankService.getIncomeSources(userId);
    }

    @RequestMapping(value = "/addLoanDetails", method = RequestMethod.POST)
    public LoanDetails addLoanDetails(@RequestBody LoanDetails loanDetails) {
        return userBankService.addLoanDetails(loanDetails);
    }

    @RequestMapping(value = "/getLoanDetails", method = RequestMethod.GET)
    public List<LoanDetails> getLoanDetails(@RequestParam String userId) {
        return userBankService.getLoanDetails(userId);
    }

    @RequestMapping(value = "/addInsuranceDetails", method = RequestMethod.POST)
    public InsuranceDetails addInsuranceDetails(@RequestBody InsuranceDetails insuranceDetails) {
        return userBankService.addInsuranceDetails(insuranceDetails);
    }

    @RequestMapping(value = "/getInsuranceDetails", method = RequestMethod.GET)
    public List<InsuranceDetails> getInsuranceDetails(@RequestParam String userId) {
        return userBankService.getInsuranceDetails(userId);
    }

    @RequestMapping(value = "/getLoanTypes", method = RequestMethod.GET)
    public List<LoanDetails.LoanType> getLoanTypes() {
        return Arrays.asList(LoanDetails.LoanType.values());
    }

    @RequestMapping(value = "/getInsuranceTypes", method = RequestMethod.GET)
    public List<InsuranceDetails.InsuranceType> getInsuranceType() {
        return Arrays.asList(InsuranceDetails.InsuranceType.values());
    }

    @RequestMapping(value = "/getAccountTypes", method = RequestMethod.GET)
    public List<FinancialDetails.AccountType> getAccountType() {
        return Arrays.asList(FinancialDetails.AccountType.values());
    }

    @RequestMapping(value = "/getBankDetailsById", method = RequestMethod.GET)
    public FinancialDetails getBankDetailsById(@RequestParam String bankId) {
        return userBankService.getBankDetailsById(bankId);
    }

    @RequestMapping(value = "/getLoanDetailsById", method = RequestMethod.GET)
    public LoanDetails getLoanDetailsById(@RequestParam String loanId) {
        return userBankService.getLoanDetailsById(loanId);
    }

    @RequestMapping(value = "/getInsuranceDetailsById", method = RequestMethod.GET)
    public InsuranceDetails getInsuranceDetailsById(@RequestParam String insuranceId) {
        return userBankService.getInsuranceDetailsById(insuranceId);
    }

    @RequestMapping(value = "/getUserBankList", method = RequestMethod.GET)
    public List<String> getUserBankList(@RequestParam String userId) {
        return userBankService.getUserBankList(userId);
    }

    @RequestMapping(value = "/setPrimaryBank", method = RequestMethod.GET)
    public String  setPrimaryBank(@RequestParam String financialDetailId,
                                  @RequestParam String userId) {
        return userBankService.setPrimaryBank(financialDetailId,userId);
    }

    /**
     * input params Bank Id and LoginUserId
     * @param verifyAccountReqBody
     * @return
     */
    //    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT_MANAGER')")
    @RequestMapping(value = "/verifyBankAccount", method = RequestMethod.POST)
    public FinancialDetails verifyBankAccount(@RequestBody VerifyAccountReqBody verifyAccountReqBody) {
        return userBankService.verifyBankAccount(verifyAccountReqBody);
    }

    @RequestMapping(value = "/getBankAccountsByStatus",method = RequestMethod.GET)
    @ResponseBody
    public Page<FinancialDetails> getBankAccountsByStatus(@Nullable @RequestParam Boolean verified,
                                                          @RequestParam(value = "page",defaultValue = "0")int page,
                                                          @RequestParam(value = "size",defaultValue = "20")int size){
        return userBankService.getBankAccountsByStatus(verified,page,size);
    }
}
