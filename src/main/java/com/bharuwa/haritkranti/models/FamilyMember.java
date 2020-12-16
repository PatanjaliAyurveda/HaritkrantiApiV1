package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "name_farmerId", unique = true, def = "{'firstName' : 1,'lastName' : 1,'farmerId': 1,'relationship' : 1}")
})
public class FamilyMember extends BaseObject {

    private String firstName;

    private String middleName;

    private String lastName;

    private User.Relationship relationship = User.Relationship.Other;

    private Integer age;

    private Address address;

    private boolean sameAddress = Boolean.FALSE;

    private String farmerId;

    private String agentId;

    private boolean dependent = Boolean.FALSE;

    private User.Gender gender;

    private String relation;

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User.Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(User.Relationship relationship) {
        this.relationship = relationship;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(boolean sameAddress) {
        this.sameAddress = sameAddress;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public boolean isDependent() {
        return dependent;
    }

    public void setDependent(boolean dependent) {
        this.dependent = dependent;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
