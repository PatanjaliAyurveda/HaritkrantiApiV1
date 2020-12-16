package com.bharuwa.haritkranti.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author harman
 */
@Document
public class Equipment extends BaseObject {

    public Equipment() {
    }

    public enum EquipmentType {
        Moveable, Immoveable
    }

    private String name;

    private String description;

    private Equipment.EquipmentType equipmentType = EquipmentType.Moveable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Equipment(String name, EquipmentType equipmentType) {
        this.name = name;
        this.equipmentType = equipmentType;
    }
}
