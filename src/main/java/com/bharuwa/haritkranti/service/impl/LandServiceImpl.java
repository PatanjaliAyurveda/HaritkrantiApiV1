package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.aws.AwsS3;
import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.repositories.*;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.location.CityCropSoil;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.service.CountService;
import com.bharuwa.haritkranti.service.LandService;
import com.bharuwa.haritkranti.service.LocationServices;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.bharuwa.haritkranti.models.newmodels.GovtLandDetail;

/**
 * @author anuragdhunna
 */
@Service
public class LandServiceImpl implements LandService {

    private static final Logger logger = LoggerFactory.getLogger(LandServiceImpl.class);

    private final MongoTemplate mongoTemplate;

    AwsS3 awsS3;

    public LandServiceImpl(MongoTemplate mongoTemplate, AwsS3 awsS3) {
        this.mongoTemplate = mongoTemplate;
        this.awsS3 = awsS3;
    }


    @Autowired
    private SoilRepo soilRepo;

    @Autowired
    private FieldSizeRepo fieldSizeRepo;

    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private LocationServices locationServices;

    @Autowired
    private CityCropSoilRepo cityCropSoilRepo;

    @Autowired
    private HorticultureRepo horticultureRepo;

    @Autowired
    private CountService countService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    @Override
    public Crop storeCrop(Crop crop) {
        // check if same crop exist or not
//        Crop cropByNameAndType = getCropByNameAndType(crop.getCropName(), crop.getCropType().toString());
//        if (cropByNameAndType != null) {
//            throw new CustomException("Crop already exists with name:"+crop.getCropName()+ " cropType:"+crop.getCropType());
//        }
        return cropRepo.save(crop);
    }

    @Override
    public Crop createDefaultCrop(Crop crop) {
        Query query = new Query();
        query.addCriteria(Criteria.where("cropName").is(crop.getCropName()));
        Crop value = mongoTemplate.findOne(query, Crop.class);

        if (value == null) {
            cropRepo.save(crop);
        }
        return value;
    }

    @Override
    public Page<Crop> getCrops(int page, int size) {
//        return cropRepo.findAll();
        return genericMongoTemplate.paginationWithQuery(page,size,Crop.class);
    }

    @Override
    public Crop getCrop(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, Crop.class);
    }

    @Override
    public Soil storeSoil(Soil soil) {
        return soilRepo.save(soil);
    }

    @Override
    public void createDefaultSoil(Soil soil) {
        Query query = new Query();
        query.addCriteria(Criteria.where("soilName").is(soil.getSoilName()));
        Soil value = mongoTemplate.findOne(query, Soil.class);

        if (value == null) {
            soilRepo.save(soil);
        }else{
            value.setSoilName(soil.getSoilName());
            soilRepo.save(value);
        }

    }

    @Override
    public List<Soil> getSoils() {
        return soilRepo.findAll(Sort.by(Sort.Direction.ASC, "soilName"));
    }

    @Override
    public List<Soil> getSoils(List<String> soilNames) {
        List<Soil> soilList = new ArrayList<>();
        for (String soilName : soilNames){
            Soil soil = findSoilByName(soilName);
            if (soil == null) {
                throw new ResourceNotFoundException("Soil not found. Entered Soil name is "+soilName);
            }
            soilList.add(soil);
        }
//        List<Soil> soils = new ArrayList<>();
//        Query query = new Query();
//        query = query.addCriteria(Criteria.where("soilName").in(soilNames));
//        soils = mongoTemplate.find(query, Soil.class);
//        return soils;
        return soilList;
    }

    @Override
    public FieldSize storeFieldSize(FieldSize fieldSize) {
        return fieldSizeRepo.save(fieldSize);
    }

    @Override
    public void createDefaultFieldSize(FieldSize fieldSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fieldSizeType").is(fieldSize.getFieldSizeType()));
        FieldSize value = mongoTemplate.findOne(query, FieldSize.class);

        if (value == null) {
            fieldSizeRepo.save(fieldSize);
        }
    }

    @Override
    public List<FieldSize> getFieldSizes() {
        return fieldSizeRepo.findAll();
    }

    @Override
    public List<Crop> findCropByIds(List<String> cropIds) {
        List<Crop> cropList = new ArrayList<>();
        for (String cropId : cropIds) {
            Query query = new Query(Criteria.where("id").is(new ObjectId(cropId)));
            cropList.add(mongoTemplate.findOne(query, Crop.class));
        }
        return cropList;
    }

    @Override
    public List<Soil> getSoilsByCropId(String cropId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("cropList._id").is(new ObjectId(cropId)));
        return mongoTemplate.find(query, Soil.class);
    }

    @Override
    public List<Fertilizer> getFertilizersByCropId(String cropId, String fertilizerType, String categoryType) {

        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(cropId)));
        Crop crop =  mongoTemplate.findOne(query, Crop.class);

        if (crop == null) {
            throw new ResourceNotFoundException("Invalid cropId");
        }
        List<String> fertilizerIds = crop.getFertilizers().stream().map(Fertilizer::getId).collect(Collectors.toList());
//        System.out.println(">>>>>>>>>>>>>>>>>>>"+fertilizerIds);

        query = new Query();
        query = query.addCriteria(Criteria.where("_id").in(fertilizerIds).and("fertilizerType").is(fertilizerType).and("categoryType").is(categoryType));
        List<Fertilizer> fertilizers = mongoTemplate.find(query, Fertilizer.class);
        return fertilizers;
    }

    @Override
    public Soil getSoil(String soilId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(soilId)));
        return mongoTemplate.findOne(query, Soil.class);
    }

    @Override
    public List<Fertilizer> getAllFertilizersByCropId(String cropId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(cropId)));
        Crop crop = mongoTemplate.findOne(query, Crop.class);
        return crop != null ? crop.getFertilizers() : new ArrayList<>();
    }

    @Override
    public List<Crop> getCropByType(String cropType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("cropType").is(cropType));
        return mongoTemplate.find(query, Crop.class);
    }

    @Override
    public Crop getCropByNameAndType(String cropName, String cropType) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("cropName").is(cropName.trim()).and("cropType").regex(Pattern.compile(Pattern.quote(cropType.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)).regex(cropType.trim(),"i"));
        return mongoTemplate.findOne(query, Crop.class);
    }

    @Override
    public Crop checkCropExists(Crop crop) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("cropName").is(crop.getCropName().trim()).and("cropType").is(crop.getCropType()));
        return mongoTemplate.findOne(query, Crop.class);
    }

    @Override
    public List<Soil> checkSoilForCrop(String cropId, String soilId) {
//        Optional<Crop> cropById = cropRepo.findById(cropId);
//        if (!cropById.isPresent()) {
//            throw new ResourceNotFoundException("Crop doesn't exist");
//        }
//        List<Soil> cropSoils = cropById.get().getSoils();
//        Optional<Soil> soilById = soilRepo.findById(soilId);
//        if (!soilById.isPresent()) {
//            throw new ResourceNotFoundException("Soil doesn't exist");
//        }
//
//        for (Soil soil : cropSoils) {
//            if (soil.getId().equals(soilById.get().getId())) {
//                // Returning empty list because Soil is suitable for crop
//                return new ArrayList<>();
//            }
//        }
        // Returnining List of suitableSoils, if selected Crop and Soil are not suitable
        return new ArrayList<>();
    }

    @Override
    public Soil findSoilByName(String soilName) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("soilName").regex(Pattern.compile(Pattern.quote(soilName.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        return mongoTemplate.findOne(query, Soil.class);
    }

    @Override
    public CityCropSoil getCityCropSoil(String cityId, String cropId, String soilId) {
        Query query = new Query(Criteria.where("city._id").is(cityId).and("crop._id").is(cropId).and("soil._id").is(soilId));
        return mongoTemplate.findOne(query, CityCropSoil.class);
    }

    @Override
    public CityCropSoil getCityCropSoil(String cityId, String cropId, String soilId, LandType.Type irrigationType) {
        System.out.println("========cityId===new======"+cityId);
        System.out.println("========cropId===new======"+cropId);
        System.out.println("========soilId===new======"+soilId);
        System.out.println("========irrigationType========="+irrigationType);
        Query query = new Query(Criteria.where("city._id").is(cityId).and("crop._id").is(cropId).and("soil._id").is(soilId).and("irrigationType").is(irrigationType));
        return mongoTemplate.findOne(query, CityCropSoil.class);
    }

    @Override
    public List<String> getCropTypesByDistrict(String cityId, String stateId) {
        Query query = new Query(Criteria.where("city._id").is(cityId).and("state._id").is(stateId));
        return mongoTemplate.findDistinct(query, "crop.cropType", CityCropSoil.class, String.class);
    }

    @Override
    public List<Crop> getCropTypesByDistrictAndType(String cityId, String stateId, String cropType) {
        Query query = new Query(Criteria.where("city._id").is(cityId).and("state._id").is(stateId).and("crop.cropType").is(cropType));
        List<ObjectId> cropIds = mongoTemplate.findDistinct(query, "crop._id", CityCropSoil.class, ObjectId.class);

        List<Crop> crops = new ArrayList<>();
        cropIds.forEach(objectId -> {
            crops.add(genericMongoTemplate.findById(String.valueOf(objectId), Crop.class));
        });
        return crops;
    }

    @Override
    public List<Soil> getSoilsByDistrict(String cityId, String stateId) {
        Query query = new Query(Criteria.where("city._id").is(cityId).and("state._id").is(stateId));
        query.with(Sort.by(Sort.Direction.ASC, "soilName"));
        List<Soil> soils = mongoTemplate.findDistinct(query, "soil", CityCropSoil.class, Soil.class);

        List<Soil> soilList = new ArrayList<>();
        for (Soil soil : soils) {
            if (soilList.stream().noneMatch(obj -> obj.getSoilName().equals(soil.getSoilName()))) {
                soilList.add(soil);
            }
        }
        return soilList;
    }

    @Override
    public Horticulture addHorticultureDetails(Horticulture horticulture) {
        User user = mongoTemplate.findById(horticulture.getUserId(),User.class);
        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }
        horticulture.setAddress(user.getAddressModel());
        Horticulture storedHorticulture = mongoTemplate.save(horticulture);

        //count set for Horticulture Details
        CountAll countAll = countService.getCountAll();
        countAll.setTotalHorticultureFarmers(countService.getHorticultureFarmerCount());
        countService.storeCountAll(countAll);

        return storedHorticulture;
    }

    @Override
    public List<Horticulture> getHorticultureDetails(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Horticulture.class);
    }

    @Override
    public Horticulture getHorticultureDetailById(String horticultureId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(horticultureId)));
        return mongoTemplate.findOne(query, Horticulture.class);
    }

    @Autowired
    private UserCropRepo userCropRepo;


    @Override
    public void cropIdtoCropModel() {
        Query query = new Query(Criteria.where("cropId").ne(null));
        List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);

        for (UserCrop userCrop : userCrops) {
            String cropId = userCrop.getCropId();

            Optional<Crop> byId = cropRepo.findById(cropId);
            if (byId.isPresent()) {
                Crop crop = byId.get();

                userCrop.setCrop(crop);
                userCropRepo.save(userCrop);
            }
        }
    }

    @Autowired
    private UserLandDetailRepo userLandDetailRepo;

    @Override
    public void bighaToAcre(String stateId, String size) {

        // formula
        Query query;
        query = new Query(Criteria.where("landSizeType").is("Bigha"));
        if (!StringUtils.isEmpty(stateId)) {
            query = new Query(Criteria.where("landSizeType").is("Bigha").and("stateId").is(stateId));
        }
        List<UserLandDetail> userLandDetails = mongoTemplate.find(query, UserLandDetail.class);

        if (StringUtils.isEmpty(size)) {
            size = "5";
        }
        for (UserLandDetail userLandDetail: userLandDetails) {
            BigDecimal landSize = userLandDetail.getLandSize();
            FieldSize.FieldSizeType landSizeType = userLandDetail.getLandSizeType();


            landSize = landSize.divide(new BigDecimal(size), BigDecimal.ROUND_HALF_DOWN);
            landSizeType = FieldSize.FieldSizeType.Acre;

            userLandDetail.setLandSize(landSize);
            userLandDetail.setLandSizeType(landSizeType);
            userLandDetailRepo.save(userLandDetail);

        }

        // change type

    }

    @Override
    public void organiseCityCropSoil() {
        List<CityCropSoil> cityCropSoils = cityCropSoilRepo.findAll();
        for(CityCropSoil cityCropSoil : cityCropSoils){
            String id = cityCropSoil.getCrop().getId();
            Crop crop = getCrop(id);
            cityCropSoil.setCrop(crop);
            cityCropSoilRepo.save(cityCropSoil);
        }

    }

    private List<CityCropSoil> checkCityCropSoilExist(City city, Crop crop , Soil soil){
        Query query = new Query();
        query.addCriteria(Criteria.where("city._id").is(city.getId()).and("state._id").is(city.getState().getId()).and("soil._id").is(soil.getId()).and("crop._id").is(crop.getId()));
        return mongoTemplate.find(query, CityCropSoil.class);
    }

    @Override
    public void removeDuplicateRecordsCityCropSoil() {
//
//        List<State> stateList = locationServices.getStates();
//
//        for(State state : stateList){
//
//            List<City> cityList = locationServices.getCitiesByStateId(state.getId());
//
//            for (City city : cityList){
//                for (Crop crop : getCrops()){
//                    for (Soil soil : getSoils()){
//
//                        List<CityCropSoil> cityCropSoilList = checkCityCropSoilExist(city,crop,soil);
//
//                        if(cityCropSoilList.size() > 1){
//                            cityCropSoilRepo.delete(cityCropSoilList.get(1));
//                        }
//
//
//                    }
//
//                }
//
//            }
//
//        }

    }

    @Override
    public List<Crop> getCropsBySeason(String season) {
        Query query = new Query(Criteria.where("cropSeasons").is(season));
        return mongoTemplate.find(query, Crop.class);
    }


    @Override
    public String uploadSoilImage(MultipartFile file, String soilId) {
        Soil soil = getSoil(soilId);
        if(soil == null){
            throw new ResourceNotFoundException("Soil not Found");
        }
        String soilImage = this.awsS3.uploadFile(file);
        soil.setImageUrl(soilImage);
        soilRepo.save(soil);
        return soil.getImageUrl();
    }

    @Override
    public Soil addSoilLocale(Locale locale, String soilId) {
        System.out.println("=========soilId======"+soilId);
        Soil soil = genericMongoTemplate.findById(soilId, Soil.class);
        if (soil == null) {
            throw new ResourceNotFoundException("Soil not found.");
        }
        soil.addLocale(locale);
        return mongoTemplate.save(soil);
    }

    @Override
    public <T> T genericAddLocale(Locale locale, String id, String type) {
        switch (type) {
            case "Crop":
                logger.info("Adding Locale for Crop");
                Crop crop = genericMongoTemplate.findById(id, Crop.class);
                if (crop == null) {
                    throw new ResourceNotFoundException("Crop not found.");
                }
                crop.addLocale(locale);
                return (T) mongoTemplate.save(crop);

            case "Soil":
                logger.info("Adding Locale for Soil");
                Soil soil = genericMongoTemplate.findById(id, Soil.class);
                if (soil == null) {
                    throw new ResourceNotFoundException("Soil not found.");
                }
                soil.addLocale(locale);
                return (T) mongoTemplate.save(soil);

            case "com.bharuwa.annadata.models.fertilizerModels.Fertilizer":
                logger.info("Adding Locale for Fertilizer");
                Fertilizer fertilizer = genericMongoTemplate.findById(id, Fertilizer.class);
                if (fertilizer == null) {
                    throw new ResourceNotFoundException("Fertilizer not found.");
                }
                fertilizer.addLocale(locale);
                return (T) mongoTemplate.save(fertilizer);

            default:
                throw new CustomException("Value for parameter type is invalid");
        }
    }
    
    @Override
    public List<String> getAllKhasraByFarmerId(String farmerId) {
    	Criteria criteria = new Criteria("userId").is(farmerId);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<String> khasraList = mongoTemplate.findDistinct(query,"khasraNo",UserLandDetail.class,String.class);
		return khasraList;
    }
    
    @Override
    public List<UserCrop> getAllKhasraCropByFarmerId(String farmerId) {
    	Criteria criteria = new Criteria("userId").is(farmerId);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	return mongoTemplate.find(query, UserCrop.class);
    }
    
    @Override
    public List<GovtLandDetail> getGovtLandDetail(String khasraNumber,String languge) {
    	Criteria criteria = new Criteria("khasraNumber").is(khasraNumber);
    	Query query = new Query();
    	query.addCriteria(criteria);
    	List<GovtLandDetail> landDetailList = mongoTemplate.find(query, GovtLandDetail.class);
    	List<GovtLandDetail> resultingList = new ArrayList<>();
    	for(GovtLandDetail landDetail:landDetailList) {
    		if(languge.equals("English")) {
    			landDetail.setFarmerDisplayName(landDetail.getFarmerName());
    			resultingList.add(landDetail);
    		}else if(languge.equals("Hindi")) {
    			landDetail.setFarmerDisplayName(landDetail.getFarmerNameInHindi());
    			resultingList.add(landDetail);
    		}
    			
    	}
    	return resultingList;
    }

    @Override
    public UserKhasraMapping storeUserKhasraMapping(UserKhasraMapping UserKhasraMapping) {
        return mongoTemplate.save(UserKhasraMapping);
    }
}
