package com.bharuwa.haritkranti.models.fertilizerModels;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.Locale;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author anuragdhunna
 */
public class Fertilizer extends BaseObject {

    public Fertilizer() {
    }

    public Fertilizer(String name, CategoryType categoryType, SubFarmingType.FertilizerType fertilizerType) {
        super();
        this.name = name;
        this.categoryType = categoryType;
        this.fertilizerType = fertilizerType;
    }

    public Fertilizer(String name, Fertilizer.CategoryType categoryType, SubFarmingType.FertilizerType farmingType, double nRatio, double pRatio, double kRatio) {
        super();
        this.name = name;
        this.categoryType = categoryType;
        this.fertilizerType = farmingType;
        this.nRatio = nRatio;
        this.pRatio = pRatio;
        this.kRatio = kRatio;
    }

    public Fertilizer(String name, Fertilizer.CategoryType categoryType, SubFarmingType.FertilizerType farmingType, double nRatio, double pRatio, double kRatio,
                      BigDecimal quantityGood) {
        super();
        this.name = name;
        this.categoryType = categoryType;
        this.fertilizerType = farmingType;
        this.nRatio = nRatio;
        this.pRatio = pRatio;
        this.kRatio = kRatio;
        this.quantityGood = quantityGood;
    }

    public Fertilizer(String name, CategoryType categoryType, SubFarmingType.FertilizerType farmingType, double nRatio, double pRatio, double kRatio,
                      BigDecimal quantityGood, BigDecimal quantityMedium, BigDecimal quantityPoor) {
        super();
        this.name = name;
        this.categoryType = categoryType;
        this.fertilizerType = farmingType;
        this.nRatio = nRatio;
        this.pRatio = pRatio;
        this.kRatio = kRatio;
        this.quantityGood = quantityGood;
        this.quantityMedium = quantityMedium;
        this.quantityPoor = quantityPoor;
    }

    public Fertilizer(String name, SubFarmingType.FertilizerType fertilizerType) {
        this.name = name;
        this.fertilizerType = fertilizerType;
    }

    public enum CategoryType {
        Nitrogen, Phosphorus, Potash, OtherBio,

        //
        FYM, BothOM, OtherOM,

        // Chemical and Bio
        Nitrogenus, Potassic, Phosphatic, Complex,

        // Organic(Patanjali Manure)
        PatanjaliManure , Manure, Compost, OilCake, GreenManure, Jivamrit
    }

    public enum Unit {
        KG, L, T, SPRAY, Q, NUM, GM
    }

    @NotNull(message = "Fertilizer name must not be null")
    @Indexed(unique = true)
    private String name;

    private CategoryType categoryType = CategoryType.Nitrogen;
    private SubFarmingType.FertilizerType fertilizerType = SubFarmingType.FertilizerType.Bio;
//    private List<Crop> cropList;

    private double nRatio;
    private double pRatio;
    private double kRatio;

    private BigDecimal quantityGood = BigDecimal.ZERO;
    private BigDecimal quantityMedium = BigDecimal.ZERO;
    private BigDecimal quantityPoor = BigDecimal.ZERO;

    private BigDecimal irrigated = BigDecimal.ZERO;
    private BigDecimal semiIrrigated = BigDecimal.ZERO;
    private BigDecimal rainfed = BigDecimal.ZERO;

    //    unique fertId String
    private String fertId;
    private Unit unit = Unit.KG;

    private Set<Locale> locales;


    public void addLocale(Locale locale) {
        if (locale == null) return;
        if (getLocales() == null) {
            this.locales = new LinkedHashSet<>();
        }
        this.locales.add(locale);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public SubFarmingType.FertilizerType getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(SubFarmingType.FertilizerType fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

//    public List<Crop> getCropList() {
//        return cropList;
//    }
//
//    public void setCropList(List<Crop> cropList) {
//        this.cropList = cropList;
//    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    public double getnRatio() {
        return nRatio;
    }

    public void setnRatio(double nRatio) {
        this.nRatio = nRatio;
    }

    public double getpRatio() {
        return pRatio;
    }

    public void setpRatio(double pRatio) {
        this.pRatio = pRatio;
    }

    public double getkRatio() {
        return kRatio;
    }

    public void setkRatio(double kRatio) {
        this.kRatio = kRatio;
    }

    public BigDecimal getQuantityGood() {
        return quantityGood;
    }

    public void setQuantityGood(BigDecimal quantityGood) {
        this.quantityGood = quantityGood;
    }

    public BigDecimal getQuantityMedium() {
        return quantityMedium;
    }

    public void setQuantityMedium(BigDecimal quantityMedium) {
        this.quantityMedium = quantityMedium;
    }

    public BigDecimal getQuantityPoor() {
        return quantityPoor;
    }

    public void setQuantityPoor(BigDecimal quantityPoor) {
        this.quantityPoor = quantityPoor;
    }

    public BigDecimal getIrrigated() {
        return irrigated;
    }

    public void setIrrigated(BigDecimal irrigated) {
        this.irrigated = irrigated;
    }

    public BigDecimal getSemiIrrigated() {
        return semiIrrigated;
    }

    public void setSemiIrrigated(BigDecimal semiIrrigated) {
        this.semiIrrigated = semiIrrigated;
    }

    public BigDecimal getRainfed() {
        return rainfed;
    }

    public void setRainfed(BigDecimal rainfed) {
        this.rainfed = rainfed;
    }

    public String getFertId() {
        return fertId;
    }

    public void setFertId(String fertId) {
        this.fertId = fertId;
    }

    public Set<Locale> getLocales() {
        return locales;
    }

    public void setLocales(Set<Locale> locales) {
        this.locales = locales;
    }
}
