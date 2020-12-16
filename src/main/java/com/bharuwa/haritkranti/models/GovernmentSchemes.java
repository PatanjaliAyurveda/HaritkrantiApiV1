package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.State;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author harman
 */
public class GovernmentSchemes extends BaseObject {

    private String schemeName;

    private String schemeCode;

    private String schemeSponser;

    private boolean isCentral = false;

    @DBRef
    private List<State> states;

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSchemeSponser() {
        return schemeSponser;
    }

    public void setSchemeSponser(String schemeSponser) {
        this.schemeSponser = schemeSponser;
    }

    public boolean isCentral() {
        return isCentral;
    }

    public void setCentral(boolean central) {
        isCentral = central;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
