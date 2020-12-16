package com.bharuwa.haritkranti.models;

import java.util.Date;

/**
 * @author harman
 */
public class UserCertificate extends BaseObject{

    public enum CertificateType{
        Land_Certificate, Crop_Certificate
    }

    private String userId;

    private String agentId;

    private String khasraNo;

    private CertificateType certificateType = CertificateType.Land_Certificate;

    private String certificateAgency;

    private String certificateNumber;

    private String duration;

    private Date certificateValidity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateAgency() {
        return certificateAgency;
    }

    public void setCertificateAgency(String certificateAgency) {
        this.certificateAgency = certificateAgency;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getCertificateValidity() {
        return certificateValidity;
    }

    public void setCertificateValidity(Date certificateValidity) {
        this.certificateValidity = certificateValidity;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }
}
