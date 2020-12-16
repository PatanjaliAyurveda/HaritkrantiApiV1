package com.bharuwa.haritkranti.models.responseModels;

/**
 * @author anuragdhunna
 */
public class ManagerFarmerCount {

    private long stateManagerCount;
    private long districtManagerCount;
    private long agentManagerCount;
    private long agentCount;
    private long farmerCount;

    public long getDistrictManagerCount() {
        return districtManagerCount;
    }

    public void setDistrictManagerCount(long districtManagerCount) {
        this.districtManagerCount = districtManagerCount;
    }

    public long getAgentManagerCount() {
        return agentManagerCount;
    }

    public void setAgentManagerCount(long agentManagerCount) {
        this.agentManagerCount = agentManagerCount;
    }

    public long getFarmerCount() {
        return farmerCount;
    }

    public void setFarmerCount(long farmerCount) {
        this.farmerCount = farmerCount;
    }

    public long getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(long agentCount) {
        this.agentCount = agentCount;
    }

    public long getStateManagerCount() {
        return stateManagerCount;
    }

    public void setStateManagerCount(long stateManagerCount) {
        this.stateManagerCount = stateManagerCount;
    }
}
