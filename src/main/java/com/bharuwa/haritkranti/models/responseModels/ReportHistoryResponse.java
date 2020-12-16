package com.bharuwa.haritkranti.models.responseModels;

import java.util.Date;

/**
 * @author harman
 */
public class ReportHistoryResponse {

    private String reportId;

    private String userId;

    private String soilReportNumber;

    private String khasraNo;

    private String farmingType;

    private String cropName;

    private String cropType;

    private String cropSeason;

    private Date cropSowingDate;

    // date of record creation
    private Date creationDate;


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getSoilReportNumber() {
        return soilReportNumber;
    }

    public void setSoilReportNumber(String soilReportNumber) {
        this.soilReportNumber = soilReportNumber;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getFarmingType() {
        return farmingType;
    }

    public void setFarmingType(String farmingType) {
        this.farmingType = farmingType;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropSeason() {
        return cropSeason;
    }

    public void setCropSeason(String cropSeason) {
        this.cropSeason = cropSeason;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Date getCropSowingDate() {
        return cropSowingDate;
    }

    public void setCropSowingDate(Date cropSowingDate) {
        this.cropSowingDate = cropSowingDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
