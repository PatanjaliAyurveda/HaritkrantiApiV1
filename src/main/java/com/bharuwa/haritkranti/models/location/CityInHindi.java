package com.bharuwa.haritkranti.models.location;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.bharuwa.haritkranti.models.BaseObject;

import javax.validation.constraints.NotNull;


/**
 * @author sunaina
 */
public class CityInHindi extends BaseObject {

    @NotNull(message = "City name must not be null")
    @Indexed(unique = true)
    private String name;

    @DBRef
    private StateInHindi state;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StateInHindi getState() {
		return state;
	}

	public void setState(StateInHindi state) {
		this.state = state;
	}
	
}
