package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.Block;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.Tehsil;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.responseModels.*;
import com.bharuwa.haritkranti.repositories.OrganicCertificateRepo;
import com.bharuwa.haritkranti.repositories.UserLandDetailRepo;
import com.bharuwa.haritkranti.service.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bharuwa.haritkranti.service.impl.AnalyticsServiceImpl.getLocationKeyLandDetails;
import static com.bharuwa.haritkranti.service.impl.PaymentReportsServiceImpl.getLocationKey;
import static com.bharuwa.haritkranti.service.impl.ReportHistoryServiceImpl.getLocationKeyVillageAsset;
import static com.bharuwa.haritkranti.utils.Constants.*;

/**
 * @author anuragdhunna
 */
@Service
public class CountServiceImpl implements CountService {

    private static final Logger logger = LoggerFactory.getLogger(CountServiceImpl.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    private LocationServices locationServices;
    
    @Autowired
    private FarmerAppLocationService farmerAppLocationService;

    @Autowired
    private UserLandDetailRepo userLandDetailRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrganicCertificateRepo organicCertificateRepo;

    @Autowired
    private ReportHistoryService reportHistoryService;


    public CountServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Long getTotalUserCount() {
        Query query = new Query(Criteria.where("roles.roleName").nin(ROLE_FAMILY_MEMBER));
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public Long getTotalAgentCount(boolean active) {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT));
        if (active){
            query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT).and("isActive").exists(true).ne(false));
        }
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public Long getTotalFarmerCount() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER).and("createdVia").is(User.CreatedVia.AGENT));
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public Long getTotalUserSignUpCount() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER).and("createdVia").is(User.CreatedVia.SIGNUP));
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public List<FarmerCountStateDistrict> getTotalFarmerCountByStates() {
        List<FarmerCountStateDistrict> farmerCounts = new ArrayList<>();
        List<State> states = locationServices.getStates();
        for(State state : states){
            Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER)
                    .and("addressModel.state.$id").is(new ObjectId(state.getId())));

            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            farmerCount.setName(state.getName());
            farmerCount.setId(state.getId());
            farmerCount.setCount(mongoTemplate.count(query, User.class));
            farmerCounts.add(farmerCount);

        }
        return farmerCounts;
    }

    @Override
    public List<FarmerCountStateDistrict> getTotalFarmerCountByDistrict(String stateId) {
        List<FarmerCountStateDistrict> count = new ArrayList<>();
        List<City> cities = farmerAppLocationService.getCitiesByStateId(stateId,"Hindi");
        for(City city : cities){
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER)
                    .and("addressModel.city.$id").is(new ObjectId(city.getId())));

            farmerCount.setName(city.getName());
            farmerCount.setId(city.getId());
            farmerCount.setCount(mongoTemplate.count(query, User.class));
            count.add(farmerCount);
        }
        return count;
    }

    @Override
    public Long getTotalKhasraCount() {
        // TODO: need to change logic to get distint khasra count
        Query query = new Query(Criteria.where("khasraNo").exists(true).ne(false));
        return this.mongoTemplate.count(query, UserLandDetail.class);
    }

    @Override
    public Long getKhasraCountByState(String stateId) {
        Query query = new Query(Criteria.where("khasraNo").ne(false).exists(true).and("state.$id").is(new ObjectId(stateId)));
        return this.mongoTemplate.count(query, UserLandDetail.class);
    }

    @Override
    public Long getKhasraCountByDistrict(String districtId) {
        Query query = new Query(Criteria.where("khasraNo").ne(false).exists(true).and("city.$id").is(new ObjectId(districtId)));
        return this.mongoTemplate.count(query, UserLandDetail.class);
    }

    @Override
    public Long getTotalFemaleFarmerCount() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER).and("gender").is(User.Gender.FEMALE));
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public Long getTotalMaleFarmerCount() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER).and("gender").is(User.Gender.MALE));
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public LandTypeSize getLandTypesAreaCountCountry() {
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        Query query = new Query();
        query.fields().include("landSizeType").include("landSize"); // Fetch only required Fields
        List<UserLandDetail> userLandDetails = mongoTemplate.find(query, UserLandDetail.class);

        for (UserLandDetail detail : userLandDetails) {
            BigDecimal convertedlandSize = detail.getLandSize();
            // Converting to Acre
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                convertedlandSize = convertedlandSize.multiply(BigDecimal.valueOf(2.5));
            }
            switch (detail.getFarmType()) {
                case Irrigated:
                    irrigatedLandSize = irrigatedLandSize.add(convertedlandSize);
                    break;
                case SemiIrrigated:
                    semiIrrigatedLandSize = semiIrrigatedLandSize.add(convertedlandSize);
                    break;
                case Rainfed:
                    rainfedLandSize = rainfedLandSize.add(convertedlandSize);
                    break;
            }
            totalAreaCount = totalAreaCount.add(convertedlandSize);
        }

        LandTypeSize landTypeSize = new LandTypeSize();
        landTypeSize.setFieldSizeType(FieldSize.FieldSizeType.Acre);
        landTypeSize.setIrrigatedLandSize(irrigatedLandSize);
        landTypeSize.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
        landTypeSize.setRainfedLandSize(rainfedLandSize);
        landTypeSize.setTotalLandSize(totalAreaCount);
        return landTypeSize;
    }

    @Override
    public List<LandTypeSize> getLandTypesAreaCountStates() {
        List<LandTypeSize> landTypeSizeList = new ArrayList<>();
        List<State> states = locationServices.getStates();
        for (State state : states) {
            LandTypeSize landTypeSize = getLandTypeSizeByState(state);
            landTypeSizeList.add(landTypeSize);
        }
        return landTypeSizeList;
    }

    @Override
    public List<LandTypeSize> getLandTypesAreaCountDistricts(String stateId) {
        List<LandTypeSize> landTypeSizeList = new ArrayList<>();
        List<City> cities=  farmerAppLocationService.getCitiesByStateId(stateId,"Hindi");
        for (City city : cities) {
            LandTypeSize landTypeSize = getLandTypeSizeByCity(city);
            landTypeSizeList.add(landTypeSize);
        }
        return landTypeSizeList;
    }

    @Override
    public int getDairyFarmerCount() {
        List<MilkingAnimalDetails.MilkingAnimalType> milkingAnimalTypes= Arrays.asList(MilkingAnimalDetails.MilkingAnimalType.values());
//        Query query = new Query(Criteria.where("animalType").in(milkingAnimalTypes));
//        List<String> list= mongoTemplate.findDistinct(query,"userId", FarmDetails.class, String.class);
        Query query1 = new Query(Criteria.where("animalType").in(milkingAnimalTypes));
        List<String> list1= mongoTemplate.findDistinct(query1,"userId",MilkingAnimalDetails.class,String.class);

        // return (list.size() + list1.size());
        return list1.size();
    }

    @Override
    public Long getBeekeepingFarmerCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Hive));
        List<String> list= mongoTemplate.findDistinct(query,"userId",BeekeepingDetails.class,String.class);
        return (long) list.size();
    }

    @Override
    public int getSericultureFarmerCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Pupa));
        List<String> list = mongoTemplate.findDistinct(query,"userId", FarmDetails.class, String.class);
        return list.size();
    }

    @Override
    public int getHorticultureFarmerCount() {
        Query query = new Query();
        List<String> list = mongoTemplate.findDistinct(query,"userId", Horticulture.class, String.class);
        return list.size();
    }

    @Override
    public int getTotalCowsCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Cow));
        List<FarmDetails> farmDetails = mongoTemplate.find(query,FarmDetails.class);
        int count = 0;
        for (FarmDetails farmDetail : farmDetails){
            count = count + farmDetail.getQuantity();
        }

        Query query1 = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Cow));
        List<MilkingAnimalDetails> milkingAnimalDetails = mongoTemplate.find(query1,MilkingAnimalDetails.class);
        int mCount = 0;
        for (MilkingAnimalDetails animalDetails : milkingAnimalDetails){
            mCount = mCount + animalDetails.getTotalNoOfAnimals();
        }

        return count + mCount;
    }

    @Override
    public int getTotalSheepCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Sheep));
        List<FarmDetails> farmDetails = mongoTemplate.find(query,FarmDetails.class);
        int count = 0;
        for (FarmDetails farmDetail : farmDetails){
            count = count + farmDetail.getQuantity();
        }
        return count;
    }

    @Override
    public int getTotalGoatCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Goat));
        List<FarmDetails> farmDetails = mongoTemplate.find(query,FarmDetails.class);
        int count = 0;
        for (FarmDetails farmDetail : farmDetails){
            count = count + farmDetail.getQuantity();
        }

        Query query1 = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Goat));
        List<MilkingAnimalDetails> milkingAnimalDetails = mongoTemplate.find(query1,MilkingAnimalDetails.class);
        int mCount = 0;
        for (MilkingAnimalDetails animalDetails : milkingAnimalDetails){
            mCount = mCount + animalDetails.getTotalNoOfAnimals();
        }

        return count + mCount;
    }

    @Override
    public int getTotalBuffaloCount() {
        Query query = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Buffalo));
        List<FarmDetails> farmDetails = mongoTemplate.find(query,FarmDetails.class);
        int count = 0;
        for (FarmDetails farmDetail : farmDetails){
            count = count + farmDetail.getQuantity();
        }

        Query query1 = new Query(Criteria.where("animalType").is(FarmDetails.AnimalType.Buffalo));
        List<MilkingAnimalDetails> milkingAnimalDetails = mongoTemplate.find(query1,MilkingAnimalDetails.class);
        int mCount = 0;
        for (MilkingAnimalDetails animalDetails : milkingAnimalDetails){
            mCount = mCount + animalDetails.getTotalNoOfAnimals();
        }

        return count + mCount;
    }

    @Override
    public List<FarmerCountStateDistrict> getDairyFarmerCountByStates() {
        List<FarmerCountStateDistrict> farmerCountStateDistricts = new ArrayList<>();
        List<State> states =  locationServices.getStates();

        for(State state : states) {
            long farmerCountByState = userService.getDairyFarmerCountByLocation("STATE",state.getId());
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            farmerCount.setName(state.getName());
            farmerCount.setId(state.getId());
            farmerCount.setCount(farmerCountByState);
            farmerCountStateDistricts.add(farmerCount);
        }
        return farmerCountStateDistricts;
    }

    @Override
    public List<FarmerCountStateDistrict> getDairyFarmerCountByDistrict(String stateId) {
        List<FarmerCountStateDistrict> farmerCountStateDistricts = new ArrayList<>();
        List<City> cities =  farmerAppLocationService.getCitiesByStateId(stateId,"Hindi");

        for(City city : cities) {
            long farmerCountByDistrict = userService.getDairyFarmerCountByLocation("DISTRICT", city.getId());
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            farmerCount.setName(city.getName());
            farmerCount.setId(city.getId());
            farmerCount.setCount(farmerCountByDistrict);
            farmerCountStateDistricts.add(farmerCount);
        }
        return farmerCountStateDistricts;
    }

    private LandTypeSize getLandTypeSizeByState(State state){
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;
        LandTypeSize landTypeSize = new LandTypeSize();

        Query query = new Query(Criteria.where("stateId").is(state.getId()));
        List<UserLandDetail> userLandDetails = mongoTemplate.find(query, UserLandDetail.class);

        UserServicesImpl.calLandSizeTypeByState(state, totalAreaCount, irrigatedLandSize, semiIrrigatedLandSize, rainfedLandSize, landTypeSize, userLandDetails);
        return landTypeSize;
    }

    private LandTypeSize getLandTypeSizeByCity(City city){
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        LandTypeSize landTypeSize = new LandTypeSize();
        List<UserLandDetail> userLandDetails = getUserLandDetailsByDistrict(city.getId());
        UserServicesImpl.calLandTypeSizeByCity(city, totalAreaCount, irrigatedLandSize, semiIrrigatedLandSize, rainfedLandSize, landTypeSize, userLandDetails);
        return landTypeSize;
    }

    private List<UserLandDetail> getUserLandDetailsByDistrict(String districtId) {
        Query query = new Query(Criteria.where("cityId").is(districtId));
        return mongoTemplate.find(query, UserLandDetail.class);
    }

    @Override
    public Long getUserTypeCount(String roleName, boolean active) {
        Query query = new Query(Criteria.where("roles.roleName").is(roleName));
        if (active){
            query = new Query(Criteria.where("roles.roleName").is(roleName).and("isActive").exists(true).ne(false));
        }
        return this.mongoTemplate.count(query, User.class);
    }

    @Override
    public ManagerFarmerCount getManagerFarmerCount(String userId) {

        ManagerFarmerCount managerFarmerCount = new ManagerFarmerCount();

        User user = userService.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Invalid User");
        }

        List<Role> userRoleList = user.getRoles();
        Query query = new Query();
        if (userRoleList.contains(roleService.roleName(ROLE_NATIONAL_MANAGER))) {
            logger.info("======NationalManager========");
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_STATE_MANAGER).and("createdByUserId").is(userId));
            long smCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setStateManagerCount(smCount);

            query = new Query(Criteria.where("roles.roleName").is(ROLE_DISTRICT_MANAGER).and("createdByUserId").is(userId));
            long dmCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setDistrictManagerCount(dmCount);

            query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT_MANAGER).and("createdByUserId").is(userId));
            long amCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentManagerCount(amCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_AGENT).and("createdByUserId").is(userId));
            long agentCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentCount(agentCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_USER).and("createdByUserId").is(userId));
            long farmerCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setFarmerCount(farmerCount);

        } else if (userRoleList.contains(roleService.roleName(ROLE_STATE_MANAGER))) {
            logger.info("======stateManager========");
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_DISTRICT_MANAGER).and("createdByUserId").is(userId));
            long dmCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setDistrictManagerCount(dmCount);

            query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT_MANAGER).and("createdByUserId").is(userId));
            long amCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentManagerCount(amCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_AGENT).and("createdByUserId").is(userId));
            long agentCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentCount(agentCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_USER).and("createdByUserId").is(userId));
            long farmerCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setFarmerCount(farmerCount);

        } else if (userRoleList.contains(roleService.roleName(ROLE_DISTRICT_MANAGER))) {
            logger.info("======disrtictManager========");

            query = new Query(Criteria.where("roles.roleName").is(ROLE_AGENT_MANAGER).and("createdByUserId").is(userId));
            long amCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentManagerCount(amCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_AGENT).and("createdByUserId").is(userId));
            long agentCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentCount(agentCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_USER).and("createdByUserId").is(userId));
            long farmerCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setFarmerCount(farmerCount);

        } else if (userRoleList.contains(roleService.roleName(ROLE_AGENT_MANAGER))) {
            logger.info("======ROLE_AGENT_MANAGER========");

            query = new Query(Criteria.where("roles.roleName").in(ROLE_AGENT).and("createdByUserId").is(userId));
            long agentCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setAgentCount(agentCount);

            query = new Query(Criteria.where("roles.roleName").in(ROLE_USER).and("createdByUserId").is(userId));
            long farmerCount = mongoTemplate.count(query, User.class);
            managerFarmerCount.setFarmerCount(farmerCount);
        }

        return managerFarmerCount;
    }

    @Override
    public CountAll getCountAll() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.findOne(query,CountAll.class);
    }

    @Override
    public OCEligibleUsersAndCount getOCEligibleUsers(String fromDate, String toDate) throws ParseException {
        OCEligibleUsersAndCount ocEligibleUsersAndCount = new OCEligibleUsersAndCount();
        Set<User> userSet = new HashSet<>();
        Set<String> userIds = new HashSet<>();
        Query query;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date from = dateFormat.parse(fromDate);
        final Date to = dateFormat.parse(toDate);

        if (fromDate !=null && toDate != null) {
            query = new Query(Criteria.where("organicLandEligible").ne(false).exists(true).and("creationDate").gte(from).lt(to)).with(Sort.by(Sort.Direction.ASC, "creationDate"));;
        } else {
            query = new Query(Criteria.where("organicLandEligible").ne(false).exists(true)).with(Sort.by(Sort.Direction.ASC, "creationDate"));;
        }
        List<OrganicCertificate> organicCertificateList = mongoTemplate.find(query,OrganicCertificate.class);
        System.out.println("===organicCertificateList===="+organicCertificateList.size());
        for (OrganicCertificate organicCertificate : organicCertificateList){
            if(!StringUtils.isEmpty(organicCertificate.getUserId()) && organicCertificate.isOrganicLandEligible()){
                userIds.add(organicCertificate.getUserId());
            }
        }
//        store users in set
        for(String userId : userIds){
            User user = userService.findByid(userId);
            userSet.add(user);
        }

        ocEligibleUsersAndCount.setUsers(userSet);
        ocEligibleUsersAndCount.setCount(userSet.size());
        return ocEligibleUsersAndCount;
    }

    @Override
    public List<FarmerCountStateDistrict> getOCEligibleUsersCountByStates() throws ParseException {
        List<FarmerCountStateDistrict> ocEligibleUserCountStates = new ArrayList<>();

        List<State> states = locationServices.getStates();
        OCEligibleUsersAndCount ocEligibleUsersAndCount = getOCEligibleUsers(null,null);
        Set<User> userSet = ocEligibleUsersAndCount.getUsers();
        for(State state : states) {
            int count = 0;
            for(User user : userSet){
                if(user.getAddressModel().getState().getId().equals(state.getId())){
                    count = count+1;
                }
            }
            FarmerCountStateDistrict ocEligibleCount = new FarmerCountStateDistrict();
            ocEligibleCount.setName(state.getName());
            ocEligibleCount.setId(state.getId());
            ocEligibleCount.setCount(count);
            ocEligibleUserCountStates.add(ocEligibleCount);
        }

        return ocEligibleUserCountStates;
    }

    @Override
    public List<FarmerCountStateDistrict> getSTRCountByStates(String farmingType, String fromDate, String toDate) throws ParseException {
        List<FarmerCountStateDistrict> strCountStateDistricts = new ArrayList<>();
        List<State> states =  locationServices.getStates();

        for(State state : states) {
            long strCountByState = reportHistoryService.getReportHistoryCount(farmingType, "STATE",state.getId(),fromDate,toDate);
            FarmerCountStateDistrict strCount = new FarmerCountStateDistrict();
            strCount.setName(state.getName());
            strCount.setId(state.getId());
            strCount.setCount(strCountByState);
            strCountStateDistricts.add(strCount);
        }
        return strCountStateDistricts;
    }

    @Override
    public List<FarmerCountStateDistrict> getSTRCountByDistrict(String stateId, String farmingType, String fromDate, String toDate) throws ParseException {
        List<FarmerCountStateDistrict> strCountStateDistricts = new ArrayList<>();
        List<City> cities =  farmerAppLocationService.getCitiesByStateId(stateId,"Hindi");

        for(City city : cities) {
            long strCountByDistrict = reportHistoryService.getReportHistoryCount(farmingType, "DISTRICT",city.getId(),fromDate,toDate);;
            FarmerCountStateDistrict strCount = new FarmerCountStateDistrict();
            strCount.setName(city.getName());
            strCount.setId(city.getId());
            strCount.setCount(strCountByDistrict);
            strCountStateDistricts.add(strCount);
        }
        return strCountStateDistricts;
    }

    @Override
    public List<FarmerCountStateDistrict> getSTRCountByTehsil(String districtId, String farmingType, String fromDate, String toDate) throws ParseException {
        List<FarmerCountStateDistrict> strCountTehsils = new ArrayList<>();
        List<Tehsil> tehsils =  farmerAppLocationService.getTehsilListByDistrict(districtId,"Hindi");

        for(Tehsil tehsil : tehsils) {
            long strCountByTehsil = reportHistoryService.getReportHistoryCount(farmingType, "TEHSIL",tehsil.getId(),fromDate,toDate);;
            FarmerCountStateDistrict strCount = new FarmerCountStateDistrict();
            strCount.setName(tehsil.getName());
            strCount.setId(tehsil.getId());
            strCount.setCount(strCountByTehsil);
            strCountTehsils.add(strCount);
        }
        return strCountTehsils;
    }

    @Override
    public List<FarmerCountStateDistrict> getSTRCountByBlock(String tehsilId, String farmingType, String fromDate, String toDate) throws ParseException {
        List<FarmerCountStateDistrict> strCountBlocks = new ArrayList<>();
        List<Block> blocks =  farmerAppLocationService.getBlockListByTehsil(tehsilId,"Hindi");

        for(Block block : blocks) {
            long strCountByBlock = reportHistoryService.getReportHistoryCount(farmingType, "BLOCK",block.getId(),fromDate,toDate);;
            FarmerCountStateDistrict strCount = new FarmerCountStateDistrict();
            strCount.setName(block.getName());
            strCount.setId(block.getId());
            strCount.setCount(strCountByBlock);
            strCountBlocks.add(strCount);
        }
        return strCountBlocks;
    }

    @Override
    public List<FarmerCountStateDistrict> getTotalFarmerCountByTehsil(String districtId) {
        List<FarmerCountStateDistrict> count = new ArrayList<>();
        List<Tehsil> tehsils = farmerAppLocationService.getTehsilListByDistrict(districtId,"Hindi");
        for(Tehsil tehsil : tehsils){
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER)
                    .and("addressModel.tehsil.$id").is(new ObjectId(tehsil.getId())));

            farmerCount.setName(tehsil.getName());
            farmerCount.setId(tehsil.getId());
            farmerCount.setCount(mongoTemplate.count(query, User.class));
            count.add(farmerCount);
        }
        return count;
    }

    @Override
    public List<FarmerCountStateDistrict> getTotalFarmerCountByBlock(String tehsilId) {
        List<FarmerCountStateDistrict> count = new ArrayList<>();
        List<Block> blocks = farmerAppLocationService.getBlockListByTehsil(tehsilId,"Hindi");
        for(Block block : blocks){
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            Query query = new Query(Criteria.where("roles.roleName").is(ROLE_USER)
                    .and("addressModel.block.$id").is(new ObjectId(block.getId())));

            farmerCount.setName(block.getName());
            farmerCount.setId(block.getId());
            farmerCount.setCount(mongoTemplate.count(query, User.class));
            count.add(farmerCount);
        }
        return count;
    }

    @Override
    public List<FarmerCountStateDistrict> getDairyFarmerCountByTehsil(String districtId) {
        List<FarmerCountStateDistrict> farmerCountTehsils = new ArrayList<>();
        List<Tehsil> tehsils =  farmerAppLocationService.getTehsilListByDistrict(districtId,"Hindi");

        for(Tehsil tehsil : tehsils) {
            long farmerCountByDistrict = userService.getDairyFarmerCountByLocation("TEHSIL", tehsil.getId());
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            farmerCount.setName(tehsil.getName());
            farmerCount.setId(tehsil.getId());
            farmerCount.setCount(farmerCountByDistrict);
            farmerCountTehsils.add(farmerCount);
        }
        return farmerCountTehsils;
    }

    @Override
    public List<FarmerCountStateDistrict> getDairyFarmerCountByBlock(String tehsilId) {
        List<FarmerCountStateDistrict> farmerCountBlocks = new ArrayList<>();
        List<Block> blocks =  farmerAppLocationService.getBlockListByTehsil(tehsilId,"Hindi");

        for(Block block : blocks) {
            long farmerCountByDistrict = userService.getDairyFarmerCountByLocation("BLOCK", block.getId());
            FarmerCountStateDistrict farmerCount = new FarmerCountStateDistrict();
            farmerCount.setName(block.getName());
            farmerCount.setId(block.getId());
            farmerCount.setCount(farmerCountByDistrict);
            farmerCountBlocks.add(farmerCount);
        }
        return farmerCountBlocks;
    }

    @Override
    public long getAllUsersCountByDate(String fromDate, String toDate, String locationType, String locationId) throws ParseException {
        Query query = getQueryByReportType(fromDate, toDate,locationType,locationId,USER_STATUS);
        return mongoTemplate.count(query,User.class);
    }

    @Override
    public List<User> getAllUserListByDate(String fromDate, String toDate, String locationType, String locationId) throws ParseException {
        Query query = getQueryByReportType(fromDate, toDate,locationType,locationId,USER_STATUS);
        return mongoTemplate.find(query,User.class);
    }

    @Override
    public CountAll storeCountAll(CountAll countAll) {
        return mongoTemplate.save(countAll);
    }


    /**
     * Return location key for query
     *
     * @param locationType
     * @return
     */
    public static String getLocationKeyFromUser(String locationType) {
        String searchLocationKey;
        // Filter for locationTye
        switch (locationType) {
            case "STATE":
                searchLocationKey = "addressModel.state.$id";
                break;
            case "DISTRICT":
                searchLocationKey = "addressModel.city.$id";
                break;
            case "TEHSIL":
                searchLocationKey = "addressModel.tehsil.$id";
                break;
            case "BLOCK":
                searchLocationKey = "addressModel.block.$id";
                break;
            case "VILLAGE":
                searchLocationKey = "addressModel.villageModel.$id";
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }
        return searchLocationKey;
    }



    @Override
    public long getCountByReportType(String reportType, String fromDate, String toDate, String locationType, String locationId, String farmingType) throws ParseException {
        long count = 0;
        Query query = getQueryByReportType(fromDate, toDate, locationType, locationId, reportType);
        switch (reportType) {
            case PERSONAL_DETAILS:
                count = mongoTemplate.count(query, User.class);
                break;
            case HORTICULTURE:
                count = mongoTemplate.count(query, Horticulture.class);
                break;
            case GOVERNMENT_SCHEME:
                List<User> distinctUserList = mongoTemplate.findDistinct(query,"user", UserSchemes.class,User.class);
                count = distinctUserList.size();
                break;
            case KHASRA_LOC_COORDINATES:
                count = mongoTemplate.count(query, UserLandDetail.class);
                break;
            case BEEKEEPING_DETAIL:
                count = mongoTemplate.count(query, BeekeepingDetails.class);
                break;
            case DAIRY_FARM_DETAIL:
                count = mongoTemplate.count(query, MilkingAnimalDetails.class);
                break;
            case CROP_EXPENSE_DETAIL:
                count = mongoTemplate.count(query, UserCrop.class);
                break;
            case FAMILY_DETAILS:
                count = mongoTemplate.count(query, FamilyMember.class);
                break;
            case SUBSIDY_DETAIL:
                count = mongoTemplate.count(query, UserSubsidy.class);
                break;
            case VILLAGE_ASSET:
                count = mongoTemplate.count(query, VillageAsset.class);
                break;
            case HEALTH_DETAIL:
                count = mongoTemplate.count(query, FamilyMemberHealthRecord.class);
                break;
            case FARM_EQUIPMENT:
                count = mongoTemplate.count(query, UserEquipments.class);
                break;
            case USER_ASSIGNMENT_HISTORY:
                count = mongoTemplate.count(query, EmployeeAssignmentHistory.class);
                break;
            case USER_STATUS:
                count = mongoTemplate.count(query, User.class);
                break;
            case RECOMMENDATION_REPORT:
                count = reportHistoryService.getReportHistoryCount(farmingType, locationType, locationId,fromDate,toDate);
                break;
            default:
                throw new CustomException("Report Type is not correct.");
        }
        return count;
    }

    public static Query getQueryByReportType(String fromDate, String toDate, String locationType, String locationId, String reportType) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        Criteria criteria = new Criteria();

        if(!StringUtils.isEmpty(locationType) && !StringUtils.isEmpty(locationId)){
            // get search location key from user collection
            String searchLocationKey;
            switch (reportType) {
                case PERSONAL_DETAILS:
                    searchLocationKey = getLocationKeyFromUser(locationType);
                    criteria = criteria.and("roles.roleName").is(ROLE_USER);
                    break;
                case KHASRA_LOC_COORDINATES:
                    searchLocationKey = getLocationKeyLandDetails(locationType);
                    break;
                case VILLAGE_ASSET:
                    searchLocationKey = getLocationKeyVillageAsset(locationType);
                    break;
                case USER_STATUS:
                    searchLocationKey = getLocationKeyFromUser(locationType);
                    break;
                default:
                    searchLocationKey = getLocationKey(locationType);
                    break;
            }

            criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
        }

        criteria = criteria.and("creationDate").gte(from).lt(to);

        return new Query(criteria).with(Sort.by(Sort.Direction.ASC, "creationDate"));
    }
}
