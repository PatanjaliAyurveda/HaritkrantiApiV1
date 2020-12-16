package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
public class UserIdentificationDetails extends BaseObject{

    private String userId;

    private String familyMemberId;

    @Indexed
    private String voterId;

    @Indexed
    private String aadhaarCardNumber;

    @Indexed
    private String passport;

    @Indexed
    private String panCard;

    @Indexed
    private String drivingLicence;

    @Indexed
    private String rationCardNumber;

    private String bankAccountNumber;

    private String ifscCode;

    private String bankPassBookImage;

    private String aadharCardImage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getAadhaarCardNumber() {
        return aadhaarCardNumber;
    }

    public void setAadhaarCardNumber(String aadhaarCardNumber) {
        this.aadhaarCardNumber = aadhaarCardNumber;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
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

    public String getBankPassBookImage() {
        return bankPassBookImage;
    }

    public void setBankPassBookImage(String bankPassBookImage) {
        this.bankPassBookImage = bankPassBookImage;
    }

    public String getAadharCardImage() {
        return aadharCardImage;
    }

    public void setAadharCardImage(String aadharCardImage) {
        this.aadharCardImage = aadharCardImage;
    }
}
