package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
public class UserSchemes extends BaseObject {

    private String userId;

    @DBRef
    private GovernmentSchemes governmentSchemes;

    private boolean status = true;

    // for location search
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GovernmentSchemes getGovernmentSchemes() {
        return governmentSchemes;
    }

    public void setGovernmentSchemes(GovernmentSchemes governmentSchemes) {
        this.governmentSchemes = governmentSchemes;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
