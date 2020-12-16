package com.bharuwa.haritkranti.models;

import java.util.Date;

/**
 * @author anuragdhunna
 */
public class OTP extends BaseObject{

    private String otp;

    private Date expiryTime = new Date();

    private String phoneNumber;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
