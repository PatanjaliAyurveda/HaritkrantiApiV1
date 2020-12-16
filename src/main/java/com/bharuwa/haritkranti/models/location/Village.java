package com.bharuwa.haritkranti.models.location;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;

/**
 * @author harman
 */
public class Village extends BaseObject {

    @NotNull(message = "Village name must not be null")
    @Indexed(unique = false)
    private String name;

    @DBRef
    private State state;

    @DBRef
    private City city;

    @DBRef
    private Tehsil tehsil;

    @DBRef
    private Block block;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
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
