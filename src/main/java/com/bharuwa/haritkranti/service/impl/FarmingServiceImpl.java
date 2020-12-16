package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.repositories.fertilizerRepositories.FarmingTypeRepo;
import com.bharuwa.haritkranti.repositories.fertilizerRepositories.FertilizerRepo;
import com.bharuwa.haritkranti.repositories.fertilizerRepositories.SubFarmingTypeRepo;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.fertilizerModels.SubFarmingType;
import com.bharuwa.haritkranti.models.responseModels.FertilizerType;
import com.bharuwa.haritkranti.service.FarmingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anuragdhunna
 */
@Service
public class FarmingServiceImpl implements FarmingService {

    private final MongoTemplate mongoTemplate;

    public FarmingServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private FarmingTypeRepo farmingTypeRepo;

    @Autowired
    private SubFarmingTypeRepo subFarmingTypeRepo;

    @Autowired
    private FertilizerRepo fertilizerRepo;

    @Override
    public FarmingType storeFarmingType(FarmingType farmingType) {
        return farmingTypeRepo.save(farmingType);
    }

    @Override
    public void createDefaultFarmingType(FarmingType farmingType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("farmingType").is(farmingType.getFarmingType()));
        FarmingType value = mongoTemplate.findOne(query, FarmingType.class);
        if (value == null) {
            farmingTypeRepo.save(farmingType);
        }
    }

    @Override
    public List<FarmingType> getFarmingTypes() {
        return farmingTypeRepo.findAll();
    }

    @Override
    public SubFarmingType storeSubFarmingType(SubFarmingType subFarmingType) {
        return subFarmingTypeRepo.save(subFarmingType);
    }

    @Override
    public void createDefaultSubFarmingType(SubFarmingType subFarmingType) {

        System.out.println("========="+subFarmingType.getFertilizerType());
        Query query = new Query();
        query.addCriteria(Criteria.where("fertilizerType").is(subFarmingType.getFertilizerType()));
        SubFarmingType value = mongoTemplate.findOne(query, SubFarmingType.class);

        System.out.println("value=========="+value);
        if (value == null) {
            subFarmingTypeRepo.save(subFarmingType);
        }
    }

    @Override
    public List<SubFarmingType> getSubFarmingTypes() {
        return subFarmingTypeRepo.findAll();
    }

    @Override
    public Fertilizer storeFertilizerType(Fertilizer fertilizer) {
        return fertilizerRepo.save(fertilizer);
    }

    @Override
    public Fertilizer createDefaultFertilizerType(Fertilizer fertilizer) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(fertilizer.getName()));
        Fertilizer value = mongoTemplate.findOne(query, Fertilizer.class);

        System.out.println("value=========="+value);
        if (value == null) {
            return fertilizerRepo.save(fertilizer);
        }
        return value;
    }

    @Override
    public List<Fertilizer> getFertilizers() {
        return fertilizerRepo.findAll();
    }

    @Override
    public Fertilizer getFertilizer(String id) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, Fertilizer.class);
    }

    @Override
    public List<SubFarmingType> getSubFarmingTypesByType(String farmingType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("farmingType").is(farmingType));
        return mongoTemplate.find(query, SubFarmingType.class);
    }

    @Override
    public List<Fertilizer> getFertilizersBySubFarmingType(String fertilizerType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("fertilizerType").is(fertilizerType));
        return mongoTemplate.find(query, Fertilizer.class);
    }

    @Override
    public List<FertilizerType> getFertilizerTypes(String fertilizerType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("fertilizerType").is(fertilizerType));
        return mongoTemplate.findDistinct(query, "categoryType", "fertilizer", Fertilizer.class, FertilizerType.class);
    }

    @Override
    public List<Fertilizer> getFertilizersByType(String fertilizerType, String categoryType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("categoryType").is(categoryType).and("fertilizerType").is(fertilizerType));
        return mongoTemplate.find(query, Fertilizer.class);
    }

    @Override
    public List<Fertilizer> getComplexFertwithoutK() {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("fertilizerType").is("Chemical").and("categoryType").is("Complex").and("kRatio").lte(0));
        return mongoTemplate.find(query, Fertilizer.class);
    }

    @Override
    public Fertilizer getByFertId(String fertId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("fertId").is(fertId));
        return mongoTemplate.findOne(query, Fertilizer.class);
    }

    @Override
    public Fertilizer checkFertilizerExists(String fertilizerName, String categoryType, String fertilizerType) {

        Fertilizer.CategoryType category = Fertilizer.CategoryType.Nitrogenus ;
        SubFarmingType.FertilizerType fertType = SubFarmingType.FertilizerType.Bio;

        for (Fertilizer.CategoryType type : Fertilizer.CategoryType.values()) {
            if (type.toString().equals(categoryType.trim())) {
                category = type;
            }
        }
        for (SubFarmingType.FertilizerType type : SubFarmingType.FertilizerType.values()) {
            if (type.toString().equals(fertilizerType.trim())) {
                fertType = type;
            }
        }

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fertilizerName);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fertType);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+category);
//        System.out.println();
        
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(fertilizerName).and("fertilizerType").is(fertType).and("categoryType").is(category));
        return mongoTemplate.findOne(query, Fertilizer.class);
    }

    @Override
    public List<Fertilizer> getFertilizerFromListIds(List<String> fertIds) {
        List<Fertilizer> fertilizers = new ArrayList<>();
        if (fertIds != null && !fertIds.isEmpty()) {
            for (String fertId : fertIds) {
                Fertilizer fert = getByFertId(fertId);
                if (fert == null) {
                    throw new ResourceNotFoundException("Fertilizer not found for id:"+fertId);
                }
                fertilizers.add(fert);
            }
        }
        return fertilizers;
    }

    @Override
    public Fertilizer getFertilizerById(String fertilizerId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(fertilizerId)));
        return mongoTemplate.findOne(query, Fertilizer.class);
    }
}
