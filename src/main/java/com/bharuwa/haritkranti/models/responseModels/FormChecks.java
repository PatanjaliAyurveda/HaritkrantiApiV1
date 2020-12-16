package com.bharuwa.haritkranti.models.responseModels;

/**
 * @author anuragdhunna
 */
public class FormChecks {

    private boolean personInformation;
    private boolean landDetails;
    private boolean cropDetails;
    private boolean farmOwnerShipDetails;
    private boolean equipments;
    private boolean financialDetails;
    private boolean reports;

    public boolean isPersonInformation() {
        return personInformation;
    }

    public void setPersonInformation(boolean personInformation) {
        this.personInformation = personInformation;
    }

    public boolean isLandDetails() {
        return landDetails;
    }

    public void setLandDetails(boolean landDetails) {
        this.landDetails = landDetails;
    }

    public boolean isCropDetails() {
        return cropDetails;
    }

    public void setCropDetails(boolean cropDetails) {
        this.cropDetails = cropDetails;
    }

    public boolean isFarmOwnerShipDetails() {
        return farmOwnerShipDetails;
    }

    public void setFarmOwnerShipDetails(boolean farmOwnerShipDetails) {
        this.farmOwnerShipDetails = farmOwnerShipDetails;
    }

    public boolean isEquipments() {
        return equipments;
    }

    public void setEquipments(boolean equipments) {
        this.equipments = equipments;
    }

    public boolean isFinancialDetails() {
        return financialDetails;
    }

    public void setFinancialDetails(boolean financialDetails) {
        this.financialDetails = financialDetails;
    }

    public boolean isReports() {
        return reports;
    }

    public void setReports(boolean reports) {
        this.reports = reports;
    }
}
