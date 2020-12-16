package com.bharuwa.haritkranti.models;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class FarmerExtraDetails extends BaseObject {

    private String farmerId;

    private BigDecimal totalLandSize;
    
    private BigDecimal agricultureLandSize;

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public BigDecimal getTotalLandSize() {
        return totalLandSize;
    }

    public void setTotalLandSize(BigDecimal totalLandSize) {
        this.totalLandSize = totalLandSize;
    }

	public BigDecimal getAgricultureLandSize() {
		return agricultureLandSize;
	}

	public void setAgricultureLandSize(BigDecimal agricultureLandSize) {
		this.agricultureLandSize = agricultureLandSize;
	}
    
}
