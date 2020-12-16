package com.bharuwa.haritkranti.models.location;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.StateName;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

/**
 * @author anuragdhunna
 */
public class State extends BaseObject implements StateName{

    public State() { }

    public State(String name) {
        this.name = name;
    }

    @NotNull(message = "State name must not be null")
    @Indexed(unique = true)
    private String name;

    private String stateAbbreviation;

//    private BigDecimal availableNutrientNitrogen;
//    private BigDecimal availableNutrientPhosphrous;
//    private BigDecimal availableNutrientPotash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }
}
