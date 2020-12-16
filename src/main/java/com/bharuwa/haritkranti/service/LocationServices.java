package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.location.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author anuragdhunna
 */
public interface LocationServices {

    State saveState(State state);

    State getState(String id);

    State createDefaultState(State state);

    List<State> getStates();

    City getCity(String id);

    City findCityByNameAndState(String cityName, String stateid);

    City storeCity(String stateId, String cityName);

    void createDefaultCity(String stateId, String cityName);

    State findStateByName(String stateName);

    List<State> getStatesFromStateList(List<String> stateList);

    City saveCity(City city);

    City getCityByName(String cityName);

    CityCropSoil saveCityCropSoil(CityCropSoil cityCropSoil);

    Tehsil saveTehsil(Tehsil tehsil);

    Village saveVillage(Village village);

    List<Village> getVillages(String cityId, String tehsilId);

    void uploadBlockCsv(MultipartFile file) throws IOException;

    Block saveBlock(Block block);

    Village getVillage(String villageId);

    Tehsil getTehsil(String tehsilId);

    Block getBlock(String blockId);

    void uploadVillageCsv(MultipartFile file) throws IOException;

    Block getBlocksByNameTehsilCityState(String blockName, String tehsilId, String cityId, String stateId);

    public Village getVillageByNameBlockTehsilCityState(String villageName);
    
    Tehsil getTehsilByNameCityAndState(String tehsilName, String cityId, String stateId);

    Village getVillageByNameBlockTehsilCityState(String villageName, String blockId, String tehsilId, String cityId, String stateId);
}
