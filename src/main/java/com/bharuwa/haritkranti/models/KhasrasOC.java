package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author harman
 */
@Document
public class KhasrasOC extends BaseObject {

    private String userId;

    private List<UserLandDetail> userLandDetailList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserLandDetail> getUserLandDetailList() {
        return userLandDetailList;
    }

    public void setUserLandDetailList(List<UserLandDetail> userLandDetailList) {
        this.userLandDetailList = userLandDetailList;
    }
}

