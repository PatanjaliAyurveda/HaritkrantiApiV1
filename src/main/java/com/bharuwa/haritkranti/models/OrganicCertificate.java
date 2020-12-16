package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
public class OrganicCertificate extends BaseObject {
    
    public enum SeedType {
        GMO, Pestiside, Chemically_Treated
    }

    private String agentId;
    private String userId;
    private String khasraNo;
    private boolean organicLandEligible;
    private int duration;
    private SeedType seedType = SeedType.GMO;
    private boolean confirmOC;
    private boolean floodThisYear;
    private boolean floodAlternativeYearOrFloodedArea;
    private boolean oneSideOpenBufferZone3SideMoreThan10Acre;
    private boolean twoSideOpenBufferZone2SideMoreThan5Acre;
    private boolean threeSideOpenBufferZone1SideMoreThan3Acre;
    private boolean allSideOpenMoreThan1Acre;
    private boolean fourSideBoundaryNotWaterLogged;
    private boolean heavyMetalIndustry;
    private boolean heavyMetalWithIn10Km;
    private boolean heavyMetalMoreThan10km;
    private boolean sewagePlantOrSewageWaterAffectedField;
    private boolean sewageWithIn5km;
    private boolean sewageMoreThan5km;
    private boolean fiftyAcreCluster;
    private boolean virginLand;              //new cultivated land/forest land/pasture land

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

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

    public boolean isOrganicLandEligible() {
        return organicLandEligible;
    }

    public void setOrganicLandEligible(boolean organicLandEligible) {
        this.organicLandEligible = organicLandEligible;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public SeedType getSeedType() {
        return seedType;
    }

    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }

    public boolean isConfirmOC() {
        return confirmOC;
    }

    public void setConfirmOC(boolean confirmOC) {
        this.confirmOC = confirmOC;
    }

    public boolean isFloodThisYear() {
        return floodThisYear;
    }

    public void setFloodThisYear(boolean floodThisYear) {
        this.floodThisYear = floodThisYear;
    }

    public boolean isFloodAlternativeYearOrFloodedArea() {
        return floodAlternativeYearOrFloodedArea;
    }

    public void setFloodAlternativeYearOrFloodedArea(boolean floodAlternativeYearOrFloodedArea) {
        this.floodAlternativeYearOrFloodedArea = floodAlternativeYearOrFloodedArea;
    }

    public boolean isOneSideOpenBufferZone3SideMoreThan10Acre() {
        return oneSideOpenBufferZone3SideMoreThan10Acre;
    }

    public void setOneSideOpenBufferZone3SideMoreThan10Acre(boolean oneSideOpenBufferZone3SideMoreThan10Acre) {
        this.oneSideOpenBufferZone3SideMoreThan10Acre = oneSideOpenBufferZone3SideMoreThan10Acre;
    }

    public boolean isTwoSideOpenBufferZone2SideMoreThan5Acre() {
        return twoSideOpenBufferZone2SideMoreThan5Acre;
    }

    public void setTwoSideOpenBufferZone2SideMoreThan5Acre(boolean twoSideOpenBufferZone2SideMoreThan5Acre) {
        this.twoSideOpenBufferZone2SideMoreThan5Acre = twoSideOpenBufferZone2SideMoreThan5Acre;
    }

    public boolean isThreeSideOpenBufferZone1SideMoreThan3Acre() {
        return threeSideOpenBufferZone1SideMoreThan3Acre;
    }

    public void setThreeSideOpenBufferZone1SideMoreThan3Acre(boolean threeSideOpenBufferZone1SideMoreThan3Acre) {
        this.threeSideOpenBufferZone1SideMoreThan3Acre = threeSideOpenBufferZone1SideMoreThan3Acre;
    }

    public boolean isAllSideOpenMoreThan1Acre() {
        return allSideOpenMoreThan1Acre;
    }

    public void setAllSideOpenMoreThan1Acre(boolean allSideOpenMoreThan1Acre) {
        this.allSideOpenMoreThan1Acre = allSideOpenMoreThan1Acre;
    }

    public boolean isFourSideBoundaryNotWaterLogged() {
        return fourSideBoundaryNotWaterLogged;
    }

    public void setFourSideBoundaryNotWaterLogged(boolean fourSideBoundaryNotWaterLogged) {
        this.fourSideBoundaryNotWaterLogged = fourSideBoundaryNotWaterLogged;
    }

    public boolean isHeavyMetalIndustry() {
        return heavyMetalIndustry;
    }

    public void setHeavyMetalIndustry(boolean heavyMetalIndustry) {
        this.heavyMetalIndustry = heavyMetalIndustry;
    }

    public boolean isHeavyMetalWithIn10Km() {
        return heavyMetalWithIn10Km;
    }

    public void setHeavyMetalWithIn10Km(boolean heavyMetalWithIn10Km) {
        this.heavyMetalWithIn10Km = heavyMetalWithIn10Km;
    }

    public boolean isHeavyMetalMoreThan10km() {
        return heavyMetalMoreThan10km;
    }

    public void setHeavyMetalMoreThan10km(boolean heavyMetalMoreThan10km) {
        this.heavyMetalMoreThan10km = heavyMetalMoreThan10km;
    }

    public boolean isSewagePlantOrSewageWaterAffectedField() {
        return sewagePlantOrSewageWaterAffectedField;
    }

    public void setSewagePlantOrSewageWaterAffectedField(boolean sewagePlantOrSewageWaterAffectedField) {
        this.sewagePlantOrSewageWaterAffectedField = sewagePlantOrSewageWaterAffectedField;
    }

    public boolean isSewageWithIn5km() {
        return sewageWithIn5km;
    }

    public void setSewageWithIn5km(boolean sewageWithIn5km) {
        this.sewageWithIn5km = sewageWithIn5km;
    }

    public boolean isSewageMoreThan5km() {
        return sewageMoreThan5km;
    }

    public void setSewageMoreThan5km(boolean sewageMoreThan5km) {
        this.sewageMoreThan5km = sewageMoreThan5km;
    }

    public boolean isFiftyAcreCluster() {
        return fiftyAcreCluster;
    }

    public void setFiftyAcreCluster(boolean fiftyAcreCluster) {
        this.fiftyAcreCluster = fiftyAcreCluster;
    }

    public boolean isVirginLand() {
        return virginLand;
    }

    public void setVirginLand(boolean virginLand) {
        this.virginLand = virginLand;
    }
}