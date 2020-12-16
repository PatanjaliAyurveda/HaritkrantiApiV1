package com.bharuwa.haritkranti.service;



import java.util.List;

import com.bharuwa.haritkranti.models.location.BlockInHindi;
import com.bharuwa.haritkranti.models.location.CityInHindi;
import com.bharuwa.haritkranti.models.location.StateInHindi;
import com.bharuwa.haritkranti.models.location.TehsilInHindi;
import com.bharuwa.haritkranti.models.location.VillageInHindi;

public interface FarmerAppLocationService {

    StateInHindi storeStateInHindi(StateInHindi state);

    CityInHindi storeCityInHindi(CityInHindi city);

    TehsilInHindi storeTehsilInHindi(TehsilInHindi tehsil);

    BlockInHindi storeBlockInHindi(BlockInHindi block);

    VillageInHindi storeVillageInHindi(VillageInHindi village);

    List getStateList(String language);

    List getCitiesByStateId(String stateId, String language);


    List getTehsilListByDistrict(String districtId, String language);

    List getBlockListByTehsil(String tehsilId, String language);

    List getVillageListByBlockId(String blockId, String language);
}
