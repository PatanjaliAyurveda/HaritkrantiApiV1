package com.bharuwa.haritkranti.models.requestModels;

/**
 * @author harman
 */
public class GovernmentSchemeStatus {

    private String schemeId;

    private boolean status;

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
