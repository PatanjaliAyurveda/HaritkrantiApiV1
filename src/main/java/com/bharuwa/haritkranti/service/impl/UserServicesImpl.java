package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.aws.AwsS3;
import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.location.Block;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.newmodels.AppLoginTable;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.models.requestModels.ResetPassReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserStatus;
import com.bharuwa.haritkranti.models.responseModels.*;
import com.bharuwa.haritkranti.repositories.*;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.bharuwa.haritkranti.utils.nextSequence.NextSequenceService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bharuwa.haritkranti.models.Equipment.EquipmentType.Immoveable;
import static com.bharuwa.haritkranti.models.Equipment.EquipmentType.Moveable;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.generateUniqueCodeForUser;

/**
 * @author anuragdhunna
 */
@Service
public class UserServicesImpl implements UserService, UserDetailsService {

    private final MongoTemplate mongoTemplate;
    private AwsS3 awsS3;
    private static final Logger logger = LoggerFactory.getLogger(UserServicesImpl.class);

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private RegistrationOtpRepo regOtpRepo;

    @Autowired
    private NextSequenceService nextSequenceService;
    @Autowired
    private AadharRepo aadharRepo;
    @Autowired
    private FamilyRepo familyRepo;
    @Autowired
    private FarmRepo farmRepo;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FinancialDetailRepo financialDetailRepo;
    @Autowired
    private IncomeSourcesRepo incomeSourcesRepo;
    @Autowired
    private LoanDetailRepo loanDetailRepo;

    @Autowired
    private InsuranceDetailsRepo insuranceDetailsRepo;

    @Autowired
    private FamilyMemberHeathRepo familyMemberHeathRepo;

    @Autowired
    private UserIdentificationDetailRepo userIdentificationDetailRepo;

    @Autowired
    private UserEquipmentRepo userEquipmentRepo;

    @Autowired
    private CropImageRepo cropImageRepo;

    @Autowired
    private FarmDetailsRepo farmDetailsRepo;

    @Autowired
    private UserSubsidyRepo userSubsidyRepo;

    @Autowired
    private SubsidyRepo subsidyRepo;

    @Autowired
    private AgentService agentService;

    @Autowired
    private LocationServices locationServices;

    @Autowired
    private LandService landService;

    @Autowired
    private UserCropRepo userCropRepo;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private ReportHistoryRepo reportHistoryRepo;

    @Autowired
    private UserLandDetailRepo userLandDetailRepo;

    @Autowired
    private MilkingAnimalDetailsRepo milkingAnimalDetailsRepo;

    @Autowired
    private FamilyMemberRepo familyMemberRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FarmerExtraDetailsRepo farmerExtraDetailsRepo;

    @Autowired
    private UserSchemesRepo userSchemesRepo;

    @Autowired
    private UserCertificateRepo userCertificateRepo;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private CountService countService;

    @Autowired
    private GenericMongoTemplate genericMongoTemplate;

    public UserServicesImpl(MongoTemplate mongoTemplate, AwsS3 awsS3) {
        this.mongoTemplate = mongoTemplate;
        this.awsS3 = awsS3;
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }
    
    @Override
    public RegistrationOtp save(RegistrationOtp otp) {
        return regOtpRepo.save(otp);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User getUserByPhoneNum(String phoneNumber) {
        return genericMongoTemplate.findByKey("primaryPhone", phoneNumber, User.class);
    }

    @Override
    public User findByid(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public boolean exist(String id) {
        return userRepo.existsById(id);
    }

    @Override
    public RegistrationOtp getRegisteredOtp(String phoneNumber,String regOtp) {
    	RegistrationOtp otp=null;
    	otp=regOtpRepo.findByMobileNumber(phoneNumber,regOtp);
    	return otp;
    }
    @Override
    public Set<String> getLandKhasraNos(String userId) {
        User user = findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user.getLandKhasraNos() == null ? new HashSet<>() : user.getLandKhasraNos();
    }
    
    @Override
    public List<String> getKhasraList(String state,String district,String tehsil,String block,String village,String language) {
    	
    	Criteria stateCriteria = new Criteria("State").is(state);
    	Criteria distCriteria = new Criteria("District").is(district);
    	Criteria tehsilCriteria = new Criteria("Tehsil").is(tehsil);
    	Criteria blockCriteria = new Criteria("Block").is(block);
    	Criteria villageCriteria = new Criteria("Village").is(village);
        
    	Criteria stateInHindiCriteria = new Criteria("stateInHindi").is(state);
    	Criteria distInHindiCriteria = new Criteria("districtInHindi").is(district);
    	Criteria tehsilInHindiCriteria = new Criteria("tehsilInHindi").is(tehsil);
    	Criteria blockinHindiCriteria = new Criteria("blockInHindi").is(block);
    	Criteria villageInHindiCriteria = new Criteria("villageInHindi").is(village);
    	
    	Query query = new Query();
    	if(language.equals("Hindi")) {
    		query.addCriteria(stateInHindiCriteria);
        	query.addCriteria(distInHindiCriteria);
        	query.addCriteria(tehsilInHindiCriteria);
        	query.addCriteria(blockinHindiCriteria);
        	query.addCriteria(villageInHindiCriteria);
    	}else if(language.equals("English")) {
    		query.addCriteria(stateCriteria);
        	query.addCriteria(distCriteria);
        	query.addCriteria(tehsilCriteria);
        	query.addCriteria(blockCriteria);
        	query.addCriteria(villageCriteria);
    	}
    	
    	List<String> khasraList = mongoTemplate.findDistinct(query,"KhasraNumber",GovtMapData.class,String.class);
    	
    	//List<CropYield> list = mongoTemplate.find(query, CropYield.class);
    	//List<String> khasraList = new ArrayList<String>();
    	//for(CropYield yield:list) {
    	//	khasraList.add(yield.getKhasraNumber());
    	//}
    	
   // 	System.out.println(list);
        return khasraList;
    }

//    @Override
//    public Set<String> getVillageList(String userId) {
//        User user = findByid(userId);
//        if (user == null) {
//            throw new ResourceNotFoundException("User not found");
//        }
//        return user.getVillageList() == null ? new HashSet<>() : user.getVillageList();
//    }

//    @Override
//    public Set<String> getTehsilList(String userId) {
//        User user = findByid(userId);
//        if (user == null) {
//            throw new ResourceNotFoundException("User not found");
//        }
//        return user.getTehsils() == null ? new HashSet<>() : user.getTehsils();
//    }

    @Override
    public List<String> getCropName(String khasraNumber) {
    	Object khasra = khasraNumber;
    	Criteria stateCriteria = new Criteria("State").is(khasra);

    	Query query = new Query();
    	query.addCriteria(stateCriteria);

    	List<CropYield> list = mongoTemplate.find(query, CropYield.class);
    	List<String> cropName = new ArrayList<String>();
    	for(CropYield yield:list) {
 //   		cropName.add(yield.getKhasraNumber());
    	}
   // 	System.out.println(list);
        return cropName;
    }
    
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        Query query = new Query();
        query = query.addCriteria(Criteria.where("primaryPhone").is(phone));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getPrimaryPhone(), user.getPassword(), getAuthority(user));
    }

//    @Override
//    public User addFamilyMember(String userId, User user) {
//        if (!userRepo.existsById(userId)) {
//            throw new ResourceNotFoundException("User not found");
//        }
//
//        User userExist = getUserByPhoneNum(user.getPrimaryPhone());
//        if (userExist != null) {
//            throw new ResourceAlreadyExists("User already exists");
//        }
//
//        List<Role> roleList = new ArrayList<>();
//        Role role = roleService.roleName("FAMILY_MEMBER");
//        roleList.add(role);
//        user.setRoles(roleList);
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));
//        user.setCreatedVia(User.CreatedVia.AGENT);
//        User familyMember = userRepo.save(user);
//
//        Query query = new Query(Criteria.where("userId").is(userId));
//        Family family = mongoTemplate.findOne(query, Family.class);
//
//        List<User> users;
//        if (family == null) {
//            users = new ArrayList<>();
//            users.add(familyMember);
//            familyRepo.save(new Family(userId, users));
//        } else {
//            users = family.getFamilyMembers();
//            users.add(familyMember);
//            familyRepo.save(family);
//        }
//        return familyMember;
//    }

//    @Override
//    public List<User> getFamilyMembers(String userId) {
//        Query query = new Query(Criteria.where("userId").is(userId));
//        Family family = mongoTemplate.findOne(query, Family.class);
//        if (family == null) {
//            return new ArrayList<>();
//        }
//        return family.getFamilyMembers();
//    }

    @Override
    public Farm addFarmDetail(Farm farm) {
        if (!userRepo.existsById(farm.getUserId())) {
            throw new ResourceNotFoundException("User not found");
        }
        return farmRepo.save(farm);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            //authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public List<User> getAgentList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);
        query.addCriteria(Criteria.where("roles.roleName").is(ROLE_USER));
        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    @Override
    public FamilyMemberHealthRecord addFamilyMemberHealthDetails(FamilyMemberHealthRecord familyMemberHealthRecord) {
        User user = userRepo.findByid(familyMemberHealthRecord.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        familyMemberHealthRecord.setAddress(user.getAddressModel());
        return familyMemberHeathRepo.save(familyMemberHealthRecord);
    }

    @Override
    public FamilyMemberHealthRecord getFamilyMemberHealthDetails(String userId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, FamilyMemberHealthRecord.class);
    }

    @Override
    public List<FamilyMemberHealthRecord> getAllFamilyMembersHealthDetails(String headUserId) {
        User user = userRepo.findByid(headUserId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("headUserId").is(headUserId));
        return mongoTemplate.find(query, FamilyMemberHealthRecord.class);
    }

    @Override
    public UserIdentificationDetails addUserIdentificationDetails(UserIdentificationDetails userIdentificationDetails) {
        if (!StringUtils.isEmpty(userIdentificationDetails.getPanCard()) && !userIdentificationDetails.getPanCard().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            throw new CustomException("PAN Card Number is invalid");
        }
        return mongoTemplate.save(userIdentificationDetails);
    }

    @Override
    public UserIdentificationDetails getUserIdentificationDetails(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, UserIdentificationDetails.class);
    }

    @Override
    public UserIdentificationDetails getFamilyMemberIdentificationDetails(String memberId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("familyMemberId").is(memberId));
        return mongoTemplate.findOne(query, UserIdentificationDetails.class);
    }

    @Override
    public UserEquipments addUserEquipment(UserEquipments userEquipments) {
        User user = userRepo.findByid(userEquipments.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        UserEquipments value = getUserEquipments(userEquipments.getUserId());

        if (value == null) {
            userEquipments.setAddress(user.getAddressModel());
            userEquipmentRepo.save(userEquipments);
        } else {
            value.setMoveableEquipments(userEquipments.getMoveableEquipments());
            value.setImmoveableEquipments(userEquipments.getImmoveableEquipments());
            value.setAddress(user.getAddressModel());
            userEquipmentRepo.save(value);
        }
        return userEquipments;
    }

    @Override
    public UserEquipments getUserEquipments(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, UserEquipments.class);
    }

    public MessageResponse uploadCropImages(MultipartFile file, String cropId, String khasraNo, String userId, String agentId) {

        User user = userRepo.findByid(userId);
        User agent = userRepo.findByid(agentId);
        if (user == null || agent == null) {
            throw new ResourceNotFoundException("User not found");
        }

        String url = this.awsS3.uploadFile(file);
        CropImage cropImage = new CropImage();
        cropImage.setCropId(cropId);
        cropImage.setKhasraNo(khasraNo);
        cropImage.setUserId(userId);
        cropImage.setAgentId(agentId);
        cropImage.setImageUrl(url);
        cropImageRepo.save(cropImage);
        MessageResponse response = new MessageResponse();
        response.setMessage("Images saved");
        return response;
    }


    @Override
    public String uploadImageS3(MultipartFile file) {
        return this.awsS3.uploadFile(file);
    }

    @Override
    public FarmDetails addFarmDetails(FarmDetails farmDetails) {
        User user = userRepo.findByid(farmDetails.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        farmDetails.setAddress(user.getAddressModel());
        FarmDetails storedFarmDetails = farmDetailsRepo.save(farmDetails);

        //count set for Farm Details like Sheep,Sericulture
        CountAll countAll = countService.getCountAll();
        if (farmDetails.getAnimalType().equals(FarmDetails.AnimalType.Sheep)) {
            countAll.setTotalSheeps(countService.getTotalSheepCount());
        } else if (farmDetails.getAnimalType().equals(FarmDetails.AnimalType.Pupa)) {
            countAll.setTotalSericultureFarmers(countService.getSericultureFarmerCount());
        }
        countService.storeCountAll(countAll);

        return farmDetails;
    }

    @Override
    public List<FarmDetails> getFarmDetails(String userId, String animalType) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("animalType").is(animalType.trim()));
        return mongoTemplate.find(query, FarmDetails.class);
    }

    @Override
    public UserSubsidy storeUserSubsidy(UserSubsidy userSubsidy) {
        User user = findByid(userSubsidy.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        userSubsidy.setAddress(user.getAddressModel());
        return userSubsidyRepo.save(userSubsidy);
    }

    @Override
    public List<Subsidy> getSubsidies() {
        return subsidyRepo.findAll();
    }

    @Override
    public UserSubsidy getUserSubsidyDetails(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, UserSubsidy.class);
    }

    @Override
    public void storeSubsidy(Subsidy subsidy) {
        Query query = new Query(Criteria.where("name").is(subsidy.getName()));
        Subsidy exist = mongoTemplate.findOne(query, Subsidy.class);
        if (exist == null) {
            subsidyRepo.save(subsidy);
        }
    }

    @Override
    public List<UserSubsidy> getUserSubsidies(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, UserSubsidy.class);
    }

    @Override
    public FamilyMember addFamilyMemberNew(FamilyMember familyMember) {

        User user = userRepo.findByid(familyMember.getFarmerId());
        if (user == null) {
            throw new ResourceNotFoundException("Farmer not found");
        }

//        Query query = new Query(Criteria.where("_id").is(new ObjectId(user.getStateId())));
//        Query query = new Query(Criteria.where("_id").is(new ObjectId(user.getAddressModel().getState().getId())));
//        State state = mongoTemplate.findOne(query, State.class);

//        query = new Query(Criteria.where("_id").is(new ObjectId(user.getDistrictId())));
//        query = new Query(Criteria.where("_id").is(new ObjectId(user.getAddressModel().getCity().getId())));
//        City city = mongoTemplate.findOne(query, City.class);


//        if (familyMember.isSameAddress()) {
//            Address address = familyMember.getAddress();
//            if (address == null) {
//                address = new Address();
//            }
//            address.setCity(city);
//            address.setState(state);
//            if (user.getAddressModel().getTehsil() != null) {
//                address.setTehsil(user.getAddressModel().getTehsil());
//            }
//            if (user.getAddressModel().getBlock() != null) {
//                address.setBlock(user.getAddressModel().getBlock());
//            }
//            if (user.getAddressModel().getVillageModel() != null) {
//                address.setVillageModel(user.getAddressModel().getVillageModel());
//            }
//            address.setAddress(user.getAddressModel().getAddress());
//            familyMember.setAddress(address);
//        }
        familyMember.setAddress(user.getAddressModel());
        return familyMemberRepo.save(familyMember);
    }

    @Override
    public List<FamilyMember> getFamilyMembersNew(String userId) {
        Query query = new Query(Criteria.where("farmerId").is(userId));
        return mongoTemplate.find(query, FamilyMember.class);
    }

    @Override
    public FamilyMember getFamilyMemberById(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, FamilyMember.class);
    }

    @Override
    public void organiseFamilyMember(String phoneNumber, String stateName, String cityName) {


//        "9595222360"  district >> agent managers


        User adminUser = getUserByPhoneNum("1212121212");
        User userByPhoneNum = getUserByPhoneNum(phoneNumber);
        if (userByPhoneNum == null) {
            throw new ResourceNotFoundException("userByPhoneNum not found");
        }

        // get list of all agent managers
        State maharashtra = locationServices.findStateByName(stateName);
        City haridwar = locationServices.getCityByName(cityName);

//        List<String> roleList = new ArrayList<>();
//        roleList.add(ROLE_DISTRICT_MANAGER);

        Query query = new Query(Criteria.where("addressModel.state").is(maharashtra).and("addressModel.city")
                .is(haridwar).and("roles.roleName").is(ROLE_AGENT_MANAGER).and("createdByUserId").is(adminUser.getId()));

        // check role

        List<User> users = mongoTemplate.find(query, User.class);

        for (User userExist : users) {
            userExist.setCreatedByUserId(userByPhoneNum.getId());
            userRepo.save(userExist);
        }
        //get agents of maharastra


    }
//        List<Family> families = mongoTemplate.findAll(Family.class);
//        for (Family family : families) {
//            String farmerId = family.getUserId();
//            List<User> familyMembers = family.getFamilyMembers();
//
//            for (User user : familyMembers) {
//                FamilyMember member = new FamilyMember();
//                Address address = new Address();
//
//                String stateId = user.getAddressModel().getState().getId();
//                State state;
//                Query query;
//                if (!StringUtils.isEmpty(stateId)) {
//                    query = new Query(Criteria.where("_id").is(new ObjectId(stateId)));
//                    state = mongoTemplate.findOne(query, State.class);
//                } else {
//                    query = new Query(Criteria.where("name").is(user.getAddressModel().getState().getName()));
//                    state = mongoTemplate.findOne(query, State.class);
//                }
//                address.setState(state);
//
//                String districtId = user.getAddressModel().getCity().getId();
//                City city;
//
//                if (!StringUtils.isEmpty(districtId)) {
//                    query = new Query(Criteria.where("_id").is(new ObjectId(districtId)));
//                    city = mongoTemplate.findOne(query, City.class);
//                } else {
//                    query = new Query(Criteria.where("name").is(user.getAddressModel().getCity().getName()));
//                    city = mongoTemplate.findOne(query, City.class);
//                }
//                address.setCity(city);
//
//                address.setVillage(user.getAddressModel().getVillage());
//                address.setAddress(user.getAddressModel().getAddress());
//                member.setAddress(address);
//
//                member.setFirstName(user.getFirstName());
//                member.setLastName(user.getLastName());
//                member.setSameAddress(true);
//
//                member.setFarmerId(farmerId);
//
//                query = new Query(Criteria.where("users.$id").is(new ObjectId(farmerId)));
//                UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
//                if (userAgent != null) {
//                    member.setAgentId(userAgent.getAgentId());
//                    // Check if user exists
//                    query = new Query(Criteria.where("agentId").is(userAgent.getAgentId()).and("farmerId").is(farmerId)
//                            .and("firstName").is(user.getFirstName()).and("lastName").is(user.getLastName()));
//                    List<FamilyMember> existFamilyMembers = mongoTemplate.find(query, FamilyMember.class);
//
//                    if (existFamilyMembers.isEmpty()) {
//                        mongoTemplate.insert(member);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void organiseUserCrop() {
        List<UserCrop> userCrops = mongoTemplate.findAll(UserCrop.class);

        for (UserCrop userCrop : userCrops) {
            Crop crop = landService.getCrop(userCrop.getCropId());
            userCrop.setCrop(crop);
            userCrop.setCropYield(userCrop.getLastCultivateCropYield());
            userCrop.setCropYieldUnit(userCrop.getLastCultivateCropYieldUnit());
            userCrop.setYearOfSowing(Calendar.getInstance().getTime());
            userExtraService.addUserCrop(userCrop);
        }
    }

    @Override
    public FarmDetails getFarmDetailById(String farmDetailId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(farmDetailId)));
        return mongoTemplate.findOne(query, FarmDetails.class);
    }

    @Override
    public void removeUserAccountOfAgent(String phoneNumber) {
        User user = getUserByPhoneNum(phoneNumber);
        Role role = roleService.roleName(ROLE_USER); //getRole
        Role agentRole = roleService.roleName(ROLE_AGENT);

        if (user == null) {
            throw new ResourceNotFoundException("User not Found");
        }
        //checkRole
        if (user.getRoles().contains(role)) {
            user.removeRole(role);
            user.addRole(agentRole);
        }
        userRepo.save(user);
    }

    @Override
    public void addLandDetailInUserCrop() {
        List<UserCrop> userCropList = userCropRepo.findAll();
        for (UserCrop userCrop : userCropList) {
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(userCrop.getKhasraNo(), userCrop.getUserId());
            if (userLandDetail != null) {
                userCrop.setUserLandDetailId(userLandDetail.getId());
                userCropRepo.save(userCrop);
            }
        }
    }

    private List<UserLandDetail> getUserLandDetailsByState(String id) {
        Query query = new Query(Criteria.where("stateId").is(id));
        return mongoTemplate.find(query, UserLandDetail.class);
    }

    @Override
    public List<CropArea> getCropsAreaCountry(String season) {
        List<CropArea> cropAreaList = new ArrayList<>();
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        int month = Calendar.getInstance().getTime().getMonth() + 1;
        if (StringUtils.isEmpty(season)) {
            if ((month >= 7) && (month <= 10)) {
                season = Crop.CropSeason.Kharif.toString();
            } else if ((month >= 11) || (month <= 3)) {
                season = Crop.CropSeason.Rabi.toString();
            } else if ((month >= 4) && (month <= 6)) {
                season = Crop.CropSeason.Zaid.toString();
            }
        }
        Query query = new Query();
        query.fields().include("landSizeType").include("landSize"); // Fetch only required Fields
        List<UserLandDetail> userLandDetails = mongoTemplate.find(query, UserLandDetail.class);

//        List<UserLandDetail> userLandDetails = genericMongoTemplate.findAll(UserLandDetail.class);
        System.out.println("=======33====" + new Date());
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal landSize = detail.getLandSize();
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                landSize = landSize.multiply(BigDecimal.valueOf(2.5));
            }
            totalAreaCount = totalAreaCount.add(landSize);
        }

        query = new Query(Criteria.where("cropSeasons").is(season));
        query.fields().include("id").include("cropName").include("cropType");
        List<Crop> cropList = mongoTemplate.find(query, Crop.class);
        for (Crop crop : cropList) {
            BigDecimal cropAreaCount = BigDecimal.ZERO;
            List<String> userLandDetailIds = userLandDetails.stream().map(UserLandDetail::getId).collect(Collectors.toList());
            query = new Query(Criteria.where("cropId").is(crop.getId()).and("userLandDetailId").in(userLandDetailIds));
            List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);
            CropArea cropArea = getCropArea(totalAreaCount, crop, cropAreaCount, userCrops);
            cropAreaList.add(cropArea);
        }
        return cropAreaList;
    }

    @Override
    public List<CropArea> getCropsAreaByState(String stateId, String season) {
        List<CropArea> cropAreaList = new ArrayList<>();
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        int month = Calendar.getInstance().getTime().getMonth() + 1;
        if (StringUtils.isEmpty(season)) {
            if ((month >= 7) && (month <= 10)) {
                season = Crop.CropSeason.Kharif.toString();
            } else if ((month >= 11) || (month <= 3)) {
                season = Crop.CropSeason.Rabi.toString();
            } else if ((month >= 4) && (month <= 6)) {
                season = Crop.CropSeason.Zaid.toString();
            }
        }
        List<Crop> cropList = landService.getCropsBySeason(season);

        State state = locationServices.getState(stateId);
        List<UserLandDetail> userLandDetails = getUserLandDetailsByState(stateId);
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal landSize = detail.getLandSize();
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                landSize = landSize.multiply(BigDecimal.valueOf(2.5));
            }
            totalAreaCount = totalAreaCount.add(landSize);
        }

        for (Crop crop : cropList) {
            BigDecimal cropAreaCount = BigDecimal.ZERO;
            List<String> userLandDetailIds = userLandDetails.stream().map(UserLandDetail::getId).collect(Collectors.toList());
            Query query = new Query(Criteria.where("cropId").is(crop.getId()).and("userLandDetailId").in(userLandDetailIds));
            List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);
            CropArea cropArea = getCropArea(totalAreaCount, crop, cropAreaCount, userCrops);
            cropArea.setStateName(state.getName());
            cropAreaList.add(cropArea);
        }
        return cropAreaList;
    }

    @Override
    public List<CropArea> getCropsAreaByDistrict(String districtId, String season) {
        List<CropArea> cropAreaList = new ArrayList<>();
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        int month = Calendar.getInstance().getTime().getMonth() + 1;
        if (StringUtils.isEmpty(season)) {
            if ((month >= 7) && (month <= 10)) {
                season = Crop.CropSeason.Kharif.toString();
            } else if ((month >= 11) || (month <= 3)) {
                season = Crop.CropSeason.Rabi.toString();
            } else if ((month >= 4) && (month <= 6)) {
                season = Crop.CropSeason.Zaid.toString();
            }
        }
        List<Crop> cropList = landService.getCropsBySeason(season);

        City city = locationServices.getCity(districtId);

        List<UserLandDetail> userLandDetails = getUserLandDetailsByDistrict(districtId);
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal landSize = detail.getLandSize();
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                landSize = landSize.multiply(BigDecimal.valueOf(2.5));
            }
            totalAreaCount = totalAreaCount.add(landSize);
        }

        for (Crop crop : cropList) {
            BigDecimal cropAreaCount = BigDecimal.ZERO;
            List<String> userLandDetailIds = userLandDetails.stream().map(UserLandDetail::getId).collect(Collectors.toList());
            Query query = new Query(Criteria.where("cropId").is(crop.getId()).and("userLandDetailId").in(userLandDetailIds));
            List<UserCrop> userCrops = mongoTemplate.find(query, UserCrop.class);
            CropArea cropArea = getCropArea(totalAreaCount, crop, cropAreaCount, userCrops);
            cropArea.setDistrictName(city.getName());
            cropAreaList.add(cropArea);
        }
        return cropAreaList;
    }

    private CropArea getCropArea(BigDecimal totalAreaCount, Crop crop, BigDecimal cropAreaCount, List<UserCrop> userCrops) {
        Query query;
        for (UserCrop userCrop : userCrops) {
            query = new Query(Criteria.where("_id").is(new ObjectId(userCrop.getUserLandDetailId())));
            query.fields().include("landSizeType").include("landSize"); // Fetch only required Fields
            UserLandDetail landDetail = mongoTemplate.findOne(query, UserLandDetail.class);
            if (landDetail != null) {
                BigDecimal landSize = landDetail.getLandSize();
                if (landDetail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    landSize = landSize.multiply(BigDecimal.valueOf(2.5));
                }
                cropAreaCount = cropAreaCount.add(landSize);
            }
        }

        CropArea cropArea = new CropArea();
        cropArea.setCropName(crop.getCropName());
        cropArea.setCropType(crop.getCropType().toString());
        cropArea.setTotalLandSize(totalAreaCount);
        cropArea.setCropLandSize(cropAreaCount);
        return cropArea;
    }

    private List<UserLandDetail> getUserLandDetailsByDistrict(String districtId) {
        Query query = new Query(Criteria.where("cityId").is(districtId));
        return mongoTemplate.find(query, UserLandDetail.class);
    }

    @Override
    public BigDecimal remainingAreaForUserCrop(String userId, String khasraNo) {

        UserLandDetail landDetail = agentService.getUserLandDetailByKhasraNo(khasraNo, userId);
        BigDecimal totalLandSize = landDetail.getLandSize();
//        FieldSize.FieldSizeType landSizeType= landDetail.getLandSizeType();

        BigDecimal count = BigDecimal.ZERO;

        List<UserCrop> userCrops = agentService.getUserCropListByKhasraNo(khasraNo, userId);
        for (UserCrop userCrop : userCrops) {
            count = count.add(userCrop.getLandSize());
        }
        return totalLandSize.subtract(count);
    }

    @Override
    public List<CropPercentage> getCropsPercentageCountry(String season) {

        List<CropPercentage> cropPercentageList = new ArrayList<>();
        List<CropArea> cropAreaList = getCropsAreaCountry(season);
        BigDecimal totalCropLandSize = getTotalCropSizeByCropAreaList(cropAreaList);

        for (CropArea cropArea : cropAreaList) {
            CropPercentage cropPercentage = new CropPercentage();
            BigDecimal percentage = BigDecimal.ZERO;
            BigDecimal cropLandSize = cropArea.getCropLandSize();
            if (totalCropLandSize.compareTo(BigDecimal.ZERO) > 0) {
                percentage = cropLandSize.divide(totalCropLandSize, 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            }
            cropPercentage.setCropName(cropArea.getCropName());
            cropPercentage.setCropType(cropArea.getCropType());
            cropPercentage.setCropPercentage(percentage);
            cropPercentageList.add(cropPercentage);
        }
        return cropPercentageList;
    }

    private BigDecimal getTotalCropSizeByCropAreaList(List<CropArea> cropAreaList) {
        BigDecimal totalCropLandSize = BigDecimal.ZERO;
        for (CropArea area : cropAreaList) {
            totalCropLandSize = totalCropLandSize.add(area.getCropLandSize());
        }
        return totalCropLandSize;
    }

    @Override
    public List<CropPercentage> getCropsPercentageByState(String stateId, String season) {
        List<CropPercentage> cropPercentageList = new ArrayList<>();
        List<CropArea> cropAreaList = getCropsAreaByState(stateId, season);
        BigDecimal totalCropLandSize = getTotalCropSizeByCropAreaList(cropAreaList);

        for (CropArea area : cropAreaList) {
            totalCropLandSize = totalCropLandSize.add(area.getCropLandSize());
        }

        for (CropArea cropArea : cropAreaList) {
            CropPercentage cropPercentage = new CropPercentage();
            BigDecimal percentage = BigDecimal.ZERO;
            BigDecimal cropLandSize = cropArea.getCropLandSize();

            if (totalCropLandSize.compareTo(BigDecimal.ZERO) > 0) {
                percentage = cropLandSize.divide(totalCropLandSize, 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            }
            cropPercentage.setCropName(cropArea.getCropName());
            cropPercentage.setCropType(cropArea.getCropType());
            cropPercentage.setCropPercentage(percentage);
            cropPercentageList.add(cropPercentage);
        }
        return cropPercentageList;
    }

    @Override
    public List<CropPercentage> getCropsPercentageByDistrict(String districtId, String season) {
        List<CropPercentage> cropPercentageList = new ArrayList<>();
        List<CropArea> cropAreaList = getCropsAreaByDistrict(districtId, season);
        BigDecimal totalCropLandSize = getTotalCropSizeByCropAreaList(cropAreaList);
        for (CropArea cropArea : cropAreaList) {
            CropPercentage cropPercentage = new CropPercentage();
            BigDecimal percentage = BigDecimal.ZERO;
            BigDecimal cropLandSize = cropArea.getCropLandSize();
            if (totalCropLandSize.compareTo(BigDecimal.ZERO) > 0) {
                percentage = cropLandSize.divide(totalCropLandSize, 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            }
            cropPercentage.setCropName(cropArea.getCropName());
            cropPercentage.setCropType(cropArea.getCropType());
            cropPercentage.setCropPercentage(percentage);
            cropPercentageList.add(cropPercentage);
        }
        return cropPercentageList;
    }


    private LandTypeSize getLandTypeSizeByCity(City city) {
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        LandTypeSize landTypeSize = new LandTypeSize();
        List<UserLandDetail> userLandDetails = getUserLandDetailsByDistrict(city.getId());
        calLandTypeSizeByCity(city, totalAreaCount, irrigatedLandSize, semiIrrigatedLandSize, rainfedLandSize, landTypeSize, userLandDetails);
        return landTypeSize;
    }

    public static void calLandTypeSizeByCity(City city, BigDecimal totalAreaCount, BigDecimal irrigatedLandSize, BigDecimal semiIrrigatedLandSize, BigDecimal rainfedLandSize, LandTypeSize landTypeSize, List<UserLandDetail> userLandDetails) {
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal convertedlandSize;
            if (detail.getFarmType().equals(LandType.Type.Irrigated)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                irrigatedLandSize = irrigatedLandSize.add(convertedlandSize);
            }

            if (detail.getFarmType().equals(LandType.Type.SemiIrrigated)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                semiIrrigatedLandSize = semiIrrigatedLandSize.add(convertedlandSize);
            }
            if (detail.getFarmType().equals(LandType.Type.Rainfed)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                rainfedLandSize = rainfedLandSize.add(convertedlandSize);
            }

            BigDecimal landSize;
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                landSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
            } else {
                landSize = detail.getLandSize();
            }
            totalAreaCount = totalAreaCount.add(landSize);
        }

        landTypeSize.setFieldSizeType(FieldSize.FieldSizeType.Acre);
        landTypeSize.setIrrigatedLandSize(irrigatedLandSize);
        landTypeSize.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
        landTypeSize.setRainfedLandSize(rainfedLandSize);
        landTypeSize.setTotalLandSize(totalAreaCount);
        landTypeSize.setDistrictName(city.getName());
        landTypeSize.setDistrictId(city.getId());
    }

    private LandTypeSize getLandTypeSizeByState(State state) {
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        LandTypeSize landTypeSize = new LandTypeSize();

        List<UserLandDetail> userLandDetails = getUserLandDetailsByState(state.getId());

        calLandSizeTypeByState(state, totalAreaCount, irrigatedLandSize, semiIrrigatedLandSize, rainfedLandSize, landTypeSize, userLandDetails);
        return landTypeSize;
    }

    static void calLandSizeTypeByState(State state, BigDecimal totalAreaCount, BigDecimal irrigatedLandSize, BigDecimal semiIrrigatedLandSize, BigDecimal rainfedLandSize, LandTypeSize landTypeSize, List<UserLandDetail> userLandDetails) {
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal convertedlandSize = detail.getLandSize();
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

        landTypeSize.setFieldSizeType(FieldSize.FieldSizeType.Acre);
        landTypeSize.setIrrigatedLandSize(irrigatedLandSize);
        landTypeSize.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
        landTypeSize.setRainfedLandSize(rainfedLandSize);
        landTypeSize.setTotalLandSize(totalAreaCount);
        landTypeSize.setStateName(state.getName());
        landTypeSize.setStateId(state.getId());
    }

    @Override
    public LandTypePercentage getLandTypesAreaPercentageCountry() {
        BigDecimal percentageIrrigated = BigDecimal.ZERO;
        BigDecimal percentageSemiIrrigated = BigDecimal.ZERO;
        BigDecimal percentageRainfed = BigDecimal.ZERO;

        LandTypePercentage landTypePercentage = new LandTypePercentage();

        LandTypeSize landTypeSize = countService.getLandTypesAreaCountCountry();

        if (landTypeSize.getTotalLandSize().compareTo(BigDecimal.ZERO) > 0) {
            percentageIrrigated = landTypeSize.getIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageSemiIrrigated = landTypeSize.getSemiIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageRainfed = landTypeSize.getRainfedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
        }
        landTypePercentage.setIrrigatedLandPercentage(percentageIrrigated);
        landTypePercentage.setRainfedLandPercentage(percentageRainfed);
        landTypePercentage.setSemiIrrigatedLandPercentage(percentageSemiIrrigated);

        return landTypePercentage;
    }

    @Override
    public LandTypePercentage getLandTypesAreaPercentageStates(String stateId) {
        State state = locationServices.getState(stateId);
        LandTypeSize landTypeSize = getLandTypeSizeByState(state);

        BigDecimal percentageIrrigated = BigDecimal.ZERO;
        BigDecimal percentageSemiIrrigated = BigDecimal.ZERO;
        BigDecimal percentageRainfed = BigDecimal.ZERO;

        LandTypePercentage landTypePercentage = new LandTypePercentage();

        if (landTypeSize.getTotalLandSize().compareTo(BigDecimal.ZERO) > 0) {
            percentageIrrigated = landTypeSize.getIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageSemiIrrigated = landTypeSize.getSemiIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageRainfed = landTypeSize.getRainfedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
        }
        landTypePercentage.setIrrigatedLandPercentage(percentageIrrigated);
        landTypePercentage.setRainfedLandPercentage(percentageRainfed);
        landTypePercentage.setSemiIrrigatedLandPercentage(percentageSemiIrrigated);
        landTypePercentage.setStateName(landTypeSize.getStateName());
        return landTypePercentage;
    }

    @Override
    public LandTypePercentage getLandTypesAreaPercentageDistricts(String districtId) {
        City city = locationServices.getCity(districtId);
        LandTypeSize landTypeSize = getLandTypeSizeByCity(city);

        BigDecimal percentageIrrigated = BigDecimal.ZERO;
        BigDecimal percentageSemiIrrigated = BigDecimal.ZERO;
        BigDecimal percentageRainfed = BigDecimal.ZERO;

        LandTypePercentage landTypePercentage = new LandTypePercentage();

        if (landTypeSize.getTotalLandSize().compareTo(BigDecimal.ZERO) > 0) {
            percentageIrrigated = landTypeSize.getIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageSemiIrrigated = landTypeSize.getSemiIrrigatedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
            percentageRainfed = landTypeSize.getRainfedLandSize().divide(landTypeSize.getTotalLandSize(), 3, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100));
        }
        landTypePercentage.setIrrigatedLandPercentage(percentageIrrigated);
        landTypePercentage.setRainfedLandPercentage(percentageRainfed);
        landTypePercentage.setSemiIrrigatedLandPercentage(percentageSemiIrrigated);
        landTypePercentage.setDistrictName(landTypeSize.getDistrictName());
        return landTypePercentage;
    }

    @Override
    public MilkingAnimalDetails addMilkingAnimalDetails(MilkingAnimalDetails milkingAnimalDetails) {
        if ((milkingAnimalDetails.getMilkingCondition() == 0) && (milkingAnimalDetails.getMilkProductionOutput().compareTo(BigDecimal.ZERO) > 0)) {
            throw new CustomException("Invalid data entered.");
        }

        User user = userRepo.findByid(milkingAnimalDetails.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        //count set for milking details
        CountAll countAll = countService.getCountAll();
        if (milkingAnimalDetails.getAnimalType().equals(MilkingAnimalDetails.MilkingAnimalType.Cow)) {
            countAll.setTotalCowss(countService.getTotalCowsCount());
        } else if (milkingAnimalDetails.getAnimalType().equals(MilkingAnimalDetails.MilkingAnimalType.Buffalo)) {
            countAll.setTotalBuffalos(countService.getTotalBuffaloCount());
        } else if (milkingAnimalDetails.getAnimalType().equals(MilkingAnimalDetails.MilkingAnimalType.Goat)) {
            countAll.setTotalGoats(countService.getTotalGoatCount());
        }
        countAll.setTotalDairyFarmers(countService.getDairyFarmerCount());
        countService.storeCountAll(countAll);

        milkingAnimalDetails.setAddress(user.getAddressModel());
        return milkingAnimalDetailsRepo.save(milkingAnimalDetails);
    }

    @Override
    public List<MilkingAnimalDetails> getMilkingAnimalDetails(String userId, String animalType) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("animalType").is(animalType.trim()));
        return mongoTemplate.find(query, MilkingAnimalDetails.class);
    }

    @Override
    public MilkingAnimalDetails getMilkingAnimalDetailById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, MilkingAnimalDetails.class);
    }

    @Override
    public List<User> getFarmersByDistrict(String districtId) {
        City city = locationServices.getCity(districtId);
        Query query = new Query();
        query.addCriteria(Criteria.where("addressModel.city.$id").is(new ObjectId(city.getId())).and("addressModel.state.$id").is(new ObjectId(city.getState().getId())).and("roles.roleName").is(ROLE_USER));
        return mongoTemplate.find(query, User.class);
    }

//    @Override
//    public List<User> getDairyFarmerListByDistrict(String districtId) {
//        City city = locationServices.getCity(districtId);
//
//        List<MilkingAnimalDetails.MilkingAnimalType> milkingAnimalTypes = Arrays.asList(MilkingAnimalDetails.MilkingAnimalType.values());
//        Query query = new Query(Criteria.where("animalType").in(milkingAnimalTypes));
//        List<User> users = mongoTemplate.findDistinct(query,"userId",FarmDetails.class,User.class);
//
//        Query q = new Query(Criteria.where("_id").in(users));
//        List<User> allUsers= mongoTemplate.find(q,User.class);
//
//        List<User> userList = new ArrayList<>();
//        for (User user : allUsers){
//            if ( user.getDistrict().equals(city.getName())){
//                userList.add(user);
//            }
//        }
//        return userList;
//    }

    @Override
    public List<User> getDairyFarmerListByLocation(String locationType, String locationId) {
        Query query = dairyFarmerQuery(locationType, locationId);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public long getDairyFarmerCountByLocation(String locationType, String locationId) {
        Query query = dairyFarmerQuery(locationType, locationId);
        return mongoTemplate.count(query, User.class);
    }

    private Query dairyFarmerQuery(String locationType, String locationId) {
        List<MilkingAnimalDetails.MilkingAnimalType> milkingAnimalTypes = Arrays.asList(MilkingAnimalDetails.MilkingAnimalType.values());

        Query query = new Query(Criteria.where("animalType").in(milkingAnimalTypes));
        List<User> users = mongoTemplate.findDistinct(query, "userId", MilkingAnimalDetails.class, User.class);

        switch (locationType) {
            case "STATE":
                query = new Query(Criteria.where("_id").in(users).and("addressModel.state.$id").is(new ObjectId(locationId)));
                break;
            case "DISTRICT":
                query = new Query(Criteria.where("_id").in(users).and("addressModel.city.$id").is(new ObjectId(locationId)));
                break;
            case "TEHSIL":
                query = new Query(Criteria.where("_id").in(users).and("addressModel.tehsil.$id").is(new ObjectId(locationId)));
                break;
            case "BLOCK":
                query = new Query(Criteria.where("_id").in(users).and("addressModel.block.$id").is(new ObjectId(locationId)));
                break;
            default:
                throw new CustomException("Location Type is not correct.");
        }

        return query;
    }

    @Override
    public void addCropSeasonInUserCrop() {
        List<UserCrop> userCropList = userCropRepo.findAll();

        int counter = 0;
        for (UserCrop userCrop : userCropList) {
            counter++;
            if (!StringUtils.isEmpty(userCrop.getYearOfSowing())) {
                int month = userCrop.getYearOfSowing().getMonth() + 1;
                if ((month >= 7) && (month <= 10)) {
//                List<Crop.CropSeason> seasons = userCrop.getCrop().getCropSeasons();
//                if(seasons.contains(Crop.CropSeason.Kharif)) {
                    Crop.CropSeason season1 = Crop.CropSeason.Kharif;
                    userCrop.setCropSeason(season1);
                    userCropRepo.save(userCrop);
//                }
                } else if ((month >= 11) || (month <= 3)) {
//                List<Crop.CropSeason> seasons = userCrop.getCrop().getCropSeasons();
//                if(seasons.contains(Crop.CropSeason.Rabi)) {
                    Crop.CropSeason season2 = Crop.CropSeason.Rabi;
                    userCrop.setCropSeason(season2);
                    userCropRepo.save(userCrop);
//                }
                } else if ((month >= 4) && (month <= 6)) {
                    Crop.CropSeason season3 = Crop.CropSeason.Zaid;
                    userCrop.setCropSeason(season3);
                    userCropRepo.save(userCrop);
                }
            } else {
                if (userCrop.getCrop().getCropSeasons() != null) {
                    Crop.CropSeason season = userCrop.getCrop().getCropSeasons().get(0);
                    userCrop.setCropSeason(season);
                    userCropRepo.save(userCrop);
                }
            }
        }
    }

    /*private LandTypeSize getLandTypesAreaCountCountry() {
        BigDecimal totalAreaCount = BigDecimal.ZERO;
        BigDecimal irrigatedLandSize = BigDecimal.ZERO;
        BigDecimal semiIrrigatedLandSize = BigDecimal.ZERO;
        BigDecimal rainfedLandSize = BigDecimal.ZERO;

        LandTypeSize landTypeSize = new LandTypeSize();

        List<UserLandDetail> userLandDetails = userLandDetailRepo.findAll();
        for (UserLandDetail detail : userLandDetails) {
            BigDecimal convertedlandSize;
            if (detail.getFarmType().equals(LandType.Type.Irrigated)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                irrigatedLandSize = irrigatedLandSize.add(convertedlandSize);
            }

            if (detail.getFarmType().equals(LandType.Type.SemiIrrigated)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                semiIrrigatedLandSize = semiIrrigatedLandSize.add(convertedlandSize);
            }
            if (detail.getFarmType().equals(LandType.Type.Rainfed)) {
                if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                    convertedlandSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
                } else {
                    convertedlandSize = detail.getLandSize();
                }
                rainfedLandSize = rainfedLandSize.add(convertedlandSize);
            }

            BigDecimal landSize;
            if (detail.getLandSizeType().equals(FieldSize.FieldSizeType.Hectare)) {
                landSize = detail.getLandSize().multiply(BigDecimal.valueOf(2.5));
            } else {
                landSize = detail.getLandSize();
            }
            totalAreaCount = totalAreaCount.add(landSize);
        }

        landTypeSize.setFieldSizeType(FieldSize.FieldSizeType.Acre);
        landTypeSize.setIrrigatedLandSize(irrigatedLandSize);
        landTypeSize.setSemiIrrigatedLandSize(semiIrrigatedLandSize);
        landTypeSize.setRainfedLandSize(rainfedLandSize);
        landTypeSize.setTotalLandSize(totalAreaCount);
        return landTypeSize;
    }*/

    @Override
    public String getCropImages(String cropId, String khasraNo, String userId) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        query = query.addCriteria(Criteria.where("userId").is(userId).and("khasraNo").is(khasraNo).and("cropId").is(cropId));
        CropImage cropImage = mongoTemplate.findOne(query, CropImage.class);
        String imageUrl = null;
        if (cropImage != null) {
            imageUrl = cropImage.getImageUrl();
        }
        return imageUrl;
    }

    @Override
    public String getCropImageByUserId(String userId) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        query = query.addCriteria(Criteria.where("userId").is(userId));
        CropImage cropImage = mongoTemplate.findOne(query, CropImage.class);
        String imageUrl = null;
        if (cropImage != null) {
            imageUrl = cropImage.getImageUrl();
        }
        return imageUrl;
    }

    @Override
    public void organiseUserAddress() {
        List<User> userList = userRepo.findAll();
        for (User user : userList) {
            if (user != null) {

                if (!StringUtils.isEmpty(user.getStateId()) || !StringUtils.isEmpty(user.getState())) {
                    State state;
                    if (!StringUtils.isEmpty(user.getStateId())) {
                        state = locationServices.getState(user.getStateId());
                    } else {
                        state = locationServices.findStateByName(user.getState());
                    }
                    user.getAddressModel().setState(state);
                }
                if (!StringUtils.isEmpty(user.getDistrictId()) || !StringUtils.isEmpty(user.getDistrict())) {
                    City city;
                    if (!StringUtils.isEmpty(user.getStateId())) {
                        city = locationServices.getCity(user.getDistrictId());
                    } else {
                        city = locationServices.getCityByName(user.getDistrict());
                    }
                    user.getAddressModel().setCity(city);
                }
                if (!StringUtils.isEmpty(user.getVillage())) {
                    user.getAddressModel().setVillage(user.getVillage());
                }
                if (!StringUtils.isEmpty(user.getAddress())) {
                    user.getAddressModel().setAddress(user.getAddress());
                }
                userRepo.save(user);
            }
        }
    }

//    @Override
//    public void organiseUserLandDetails() {
//        List<UserLandDetail> userLandDetails = userLandDetailRepo.findAll();
//        for (UserLandDetail detail : userLandDetails) {
//            if (detail != null) {
//                if (!StringUtils.isEmpty(detail.getStateId())) {
//                    System.out.println(">>>>>>>>>>>>inState>>>>>>>>>>>>>>>>>>");
//
//                    State state = locationServices.getState(detail.getStateId());
//                    detail.setState(state);
//                }
//                if (!StringUtils.isEmpty(detail.getCityId())) {
//                    System.out.println(">>>>>>>>>>>>inCity>>>>>>>>>>>>>>>>>>");
//
//                    City city = locationServices.getCity(detail.getCityId());
//                    detail.setCity(city);
//                }
//                if (!StringUtils.isEmpty(detail.getSoilId())) {
//                    System.out.println(">>>>>>>>>>>>inSoil>>>>>>>>>>>>>>>>>>");
//
//                    Soil soil = landService.getSoil(detail.getSoilId());
//                    detail.setSoil(soil);
//                }
//                userLandDetailRepo.save(detail);
//            }
//        }
//    }


    @Override
    public String uploadUserProfilePic(MultipartFile file, String userId) {
        User user = findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Invalid UserId!");
        }
        String profileUrl = this.awsS3.uploadFile(file);
        user.setProfileImage(profileUrl);
        userRepo.save(user);

        return user.getProfileImage();
    }

    @Override
    public BigDecimal getRemainingLandSize(String userId, String khasraNo) {

        UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(khasraNo, userId);
        BigDecimal landSize = BigDecimal.ZERO;
        BigDecimal remainingLandSize = BigDecimal.ZERO;
        if (userLandDetail == null) {
            throw new ResourceNotFoundException("There are no details added for this khasraNo and User");
        }
        landSize = userLandDetail.getLandSize();
        List<UserCrop> userCropList = agentService.getUserCropListByKhasraNo(khasraNo, userId);
        if (!userCropList.isEmpty()) {
            for (UserCrop userCrop : userCropList) {
                if (userCrop.isCurrentCrop()) {
                    landSize = landSize.subtract(userCrop.getLandSize());
                }
            }
        }
        return landSize;
    }

    @Override
    public MilkingAnimalDetails getAnimalDetail(String userId, String animalType, String fromDate, String toDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        query = query.addCriteria(Criteria.where("userId").is(userId).and("animalType").is(animalType).and("date").gte(from).lt(to));
//        query = query.addCriteria(Criteria.where("userId").is(userId).and("animalType").is(animalType));
        return mongoTemplate.findOne(query, MilkingAnimalDetails.class);
    }

    @Override
    public FarmDetails getFarmAnimalDetail(String userId, String animalType, String fromDate, String toDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        final Date to = dateFormat.parse(toDate);
        final Date from = dateFormat.parse(fromDate);

        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "creationDate"));
        query = query.addCriteria(Criteria.where("userId").is(userId).and("animalType").is(animalType).and("date").gte(from).lt(to));
        return mongoTemplate.findOne(query, FarmDetails.class);
    }

    @Override
    public User getAgentByUserId(String userId) {
        Query query = new Query(Criteria.where("users.$id").is(new ObjectId(userId)));
        UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);
        User agent = null;
        if (userAgent != null) {
            agent = findByid(userAgent.getAgentId());
        }
        return agent;
    }

    @Override
    public MessageResponse resetPassword(ResetPassReqBody resetPassReqBody) {

        User user = findByid(resetPassReqBody.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("Invalid User");
        }
        if (!resetPassReqBody.getNewPassword().equals(resetPassReqBody.getNewPasswordVerify())) {
            throw new CustomException("Passwords doesn't match!");
        }
        user.setPassword(bCryptPasswordEncoder.encode(resetPassReqBody.getNewPassword()));
        user.setRequirePassword(false);
        user.setPasswordResetTime(LocalDateTime.now());
        userRepo.save(user);
        MessageResponse response = new MessageResponse();
        response.setMessage("Password Reset Successfully");
        return response;
    }

    @Override
    public Page<User> getManagers(String managerType, String userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);

        User user = findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Invalid User");
        }

        Role adminRole = roleService.roleName(ROLE_ADMIN);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role disrtictManager = roleService.roleName(ROLE_DISTRICT_MANAGER);

        List<Role> userRoleList = user.getRoles();

        List<String> roleNames = new ArrayList<>();
        if (userRoleList.contains(adminRole)) {
            roleNames.add(ROLE_STATE_MANAGER);
            roleNames.add(ROLE_DISTRICT_MANAGER);
            roleNames.add(ROLE_AGENT_MANAGER);

            if (StringUtils.isEmpty(managerType)) {
                query.addCriteria(Criteria.where("roles.roleName").in(roleNames));
            } else {
                query.addCriteria(Criteria.where("roles.roleName").is(managerType.trim()));
            }
        } else if (userRoleList.contains(stateManager)) {
            System.out.println("=========stateManager=============");
            roleNames.add(ROLE_DISTRICT_MANAGER);
            roleNames.add(ROLE_AGENT_MANAGER);
            if (StringUtils.isEmpty(managerType)) {
                query.addCriteria(Criteria.where("roles.roleName").in(roleNames).and("createdByUserId").is(userId));
            } else {
                query.addCriteria(Criteria.where("roles.roleName").is(managerType.trim()).and("createdByUserId").is(userId));
            }
        } else if (userRoleList.contains(disrtictManager)) {
            System.out.println("=========disrtictManager=============");
            roleNames.add(ROLE_AGENT_MANAGER);
            query.addCriteria(Criteria.where("roles.roleName").in(roleNames).and("createdByUserId").is(userId));
        }

        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    @Override
    public FarmerExtraDetails storeFarmerExtraDetail(FarmerExtraDetails farmerExtraDetails) {
        return farmerExtraDetailsRepo.save(farmerExtraDetails);
    }

    @Override
    public FarmerExtraDetails getFarmerExtraDetail(String farmerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("farmerId").is(farmerId));
        return mongoTemplate.findOne(query, FarmerExtraDetails.class);
    }

    @Override
    public String setUserStatus(UserStatus userStatus, String loggedInUserName) {
        User userExist = findByid(userStatus.getUserId());
        if (userExist == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        EmployeeHistory employeeHistory = new EmployeeHistory();
        employeeHistory.setUser(userExist);
        // Activate
        if (userStatus.isStatus()) {
            userExist.setActive(true);
            userExist.setDeleted(false);
            employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
        } else {
            //Deactivate
            userExist.setActive(false);
            userExist.setDeleted(true);
            employeeHistory.setStatus(EmployeeHistory.Status.INACTIVE);
        }
        saveUser(userExist);

        // Storing History
        User userByPhoneNum = getUserByPhoneNum(loggedInUserName);
        if (userByPhoneNum == null) {
            throw new CustomException("Logged in User not Found");
        }
        employeeHistory.setCreatedByUserId(userByPhoneNum.getId());
        employeeHistoryService.storeEmployeeHistory(employeeHistory);

        return "Status Updated";
    }

    private User getActiveMasterUser(User user) {
        User masterUser = findByid(user.getCreatedByUserId());
        if (!masterUser.isActive()) {
            masterUser = getActiveMasterUser(masterUser);
        }
        return masterUser;
    }

    @Override
    public UserSchemes saveUserScheme(UserSchemes userSchemes) {
        User user = mongoTemplate.findById(userSchemes.getUserId(), User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userSchemes.getUserId()).and("governmentSchemes").is(userSchemes.getGovernmentSchemes()));
        UserSchemes userSchemesExist = mongoTemplate.findOne(query, UserSchemes.class);
        if (userSchemesExist == null) {
            userSchemes.setAddress(user.getAddressModel());
            userSchemesRepo.save(userSchemes);
        } else {
            userSchemesExist.setGovernmentSchemes(userSchemes.getGovernmentSchemes());
            userSchemesExist.setUserId(userSchemes.getUserId());
            userSchemesExist.setStatus(userSchemes.isStatus());
            userSchemesRepo.save(userSchemesExist);
        }
        return userSchemes;
    }

    @Override
    public List<UserSchemes> getUserSchemesList(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("status").ne(false));
        return mongoTemplate.find(query, UserSchemes.class);
    }

    @Override
    public MessageResponse assignAgentRoleToManagers() {
        List<User> managerList = getAllManagers();

        Role agentRole = roleService.roleName(ROLE_AGENT);

        for (User manager : managerList) {
            manager.addRole(agentRole);
            userRepo.save(manager);
        }

        MessageResponse response = new MessageResponse();
        response.setMessage("Roles Added");
        return response;
    }

    @Override
    public MessageResponse updateManagerPassword() {
        List<User> managerList = getAllManagers();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        for (User manager : managerList) {
            manager.setPassword(bCryptPasswordEncoder.encode(manager.getPrimaryPhone()));
            userRepo.save(manager);
        }

        MessageResponse response = new MessageResponse();
        response.setMessage("Password Updated");
        return response;
    }
    
    @Override
    public MessageResponse updateFarmerPassword(User user) {
    	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    	String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepo.save(user);
        MessageResponse response = new MessageResponse();
        response.setMessage("Password Updated");
        return response;
    }

    private List<User> getAllManagers() {
        List<String> roleList = new ArrayList<>();
        roleList.add(ROLE_STATE_MANAGER);
        roleList.add(ROLE_DISTRICT_MANAGER);
        roleList.add(ROLE_TEHSIL_MANAGER);
        roleList.add(ROLE_AGENT_MANAGER);

        Query query = new Query();
        query.addCriteria(Criteria.where("roles.roleName").in(roleList));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public UserCertificate saveUserCertificate(UserCertificate userCertificate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userCertificate.getUserId()).and("certificateNumber").is(userCertificate.getCertificateNumber()).and("certificateAgency").is(userCertificate.getCertificateAgency()).and("khasraNo").in(userCertificate.getKhasraNo()));
        UserCertificate userCertificateExist = mongoTemplate.findOne(query, UserCertificate.class);
        UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(userCertificate.getKhasraNo(), userCertificate.getUserId());
        if (userCertificateExist == null) {
            userLandDetail.setAlreadyReceivedOC(true);
            userLandDetailRepo.save(userLandDetail);
            userCertificateRepo.save(userCertificate);
        } else {
            userCertificateExist.setCertificateAgency(userCertificate.getCertificateAgency());
            userCertificateExist.setCertificateNumber(userCertificate.getCertificateNumber());
            userCertificateExist.setCertificateType(userCertificate.getCertificateType());
            userCertificateExist.setCertificateValidity(userCertificate.getCertificateValidity());
            userCertificateExist.setDuration(userCertificate.getDuration());
            userCertificateExist.setKhasraNo(userCertificate.getKhasraNo());
            userCertificateRepo.save(userCertificateExist);
        }
        return userCertificate;
    }

    @Override
    public UserCertificate getUserCertificate(String userId, String khasraNo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("khasraNo").is(khasraNo));
        return mongoTemplate.findOne(query, UserCertificate.class);
    }

    @Override
    public AssignLocation setManagersLocationStatus(String assignLocationId, boolean status) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(assignLocationId)));
        AssignLocation assignLocation = mongoTemplate.findOne(query, AssignLocation.class);

        if (assignLocation == null) {
            throw new ResourceNotFoundException("No Loction Found.");
        }
        assignLocation.setActive(status);
        return mongoTemplate.save(assignLocation);

    }

    @Override
    public String assignUserCodeToUsers() {
        List<User> userList = userRepo.findAll();
        for (User user : userList) {
            if (StringUtils.isEmpty(user.getUserCode())) {
                user.setUserCode(generateUniqueCodeForUser());
                userRepo.save(user);
            }
        }
        return "UserCode Updated";
    }

    @Override
    public List<User> getAdminUsers() {
        Query query = new Query(Criteria.where("roles.roleName").is(ROLE_ADMIN));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<FamilyMemberHealthRecord> getFamilyMemberHealthDetail(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, FamilyMemberHealthRecord.class);
    }

    @Override
    public FamilyMemberHealthRecord getHealthDetailById(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, FamilyMemberHealthRecord.class);
    }

    @Override
    public Page<User> getFarmersByBlock(String blockId, int page, int size) {
        Block block = locationServices.getBlock(blockId);
        Query query = new Query();
        query.addCriteria(Criteria.where("addressModel.block.$id").is(new ObjectId(block.getId())).and("addressModel.state.$id").is(new ObjectId(block.getState().getId())).and("addressModel.city.$id").is(new ObjectId(block.getCity().getId())).and("roles.roleName").is(ROLE_USER));
        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    @Override
    public Subsidy getUserSubsidyByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Subsidy.class);
    }
    
    @Override
    public void saveAppLoginOtp(AppLoginTable loginOtp) {
    	mongoTemplate.save(loginOtp);
    }
    
    @Override
    public AppLoginTable getAppLoginOtp(String otp) {
    	Query query = new Query(Criteria.where("otp.otp").is(otp));
    	return mongoTemplate.findOne(query, AppLoginTable.class);
    }
}
