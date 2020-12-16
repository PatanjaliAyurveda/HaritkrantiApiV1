package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author anuragdhunna
 */
public class Family extends BaseObject {

    public Family(String userId, List<User> familyMembers) {
        this.userId = userId;
        this.familyMembers = familyMembers;
    }

    @Indexed(unique = true)
    private String userId;

    @DBRef
    private List<User> familyMembers;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<User> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<User> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
