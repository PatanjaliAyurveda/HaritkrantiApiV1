package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.User;

/**
 * @author harman
 */
public class CreateManagerReqBody {

    private User user;

    private String roleName;

    private String createdByUserId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
