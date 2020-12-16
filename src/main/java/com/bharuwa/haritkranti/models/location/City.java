package com.bharuwa.haritkranti.models.location;

import com.bharuwa.haritkranti.models.BaseObject;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;

/**
 * @author anuragdhunna
 */
public class City extends BaseObject {

    @NotNull(message = "City name must not be null")
    @Indexed(unique = true)
    private String name;

    @DBRef
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
