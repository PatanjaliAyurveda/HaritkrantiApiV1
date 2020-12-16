package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * @author anuragdhunna
 */
public class Aadhar extends BaseObject{

    private enum Gender {
        Male, Female
    }

    private String userId;
    @Indexed(unique = true)
    private String cardNo;
    private String name;
    private String fatherName;
    private Date dob = new Date();
    private Address address;
    private boolean verified = false;
    private Gender gender = Gender.Male;
    private String aadharPicUrl;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getAadharPicUrl() {
        return aadharPicUrl;
    }

    public void setAadharPicUrl(String aadharPicUrl) {
        this.aadharPicUrl = aadharPicUrl;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
