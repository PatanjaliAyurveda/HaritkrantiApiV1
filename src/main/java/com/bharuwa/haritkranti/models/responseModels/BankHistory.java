package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BankHistory extends BaseObject {

    private String userId;

    private  String agentId;

    private String bankName;

    private String bankAccountNumber;

    private String ifscCode;

    private String passBookUpload;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getPassBookUpload() {
        return passBookUpload;
    }

    public void setPassBookUpload(String passBookUpload) {
        this.passBookUpload = passBookUpload;
    }
}
