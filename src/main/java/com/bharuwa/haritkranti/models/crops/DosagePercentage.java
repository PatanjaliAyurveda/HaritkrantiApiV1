package com.bharuwa.haritkranti.models.crops;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
public class DosagePercentage {

    public DosagePercentage() {
    }

    public DosagePercentage(TimeUnit timeUnit, int time, BigDecimal fymPercentage, BigDecimal nPercentage, BigDecimal pPercentage, BigDecimal kPercentage) {
        this.timeUnit = timeUnit;
        this.time = time;
        this.fymPercentage = fymPercentage;
        this.nPercentage = nPercentage;
        this.pPercentage = pPercentage;
        this.kPercentage = kPercentage;
    }

    public enum TimeUnit {
        Month, Year
    }

    private TimeUnit timeUnit;
    private int time; // Ex: 1 year, 1 month

    // Percentages
    private BigDecimal fymPercentage = BigDecimal.ZERO;
    private BigDecimal nPercentage = BigDecimal.ZERO;
    private BigDecimal pPercentage = BigDecimal.ZERO;
    private BigDecimal kPercentage = BigDecimal.ZERO;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public BigDecimal getnPercentage() {
        return nPercentage;
    }

    public void setnPercentage(BigDecimal nPercentage) {
        this.nPercentage = nPercentage;
    }

    public BigDecimal getpPercentage() {
        return pPercentage;
    }

    public void setpPercentage(BigDecimal pPercentage) {
        this.pPercentage = pPercentage;
    }

    public BigDecimal getkPercentage() {
        return kPercentage;
    }

    public void setkPercentage(BigDecimal kPercentage) {
        this.kPercentage = kPercentage;
    }

    public BigDecimal getFymPercentage() {
        return fymPercentage;
    }

    public void setFymPercentage(BigDecimal fymPercentage) {
        this.fymPercentage = fymPercentage;
    }
}
