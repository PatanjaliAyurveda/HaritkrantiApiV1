package com.bharuwa.haritkranti.models.location;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.bharuwa.haritkranti.models.BaseObject;

import javax.validation.constraints.NotNull;

/**
 * @author sunaina
 */
public class BlockInHindi extends BaseObject {

    @NotNull(message = "Block name must not be null")
    @Indexed(unique = true)
    private String name;

    @DBRef
    private TehsilInHindi tehsil;

    @DBRef
    private CityInHindi city;

    @DBRef
    private StateInHindi state;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TehsilInHindi getTehsil() {
		return tehsil;
	}

	public void setTehsil(TehsilInHindi tehsil) {
		this.tehsil = tehsil;
	}

	public CityInHindi getCity() {
		return city;
	}

	public void setCity(CityInHindi city) {
		this.city = city;
	}

	public StateInHindi getState() {
		return state;
	}

	public void setState(StateInHindi state) {
		this.state = state;
	}
	
}
