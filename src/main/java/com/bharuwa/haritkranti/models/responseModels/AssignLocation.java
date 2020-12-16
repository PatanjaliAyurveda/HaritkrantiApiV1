package com.bharuwa.haritkranti.models.responseModels;

import com.bharuwa.haritkranti.models.BaseObject;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.location.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public  class AssignLocation extends BaseObject {

    @DBRef
    private User user;

    private String roleName;

    @DBRef
    private State state;

    @DBRef
    private City district;

    @DBRef
    private Tehsil tehsil;

    @DBRef
    private Village village;

    @DBRef
    private Block block;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getDistrict() {
        return district;
    }

    public Tehsil getTehsil() {
        return tehsil;
    }

    public void setTehsil(Tehsil tehsil) {
        this.tehsil = tehsil;
    }

    public void setDistrict(City district) {
        this.district = district;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}


