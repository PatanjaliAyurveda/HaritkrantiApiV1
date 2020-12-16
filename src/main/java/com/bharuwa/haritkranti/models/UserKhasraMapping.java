package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bharuwa.haritkranti.models.location.Block;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.Tehsil;
import com.bharuwa.haritkranti.models.location.Village;

@Document(collection = "userKhasraMapping")
public class UserKhasraMapping {
	@DBRef
    @Indexed
    private State state;
    
    @DBRef
    @Indexed
    private City city;
    
    @DBRef
    @Indexed
    private Tehsil tehsil;
    
    @DBRef
    @Indexed
    private Block block;
    
    @DBRef
    @Indexed
    private Village villageModel;
    
    private String khasraNumber;
    
    private String userId;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Tehsil getTehsil() {
		return tehsil;
	}

	public void setTehsil(Tehsil tehsil) {
		this.tehsil = tehsil;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Village getVillageModel() {
		return villageModel;
	}

	public void setVillageModel(Village villageModel) {
		this.villageModel = villageModel;
	}

	public String getKhasraNumber() {
		return khasraNumber;
	}

	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
   
}
