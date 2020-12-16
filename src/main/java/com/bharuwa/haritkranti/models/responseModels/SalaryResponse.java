package com.bharuwa.haritkranti.models.responseModels;

import java.math.BigDecimal;

/**
 * @author harman
 */
public class SalaryResponse {

    private BigDecimal basicSalary;

    private BigDecimal commission;

    private BigDecimal totalSalary;

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }
}
