package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public class Soil extends BaseObject {

    public Soil() {
    }

    public Soil(String soilName) {
        super();
        this.soilName = soilName;
    }

//    public Soil(String soilName, List<LandType> landTypes, List<Crop> cropList) {
//        this.soilName = soilName;
//        this.landTypes = landTypes;
//        this.cropList = cropList;
//    }

    @NotNull(message = "Soil name must not be null")
    @Indexed(unique = true)
    private String soilName;
//    private List<LandType> landTypes = new ArrayList<>();

    private String imageUrl;

//    private List<Crop> cropList;

    private Set<Locale> locales;

    public void addLocale(Locale locale) {
        if (locale == null) return;
        if (getLocales() == null) {
            this.locales = new LinkedHashSet<>();
        }
        this.locales.add(locale);
    }

    public String getSoilName() {
        return soilName;
    }

    public void setSoilName(String soilName) {
        this.soilName = soilName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //    public List<Crop> getCropList() {
//        return cropList;
//    }

//    public void setCropList(List<Crop> cropList) {
//        this.cropList = cropList;
//    }


    public Set<Locale> getLocales() {
        return locales;
    }

    public void setLocales(Set<Locale> locales) {
        this.locales = locales;
    }
}

