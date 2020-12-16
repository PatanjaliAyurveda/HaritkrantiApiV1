package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.crops.Crop;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author anuragdhunna
 */
@Document
public class CropRate {

    @DBRef
    private Crop crop;

    private String year;

    private BigDecimal price;

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
