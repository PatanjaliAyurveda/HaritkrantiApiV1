package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.Tehsil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author anuragdhunna
 */
public class Address {

    private static final String DEFAULT_COUNTRY = "IND";

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
    
    private String village;

    private String address;

    private String zipCode = "";
    
    private String country = DEFAULT_COUNTRY;
    
    private String userTehsil;
    
    private String userBlock;

    public static String getDefaultCountry() {
        return DEFAULT_COUNTRY;
    }

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

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

	public String getUserTehsil() {
		return userTehsil;
	}

	public void setUserTehsil(String userTehsil) {
		this.userTehsil = userTehsil;
	}

	public String getUserBlock() {
		return userBlock;
	}

	public void setUserBlock(String userBlock) {
		this.userBlock = userBlock;
	}

}
