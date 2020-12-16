package com.bharuwa.haritkranti.models.payments;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.User;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author anuragdhunna
 */
@Document
public class SoilTestPayment extends BaseObject {

    public enum PaymentStatus {
        Received, Pending, Hold
    }

    public enum Status {
        Approved, Pending, Hold, Fraud, Bhulekh_Mismatch
    }

    @DBRef
    private PaymentCycle paymentCycle;

    @DBRef
    private SoilTest soilTest;

    @Indexed
    @DBRef
    private User agent;

    @Indexed
    private String managerId; // Id of the Manager to which the agent is assigned

    // For Agents
    private BigDecimal khasraRate;  // new farmer's first khasra Rate(35) or second khasra Rate(25)
    private CommissionRate.LocationType khasraRateLocationType;
    // For Managers
    private BigDecimal commissionRate;  // Commission Rate according to Location(State, District)
    private CommissionRate.LocationType commissionRateLocationType;

    private PaymentStatus paymentStatus;
    
    private Date paymentReceivedDate;

    private Address address;

    private Status status;

    private String remarks;

    private String paymentRemarks;

    private String transactionId;

    //to check payment of agent is of 15rs. or normal rate (BasicDetailPayment is 15 and soilTestPayment 40rs
    public enum PaymentType {
        BasicDetailPayment , SoilTestPayment
    }

    private PaymentType paymentType = PaymentType.SoilTestPayment;

    private String khasraNo;


    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public SoilTest getSoilTest() {
        return soilTest;
    }

    public void setSoilTest(SoilTest soilTest) {
        this.soilTest = soilTest;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public BigDecimal getKhasraRate() {
        return khasraRate;
    }

    public void setKhasraRate(BigDecimal khasraRate) { this.khasraRate = khasraRate; }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentReceivedDate() {
        return paymentReceivedDate;
    }

    public void setPaymentReceivedDate(Date paymentReceivedDate) {
        this.paymentReceivedDate = paymentReceivedDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentRemarks() {
        return paymentRemarks;
    }

    public void setPaymentRemarks(String paymentRemarks) {
        this.paymentRemarks = paymentRemarks;
    }

    public CommissionRate.LocationType getKhasraRateLocationType() {
        return khasraRateLocationType;
    }

    public void setKhasraRateLocationType(CommissionRate.LocationType khasraRateLocationType) { this.khasraRateLocationType = khasraRateLocationType; }

    public CommissionRate.LocationType getCommissionRateLocationType() {
        return commissionRateLocationType;
    }

    public void setCommissionRateLocationType(CommissionRate.LocationType commissionRateLocationType) { this.commissionRateLocationType = commissionRateLocationType; }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getKhasraNo() { return khasraNo; }

    public void setKhasraNo(String khasraNo) { this.khasraNo = khasraNo; }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
