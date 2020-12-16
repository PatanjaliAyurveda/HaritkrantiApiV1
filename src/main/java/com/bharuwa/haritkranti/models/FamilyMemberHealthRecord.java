package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author harman
 */
@Document
public class FamilyMemberHealthRecord extends BaseObject {

    public enum Disability {
        Polio, Dystrophy, Cerebral_Palsy, Hemiplegia, Multiple_Sclerosis, Quadriplegia, Paraplegia, None, Other
    }

    public enum MedicalProblem {
        Hypertension, Asthma, Cancer, Chronic_Bronchitis, Coronary_heart_disease, Dementia, Paraplegia, Diabetes,
        Physical_Activity_and_Nutrition, Overweight_and_Obesity, Substance_Abuse, Mental_Health, Immunization, None, Other
    }

    // Date fields added for getting expenses for one financial year i.e. 1,April,2019 to 31,March,2020
    private Date fromDate = new Date();

    private Date toDate = new Date();

    @Indexed
    private String userId;

    private String headUserId;

    private BigDecimal medicalExpense = BigDecimal.ZERO;

    private String disability;

    private String medicalProblem;

    // for location search (filter)
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUserId() {
        return headUserId;
    }

    public void setHeadUserId(String headUserId) {
        this.headUserId = headUserId;
    }

    public BigDecimal getMedicalExpense() {
        return medicalExpense;
    }

    public void setMedicalExpense(BigDecimal medicalExpense) {
        this.medicalExpense = medicalExpense;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getMedicalProblem() {
        return medicalProblem;
    }

    public void setMedicalProblem(String medicalProblem) {
        this.medicalProblem = medicalProblem;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
