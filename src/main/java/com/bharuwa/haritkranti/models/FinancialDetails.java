package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
public class FinancialDetails extends BaseObject{

    public enum AccountType{
        Saving,Current,Salary
    }

    public enum UserType{
        Manager,Agent,Farmer
    }

    private String bankName;

//    @Indexed(unique = true)
    private String accountNumber;

    private AccountType accountType = AccountType.Saving;

    private String ifscCode;

    @Indexed
    private String aadhaarCardNumber;

    private String aadharCardImage;

    private String bankPassBookImage;

    @Deprecated
    @Indexed
    private String userId;

    private String createdByUserId;

    private String accountHolderName;

    private UserType userType;

    private boolean primaryAccount = false;

    private boolean accountVerified = false;

    private String verifiedByUserId;

    @Indexed
    @DBRef
    private User user;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankPassBookImage() {
        return bankPassBookImage;
    }

    public void setBankPassBookImage(String bankPassBookImage) {
        this.bankPassBookImage = bankPassBookImage;
    }

    public String getAadhaarCardNumber() {
        return aadhaarCardNumber;
    }

    public void setAadhaarCardNumber(String aadhaarCardNumber) {
        this.aadhaarCardNumber = aadhaarCardNumber;
    }

    public String getAadharCardImage() {
        return aadharCardImage;
    }

    public void setAadharCardImage(String aadharCardImage) {
        this.aadharCardImage = aadharCardImage;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(boolean primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public boolean isAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public String getVerifiedByUserId() {
        return verifiedByUserId;
    }

    public void setVerifiedByUserId(String verifiedByUserId) {
        this.verifiedByUserId = verifiedByUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
