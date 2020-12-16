package com.bharuwa.haritkranti.models.location;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;

/**
 * @author harman
 */
public class Block extends BaseObject {

    @NotNull(message = "Block name must not be null")
    @Indexed(unique = true)
    private String name;

    @DBRef
    private Tehsil tehsil;

    @DBRef
    private City city;

    @DBRef
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tehsil getTehsil() {
        return tehsil;
    }

    public void setTehsil(Tehsil tehsil) {
        this.tehsil = tehsil;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
