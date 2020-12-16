package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
@Document
public class UserEquipments extends BaseObject {

    private String userId;

    private List<Equipment> moveableEquipments = new ArrayList<>();
    private List<Equipment> immoveableEquipments = new ArrayList<>();

    // for location search (filter)
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Equipment> getMoveableEquipments() {
        return moveableEquipments;
    }

    public void setMoveableEquipments(List<Equipment> moveableEquipments) {
        this.moveableEquipments = moveableEquipments;
    }

    public List<Equipment> getImmoveableEquipments() {
        return immoveableEquipments;
    }

    public void setImmoveableEquipments(List<Equipment> immoveableEquipments) {
        this.immoveableEquipments = immoveableEquipments;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
