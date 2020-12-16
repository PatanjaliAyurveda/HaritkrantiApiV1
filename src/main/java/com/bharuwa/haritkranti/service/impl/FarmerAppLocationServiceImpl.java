package com.bharuwa.haritkranti.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.models.GovtMapData;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.location.CityInHindi;
import com.bharuwa.haritkranti.models.location.StateInHindi;
import com.bharuwa.haritkranti.models.location.TehsilInHindi;
import com.bharuwa.haritkranti.models.location.VillageInHindi;
import com.bharuwa.haritkranti.service.FarmerAppLocationService;

import java.util.List;

/**
 * @author sunaina
 */
@Service
public class FarmerAppLocationServiceImpl implements FarmerAppLocationService{

    private final MongoTemplate mongoTemplate;

    public FarmerAppLocationServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public StateInHindi storeStateInHindi(StateInHindi state) { return mongoTemplate.save(state); }

    @Override
    public CityInHindi storeCityInHindi(CityInHindi city) {
        return mongoTemplate.save(city);
    }

    @Override
    public TehsilInHindi storeTehsilInHindi(TehsilInHindi tehsil) { return mongoTemplate.save(tehsil); }

    @Override
    public BlockInHindi storeBlockInHindi(BlockInHindi block) { return mongoTemplate.save(block); }

    @Override
    public VillageInHindi storeVillageInHindi(VillageInHindi village) { return mongoTemplate.save(village); }

    @Override
    public List getStateList(String language) {
        switch (language) {
            case "Hindi":
                return mongoTemplate.findAll(StateInHindi.class);

            case "English":
               // return mongoTemplate.findAll(State.class);
            	List<String> stateList = mongoTemplate.findDistinct("state",GovtMapData.class,String.class);
            	System.out.println(stateList);
            	Query query = new Query();
                query.addCriteria(Criteria.where("name").in(stateList));
                return mongoTemplate.find(query, State.class);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public List getCitiesByStateId(String stateId, String language) {
        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("state.$id").is(new ObjectId(stateId))).with(Sort.by(Sort.Direction.ASC,"नाम"));
                return mongoTemplate.find(query, CityInHindi.class);

            case "English":
              //  query = new Query(Criteria.where("state.$id").is(new ObjectId(stateId))).with(Sort.by(Sort.Direction.ASC, "name"));
              //  return mongoTemplate.find(query, City.class);
            	List<String> cityList = mongoTemplate.findDistinct("district",GovtMapData.class,String.class);
                query = new Query();
                query.addCriteria(Criteria.where("state.id").is(stateId).and("name").in(cityList));
                query.with(Sort.by(Sort.Direction.ASC, "name"));
                return  mongoTemplate.find(query, City.class);
            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public List getTehsilListByDistrict(String districtId, String language) {
        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("city.$id").is(new ObjectId(districtId))).with(Sort.by(Sort.Direction.ASC,"नाम"));
                return mongoTemplate.find(query, TehsilInHindi.class);

            case "English":
             //   query = new Query(Criteria.where("city.$id").is(new ObjectId(districtId))).with(Sort.by(Sort.Direction.ASC, "name"));
             //   return mongoTemplate.find(query, Tehsil.class);
            	List<String> tehsilList = mongoTemplate.findDistinct("tehsil",GovtMapData.class,String.class);
                query = new Query();
                query.addCriteria(Criteria.where("city.id").is(districtId).and("name").in(tehsilList));
                query.with(Sort.by(Sort.Direction.ASC, "name"));
                return  mongoTemplate.find(query, Tehsil.class);
            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public List getBlockListByTehsil(String tehsilId, String language) {
        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("tehsil.$id").is(new ObjectId(tehsilId))).with(Sort.by(Sort.Direction.ASC,"नाम"));
                return mongoTemplate.find(query, BlockInHindi.class);

            case "English":
            //    query = new Query(Criteria.where("tehsil.$id").is(new ObjectId(tehsilId))).with(Sort.by(Sort.Direction.ASC, "name"));
            //    return mongoTemplate.find(query, Block.class);
            	List<String> blockList = mongoTemplate.findDistinct("block",GovtMapData.class,String.class);
                query = new Query();
                query.addCriteria(Criteria.where("tehsil.id").is(tehsilId).and("name").in(blockList));
                query.with(Sort.by(Sort.Direction.ASC, "name"));
                return  mongoTemplate.find(query, Block.class);
            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }

    @Override
    public List getVillageListByBlockId(String blockId, String language) {
        switch (language) {
            case "Hindi":
                Query query = new Query(Criteria.where("block.$id").is(new ObjectId(blockId))).with(Sort.by(Sort.Direction.ASC,"नाम"));
                return mongoTemplate.find(query, VillageInHindi.class);

            case "English":
             //   query = new Query(Criteria.where("block.$id").is(new ObjectId(blockId))).with(Sort.by(Sort.Direction.ASC, "name"));
             //   return mongoTemplate.find(query, Village.class);
            	List<String> villageList = mongoTemplate.findDistinct("village",GovtMapData.class,String.class);
                query = new Query();
                query.addCriteria(Criteria.where("block.id").is(blockId).and("name").in(villageList));
                query.with(Sort.by(Sort.Direction.ASC, "name"));
                return  mongoTemplate.find(query, Village.class);
            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }
}
