package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceAlreadyExists;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.exceptionHandler.UnauthorizedException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.location.Block;
import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;
import com.bharuwa.haritkranti.models.location.Village;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.models.requestModels.AssignLocationRequestBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.responseModels.AssignLocation;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.models.responseModels.FormChecks;
import com.bharuwa.haritkranti.msg91.Msg91Services;
import com.bharuwa.haritkranti.repositories.*;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.opencsv.CSVWriter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.getEmployeeRelationshipByToUserType;

/**
 * @author harmanpreet
 */
@Service
public class AgentServiceImpl implements AgentService {

    private final MongoTemplate mongoTemplate;


    public AgentServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private UserAgentRepo userAgentRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLandDetailRepo userLandDetailRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserCropRepo userCropRepo;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private OrganicCertificateRepo organicCertificateRepo;

    @Autowired
    private UserLandMappingRepo userLandMappingRepo;

    @Autowired
    private UserCropHistoryRepo userCropHistoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private AgentVillageRepo agentVillageRepo;

    @Autowired
    private LocationServices locationServices;

    @Autowired
    private VillageAssetRepo villageAssetRepo;

    @Autowired
    private AgentCertificateCodeRepo agentCertificateCodeRepo;

    @Autowired
    AssignLocationRepo assignLocationRepo;

    @Autowired
    private VillageMappingRepo villageMappingRepo;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private CountService countService;

    @Autowired
    private AnalyticsService analyticsService;

    @Override
    public void saveUserForAgent(UserAgent userAgent) {
        userAgentRepo.save(userAgent);
    }

    @Override
    public UserLandDetail addUserLandDetail(UserLandDetail userLandDetail) {
        UserLandDetail  landDetail = checkUserLandDetailExist(userLandDetail.getUserId(),userLandDetail.getKhasraNo(),userLandDetail.getVillage());
        if(landDetail != null){
            throw new ResourceAlreadyExists("Land Detail already exist for same Farmer");
        }

        if (!StringUtils.isEmpty(userLandDetail.getOwnershipType().toString()) && userLandDetail.getOwnershipType().equals(UserLandDetail.OwnershipType.Self)) {
            User user = userService.findByid(userLandDetail.getUserId());
            if (user != null) {
                if (!StringUtils.isEmpty(user.getMiddleName()) && !StringUtils.isEmpty(user.getLastName())) {
                    userLandDetail.setOwnerName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                } else {
                    userLandDetail.setOwnerName(user.getFirstName() + " " + user.getLastName());
                }
            }
        }

        UserLandDetail storedUserLandDetail = userLandDetailRepo.save(userLandDetail);

        // update analytics
        analyticsService.storeUserLandAnalytics(storedUserLandDetail.getId());

        //count set for land details
        CountAll countAll = countService.getCountAll();
        countAll.setTotalKhasras(countService.getTotalKhasraCount());
        countService.storeCountAll(countAll);

        return storedUserLandDetail;
    }

    private UserLandDetail checkUserLandDetailExist(String userId, String khasraNo, String villageId) {
        Query query = new Query(Criteria.where("khasraNo").is(khasraNo).and("userId").is(userId).and("villageModel.$name").is(villageId));
        return mongoTemplate.findOne(query, UserLandDetail.class);
    }

    @Override
    public UserLandDetail getUserLandDetailByKhasraNo(String khasraNo, String userId) {
        if (khasraNo == null || userId == null) {
            return null;
        }
        Query query = new Query(Criteria.where("khasraNo").is(khasraNo).and("userId").is(userId));
        return mongoTemplate.findOne(query, UserLandDetail.class);
    }

    // TODO: Need to check
    // it can return list of UserCrops as khasraNo is not unique in system
    @Override
    public UserCrop getUserCropByKhasraNo(String khasraNo) {
        Query query = new Query(Criteria.where("khasraNo").is(khasraNo));
        return mongoTemplate.findOne(query, UserCrop.class);
    }

    @Override
    public User addUserForAgent(String agentId, UserReqBody userReqBody) {
        User userExist = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone());
        if (userExist != null && userReqBody.getCreatedVia().equals(User.CreatedVia.SIGNUP)) {
            throw new ResourceAlreadyExists("PhoneNumber already exists");
        }

        // create new user
        User user = new User();
        user.setFirstName(userReqBody.getFirstName());
        user.setMiddleName(userReqBody.getMiddleName());
        user.setLastName(userReqBody.getLastName());
        user.setFatherName(userReqBody.getFatherName());
        user.setPrimaryPhone(userReqBody.getPrimaryPhone());
        user.setGender(userReqBody.getGender());
        user.setQualification(userReqBody.getQualification());
        user.setReligion(userReqBody.getReligion());
        user.setEmail(userReqBody.getEmail());
        user.setDateOfBirth(userReqBody.getDateOfBirth());
        user.setAddressModel(userReqBody.getAddressModel());
        user.setProfileImage(userReqBody.getProfileImage());

        user.setCreationDate(Calendar.getInstance().getTime());
        user.setCreatedVia(User.CreatedVia.AGENT);
        user.setPrimaryPhone(user.getPrimaryPhone());

        // Encrypt Password
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));

        List<Role> roleList = new ArrayList<>();
        Role role = roleService.roleName(ROLE_USER);
        roleList.add(role);
        user.setRoles(roleList);

//        createdByUserId
        user.setCreatedByUserId(agentId);

        user.setActive(true);
        //set userCode for farmer
        user.setUserCode(userExtraService.generateUniqueCodeForFarmer(user));
        userService.saveUser(user);

        //count set for farmer
        CountAll countAll = countService.getCountAll();
        countAll.setTotalFarmers(countService.getTotalFarmerCount());
        countAll.setTotalUsers(countService.getTotalUserCount());
        countAll.setTotalFemaleFarmers(countService.getTotalFemaleFarmerCount());
        countAll.setTotalMaleFarmers(countService.getTotalMaleFarmerCount());
        countService.storeCountAll(countAll);

        // add Employee History
        EmployeeHistory employeeHistory = new EmployeeHistory();
        employeeHistory.setUser(user);
        employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
        employeeHistory.setCreatedByUserId(user.getCreatedByUserId());
        employeeHistoryService.storeEmployeeHistory(employeeHistory);

        // add EmployeeAssignmentHistory
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);

        User fromUser = userService.findByid(user.getCreatedByUserId());

        EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
        employeeAssignmentHistory.setToUser(user);
        employeeAssignmentHistory.setFromUser(fromUser);
        employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(ROLE_USER, fromUser,nationalManager,stateManager,districtManager,blockManager));
        employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);

        // Identification Details
        UserIdentificationDetails userIdentificationDetails = new UserIdentificationDetails();
        userIdentificationDetails.setUserId(user.getId());
        userIdentificationDetails.setAadhaarCardNumber(userReqBody.getAadhaarCardNumber());
        userService.addUserIdentificationDetails(userIdentificationDetails);

        //set agent having farmers true
        User agent = userService.findByid(agentId);
        agent.setHasFarmers(true);
        userService.saveUser(agent);

        Query query = new Query(Criteria.where("agentId").is(agentId.trim()));
        UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);

        // TODO: Need to check this condition
        // Create UserAgent to add Users for Agent
        if (userAgent == null) {
            userAgent = new UserAgent();
            userAgent.setAgentId(agentId);
            userAgent.setCreationDate(Calendar.getInstance().getTime());
            userAgent.addUser(user);
            saveUserForAgent(userAgent);
        } else {
            userAgent.addUser(user);
            saveUserForAgent(userAgent);
        }

        // Send Farmer App Link
        sendFarmerAppLink(user);
        return user;
    }

    public static void sendFarmerAppLink(User user) {
        String appLinkMessage = "प�?रिय किसान, आपका अन�?नदाता �?प में स�?वागत है। \n" +
                "\n" +
                "कृपया नीचे दि�? ग�? लिंक पे क�?लिक करके अपने मोबाइल पर �?प इनस�?टॉल करें \n" + FARMER_APP_LINK;
        String response = null;
        try {
            response = Msg91Services.sendFarmerAppLinkMsg(appLinkMessage, user.getPrimaryPhone());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(response);
        }
    }


    @Override
    public List<UserLandDetail> getUserLandDetails(String userId) {

        User userExist = userService.findByid(userId);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, UserLandDetail.class);
    }

    @Override
    public List<UserCrop> getUserCrops(String userId, String cropGrouptype) {

        User userExist = userService.findByid(userId);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId));
        if (!StringUtils.isEmpty(cropGrouptype)) {
            query = query.addCriteria(Criteria.where("userId").is(userId).and("type").is(cropGrouptype));
        }
        return mongoTemplate.find(query, UserCrop.class);
    }

    @Override
    public List<User> getAgentUsersList(String agentId) {
        User userExist = userService.findByid(agentId);
        if (userExist == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("agentId").is(agentId.trim()));
        UserAgent agent = mongoTemplate.findOne(query, UserAgent.class);
        List<User> users = new ArrayList<>();
        if (agent != null && !agent.getUsers().isEmpty()) {
            users.addAll(agent.getUsers());
        }
        return users;
    }

    @Override
    public FormChecks userFormChecks(String userId) {

        FormChecks checks = new FormChecks();
        if (getUserLandDetails(userId) != null) {
            checks.setLandDetails(true);
        }

        if (!getUserCrops(userId, null).isEmpty()) {
            checks.setLandDetails(true);
        }

        if (equipmentService.getMoveableEquipments() != null) {
            checks.setLandDetails(true);
        }

        if ((userId) != null) {
            checks.setLandDetails(true);
        }

        if (getUserLandDetails(userId) != null) {
            checks.setLandDetails(true);
        }

        if (getUserLandDetails(userId) != null) {
            checks.setLandDetails(true);
        }

        if (getUserLandDetails(userId) != null) {
            checks.setLandDetails(true);
        }

        return null;
    }

    //Shows agents list present under a particular Login user(userId)
    @Override
    public Page<User> getAllAgents(String userId, int page, int size) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Role adminRole = roleService.roleName(ROLE_ADMIN);
        List<Role> userRoleList = user.getRoles();

        Query query = new Query();
        if (userRoleList.contains(adminRole)) {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT).and("hasFarmers").is(true));
        } else {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT).and("createdByUserId").is(userId).and("hasFarmers").is(true));
        }

        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    //Give organic certificate to the eligible land
    @Override
    public MessageResponse storeOrganicCertificate(OrganicCertificate organicCertificate) {
        // TODO: Check Eligibility of the Khasra(Land)

        UserLandDetail userLandDetailByKhasraNo = getUserLandDetailByKhasraNo(organicCertificate.getKhasraNo(), organicCertificate.getUserId());

        if (userLandDetailByKhasraNo == null) {
            throw new ResourceNotFoundException("Khasra details not found");
        }
        userLandDetailByKhasraNo.setOrganicLandEligible(true);

        organicCertificate.setOrganicLandEligible(true);

        // check factors
        if (organicCertificate.isFloodAlternativeYearOrFloodedArea()
                || (organicCertificate.isHeavyMetalIndustry() && organicCertificate.isHeavyMetalWithIn10Km())
                || (organicCertificate.isSewagePlantOrSewageWaterAffectedField() && organicCertificate.isSewageWithIn5km())) {
            organicCertificate.setOrganicLandEligible(false);
            userLandDetailByKhasraNo.setOrganicLandEligible(false);
        }

        // Store organicLandEligible in UserLandDetail
        userLandDetailRepo.save(userLandDetailByKhasraNo);
        organicCertificateRepo.save(organicCertificate);

        MessageResponse response = new MessageResponse();
//      check if eligible or not & return response
        if (organicCertificate.isOrganicLandEligible()) {
            response.setMessage("You are eligible to obtain Organic Certificate");
        } else {
            response.setMessage("You are not eligible to obtain Organic Certificate");
        }
        return response;
    }

    //Get organic certificate for a land by giving userId and khasraNo as an input
    @Override
    public OrganicCertificate getOrganicCertificate(String userId, String khasraNo) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("khasraNo").is(khasraNo));
        return mongoTemplate.findOne(query, OrganicCertificate.class);
    }
    
    @Override
    public List<OrganicCertificate> getOrganicCertificateList(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, OrganicCertificate.class);
    }


    @Override
    public List<User> getAgentUsers(String agentId) {
        User userExist = userService.findByid(agentId);
        if (userExist == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("agentId").is(agentId.trim()));
        return mongoTemplate.findDistinct(query, "users", UserAgent.class, User.class);
    }

    @Override
    public UserCropHistory addUserCropHistory(UserCropHistory userCropHistory) {
        return userCropHistoryRepo.save(userCropHistory);
    }

    @Override
    public List<UserCropHistory> getUserCropHistory(String userId) {
        User userExist = userService.findByid(userId);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not exists");
        }
        Query query = new Query();
        query = query.addCriteria(Criteria.where("userId").is(userId.trim()));
        return mongoTemplate.find(query, UserCropHistory.class);
    }

    @Override
    public UserCropHistory getUserCropHistoryById(String id) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, UserCropHistory.class);
    }

    @Override
    public List<User> searchAgents(String text) {
        Query query = new Query();
        if (text.matches("[0-9]+") && text.length() >= 2) {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT).and("primaryPhone").regex(text, "i"));
        } else {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_AGENT).and("firstName").regex(text, "i"));
        }
        return mongoTemplate.find(query, User.class);
    }


    // search users
    @Override
    public Page<User> searchUsers(String userId, String searchText, String userType, String managerType, String locationId, String locationType, Boolean active, Boolean searchAllUser, int page, int size) {

        //Checking that text provided for search should not be empty and length is not less than three character
        if (!StringUtils.isEmpty(searchText) && searchText.length() < 3) {
            throw new CustomException("Search input should have atleast three characters.");
        }

        // Getting all user/agent/manager that are created by the Login user
        User user = userService.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exists");
        }
        List<String> roleList = getRoleListBySearchUserTypeAndLoginUserRole(userType, managerType, user);

        return getSearchedUsers(searchText, roleList, user, locationId, locationType, active, false, page, size);
    }

    // get RoleList according to searched User-Type and loggedIn User Role
    private List<String> getRoleListBySearchUserTypeAndLoginUserRole(String userType, String managerType, User user) {
        //        get Roles of LoggedIn User
        List<Role> loggedInUserRoles = user.getRoles();

        Role admin = roleService.roleName(ROLE_ADMIN);
        Role adminView = roleService.roleName(ROLE_ADMIN_VIEW);
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);

        List<String> roleList = new ArrayList<>();

        // Filter according to the role
        switch (userType) {
            case "AGENT":
                roleList.add(ROLE_AGENT);
                break;
            // TODO: need to remove this USER case
            case "USER":
                roleList.add(ROLE_USER);
                break;
            case "FARMER":
                roleList.add(ROLE_USER);
                break;
            case "MANAGER":
                // search according to different manager roles
                if (loggedInUserRoles.contains(admin) || loggedInUserRoles.contains(adminView)) {
                    if (!StringUtils.isEmpty(managerType)) {
                        roleList.add(managerType);
                    } else {
                        roleList.add(ROLE_NATIONAL_MANAGER);
                        roleList.add(ROLE_STATE_MANAGER);
                        roleList.add(ROLE_DISTRICT_MANAGER);
                        roleList.add(ROLE_AGENT_MANAGER);
                    }
                } else if (loggedInUserRoles.contains(nationalManager)) {
                    if (!StringUtils.isEmpty(managerType)) {
                        roleList.add(managerType);
                    } else {
                        roleList.add(ROLE_STATE_MANAGER);
                        roleList.add(ROLE_DISTRICT_MANAGER);
                        roleList.add(ROLE_AGENT_MANAGER);
                    }
                } else if (loggedInUserRoles.contains(stateManager)) {
                    if (!StringUtils.isEmpty(managerType)) {
                        roleList.add(managerType);
                    } else {
                        roleList.add(ROLE_DISTRICT_MANAGER);
                        roleList.add(ROLE_AGENT_MANAGER);
                    }
                } else if (loggedInUserRoles.contains(districtManager)) {
                    if (!StringUtils.isEmpty(managerType)) {
                        roleList.add(managerType);
                    } else {
                        roleList.add(ROLE_AGENT_MANAGER);
                    }
                }
                break;
            case "ACCOUNTANT_MANAGER":
                roleList.add(ROLE_ACCOUNTANT);
                break;
            default:
                throw new UnauthorizedException("Invalid Role");
        }
        return roleList;
    }

    // get all users of an authorized user
    private Page<User> getSearchedUsers(String searchText, List<String> roleList, User user, String locationId, String locationType, Boolean active, Boolean searchAllUser, int page, int size) {

        // check if locationId and locationType are empty or not
        if (StringUtils.isEmpty(locationId) && StringUtils.isEmpty(locationType)) {
            return getUsersByUserType(searchText, roleList, user, null, locationId, active, searchAllUser, page, size);
        }

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
        return getUsersByUserType(searchText, roleList, user, searchLocationKey, locationId, active, searchAllUser, page, size);
    }

    /**
     * Searching user with defined role type , locationId  and locationType
     */
    private Page<User> getUsersByUserType(String text, List<String> roleList, User user, String searchLocationKey, String locationId, Boolean active, Boolean searchAllUser, int page, int size) {

        Role adminRole = roleService.roleName(ROLE_ADMIN);
        Role adminView = roleService.roleName(ROLE_ADMIN_VIEW);

        List<Role> adminRoleList = new ArrayList<>();
        adminRoleList.add(adminRole);
        adminRoleList.add(adminView);

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);
        Criteria criteria = Criteria.where("roles.roleName").in(roleList);

//        TODO: need to remove searchALLUser
        if (Collections.disjoint(user.getRoles(),adminRoleList)) {
            System.out.println("======not admin==========");
            if (searchAllUser != null && !searchAllUser) {
                criteria = criteria.and("createdByUserId").is(user.getId());
            }
        }

//      Check for search according to Location
        if (StringUtils.isEmpty(text) && !StringUtils.isEmpty(searchLocationKey) && !StringUtils.isEmpty(locationId)) {
            // search only location
            criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
        } else if (!StringUtils.isEmpty(text) && text.matches("[0-9]+")) {
            // Check for searchText: Number with Location
            if (!StringUtils.isEmpty(searchLocationKey) && !StringUtils.isEmpty(locationId)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId)).and("primaryPhone").regex(text, "i");
            } else {
                criteria = criteria.and("primaryPhone").regex(text, "i");
            }
        } else if (!StringUtils.isEmpty(text)) {
            System.out.println("===========only name=========");
            // Check for searchText: Name with Location
            // Text Search
            criteria = searchFullName(text, criteria);
            // If Location is also selected
            if (!StringUtils.isEmpty(text) && !StringUtils.isEmpty(searchLocationKey) && !StringUtils.isEmpty(locationId)) {
                criteria = criteria.and(searchLocationKey).is(new ObjectId(locationId));
            }
        }

        if(active != null) {
            if (active) {
                criteria.and("isActive").ne(false).exists(true);
            } else {
                criteria.and("isActive").ne(true).exists(true);
            }
        }
        query.addCriteria(criteria);
        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    /**
     * Create Criteria to search text in all fields for Name
     *
     * @param text
     * @param criteria
     * @return
     */
    private Criteria searchFullName(String text, Criteria criteria) {
        String[] name = text.split("\\s");
        switch (name.length) {
            case 1: {
                // Single Field
                criteria = criteria.orOperator(
                        Criteria.where("firstName").regex(name[0], "i"),
                        Criteria.where("middleName").regex(name[0], "i"),
                        Criteria.where("lastName").regex(name[0], "i"));
                break;
            }
            case 2: {
                // Combination of two Fields
                criteria = criteria.orOperator(
                        Criteria.where("firstName").regex(name[0], "i").and("middleName").regex(name[1], "i"),
                        Criteria.where("firstName").regex(name[0], "i").and("lastName").regex(name[1], "i"),
                        Criteria.where("middleName").regex(name[0], "i").and("lastName").regex(name[1], "i"));
                break;
            }
            case 3: {
                // Combination of three Fields
                criteria = criteria.and("firstName").regex(name[0], "i")
                        .and("middleName").regex(name[1], "i")
                        .and("lastName").regex(name[2], "i");
                break;
            }
            default: {
                criteria = criteria.and("firstName").regex(text, "i");
                break;
            }
        }
        return criteria;
    }

    @Override
    public List<User> searchFarmers(String text) {
        Query query = new Query();
        if (text.matches("[0-9]+") && text.length() >= 2) {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_USER).and("primaryPhone").regex(text, "i"));
        } else {
            query.addCriteria(Criteria.where("roles.roleName").is(ROLE_USER).and("firstName").regex(text, "i"));
        }
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<UserCrop> getUserCropListByKhasraNo(String khasraNo, String userId) {
        Query query = new Query(Criteria.where("khasraNo").is(khasraNo).and("userId").is(userId));
        return mongoTemplate.find(query, UserCrop.class);
    }

    @Override
    public UserCrop getUserCropById(String id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, UserCrop.class);
    }

    @Override
    public UserLandMapping storeUserLandMapping(UserLandMapping userLandMapping) {

        UserLandDetail userLandDetail = getUserLandDetailByKhasraNo(userLandMapping.getKhasraNo(), userLandMapping.getUserId());
        if (userLandDetail == null) {
            throw new ResourceNotFoundException("User Land Details not found");
        }

        if (!StringUtils.isEmpty(userLandMapping.getUserId()) && !StringUtils.isEmpty(userLandMapping.getKhasraNo())) {
            String khasraNo = userLandMapping.getKhasraNo();
            String userId = userLandMapping.getUserId();
            ReportHistory history = reportHistoryService.getReportHistoryByKhasraAndUserId(khasraNo, userId);
            if (history != null) {
                userLandMapping.setSoilTest(history.getNpkRecommendation().getSoilTest());
            }
        }
        UserLandMapping landMapping = userLandMappingRepo.save(userLandMapping);

        // store UserLandMapping Id in UserLandDetails
        userLandDetail.setLandMapId(landMapping.getId());
        mongoTemplate.save(userLandDetail);

        return landMapping;

    }

    @Override
    public UserLandMapping getUserLandMapping(String userId, String khasraNo) {
        Query query = new Query(Criteria.where("khasraNo").is(khasraNo).and("userId").is(userId));
        return mongoTemplate.findOne(query, UserLandMapping.class);
    }

    @Override
    public List<UserLandMapping> getUserLandMappingList(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, UserLandMapping.class);
    }

    @Override
    public UserLandMapping getUserLandMappingById(String landMappingId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(landMappingId)));
        return mongoTemplate.findOne(query, UserLandMapping.class);
    }

    @Override
    public OrganicCertificate getOrganicCertificateById(String certificateId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(certificateId)));
        return mongoTemplate.findOne(query, OrganicCertificate.class);
    }

    @Override
    public void sendAppLink() {
        List<User> users = userRepo.findAll();

//        for (User user : users) {
//            Role role = roleService.roleName("USER");
//            if (user.getRoles().contains(role)) {
//                // send message for appp link
//                sendFarmerAppLink(user);
//            }
//        }

        final String PATH = "./farmer.csv";

        File file = new File(PATH);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"Phone Number"};
            writer.writeNext(header);

            for (User user : users) {
                Role role = roleService.roleName(ROLE_USER);
                if (user.getRoles().contains(role)) {
                    // add data to csv
                    if (!StringUtils.isEmpty(user.getPrimaryPhone())) {
                        String[] data = {user.getPrimaryPhone()};
                        writer.writeNext(data);
                    }
                }
            }

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public MessageResponse assignVillageToAgent(String agentId, String villageId) {
        MessageResponse response = new MessageResponse();
        User agent = userService.findByid(agentId);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        Village village = locationServices.getVillage(villageId);
        if (village == null) {
            throw new ResourceNotFoundException("Village does not exists");
        }

        Query query = new Query();
        query = query.addCriteria(Criteria.where("agent").is(agent).and("village").is(village));
        AgentVillage agentVillageExist = mongoTemplate.findOne(query, AgentVillage.class);
        if (agentVillageExist != null) {
            response.setMessage("Already assigned this Village");
        } else {
            AgentVillage agentVillage = new AgentVillage();
            agentVillage.setAgent(agent);
            agentVillage.setVillage(village);
            agentVillageRepo.save(agentVillage);
            response.setMessage("Village Assigned Successfully");
        }
        return response;
    }

    @Override
    public AgentVillage getAgentVillage(String id) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, AgentVillage.class);

    }

    @Override
    public List<Village> getAgentVillageList(String agentId) {
        List<Village> villageList = new ArrayList<>();
        Query query = new Query();
        query = query.addCriteria(Criteria.where("agentId").is(agentId));
        List<AgentVillage> agentVillageList = mongoTemplate.find(query, AgentVillage.class);
        for (AgentVillage agentVillage : agentVillageList) {
            if (agentVillage.getVillage() != null) {
                villageList.add(agentVillage.getVillage());
            }
        }
        return villageList;
    }


    @Override
    public VillageAsset storeVillageAsset(VillageAsset villageAsset) {
        return villageAssetRepo.save(villageAsset);
    }


    //TODO:Retrieve users whose khasra land is/are certified for organic farming
    @Override
    public List<UserLandDetail> getOrganicCertifiedKhasra(String userId) {
        User user = userRepo.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("organicLandEligible").ne(false));
        return mongoTemplate.find(query, UserLandDetail.class);

    }

    @Override
    public List<User> getOrganicLandUserList(String agentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("agentId").is(agentId).and("organicLandEligible").ne(false));
        List<OrganicCertificate> organicCertificateList = mongoTemplate.find(query, OrganicCertificate.class);
        Set<String> userIds = new HashSet<>();
        for (OrganicCertificate certificate : organicCertificateList) {
            userIds.add(certificate.getUserId());
        }

        List<User> userSet = new ArrayList<>();
        for (String id : userIds) {
            User user = userService.findByid(id);
            userSet.add(user);
        }
        return userSet;
    }

    @Override
    public VillageAsset getVillageAssetbyId(String agentId, String villageId) {
        User agent = userRepo.findByid(agentId);
        if (agent == null) {
            throw new ResourceNotFoundException("agent not found");
        }

        Query query = new Query();
        query = query.addCriteria(Criteria.where("agentId").is(agentId).and("villageId").is(villageId));
        return mongoTemplate.findOne(query, VillageAsset.class);
    }

    @Override
    public String generateCertificateCode(String agentId) {
        User agent = userService.findByid(agentId);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        String stateCode = agent.getAddressModel().getState().getStateAbbreviation();
        String agentCode = agent.getUserCode();

        String serialCode = generateSerialCodeAgent(agentCode);

        return "POF/" + stateCode + "/" + agentCode + "/" + serialCode;
    }

    private String generateSerialCodeAgent(String agentCode) {
        if (StringUtils.isEmpty(agentCode)) {
            throw new ResourceNotFoundException("Agent Code not found");
        }
        String serialCode;
        Query query = new Query();
        query.addCriteria(Criteria.where("agentCode").is(agentCode));
        AgentCertificateCode agentCertificateCode = mongoTemplate.findOne(query, AgentCertificateCode.class);
        if (agentCertificateCode != null) {
            String value = agentCertificateCode.getSerialNo();
            serialCode = generateUniqueCodeForCertificate(Integer.parseInt(value));
            agentCertificateCode.setSerialNo(serialCode);
            agentCertificateCodeRepo.save(agentCertificateCode);
        } else {
            AgentCertificateCode agentCertificateCode1 = new AgentCertificateCode();
            agentCertificateCode1.setAgentCode(agentCode);
            serialCode = generateUniqueCodeForCertificate(0);
            agentCertificateCode1.setSerialNo(serialCode);
            agentCertificateCodeRepo.save(agentCertificateCode1);
        }
        return serialCode;
    }

    private static String generateUniqueCodeForCertificate(int value) {
        int newValue = ++value;
        return String.format("%03d", newValue);
    }

    @Override
    public AssignLocation assignLocToManager(AssignLocationRequestBody assignLocationRequestBody) {

        User user = userService.findByid(assignLocationRequestBody.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }

        // TODO: check existing bY compound Index

        AssignLocation assignLocation = new AssignLocation();

        if (StringUtils.isEmpty(assignLocationRequestBody.getRoleName())) {
            throw new CustomException("Role Name is not defined");
        }

        switch (assignLocationRequestBody.getRoleName()) {
            case ROLE_STATE_MANAGER:
                if (!StringUtils.isEmpty(assignLocationRequestBody.getStateId())) {
                    State state = locationServices.getState(assignLocationRequestBody.getStateId());
                    if (state == null) {
                        throw new ResourceNotFoundException("State does not exists");
                    }

                    assignLocation.setUser(user);
                    assignLocation.setRoleName(ROLE_STATE_MANAGER);
                    assignLocation.setState(state);
                }
                break;

            case ROLE_DISTRICT_MANAGER:
                if (!StringUtils.isEmpty(assignLocationRequestBody.getDistrictId())) {
                    City city = locationServices.getCity(assignLocationRequestBody.getDistrictId());
                    if (city == null) {
                        throw new ResourceNotFoundException("District does not exists");
                    }

                    assignLocation.setUser(user);
                    assignLocation.setRoleName(ROLE_DISTRICT_MANAGER);
                    assignLocation.setDistrict(city);
                }
                break;

            case ROLE_AGENT_MANAGER:
                if (!StringUtils.isEmpty(assignLocationRequestBody.getBlockId())) {

                    Block block = locationServices.getBlock(assignLocationRequestBody.getBlockId());
                    if (block == null) {
                        throw new ResourceNotFoundException("Block does not exists");
                    }

                    assignLocation.setUser(user);
                    assignLocation.setRoleName(ROLE_AGENT_MANAGER);
                    assignLocation.setBlock(block);
                }
                break;
            default:

                throw new CustomException("Incorrect roleName Inserted");

        }
        return assignLocationRepo.save(assignLocation);
    }

    @Override
    public List<AssignLocation> getAssignLocsOfManager(String userId) {
        User user = userService.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, AssignLocation.class);
    }

    @Override
    public VillageMapping storeVillageMapping(VillageMapping villageMapping) {

        VillageMapping value = getVillageMapping(villageMapping.getAgentId(), villageMapping.getVillageId());

        if (value == null) {
            villageMappingRepo.save(villageMapping);
        } else {
            value.setVillageMapImageUrl(villageMapping.getVillageMapImageUrl());
            value.setLocationPins(villageMapping.getLocationPins());
            villageMappingRepo.save(value);
        }
        return villageMapping;
    }

    @Override
    public VillageMapping getVillageMapping(String agentId, String villageId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("agentId").is(agentId).and("villageId").is(villageId));
        return mongoTemplate.findOne(query, VillageMapping.class);
    }

    @Override
    public Page<User> searchParentManager(String userId, String searchText, int page, int size) {
        //Checking that text provided for search should not be empty and length is not less than three character
        if (!StringUtils.isEmpty(searchText) && searchText.length() < 3) {
            throw new CustomException("Search input should have atleast three characters.");
        }

        // Getting all user/agent/manager that are created by the Login user
        User user = userService.findByid(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exists");
        }

//        get Roles of LoggedIn User
        List<Role> loggedInUserRoles = user.getRoles();

        Role admin = roleService.roleName(ROLE_ADMIN);
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role agentManager = roleService.roleName(ROLE_AGENT_MANAGER);
        Role agent = roleService.roleName(ROLE_AGENT);

        List<String> roleList = new ArrayList<>();

        // Filter according to the role
        if (loggedInUserRoles.contains(agent) && !loggedInUserRoles.contains(nationalManager) && !loggedInUserRoles.contains(stateManager)
                && !loggedInUserRoles.contains(districtManager) && !loggedInUserRoles.contains(agentManager)) {
            roleList.add(ROLE_NATIONAL_MANAGER);
            roleList.add(ROLE_STATE_MANAGER);
            roleList.add(ROLE_DISTRICT_MANAGER);
            roleList.add(ROLE_AGENT_MANAGER);
        } else if (loggedInUserRoles.contains(agentManager)) {
            roleList.add(ROLE_NATIONAL_MANAGER);
            roleList.add(ROLE_DISTRICT_MANAGER);
            roleList.add(ROLE_STATE_MANAGER);
        } else if (loggedInUserRoles.contains(districtManager)) {
            roleList.add(ROLE_NATIONAL_MANAGER);
            roleList.add(ROLE_STATE_MANAGER);
        } else if (loggedInUserRoles.contains(stateManager)) {
            roleList.add(ROLE_NATIONAL_MANAGER);

        }

        return getUsersByUserType(searchText, roleList, user, null, null, null, true, page, size);
    }

    @Override
    public List<User> getFarmerListOfVillage(String agentId, String villageId) {

        List<User> farmers = new ArrayList<>();
        List<User> farmerList = getAgentUsersList(agentId);
        for (User farmer : farmerList) {
            if (farmer.getAddressModel().getVillageModel().getId().equals(villageId)) {
                farmers.add(farmer);
            }
        }
        return farmers;
    }

    @Override
    public Page<User> getAgentUsersByPagination(String agentId, int page, int size) {
        User userExist = userService.findByid(agentId);
        if (userExist == null) {
            throw new ResourceNotFoundException("Agent does not exists");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("createdByUserId").is(agentId.trim()));
        return genericMongoTemplate.paginationWithQuery(page, size, query, User.class);
    }

    @Override
    public User enableAgentRole(String userId, boolean status) {
        User user = genericMongoTemplate.findById(userId, User.class);
        Role agentRole = roleService.roleName(ROLE_AGENT);
        List<Role> userRoles = user.getRoles();
        if (!status && userRoles.contains(agentRole)) {
            user.removeRole(agentRole);
            mongoTemplate.save(user);
        } else if (!userRoles.contains(agentRole)) {
            user.addRole(agentRole);
            mongoTemplate.save(user);
        }
        return user;
    }





    @Override
    public String changeUserAssignment(String fromUserId, String toUserId) {

        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);

        // get user from whom all users under him/her will be assigned to new master user
        User fromUser = userService.findByid(fromUserId);

        // get new master user to whom users to be assigned
        User masterUser = userService.findByid(toUserId);

        List<User> userList = getUsersUnderUserByStatus(fromUser,"","",true);
        if(!userList.isEmpty()){
            for (User user : userList){
                user.setCreatedByUserId(toUserId);
                userService.saveUser(user);

                EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
                employeeAssignmentHistory.setToUser(user);
                employeeAssignmentHistory.setFromUser(masterUser);
                // get relationship between masterUser and user
                String userPrimaryRoleName = null;
                if(user.getRoles().size() == 1){
                    userPrimaryRoleName = user.getRoles().get(0).getRoleName();
                } else {
                    if(user.getRoles().contains(nationalManager)){
                        userPrimaryRoleName = ROLE_NATIONAL_MANAGER;
                    } else if (user.getRoles().contains(stateManager)){
                        userPrimaryRoleName = ROLE_STATE_MANAGER;
                    } else if (user.getRoles().contains(districtManager)){
                        userPrimaryRoleName = ROLE_DISTRICT_MANAGER;
                    } else if (user.getRoles().contains(blockManager)){
                        userPrimaryRoleName = ROLE_AGENT_MANAGER;
                    }
                }
                employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(userPrimaryRoleName, masterUser,nationalManager,stateManager,districtManager,blockManager));
                employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);
            }
        }

        return "Success";
    }

    @Override
    public List<User> getUsersUnderDeactivatedUsersByMasterUserId(String userId,String userType, String managerType) {

        User masterUser = userService.findByid(userId);
        if(masterUser == null){
            throw new ResourceNotFoundException("User not found!");
        }

        // get deactivated users of master user
        List<User> deactivatedUserList = getUsersUnderUserByStatus(masterUser, "", "",false);

        List<User> userList = new ArrayList<>();
        //  add users which are under deactivated users of master user to list
        if(!deactivatedUserList.isEmpty()){
            for (User deactivatedUser : deactivatedUserList) {
                List<User> usersUnderDeactivatedUser = getUsersUnderUserByStatus(deactivatedUser,userType,managerType,true);
                if(!usersUnderDeactivatedUser.isEmpty()){
                    userList.addAll(usersUnderDeactivatedUser);
                }
            }
        }
        return userList;
    }

    private List<User> getUsersUnderUserByStatus(User user, String userType, String managerType, Boolean active) {
        Role admin = roleService.roleName(ROLE_ADMIN);
        Criteria criteria = new Criteria();
        if(!user.getRoles().contains(admin)) {
            criteria = criteria.and("createdByUserId").is(user.getId());
        }

        if(!StringUtils.isEmpty(userType) || !StringUtils.isEmpty(managerType)) {
            List<String> roleList = getRoleListBySearchUserTypeAndLoginUserRole(userType, managerType, user);
            criteria = criteria.and("roles.roleName").in(roleList);
        }

        if(active){
            criteria = criteria.and("isActive").ne(false).exists(true);
        } else {
            criteria = criteria.and("isActive").ne(true).exists(true);
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<UserLandDetail> searchLandDetail(String locationId, String khasraNo) {
        Query query = new Query(Criteria.where("villageModel.$id").is(new ObjectId(locationId)).and("khasraNo").regex(khasraNo,"i"))
                .with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.find(query,UserLandDetail.class);
    }
    
    @Override
    public List<UserLandDetail> getFarmerAllLandDetail(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "creationDate"));
        return mongoTemplate.find(query,UserLandDetail.class);
    }

    @Override
    public AgentVillage findAgentByAssignVillage(String agentId, String villageId) {
        Query query = new Query(Criteria.where("agent.$id").is(new ObjectId(agentId)).and("village.$id").is(new ObjectId(villageId)));
        return mongoTemplate.findOne(query,AgentVillage.class);
    }
    
    @Override
    public void save(PrimaryCategoryInHindi primaryCategoryInHindi) {
        mongoTemplate.save(primaryCategoryInHindi);
    }
}