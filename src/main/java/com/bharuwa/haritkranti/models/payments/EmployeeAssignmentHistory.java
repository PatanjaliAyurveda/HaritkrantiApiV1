package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

/**
 * @author harman
 */
public class EmployeeAssignmentHistory extends BaseObject {

    public enum EmplyeeRelationship {
       ADMIN_ADMIN, ADMIN_ADMINVIEW, ADMIN_ACCOUNTANT,ADMIN_NM, ADMIN_SM, ADMIN_DM, ADMIN_BM, ADMIN_AGENT, ADMIN_FARMER, NM_SM, NM_DM, NM_BM, NM_AGENT, NM_FARMER, SM_DM, SM_BM
        , SM_AGENT, SM_FARMER, DM_BM, DM_AGENT, DM_FARMER, BM_AGENT, BM_FARMER, AGENT_FARMER
    }

    @DBRef
    private User fromUser; // master user

    @DBRef
    private User toUser;    // user assigned under master user

    private EmplyeeRelationship emplyeeRelationship;

    // date when user assigned
    private Date assignmentDate = new Date();

    // for location Search (filter)
    private Address address;

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public EmplyeeRelationship getEmplyeeRelationship() {
        return emplyeeRelationship;
    }

    public void setEmplyeeRelationship(EmplyeeRelationship emplyeeRelationship) {
        this.emplyeeRelationship = emplyeeRelationship;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
