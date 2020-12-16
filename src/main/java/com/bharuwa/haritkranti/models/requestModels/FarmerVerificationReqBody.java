package com.bharuwa.haritkranti.models.requestModels;

/**
 * @author harman
 */
public class FarmerVerificationReqBody {

    private String phoneNumber;

    private boolean phoneNumberUpdated;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberUpdated() {
        return phoneNumberUpdated;
    }

    public void setPhoneNumberUpdated(boolean phoneNumberUpdated) {
        this.phoneNumberUpdated = phoneNumberUpdated;
    }
}
