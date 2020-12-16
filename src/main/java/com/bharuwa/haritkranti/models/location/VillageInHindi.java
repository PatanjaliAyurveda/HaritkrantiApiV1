package com.bharuwa.haritkranti.models.location;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.bharuwa.haritkranti.models.BaseObject;

import javax.validation.constraints.NotNull;


/**
 * @author sunaina
 */
public class VillageInHindi extends BaseObject {

    @NotNull(message = "Village name must not be null")
    @Indexed(unique = false)
    private String name;

    @DBRef
    private StateInHindi state;

    @DBRef
    private CityInHindi city;

    @DBRef
    private TehsilInHindi tehsil;

    @DBRef
    private BlockInHindi block;

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

	public CityInHindi getCity() {
		return city;
	}

	public void setCity(CityInHindi city) {
		this.city = city;
	}

	public TehsilInHindi getTehsil() {
		return tehsil;
	}

	public void setTehsil(TehsilInHindi tehsil) {
		this.tehsil = tehsil;
	}

	public BlockInHindi getBlock() {
		return block;
	}

	public void setBlock(BlockInHindi block) {
		this.block = block;
	}

}
