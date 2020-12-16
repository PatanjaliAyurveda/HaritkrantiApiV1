package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.GovtMapData;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.repositories.*;
import com.bharuwa.haritkranti.service.LocationServices;
import com.opencsv.CSVReader;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.Cacheable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author anuragdhunna
 */
@Service
public class LocationServicesImpl implements LocationServices {

    private final MongoTemplate mongoTemplate;
    public LocationServicesImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private StateRepo stateRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private TehsilRepo tehsilRepo;

    @Autowired
    private VillageRepo villageRepo;

    @Autowired
    private CityCropSoilRepo cityCropSoilRepo;

    @Autowired
    private BlockRepo blockRepo;


    @Override
    public State saveState(State state) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(state.getName()));
        State value = mongoTemplate.findOne(query, State.class);

        if (value == null) {
            stateRepo.save(state);
        }
        return state;
    }

    @Override
    public State getState(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, State.class);
    }

    @Override
    public State createDefaultState(State state) {
        if (StringUtils.isEmpty(state.getName())) {
            throw new CustomException("State name is empty");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(state.getName()));
        State value = mongoTemplate.findOne(query, State.class);

        if (value == null) {
            stateRepo.save(state);
        }
        return value;
    }

    @Override
    public List<State> getStates() {
    	List<String> stateList = mongoTemplate.findDistinct("state",GovtMapData.class,String.class);
    	System.out.println(stateList);
    	Query query = new Query();
        query.addCriteria(Criteria.where("name").in(stateList));
        return mongoTemplate.find(query, State.class);
    }
    
    @Override
    public City saveCity(City city) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(city.getName()).and("state.id").is(city.getState().getId()));
        City value = mongoTemplate.findOne(query, City.class);
        if (value ==  null){
            cityRepo.save(city);
        }else {
            value.setName(city.getName());
            value.setState(city.getState());
            cityRepo.save(value);
        }
        return city;
    }

    @Override
    public City getCity(String id) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, City.class);
    }

    @Override
    public City findCityByNameAndState(String cityName, String stateid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(Pattern.compile(Pattern.quote(cityName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE )).and("state.id").is(stateid));
        return  mongoTemplate.findOne(query, City.class);
    }

    @Override
    public City getCityByName(String cityName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(Pattern.compile(Pattern.quote(cityName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        return mongoTemplate.findOne(query, City.class);
    }

    @Override
    public CityCropSoil saveCityCropSoil(CityCropSoil cityCropSoil) {
        Query query = new Query();
        query.addCriteria(Criteria.where("city._id").is(cityCropSoil.getCity().getId()).and("state._id").is(cityCropSoil.getState().getId())
                .and("soil._id").is(cityCropSoil.getSoil().getId()).and("crop._id").is(cityCropSoil.getCrop().getId()).and("irrigationType").is(cityCropSoil.getIrrigationType()));
        CityCropSoil checkExist = mongoTemplate.findOne(query, CityCropSoil.class);
        if(checkExist == null){
            cityCropSoilRepo.save(cityCropSoil);
        } else {
            cityCropSoil.setId(checkExist.getId());
            cityCropSoilRepo.save(cityCropSoil);
        }
        return cityCropSoil;
    }

    @Override
    public City storeCity(String stateId, String cityName) {
        Optional<State> stateOptional = stateRepo.findById(stateId);
        if (!stateOptional.isPresent()) {
            throw new ResourceNotFoundException("State not found");
        }
        City city = new City();
        city.setName(cityName);
        city.setState(stateOptional.get());
        return cityRepo.save(city);
    }

    @Override
    public void createDefaultCity(String stateId, String cityName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(cityName));
        City value = mongoTemplate.findOne(query, City.class);

        if (value == null) {
            Optional<State> stateOptional = stateRepo.findById(stateId);
            if (!stateOptional.isPresent()) {
                throw new ResourceNotFoundException("State not found");
            }

            City city = new City();
            city.setName(cityName);
            city.setState(stateOptional.get());
            cityRepo.save(city);
        }
    }

    @Override
    public State findStateByName(String stateName) {
        Query query = new Query(Criteria.where("name").regex(Pattern.compile(Pattern.quote(stateName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        return  mongoTemplate.findOne(query, State.class);
    }

    @Override
    public List<State> getStatesFromStateList(List<String> stateList) {
        List<State> states = new ArrayList<>();
        if (stateList != null && !stateList.isEmpty()) {
            for (String stateName : stateList) {
                State state = findStateByName(stateName);
                states.add(state);
            }
        }
        return states;
    }

    @Override
    public Tehsil saveTehsil(Tehsil tehsil) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(tehsil.getName()).and("state.id").is(tehsil.getState().getId()).and("city.id").is(tehsil.getCity().getId()));
        Tehsil value = mongoTemplate.findOne(query, Tehsil.class);
        if (value ==  null){
            tehsilRepo.save(tehsil);
        }else {
            value.setName(tehsil.getName());
            value.setCity(tehsil.getCity());
            value.setState(tehsil.getState());
            tehsilRepo.save(value);
        }
        return tehsil;
    }

    @Override
    public Block saveBlock(Block block) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(block.getName()).and("state.id").is(block.getState().getId()).and("city.id").is(block.getCity().getId()).and("tehsil.id").is(block.getTehsil().getId()));
        Block value = mongoTemplate.findOne(query, Block.class);
        if (value ==  null){
            blockRepo.save(block);
        }else {
            value.setName(block.getName());
            value.setTehsil(block.getTehsil());
            value.setCity(block.getCity());
            value.setState(block.getState());
            blockRepo.save(value);
        }
        return block;
    }

    @Override
    public Village saveVillage(Village village) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(village.getName()).and("state.id").is(village.getState().getId()).and("city.id").is(village.getCity().getId()).and("tehsil.id").is(village.getTehsil().getId()).and("block.id").is(village.getBlock().getId()));
        Village value = mongoTemplate.findOne(query, Village.class);
        if (value ==  null){
            villageRepo.save(village);
        }else {
            value.setName(village.getName());
            value.setBlock(village.getBlock());
            value.setTehsil(village.getTehsil());
            value.setCity(village.getCity());
            value.setState(village.getState());
            villageRepo.save(value);
        }
        return village;
    }

    @Override
    public List<Village> getVillages(String cityId, String tehsilId) {
        return null;
    }

    @Override
    public void uploadBlockCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //        Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            Block block = new Block();
            block.setName(input[3].trim());

            State state = findStateByName(input[0].trim());
            if(state == null){
                throw new ResourceNotFoundException("State not found with name :"+input[0]+" (line:"+line+")");
            }
            String cityName = input[1];
            City city = findCityByNameAndState(cityName.trim(),state.getId());
            if(city == null){
                throw new ResourceNotFoundException("city not found with name : "+input[1]+" (line:"+line+")");
            }

            String tehsilName = input [2];
            Tehsil tehsil = getTehsilByNameCityAndState(tehsilName,city.getId(),state.getId());
            if(tehsil == null){
                throw new ResourceNotFoundException("tehsil not found with name : "+input[2]+" (line:"+line+")");
            }

            block.setTehsil(tehsil);
            block.setCity(city);
            block.setState(state);
            saveBlock(block);
        }
    }

    @Override
    public Tehsil getTehsilByNameCityAndState(String tehsilName, String cityId, String stateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(Pattern.compile(Pattern.quote(tehsilName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE )).and("state.id").is(stateId).and("city.id").is(cityId));
        return  mongoTemplate.findOne(query, Tehsil.class);
    }

    @Override
    public Village getVillage(String villageId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(villageId)));
        return mongoTemplate.findOne(query, Village.class);
    }

    @Override
    public Tehsil getTehsil(String tehsilId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(tehsilId)));
        return mongoTemplate.findOne(query, Tehsil.class);
    }

    @Override
    public Block getBlock(String blockId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(blockId)));
        return mongoTemplate.findOne(query, Block.class);
    }

    @Override
    public void uploadVillageCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            Village village = new Village();
            village.setName(input[4].trim());


            State state = findStateByName(input[0].trim());
            if(state == null){
                throw new ResourceNotFoundException("State not found with name : "+input[0]+" (line:"+line+")");
            }
            String cityName = input[1];
            City city = findCityByNameAndState(cityName.trim(),state.getId());
            if(city == null){
                throw new ResourceNotFoundException("city not found with name : "+input[1]+" (line:"+line+")");
            }

            String tehsilName = input [2];
            Tehsil tehsil = getTehsilByNameCityAndState(tehsilName,city.getId(),state.getId());
            if(tehsil == null){
                throw new ResourceNotFoundException("tehsil not found with name : "+input[2]+" (line:"+line+")");
            }
            String blockName = input [3];

            Block block = getBlocksByNameTehsilCityState(blockName,tehsil.getId(),city.getId(),state.getId());
            if(block == null){
                throw new ResourceNotFoundException("block not found with name : "+input[3]+" (line:"+line+")");
            }
            village.setBlock(block);
            village.setTehsil(tehsil);
            village.setCity(city);
            village.setState(state);
            saveVillage(village);
        }
    }

    @Override
    public Block getBlocksByNameTehsilCityState(String blockName, String tehsilId, String cityId, String stateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(Pattern.compile(Pattern.quote(blockName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE )).and("state.id").is(stateId).and("city.id").is(cityId).and("tehsil.id").is(tehsilId));
        return  mongoTemplate.findOne(query, Block.class);
    }

    @Override
    public Village getVillageByNameBlockTehsilCityState(String villageName, String blockId, String tehsilId, String cityId, String stateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(Pattern.compile(Pattern.quote(villageName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE )).and("state.id").is(stateId).and("city.id").is(cityId).and("tehsil.id").is(tehsilId).and("block.id").is(blockId));
        return  mongoTemplate.findOne(query, Village.class);
    }
    
    @Override
    public Village getVillageByNameBlockTehsilCityState(String villageName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(villageName));
        return  mongoTemplate.findOne(query, Village.class);
    }
}
