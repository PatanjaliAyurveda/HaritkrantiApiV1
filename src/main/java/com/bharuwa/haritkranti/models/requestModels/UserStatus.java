package com.bharuwa.haritkranti.models.requestModels;

/**
 * @author harman
 */
public class UserStatus {

    private String userId;

    private boolean status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
