package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.Village;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author anuragdhunna
 *
 * Villages assigned to a Agent for adding records
 */

public class AgentVillage {

    @DBRef
    private User agent;

    @DBRef
    private Village village;


    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }
}
