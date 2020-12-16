package com.bharuwa.haritkranti.models.responseModels;

/**
 * @author harman
 */
public class UserResponse {

    private String name;

    private String fatherName;

    private String primaryPhone;

    private String state;

    private String district;

    private String tehsil;

    private String block;

    private String village;

    private String address;

    private String organicCertificateCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganicCertificateCode() {
        return organicCertificateCode;
    }

    public void setOrganicCertificateCode(String organicCertificateCode) {
        this.organicCertificateCode = organicCertificateCode;
    }
}
