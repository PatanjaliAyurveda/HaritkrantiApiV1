package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.Equipment;

import java.util.List;

/**
 * @author harman
 */
public interface EquipmentService {

    Equipment addEquipment(Equipment equipment);

    List<Equipment> getMoveableEquipments();

    List<Equipment> getImmoveableEquipments();

    Equipment createEquipment(Equipment equipment);

    Equipment getEquipmentByNameAndType(String equipmentName, String equipmentType);
}
