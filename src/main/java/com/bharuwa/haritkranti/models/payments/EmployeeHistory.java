package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class EmployeeHistory extends BaseObject {

    public enum Status {
        ACTIVE, INACTIVE
    }

    @DBRef
    private User user;                  // Employee(Block Manager)
    private Status status;
    private String createdByUserId;     // Admin or Manager

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
