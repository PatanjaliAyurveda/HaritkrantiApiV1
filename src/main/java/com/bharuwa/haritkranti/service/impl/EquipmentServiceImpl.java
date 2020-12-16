package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.repositories.EquipmentRepo;
import com.bharuwa.haritkranti.models.Equipment;
import com.bharuwa.haritkranti.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author harman
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final MongoTemplate mongoTemplate;

    public EquipmentServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private EquipmentRepo equipmentRepo;

    @Override
    public Equipment addEquipment(Equipment equipment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(equipment.getName()).and("equipmentType").is(equipment.getEquipmentType()));
        Equipment value = mongoTemplate.findOne(query,Equipment.class);

        if (value ==  null){
            equipmentRepo.save(equipment);
        }else {
            value.setName(equipment.getName());
            value.setEquipmentType(equipment.getEquipmentType());
            value.setDescription(equipment.getDescription());
            equipmentRepo.save(value);
        }

        return equipment;
    }

    @Override
    public List<Equipment> getMoveableEquipments() {
        Query query = new Query();
        query.addCriteria(Criteria.where("equipmentType").is(Equipment.EquipmentType.Moveable.toString()));
        return mongoTemplate.find(query, Equipment.class);
    }

    @Override
    public List<Equipment> getImmoveableEquipments() {
        Query query = new Query();
        query.addCriteria(Criteria.where("equipmentType").is(Equipment.EquipmentType.Immoveable.toString()));
        return mongoTemplate.find(query, Equipment.class);
    }

    @Override
    public Equipment createEquipment(Equipment equipment) {
        return mongoTemplate.save(equipment);
    }

    @Override
    public Equipment getEquipmentByNameAndType(String equipmentName, String equipmentType ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(equipmentName).and("equipmentType").is(equipmentType));
        return mongoTemplate.findOne(query, Equipment.class);
    }
}
