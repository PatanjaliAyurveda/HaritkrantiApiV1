package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.bharuwa.haritkranti.models.location.Block;
import com.bharuwa.haritkranti.models.location.BlockInHindi;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.CityInHindi;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.StateInHindi;
import com.bharuwa.haritkranti.models.location.Tehsil;
import com.bharuwa.haritkranti.models.location.TehsilInHindi;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.location.VillageInHindi;

public class AddressInHindiForAppUser {
	@DBRef
    @Indexed
    private StateInHindi state;
    
    @DBRef
    @Indexed
    private CityInHindi city;
    
    @DBRef
    @Indexed
    private TehsilInHindi tehsil;
    
    @DBRef
    @Indexed
    private BlockInHindi block;
    
    @DBRef
    @Indexed
    private VillageInHindi villageModel;

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

	public VillageInHindi getVillageModel() {
		return villageModel;
	}

	public void setVillageModel(VillageInHindi villageModel) {
		this.villageModel = villageModel;
	}
    
}
