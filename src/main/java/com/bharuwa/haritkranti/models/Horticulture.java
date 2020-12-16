package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author harman
 */
@Document
public class Horticulture extends BaseObject {

    public enum IncomePeriod {
        Quater, Half_Year, Annually, Quarterly
    }

    public enum TreeType {
        Arecanut, Banana, Black_Pepper, Cashewnut, Chickoo, Cinnamon, Clove, Coconut, Guava, Mango, Nutmeg, Papaya, Pineapple
    }

    @Deprecated
    private String userId;

    private String khasraNo;

    private Integer noOfTrees;

    private TreeType treeType = TreeType.Arecanut;

    private Integer ageOfTree;

    private BigDecimal income = BigDecimal.ZERO;

    private IncomePeriod incomePeriod = IncomePeriod.Quater;

    // for location search
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public Integer getNoOfTrees() {
        return noOfTrees;
    }

    public void setNoOfTrees(Integer noOfTrees) {
        this.noOfTrees = noOfTrees;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
    }

    public Integer getAgeOfTree() {
        return ageOfTree;
    }

    public void setAgeOfTree(Integer ageOfTree) {
        this.ageOfTree = ageOfTree;
    }

    public IncomePeriod getIncomePeriod() {
        return incomePeriod;
    }

    public void setIncomePeriod(IncomePeriod incomePeriod) {
        this.incomePeriod = incomePeriod;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
