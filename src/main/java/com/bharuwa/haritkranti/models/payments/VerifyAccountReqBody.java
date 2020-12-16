package com.bharuwa.haritkranti.models.payments;

/**
 * @author harman
 */
public class VerifyAccountReqBody {

    private String bankId;

    private String verifiedByUserId;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getVerifiedByUserId() {
        return verifiedByUserId;
    }

    public void setVerifiedByUserId(String verifiedByUserId) {
        this.verifiedByUserId = verifiedByUserId;
    }
}
