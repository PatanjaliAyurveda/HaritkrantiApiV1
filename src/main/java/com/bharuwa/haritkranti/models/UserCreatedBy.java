package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anuragdhunna
 */
@Document
public class UserCreatedBy extends BaseObject {

    private String userId;
    private String createdByUserId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
