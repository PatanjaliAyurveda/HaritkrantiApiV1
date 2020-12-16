package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.GovernmentSchemes;

/**
 * @author harman
 */
public class UserAdoptedSchemeResponse {

    private GovernmentSchemes governmentSchemes;

    private boolean status;

    public GovernmentSchemes getGovernmentSchemes() {
        return governmentSchemes;
    }

    public void setGovernmentSchemes(GovernmentSchemes governmentSchemes) {
        this.governmentSchemes = governmentSchemes;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
