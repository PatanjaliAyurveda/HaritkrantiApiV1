package com.bharuwa.haritkranti.models.crops;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.Locale;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author anuragdhunna
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "name_type", unique = true, def = "{'cropName' : 1, 'cropType': 1}")
})
public class Crop extends BaseObject {

    public Crop() {
    }

    public Crop(String cropName, CropType cropType) {
        this.cropName = cropName;
        this.cropType = cropType;
    }

    public Crop(String cropName) {
        this.cropName = cropName;
      //  this.cropType = cropType;
    }
    
    public Crop(String cropName, CropType cropType, List<Fertilizer> fertilizers) {
        this.cropName = cropName;
        this.cropType = cropType;
        this.fertilizers = fertilizers;
    }
    public enum Type {
        Crop , Fruit;
    }

    //TODO: Remove after implementing Fruit Recommendation
    // Modify APIs
    @Deprecated()
    public enum CropType {
        Cereal, Millets, Cash, Oil_Seed,
        Pulses, Vegetable, Fodder,
        Fruit, Spices, Medicinal_And_Aromatic_Plant;

    }

    public enum CropSeason {
        Rabi, Kharif, Zaid
    }

    private String cropName;

    @NotNull(message = "CropType name must not be null")
    private CropType cropType = CropType.Cereal;

    private Type type = Type.Crop;

    @DBRef
    private CropGroup cropGroup;

    @DBRef
    private List<Fertilizer> fertilizers = new ArrayList<>();

    @Indexed
    private List<CropSeason> cropSeasons;

    private Set<Locale> locales;

    public void addCropSeason(CropSeason cropSeason) {
        if (cropSeason == null) return;
        if (cropSeasons == null) cropSeasons = new ArrayList<>();
//        roles.clear();
        cropSeasons.add(cropSeason);
    }

    public void removeCropSeason(CropSeason cropSeason) {
        if (cropSeason == null || getCropSeasons() == null || getCropSeasons().isEmpty()) return;
        getCropSeasons().remove(cropSeason);
    }

    public void addLocale(Locale locale) {
        if (locale == null) return;
        if (getLocales() == null) {
            this.locales = new LinkedHashSet<>();
        }
        this.locales.add(locale);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public List<Fertilizer> getFertilizers() {
        return fertilizers;
    }

    public void setFertilizers(List<Fertilizer> fertilizers) {
        this.fertilizers = fertilizers;
    }

    public List<CropSeason> getCropSeasons() {
        return cropSeasons;
    }

    public void setCropSeasons(List<CropSeason> cropSeasons) {
        this.cropSeasons = cropSeasons;
    }

    public Set<Locale> getLocales() {
        return locales;
    }

    public void setLocales(Set<Locale> locales) {
        this.locales = locales;
    }

    public CropGroup getCropGroup() {
        return cropGroup;
    }

    public void setCropGroup(CropGroup cropGroup) {
        this.cropGroup = cropGroup;
    }
}
