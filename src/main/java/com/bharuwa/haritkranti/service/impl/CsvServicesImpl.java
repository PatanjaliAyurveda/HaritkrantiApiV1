package com.bharuwa.haritkranti.service.impl;

//import com.bharuwa.haritkranti.bidSystem.models.Product;
//import com.bharuwa.haritkranti.bidSystem.models.ProductCategory;
//import com.bharuwa.haritkranti.bidSystem.models.ProductCategoryMember;
//import com.bharuwa.haritkranti.bidSystem.models.ProductCategoryRollup;
//import com.bharuwa.haritkranti.bidSystem.services.ProductService;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.*;
import com.bharuwa.haritkranti.models.fertilizerModels.Fertilizer;
import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.msg91.Msg91Services;
import com.bharuwa.haritkranti.repositories.PrimaryCategoryInHindiRepository;
import com.bharuwa.haritkranti.repositories.PrimaryCategoryRepository;
import com.bharuwa.haritkranti.repositories.StateRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import com.bharuwa.haritkranti.utils.MessageResponse;
import com.opencsv.CSVReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.bharuwa.haritkranti.models.Equipment.EquipmentType.Immoveable;
import static com.bharuwa.haritkranti.models.Equipment.EquipmentType.Moveable;
import static com.bharuwa.haritkranti.service.impl.AgentServiceImpl.sendFarmerAppLink;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.*;

/**
 * @author anuragdhunna
 */
@Service
public class CsvServicesImpl implements CsvServices {

    private final MongoTemplate mongoTemplate;

    public CsvServicesImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Value("${agent_app_link}")
    private String agentAppLink;
    @Value("${admin_panel_link}")
    private String adminPanelLink;

    @Autowired
    private StateRepo stateRepo;

    @Autowired
	private PrimaryCategoryRepository primaryCategoryRepo;
    
    @Autowired
	private PrimaryCategoryInHindiRepository primaryCategoryInHindiRepo;
    
    @Autowired
    private LocationServices locationServices;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GovernmentSchemesService governmentSchemesService;

    //@Autowired
   // private ProductService productService;

    @Autowired
    GenericMongoTemplate genericMongoTemplate;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private CountService countService;

    @Autowired
    private CropService cropService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private LandService landService;

    @Autowired
    private FarmDetailService farmDetailService;

    @Autowired
    private UserBankService userBankService;

    @Override
    public State saveState(State state) {

        State value = locationServices.findStateByName(state.getName());

        if (value == null) {
            stateRepo.save(state);
        } else {
            value.setName(state.getName());
            value.setStateAbbreviation(state.getStateAbbreviation());
            stateRepo.save(value);
        }

        return state;
    }

    @Override
    public List<String> uploadManagerCsv(final MultipartFile file, String userId) throws Exception {
// to check error line
        int line = 1;

        if (StringUtils.isEmpty(userId)) {
            List<User> adminUsers = userService.getAdminUsers();
            if (!adminUsers.isEmpty()) {
                User user = adminUsers.get(0);
                userId = user.getId();
            }
        }

        List<String> validRoles = new ArrayList<>();
        // National Manager role added
        validRoles.add(ROLE_NATIONAL_MANAGER);
        validRoles.add(ROLE_STATE_MANAGER);
        validRoles.add(ROLE_DISTRICT_MANAGER);
        validRoles.add(ROLE_AGENT_MANAGER);
        validRoles.add(ROLE_AGENT);

//        Check role of the user and accordingly give access to them
        User loginUser = userService.findByid(userId);
        List<Role> loginUserRoles = loginUser.getRoles();

        if (loginUser == null) {
            throw new ResourceNotFoundException("User Does Not Exist");
        }
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);

        if (loginUserRoles.contains(nationalManager)) {

            validRoles.remove(ROLE_NATIONAL_MANAGER);

        } else if (loginUserRoles.contains(stateManager)) {

            validRoles.remove(ROLE_NATIONAL_MANAGER);
            validRoles.remove(ROLE_STATE_MANAGER);

        } else if (loginUserRoles.contains(districtManager)) {

            validRoles.remove(ROLE_NATIONAL_MANAGER);
            validRoles.remove(ROLE_STATE_MANAGER);
            validRoles.remove(ROLE_DISTRICT_MANAGER);

        } else if (loginUserRoles.contains(blockManager)) {

            validRoles.remove(ROLE_NATIONAL_MANAGER);
            validRoles.remove(ROLE_STATE_MANAGER);
            validRoles.remove(ROLE_DISTRICT_MANAGER);
            validRoles.remove(ROLE_AGENT_MANAGER);

        }

        //roleName must not be empty or null
        String userPrimaryRoleName = "";

        //        Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

//        Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            User exist = userService.getUserByPhoneNum(input[4].trim());
            if (exist == null) {
                User user = new User();
                user.setFirstName(input[0].trim());
                user.setMiddleName(input[1].trim());
                user.setLastName(input[2].trim());
                user.setFatherName(input[3].trim());
                if (!StringUtils.isEmpty(input[5])) {
                    user.setEmail(input[5].trim());
                }
                user.setPrimaryPhone(input[4].trim());
                user.setCreatedByUserId(userId);

                if (!validRoles.contains(input[6])) {
                    messages.add("you are not authorize to add role " + input[6] + " for mobile number " + input[4].trim() + "(line:" + line + ")");
//                    messages.add("Role "+input[6]+" not valid for mobile no "+input[4].trim());
                    continue;
                }

                Role role = roleService.roleName(input[6]);

                // set User's Primary Role Name
                if (StringUtils.isEmpty(userPrimaryRoleName)) {
                    userPrimaryRoleName = role.getRoleName();
                }

//                TODO: temporary changes
                Role agentRole = roleService.roleName(ROLE_AGENT);

                List<Role> roleList = new ArrayList<>();
                roleList.add(role);
                if (!role.getRoleName().equals(ROLE_AGENT)) {
                    roleList.add(agentRole);
                }

                // Set Manager Role
                user.setRoles(roleList);

                State state = locationServices.findStateByName(input[7].trim());
                if (state == null) {
                    messages.add("State " + input[7] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String cityName = input[8];
                City city = locationServices.findCityByNameAndState(cityName.trim(), state.getId());
                if (city == null) {
                    messages.add("City " + input[8] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String tehsilName = input[9];
                Tehsil tehsil = locationServices.getTehsilByNameCityAndState(tehsilName.trim(), city.getId(), state.getId());
                if (tehsil == null) {
                    messages.add("Tehsil " + input[9] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String blockName = input[10];
                Block block = locationServices.getBlocksByNameTehsilCityState(blockName.trim(), tehsil.getId(), city.getId(), state.getId());
                if (block == null) {
                    messages.add("Block " + input[10] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String villageName = input[11];
                Village village = locationServices.getVillageByNameBlockTehsilCityState(villageName.trim(), block.getId(), tehsil.getId(), city.getId(), state.getId());
                if (village == null) {
                    messages.add("Village " + input[11] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                OTP mOTP = generateOtp();

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));
                user.setActive(true);
                user.setRequirePassword(true);
                user.setCreationDate(Calendar.getInstance().getTime());

//                TODO: unique code for agents and managers
                user.setUserCode(generateUniqueCodeForUser());

                user.getAddressModel().setState(state);
                user.getAddressModel().setCity(city);
                user.getAddressModel().setTehsil(tehsil);
                user.getAddressModel().setBlock(block);
                user.getAddressModel().setVillageModel(village);
                user.getAddressModel().setAddress(input[12]);

                user.setOtp(mOTP);
                userService.saveUser(user);

                // add Employee History
                EmployeeHistory employeeHistory = new EmployeeHistory();
                employeeHistory.setUser(user);
                employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
                employeeHistory.setCreatedByUserId(user.getCreatedByUserId());
                employeeHistoryService.storeEmployeeHistory(employeeHistory);

                // add EmployeeAssignmentHistory
                User fromUser = userService.findByid(user.getCreatedByUserId());

                EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
                employeeAssignmentHistory.setToUser(user);
                employeeAssignmentHistory.setFromUser(fromUser);
                employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(role.getRoleName(), fromUser, nationalManager, stateManager, districtManager, blockManager));
                employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);


                String message;
                if (input[6].equals(ROLE_AGENT)) {
                    message = "Your Username for " + APP_NAME + " Agent App is:\n" +
                            "username = " + user.getPrimaryPhone() + "\n" + agentAppLink;
                } else {
                    message = "Welcome to " + APP_NAME + "\n" +
                            "Your Username for " + APP_NAME + " is:\n" +
                            "username = " + user.getPrimaryPhone() + "\n" +
                            APP_NAME + " App Link: " + agentAppLink + "\n" +
                            APP_NAME + "Admin Panel Link: " + adminPanelLink;
                }


                Msg91Services.sendFarmerAppLinkMsg(message, user.getPrimaryPhone());
            } else {
                messages.add("User with number " + input[4] + " already exist." + "(line:" + line + ")");
            }
        }


        //count set according to Manager type
        CountAll countAll = countService.getCountAll();
        if (userPrimaryRoleName.equals(ROLE_NATIONAL_MANAGER)) {
            countAll.setTotalNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER, false));
            countAll.setTotalActiveNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER, true));
        } else if (userPrimaryRoleName.equals(ROLE_STATE_MANAGER)) {
            countAll.setTotalActiveStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER, true));
            countAll.setTotalStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER, false));
        } else if (userPrimaryRoleName.equals(ROLE_DISTRICT_MANAGER)) {
            countAll.setTotalActiveDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER, true));
            countAll.setTotalDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER, false));
        } else if (userPrimaryRoleName.equals(ROLE_AGENT_MANAGER)) {
            countAll.setTotalActiveAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER, true));
            countAll.setTotalAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER, false));
        } else if (userPrimaryRoleName.equals(ROLE_AGENT)) {
            countAll.setTotalActiveAgents(countService.getTotalAgentCount(true));
            countAll.setTotalAgents(countService.getTotalAgentCount(false));
        }
        countAll.setTotalUsers(countService.getTotalUserCount());
        countService.storeCountAll(countAll);

        return messages;
    }

    @Override
    public void uploadGovernmentSchemesCsv(MultipartFile file) throws IOException {

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        ;

        while ((input = csvReader.readNext()) != null) {

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            GovernmentSchemes scheme = new GovernmentSchemes();
            scheme.setSchemeName(input[0].trim());
            scheme.setSchemeCode(input[1].trim());
            scheme.setSchemeSponser(input[2].trim());

            String statesArray = input[3];
            String replace = statesArray.replace("[", "");
            String replace1 = replace.replace("]", "");


            //for Filter Keys
            List<String> statesList = Arrays.asList(replace1.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());

            if (statesList.contains(CENTRAL)) {
                scheme.setCentral(true);
            } else {
                List<State> stateList = new ArrayList<>();
                for (String stateName : statesList) {
                    State state = locationServices.findStateByName(stateName);
                    stateList.add(state);
                }
                scheme.setStates(stateList);
            }
            scheme.setActive(true);
            governmentSchemesService.storeGovernmentScheme(scheme);
        }

    }

    @Override
    public void uploadDairyFarmerCsv(MultipartFile file, String userId) throws IOException, ParseException {

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        while ((input = csvReader.readNext()) != null) {
            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            MilkingAnimalDetails milkingAnimalDetails = new MilkingAnimalDetails();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                throw new ResourceNotFoundException("User not found with phone number: " + phoneNumber);
            }

            milkingAnimalDetails.setUserId(user.getId());
            milkingAnimalDetails.setAnimalType(MilkingAnimalDetails.MilkingAnimalType.valueOf(input[1]));
            milkingAnimalDetails.setAnimalBreed(MilkingAnimalDetails.AnimalBreed.valueOf(input[2]));
            milkingAnimalDetails.setIncome(new BigDecimal(input[3]));
            milkingAnimalDetails.setIncomePeriod(Horticulture.IncomePeriod.valueOf(input[4]));
            milkingAnimalDetails.setTotalNoOfAnimals(Integer.parseInt(input[5]));
            milkingAnimalDetails.setMix(Integer.parseInt(input[6]));
            milkingAnimalDetails.setDesi(Integer.parseInt(input[7]));
            milkingAnimalDetails.setMilkingCondition(Integer.parseInt(input[8]));
            milkingAnimalDetails.setNonMilkingCondition(Integer.parseInt(input[9]));
            milkingAnimalDetails.setHifer(Integer.parseInt(input[10]));
            milkingAnimalDetails.setMilkProductionOutput(new BigDecimal(input[11]));
            milkingAnimalDetails.setHomeUsage(new BigDecimal(input[12]));
            milkingAnimalDetails.setSale(new BigDecimal(input[13]));
            milkingAnimalDetails.setUnitProductionOutput(Fertilizer.Unit.valueOf(input[14]));

            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(input[15]);
            milkingAnimalDetails.setDate(date);

            mongoTemplate.save(milkingAnimalDetails);
        }
    }

    @Override
    public MessageResponse uploadProductCategoryCsv(MultipartFile file) throws Exception {
        MessageResponse response = new MessageResponse();

        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        ;

  //      ProductCategory productCategory = new ProductCategory();
  //      productCategory.setName(input[0].trim());
  //      productService.addProductCategory(productCategory);

        response.setMessage("CSV Uploaded Successfully");
        return response;
    }

    @Override
    public MessageResponse uploadProductCategoryRollupCsv(MultipartFile file) throws Exception {
        MessageResponse response = new MessageResponse();

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        while ((input = csvReader.readNext()) != null) {
            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
         //   ProductCategoryRollup productCategoryRollup = new ProductCategoryRollup();
         //   productCategoryRollup.setCategoryName(input[0].trim());
        //    productCategoryRollup.setParentProductCategoryName(input[1].trim());
   //         ProductCategory productCategory = productService.findCategoryByName(input[1].trim());
	/*
	 * if (productCategory == null) { throw new
	 * ResourceNotFoundException("Category not found with name:" + input[1]); }
	 */
         //   productCategoryRollup.setProductCategory(productCategory);
        //    productService.addProductCategoryRollup(productCategoryRollup);
        }

        response.setMessage("CSV Uploaded Successfully");
        return response;
    }

    @Override
    public MessageResponse uploadProductCategoryMemberCsv(MultipartFile file) throws Exception {
        MessageResponse response = new MessageResponse();

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        while ((input = csvReader.readNext()) != null) {
            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
         //   ProductCategoryMember productCategoryMember = new ProductCategoryMember();
			/*
			 * productCategoryMember.setProductName(input[0].trim());
			 * productCategoryMember.setCategoryName(input[1].trim()); Product product =
			 * productService.findProductByName(input[0].trim()); if (product == null) {
			 * throw new ResourceNotFoundException("Product not found with name:" +
			 * input[0]); } ProductCategory productCategory =
			 * productService.findCategoryByName(input[1].trim()); if (productCategory ==
			 * null) { throw new ResourceNotFoundException("Category not found with name:" +
			 * input[1]); } productCategoryMember.setProductCategory(productCategory);
			 * productCategoryMember.setProduct(product);
			 * productService.addProductCategoryMember(productCategoryMember);
			 */
        }

        response.setMessage("CSV Uploaded Successfully");
        return response;
    }

    @Override
    public MessageResponse uploadProductCsv(MultipartFile file) throws Exception {
        MessageResponse response = new MessageResponse();

        // Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        while ((input = csvReader.readNext()) != null) {
            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
			/*
			 * Product product = new Product(); product.setProductName(input[0].trim());
			 * productService.addProduct(product);
			 */
        }

        response.setMessage("CSV Uploaded Successfully");
        return response;
    }

    @Override
    public void readFruitVarietyExcel(final MultipartFile file) throws IOException, InvalidFormatException {

        // to check error line
        int line = 1;

        DataFormatter dataFormatter = new DataFormatter();

//        FileInputStream fileInputStream = new FileInputStream(file.getInputStream());
        XSSFWorkbook myWorkBook = new XSSFWorkbook(file.getInputStream());


        int numberOfSheets = myWorkBook.getNumberOfSheets();

        XSSFSheet fruitVaritySheet = myWorkBook.getSheetAt(0);

        //iterating over excel file
        Iterator<Row> itr = fruitVaritySheet.iterator();
        itr.next();
        FruitVarietyLocation existLocation = null;

        while (itr.hasNext()) {
            line++;

            Row row = itr.next();


            System.out.println("=========row num============" + row.getRowNum());


            //iterating over each column
            String fruitVarietyName = getCellValueAsString(row.getCell(0));

            String cropGroupName = getCellValueAsString(row.getCell(1));
            String cropGroupType = getCellValueAsString(row.getCell(2));

            Query query;
//             query = new Query(Criteria.where("name").is(cropGroupName).and("type").is(cropGroupType));
//            CropGroup cropGroup = mongoTemplate.findOne(query, CropGroup.class);

            CropGroup cropGroup = cropService.getCropGroupsByNameAndType(cropGroupName, cropGroupType);

            if (cropGroup == null) {
                throw new ResourceNotFoundException("Crop Group not found with name: " + cropGroupName + " and type: " + cropGroupType + "(line:" + line + ")");
            }

            // Check Existing Fruit Variety

            query = new Query(Criteria.where("name").is(fruitVarietyName).and("cropGroup.$id").is(new ObjectId(cropGroup.getId())));
            FruitVariety fruitVariety = mongoTemplate.findOne(query, FruitVariety.class);

            if (fruitVariety == null) {
                fruitVariety = new FruitVariety();
                fruitVariety.setName(fruitVarietyName);
                fruitVariety.setCropGroup(cropGroup);
            }


            String stateName = getCellValueAsString(row.getCell(3));
            State state = null;

            if (stateName.equalsIgnoreCase("All state")) {
                List<State> allStates = genericMongoTemplate.findAll(State.class);
                for (State statea : allStates) {
                    storeFVState(row, fruitVariety, statea);
                }
            } else {
                state = locationServices.findStateByName(stateName);
                if (state == null) {
                    throw new ResourceNotFoundException("State not found with name: " + stateName + "(line:" + line + ")");
                }
                storeFVState(row, fruitVariety, state);
            }
        }
        myWorkBook.close();
    }

    private void storeFVState(Row row, FruitVariety fruitVariety, State state) {
        // Check Fruit Variety Location Exists

        // Add Dosage List

        DosagePercentage dosagePercentage = new DosagePercentage();
        dosagePercentage.setTimeUnit(DosagePercentage.TimeUnit.valueOf(getCellValueAsString(row.getCell(13))));
        dosagePercentage.setTime(Integer.parseInt(getCellValueAsString(row.getCell(14))));

        dosagePercentage.setFymPercentage(new BigDecimal(getCellValueAsString(row.getCell(15))));
        dosagePercentage.setnPercentage(new BigDecimal(getCellValueAsString(row.getCell(16))));
        dosagePercentage.setpPercentage(new BigDecimal(getCellValueAsString(row.getCell(17))));
        dosagePercentage.setkPercentage(new BigDecimal(getCellValueAsString(row.getCell(18))));


//            query = new Query(Criteria.where("name").is(cropGroupName).and("type").is(cropGroupType).and("fruitVarietyLocations.state.id").is(state.getId()));
//            List<FruitVarietyLocation> fruitVarietyLocations1 = mongoTemplate.findDistinct(query, "fruitVarietyLocations", FruitVariety.class, FruitVarietyLocation.class);

//            FruitVariety fruitVarietyByLoc = mongoTemplate.findOne(query,FruitVariety.class);
//            System.out.println("================fvl=============="+fruitVarietyLocations1.size());


        if (!fruitVariety.getFruitVarietyLocations().isEmpty()) {

            List<FruitVarietyLocation> fruitVarietyLocationList = new ArrayList<FruitVarietyLocation>(fruitVariety.getFruitVarietyLocations());
            boolean condition = false;
            for (FruitVarietyLocation fvl : fruitVarietyLocationList) {
                if (fvl.getState().getId().equals(state.getId())) {
                    condition = true;
                    // New Dosage Percentage
                    List<DosagePercentage> dosagePercentageList = new ArrayList<DosagePercentage>(fvl.getDosagePercentages());
                    boolean dosageCondition = false;
                    for (DosagePercentage dP : dosagePercentageList) {
                        if (dP.getTime() == dosagePercentage.getTime() && dP.getTimeUnit().equals(dosagePercentage.getTimeUnit())) {
                            dosageCondition = true;
                        }
//                            fruitVarietyLocation = fvl;
//                            setFruitVarietyLocation(row, fruitVariety, state, fvl, dosagePercentage);
                    }
                    if (!dosageCondition) {
                        fruitVariety.getFruitVarietyLocations().remove(fvl);
                        fvl.addDosagePercentage(dosagePercentage);
                        fruitVariety.addFruitVarietyLocation(fvl);
                    }
                }
            }
            if (!condition) {
                setFruitVarietyLocation(row, fruitVariety, state, null, dosagePercentage);
            }

        } else {
            setFruitVarietyLocation(row, fruitVariety, state, null, dosagePercentage);

        }
//                FruitVarietyLocation fruitVarietyLocation1 = fruitVarietyLocations1.get(0);

//                if (!fruitVarietyLocation1.getDosagePercentages().contains(dosagePercentage)) {
//                    fruitVarietyLocation.addDosagePercentage(dosagePercentage);
//                }
//            } else {
//                fruitVarietyLocation.addDosagePercentage(dosagePercentage);
//            }

        mongoTemplate.save(fruitVariety);
    }

    private void setFruitVarietyLocation(Row row, FruitVariety fruitVariety, State state, FruitVarietyLocation fruitVarietyLocation, DosagePercentage dosagePercentage) {

        if (fruitVarietyLocation == null) {
            fruitVarietyLocation = new FruitVarietyLocation();

            fruitVarietyLocation.setState(state);
            fruitVarietyLocation.setSpacingUnit(FruitVarietyLocation.SpacingUnit.valueOf(getCellValueAsString(row.getCell(4))));
            fruitVarietyLocation.setColumnSpace(new BigDecimal(getCellValueAsString(row.getCell(5))));
            fruitVarietyLocation.setRowSpace(new BigDecimal(getCellValueAsString(row.getCell(6))));
            fruitVarietyLocation.setNoOfPlantsPerAcre(Integer.parseInt(getCellValueAsString(row.getCell(7))));
            fruitVarietyLocation.setFertilizerUnit(Fertilizer.Unit.valueOf(getCellValueAsString(row.getCell(8))));

            fruitVarietyLocation.setFym(new BigDecimal(getCellValueAsString(row.getCell(9))));
            fruitVarietyLocation.setN(new BigDecimal(getCellValueAsString(row.getCell(10))));
            fruitVarietyLocation.setP(new BigDecimal(getCellValueAsString(row.getCell(11))));
            fruitVarietyLocation.setK(new BigDecimal(getCellValueAsString(row.getCell(12))));
        }
        fruitVarietyLocation.addDosagePercentage(dosagePercentage);
        fruitVariety.addFruitVarietyLocation(fruitVarietyLocation);
    }

    @Override
    public List<String> uploadFarmerCsv(MultipartFile file, String agentId) throws IOException, ParseException {

        // to check error line
        int line = 1;

        if (StringUtils.isEmpty(agentId)) {
            List<User> adminUsers = userService.getAdminUsers();
            if (!adminUsers.isEmpty()) {
                User user = adminUsers.get(0);
                agentId = user.getId();
            }
        }

        User agent = userService.findByid(agentId);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent Does Not Exist");
        }

        //roleName must not be empty or null

        //        Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

//        Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            User exist = userService.getUserByPhoneNum(input[4].trim());
            if (exist == null) {
                User user = new User();
                user.setFirstName(input[0].trim());
                user.setMiddleName(input[1].trim());
                user.setLastName(input[2].trim());
                user.setFatherName(input[3].trim());
                if (!StringUtils.isEmpty(input[5])) {
                    user.setEmail(input[5].trim());
                }
                user.setPrimaryPhone(input[4].trim());
                user.setCreatedByUserId(agentId);

                // add user role for farmer
                String roleName;
                if (StringUtils.isEmpty(input[6])) {
                    roleName = ROLE_USER;
                } else {
                    roleName = input[6];
                }

                Role farmerRole = roleService.roleName(roleName);

                List<Role> roleList = new ArrayList<>();
                roleList.add(farmerRole);

                // Set Manager Role
                user.setRoles(roleList);

                State state = locationServices.findStateByName(input[7].trim());
                if (state == null) {
                    messages.add("State " + input[7] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String cityName = input[8];
                City city = locationServices.findCityByNameAndState(cityName.trim(), state.getId());
                if (city == null) {
                    messages.add("City " + input[8] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String tehsilName = input[9];
                Tehsil tehsil = locationServices.getTehsilByNameCityAndState(tehsilName.trim(), city.getId(), state.getId());
                if (tehsil == null) {
                    messages.add("Tehsil " + input[9] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String blockName = input[10];
                Block block = locationServices.getBlocksByNameTehsilCityState(blockName.trim(), tehsil.getId(), city.getId(), state.getId());
                if (block == null) {
                    messages.add("Block " + input[10] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

                String villageName = input[11];
                Village village = locationServices.getVillageByNameBlockTehsilCityState(villageName.trim(), block.getId(), tehsil.getId(), city.getId(), state.getId());
                if (village == null) {
                    messages.add("Village " + input[11] + ", not found for User with mobile no " + input[4].trim() + "(line:" + line + ")");
                    continue;
                }

//                OTP mOTP = generateOtp();

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));
                user.setActive(true);
                user.setCreationDate(Calendar.getInstance().getTime());
                user.getAddressModel().setState(state);
                user.getAddressModel().setCity(city);
                user.getAddressModel().setTehsil(tehsil);
                user.getAddressModel().setBlock(block);
                user.getAddressModel().setVillageModel(village);
                user.getAddressModel().setAddress(input[12]);
                user.getAddressModel().setZipCode(input[13]);
                user.setGender(User.Gender.valueOf(input[14]));

                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = dateFormat.parse(input[15]);
                user.setDateOfBirth(date);
                user.setReligion(input[16]);
                int count = 0;
                for (User.Category category : User.Category.values()) {
                    if (category.toString().equalsIgnoreCase(input[17])) {
                        user.setCategory(category);
                        count++;
                    }
                }
                if (count <= 0) {
                    messages.add("Invalid Category type  (line:" + line + ")");
                    continue;
                }

                count = 0;
                for (User.Qualification qualification : User.Qualification.values()) {
                    if (qualification.toString().equalsIgnoreCase(input[18])) {
                        user.setQualification(qualification);
                        count++;
                    }
                }
                if (count <= 0) {
                    messages.add("Invalid Qualification type  (line:" + line + ")");
                    continue;
                }

                //set userCode for farmer
                user.setUserCode(userExtraService.generateUniqueCodeForFarmer(user));

                userService.saveUser(user);

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
                employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(ROLE_USER, fromUser, nationalManager, stateManager, districtManager, blockManager));
                employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);

                // Identification Details
                UserIdentificationDetails userIdentificationDetails = new UserIdentificationDetails();
                userIdentificationDetails.setUserId(user.getId());
                userIdentificationDetails.setAadhaarCardNumber(input[19]);
                userService.addUserIdentificationDetails(userIdentificationDetails);

                // Add User in UserAgent under particular Agent
                Query query = new Query(Criteria.where("agentId").is(agentId.trim()));
                UserAgent userAgent = mongoTemplate.findOne(query, UserAgent.class);

                // TODO: Need to check this condition
                // Create UserAgent to add Users for Agent
                if (userAgent == null) {
                    userAgent = new UserAgent();
                    userAgent.setAgentId(agentId);
                    userAgent.setCreationDate(Calendar.getInstance().getTime());
                    userAgent.addUser(user);
                    agentService.saveUserForAgent(userAgent);
                } else {
                    userAgent.addUser(user);
                    agentService.saveUserForAgent(userAgent);
                }
                // Send Farmer App Link
                sendFarmerAppLink(user);

            } else {
                messages.add("User with number " + input[4] + " already exist." + " (line:" + line + ")");
            }
        }

        // update count for farmers
        CountAll countAll = countService.getCountAll();
        countAll.setTotalFarmers(countService.getTotalFarmerCount());
        countAll.setTotalUsers(countService.getTotalUserCount());
        countAll.setTotalFemaleFarmers(countService.getTotalFemaleFarmerCount());
        countAll.setTotalMaleFarmers(countService.getTotalMaleFarmerCount());
        countService.storeCountAll(countAll);

        // set HasFarmers 'TRUE' for Agents
        agent.setHasFarmers(true);
        userService.saveUser(agent);
        return messages;
    }

    @Override
    public List<String> LandDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //        Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            //Set UserLandDetail for farmer
            UserLandDetail landDetail = new UserLandDetail();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            landDetail.setUserId(user.getId());
            User agent = userService.findByid(user.getCreatedByUserId());
            if (agent == null) {
                messages.add("Agent not found for User with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            landDetail.setAgentId(agent.getId());
            landDetail.setState(user.getAddressModel().getState());
            landDetail.setCity(user.getAddressModel().getCity());
     //       landDetail.setTehsil(user.getAddressModel().getTehsil());
     //       landDetail.setBlock(user.getAddressModel().getBlock());
     //       landDetail.setVillageModel(user.getAddressModel().getVillageModel());
            landDetail.setLandSize(new BigDecimal(input[1]));
            int count = 0;
            for (FieldSize.FieldSizeType fieldSizeType : FieldSize.FieldSizeType.values()) {
                if (fieldSizeType.toString().equalsIgnoreCase(input[2])) {
                    landDetail.setLandSizeType(fieldSizeType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Land size Type : "+input[2]+" for phone number"+phoneNumber+ " (line:" + line + ")");
                continue;
            }
            landDetail.setKhasraNo(input[3]);
            count = 0;
            for (UserLandDetail.OwnershipType ownershipType : UserLandDetail.OwnershipType.values()) {
                if (ownershipType.toString().equalsIgnoreCase(input[4])) {
                    landDetail.setOwnershipType(ownershipType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Ownership Type : "+input[4]+" for phone number"+phoneNumber+" (line:" + line + ")");
                continue;
            }
            landDetail.setOwnerName(input[5]);
            count = 0;
            for (LandType.Type type : LandType.Type.values()) {
                if (type.toString().equalsIgnoreCase(input[6])) {
                    landDetail.setFarmType(type);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Farm Type : "+input[2]+" for phone number"+phoneNumber+" (line:" + line + ")");
                continue;
            }
            landDetail.setIrrigationSource(UserLandDetail.SourceOfIrrigation.valueOf(input[7]));
            String soilName = input[8];
            Soil soil = landService.findSoilByName(soilName);
            if (soil == null) {
                throw new ResourceNotFoundException("Soil not found : "+ soilName + " for phone number"+phoneNumber+" (line:"+line+")");
            }
            landDetail.setSoil(soil);
            agentService.addUserLandDetail(landDetail);
        }
        return messages;
    }

    @Override
    public List<String> FarmerHealthDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //        Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            //Set HealthRecord for farmer
            FamilyMemberHealthRecord familyMemberHealthRecord = new FamilyMemberHealthRecord();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            familyMemberHealthRecord.setUserId(user.getId());
            familyMemberHealthRecord.setHeadUserId(user.getId());
            familyMemberHealthRecord.setAddress(user.getAddressModel());
            familyMemberHealthRecord.setDisability(input[1]);
            familyMemberHealthRecord.setMedicalProblem(input[2]);
            familyMemberHealthRecord.setMedicalExpense(new BigDecimal(input[3]));
            userService.addFamilyMemberHealthDetails(familyMemberHealthRecord);
        }
        return messages;
    }

    @Override
    public List<String> FarmerFamilyDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //        Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            //add FamilyMember for farmer
            FamilyMember familyMember = new FamilyMember();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            User agent = userService.findByid(user.getCreatedByUserId());
            if (agent == null) {
                messages.add("Agent not found for User with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            familyMember.setFarmerId(user.getId());
            familyMember.setAgentId(agent.getId());
            familyMember.setSameAddress(true);
            familyMember.setAddress(user.getAddressModel());
            familyMember.setFirstName(input[1].trim());
            familyMember.setMiddleName(input[2].trim());
            familyMember.setLastName(input[3].trim());
            familyMember.setRelation(input[4]);
            familyMember.setAge(Integer.valueOf(input[5]));

           // use iteration to check valid gender
            int count = 0;
            for (User.Gender gender : User.Gender.values()) {
                if (gender.toString().equalsIgnoreCase(input[6])) {
                    familyMember.setGender(gender);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid Gender (line:" + line + ")");
                continue;
            }
            userService.addFamilyMemberNew(familyMember);
        }
        return messages;
    }

    @Override
    public List<String> FarmerLiveStockDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        // Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            //add milking animal detail for farmer
            MilkingAnimalDetails milkingAnimalDetails = new MilkingAnimalDetails();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }

            milkingAnimalDetails.setUserId(user.getId());
            milkingAnimalDetails.setAddress(user.getAddressModel());
            int count = 0;
            for (MilkingAnimalDetails.MilkingAnimalType animalType : MilkingAnimalDetails.MilkingAnimalType.values()) {
                if (animalType.toString().equalsIgnoreCase(input[1])) {
                    milkingAnimalDetails.setAnimalType(animalType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid animal Type (line:" + line + ")");
                continue;
            }
            milkingAnimalDetails.setTotalNoOfAnimals(Integer.parseInt(input[2]));
            milkingAnimalDetails.setMilkingCondition(Integer.parseInt(input[3]));
            milkingAnimalDetails.setMilkProductionOutput(new BigDecimal(input[4]));
            milkingAnimalDetails.setUnitProductionOutput(Fertilizer.Unit.valueOf(input[5]));
            milkingAnimalDetails.setHomeUsage(new BigDecimal(input[6]));
            milkingAnimalDetails.setUnitProductionOutput(Fertilizer.Unit.valueOf(input[7]));
            milkingAnimalDetails.setSale(new BigDecimal(input[8]));
            milkingAnimalDetails.setUnitProductionOutput(Fertilizer.Unit.valueOf(input[9]));
            milkingAnimalDetails.setIncome(new BigDecimal(input[10]));
            count = 0;
            for (Horticulture.IncomePeriod incomePeriod : Horticulture.IncomePeriod.values()) {
                if (incomePeriod.toString().equalsIgnoreCase(input[11])) {
                    milkingAnimalDetails.setIncomePeriod(incomePeriod);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Income duration  (line:" + line + ")");
                continue;
            }
            userService.addMilkingAnimalDetails(milkingAnimalDetails);
        }
        return messages;
    }

    @Override
    public List<String> FarmerCropDetailCsv(MultipartFile file) throws IOException, ParseException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            //Set UserCrop for farmer
            UserCrop userCrop = new UserCrop();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            userCrop.setUserId(user.getId());
            User agent = userService.findByid(user.getCreatedByUserId());
            if (agent == null) {
                messages.add("Agent not found for User with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            userCrop.setAgentId(agent.getId());
            userCrop.setAddress(user.getAddressModel());
            String khasraNos = input[1];
            UserLandDetail userLandDetail = agentService.getUserLandDetailByKhasraNo(khasraNos, user.getId());
            if (userLandDetail == null) {
                messages.add("Land detail not exist with khasra number : " + khasraNos + "(line:" + line + ")");
                continue;
            }
            userCrop.setKhasraNo(khasraNos);
            userCrop.setUserLandDetailId(userLandDetail.getId());
//           TODO : KHASRA SIZE KHASRA UNIT (LANDSIZE AND LANDSIZE TYPE)
            int count = 0;
            for (Crop.CropSeason cropSeason : Crop.CropSeason.values()) {
                if (cropSeason.toString().equalsIgnoreCase(input[2])) {
                    userCrop.setCropSeason(cropSeason);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Crop season  (line:" + line + ")");
                continue;
            }
            String cropGroupType = input[3];
            String cropGroupName = input[4];
            String cropName = input[5];
            Crop crop = cropService.findCropByNameAndType(cropGroupName, cropGroupType, cropName);
            if (crop == null) {
                messages.add("crop not found : " + "(line:" + line + ")");
                continue;
            }
            userCrop.setCrop(crop);
            userCrop.setLandSize(new BigDecimal(input[6])); //area size
            count = 0;
            for (FieldSize.FieldSizeType fieldSizeType : FieldSize.FieldSizeType.values()) {
                if (fieldSizeType.toString().equalsIgnoreCase(input[7])) {
                    userCrop.setLandSizeType(fieldSizeType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  Area unit  (line:" + line + ")");
                continue;
            }
            userCrop.setCropYield(new BigDecimal(input[8]));
            count = 0;
            for (Fertilizer.Unit yieldUnit : Fertilizer.Unit.values()) {
                if (yieldUnit.toString().equalsIgnoreCase(input[9])) {
                    userCrop.setCropYieldUnit(yieldUnit);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid  yield unit  (line:" + line + ")");
                continue;
            }
            userCrop.setSeedQuantity(Integer.parseInt(input[10]));
            count = 0;
            for (Fertilizer.Unit unit : Fertilizer.Unit.values()) {
                if (unit.toString().equalsIgnoreCase(input[11])) {
                    userCrop.setSeedQuantityUnit(unit);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid unit  (line:" + line + ")");
                continue;
            }
            userCrop.setFertilizerQuantity(Integer.parseInt(input[12]));
            count = 0;
            for (Fertilizer.Unit unit : Fertilizer.Unit.values()) {
                if (unit.toString().equalsIgnoreCase(input[13])) {
                    userCrop.setSeedQuantityUnit(unit);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid unit  (line:" + line + ")");
                continue;
            }
            userCrop.setNumberOfManPower(Integer.parseInt(input[14]));

            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(input[15]);
            userCrop.setYearOfSowing(date);
            //TODO: crop status (need to check validation for current crop)
            userExtraService.addUserCrop(userCrop);
        }
        return messages;
    }

    @Override
    public List<String> FarmerEquipmentsCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            //set equipments for farmer
            UserEquipments userEquipments = new UserEquipments();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            userEquipments.setUserId(user.getId());
            userEquipments.setAddress(user.getAddressModel());

            String moveableEquipment = input[1];
            moveableEquipment = StringUtils.replace(moveableEquipment, "[", "").replace("]", "");
            List<String> mEquipments = Arrays.stream(moveableEquipment.split(",")).map(String::trim).collect(Collectors.toList());

            List<Equipment> moveableEquipmentList = new ArrayList<>();
            for (String equipmentName : mEquipments) {
                Equipment equipment = equipmentService.getEquipmentByNameAndType(equipmentName, Moveable.toString());
                if (equipment == null) {
                    messages.add("Equipment " + input[1] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                    continue;
                }
                moveableEquipmentList.add(equipment);
            }

            String immoveableEquipment = input[2];
            immoveableEquipment = StringUtils.replace(immoveableEquipment, "[", "").replace("]", "");
            List<String> imEquipments = Arrays.stream(immoveableEquipment.split(",")).map(String::trim).collect(Collectors.toList());

            List<Equipment> immoveableEquipmentList = new ArrayList<>();
            for (String equipmentName : imEquipments) {
                Equipment equipment = equipmentService.getEquipmentByNameAndType(equipmentName, Immoveable.toString());
                if (equipment == null) {
                    messages.add("Equipment " + input[2] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                    continue;
                }
                immoveableEquipmentList.add(equipment);
            }
            userEquipments.setMoveableEquipments(moveableEquipmentList);
            userEquipments.setImmoveableEquipments(immoveableEquipmentList);
            userService.addUserEquipment(userEquipments);
        }
        return messages;
    }

    @Override
    public List<String> FarmerSubsidyCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            UserSubsidy userSubsidy = new UserSubsidy();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }

            userSubsidy.setUserId(user.getId());
            userSubsidy.setAddress(user.getAddressModel());
            userSubsidy.setItem(input[1]);
            userSubsidy.setQuantity(Integer.parseInt(input[2]));

            String subsidyName = input[3];
            Subsidy subsidy = userService.getUserSubsidyByName(subsidyName);
            if (subsidy == null) {
                messages.add("Subsidy not found with subsidyType : " + "(line:" + line + ")");
                continue;
            }
            userSubsidy.setSubsidy(subsidy);
            userSubsidy.setAmount(new BigDecimal(input[4]));
            userService.storeUserSubsidy(userSubsidy);
        }
        return messages;
    }

    @Override
    public List<String> FarmerBeekepingDetailCsv(MultipartFile file) throws IOException, ParseException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            BeekeepingDetails beekeepingDetails = new BeekeepingDetails();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            beekeepingDetails.setUserId(user.getId());
            beekeepingDetails.setAddress(user.getAddressModel());
            int count = 0;
            for (Crop.CropSeason season : Crop.CropSeason.values()) {
                if (season.toString().equalsIgnoreCase(input[1])) {
                    beekeepingDetails.setSeason(season);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid season  (line:" + line + ")");
                continue;
            }

            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(input[2]);
            beekeepingDetails.setFromDate(date);

            date = dateFormat.parse(input[3]);
            beekeepingDetails.setToDate(date);

            beekeepingDetails.setQuantity(Integer.parseInt(input[4]));
            beekeepingDetails.setProductionOutput(new BigDecimal(input[5]));
            count = 0;
            for (Fertilizer.Unit unit : Fertilizer.Unit.values()) {
                if (unit.toString().equalsIgnoreCase(input[6])) {
                    beekeepingDetails.setUnitProductionOutput(unit);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid Unit  (line:" + line + ")");
                continue;
            }
            count = 0;
            for (FarmDetails.HoneyFlavour honeyFlavour : FarmDetails.HoneyFlavour.values()) {
                if (honeyFlavour.toString().equalsIgnoreCase(input[7])) {
                    beekeepingDetails.setHoneyFlavour(honeyFlavour);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid Honey Flavour  (line:" + line + ")");
                continue;
            }
            beekeepingDetails.setIncome(new BigDecimal(input[8]));
            farmDetailService.storeBeekeepingDetails(beekeepingDetails);
        }
        return messages;
    }

    @Override
    public List<String> FarmerHorticultureDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            Horticulture horticulture = new Horticulture();

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            horticulture.setUserId(user.getId());
            horticulture.setAddress(user.getAddressModel());
            horticulture.setKhasraNo(input[1]);
            int count = 0;
            for (Horticulture.TreeType treeType : Horticulture.TreeType.values()) {
                if (treeType.toString().equalsIgnoreCase(input[2])) {
                    horticulture.setTreeType(treeType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Invalid Tree Variety  (line:" + line + ")");
                continue;
            }
            horticulture.setAgeOfTree(Integer.valueOf(input[3]));
            horticulture.setNoOfTrees(Integer.valueOf(input[4]));
//            TODO : Fruit Tree, Area, Area Unit, Age Unit, Plant Distance, Unit, Line Distance, Unit, Fertilizer-qty ,Unit, Manpower No, Crop Status
            landService.addHorticultureDetails(horticulture);
        }
        return messages;
    }

    @Override
    public List<String> VillageAssetDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }
            VillageAsset villageAsset = new VillageAsset();

            String phoneNumber = input[0];
            User agent = userService.getUserByPhoneNum(phoneNumber);
            if (agent == null) {
                messages.add("Agent not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            villageAsset.setAgentId(agent.getId());

            String stateName = input[1];
            State state = locationServices.findStateByName(stateName);
            if (state == null) {
                messages.add("State " + input[1] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                continue;
            }

            String cityName = input[2];
            City city = locationServices.findCityByNameAndState(cityName.trim(), state.getId());
            if (city == null) {
                messages.add("City " + input[2] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                continue;
            }

            String tehsilName = input[3];
            Tehsil tehsil = locationServices.getTehsilByNameCityAndState(tehsilName, city.getId(), state.getId());
            if (tehsil == null) {
                messages.add("Tehsil " + input[3] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                continue;
            }

            String blockName = input[4];
            Block block = locationServices.getBlocksByNameTehsilCityState(blockName, tehsil.getId(), city.getId(), state.getId());
            if (block == null) {
                messages.add("Block " + input[4] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                continue;
            }

            String villageName = input[5];
            Village village = locationServices.getVillageByNameBlockTehsilCityState(villageName.trim(), block.getId(), tehsil.getId(), city.getId(), state.getId());
            if (village == null) {
                messages.add("Village " + input[5] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                continue;
            }

            AgentVillage agentVillage = agentService.findAgentByAssignVillage(agent.getId(), village.getId());
            if (agentVillage == null) {
                messages.add("Village not assigned to agent");
                continue;
            }
            System.out.println("agent====== " + agent.getId() + agent.getPrimaryPhone() + "village==== " + villageName);
            villageAsset.setVillage(village);
            villageAsset.setGovernmentSeedCentre(Integer.parseInt(input[6]));
            villageAsset.setWatershedDevelopmentProject(Integer.parseInt(input[7]));
            villageAsset.setCommunityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam(Integer.parseInt(input[8]));
            villageAsset.setNoOfFPOOrPACS(Integer.parseInt(input[9]));
            villageAsset.setFoodStorageWarehouse(Integer.parseInt(input[10]));
            villageAsset.setSoilTestingCentres(Integer.parseInt(input[11]));
            villageAsset.setFertilizerShop(Integer.parseInt(input[12]));
            villageAsset.setDripIrrigationOrsprinkler_Irrigation(Integer.parseInt(input[13]));
            villageAsset.setMilkRoutesOrChillingCentres(Integer.parseInt(input[14]));
            villageAsset.setPoultryDevelopmentProjects(Integer.parseInt(input[15]));
            villageAsset.setGoataryDevelopmentProject(Integer.parseInt(input[16]));
            villageAsset.setVeterinaryClinicHospital(Integer.parseInt(input[17]));
            villageAsset.setCommunityPondsForFisheries(Integer.parseInt(input[18]));
            villageAsset.setExtensionFacilitiesForAquaculture(Integer.parseInt(input[19]));
            villageAsset.setVillageConnectedToAllOrigin(Integer.parseInt(input[20]));
            villageAsset.setInternalCcOrBrickRoad(Integer.parseInt(input[21]));
            villageAsset.setPublicTransport(Integer.parseInt(input[22]));
            villageAsset.setRailwayStation(Integer.parseInt(input[23]));
            villageAsset.setElectricityForDomesticUse(Integer.parseInt(input[24]));
            villageAsset.setElectricitySupplyToMSMEUnits(Integer.parseInt(input[25]));
            villageAsset.setSelfHelpGroups(Integer.parseInt(input[26]));
            villageAsset.setPrimarySchool(Integer.parseInt(input[27]));
            villageAsset.setMiddleSchool(Integer.parseInt(input[28]));
            villageAsset.setHighSchool(Integer.parseInt(input[29]));
            villageAsset.setSeniorSecondarySchool(Integer.parseInt(input[30]));
            villageAsset.setGovtDegreeCollege(Integer.parseInt(input[31]));
            villageAsset.setVocationalEducationalCentreOrITIOrRSETIOrDDUGKY(Integer.parseInt(input[32]));
            villageAsset.setMarketsOrMandisOrHaats(Integer.parseInt(input[33]));
            villageAsset.setSubCentrePHCOrCHC(Integer.parseInt(input[34]));
            villageAsset.setJanAushadhiKendra(Integer.parseInt(input[35]));
            villageAsset.setDrainageFacilities(Integer.parseInt(input[36]));
            villageAsset.setCommunityWasteDisposalSystem(Integer.parseInt(input[37]));
            villageAsset.setCommunityBioGasOrRecycleOfWasteForProductionUse(Integer.parseInt(input[38]));
            villageAsset.setAanganwadiCentre(Integer.parseInt(input[39]));
            villageAsset.setpDS(Integer.parseInt(input[40]));
            villageAsset.setPanchayatBhawan(Integer.parseInt(input[41]));
            villageAsset.setCsc(Integer.parseInt(input[42]));
            villageAsset.setPublicInformationBoard(Integer.parseInt(input[43]));
            villageAsset.setHealthFacility(Integer.parseInt(input[44]));
            villageAsset.setCommunityForest(Integer.parseInt(input[45]));
            villageAsset.setCottage(Integer.parseInt(input[46]));
            villageAsset.setCommonPastures(Integer.parseInt(input[47]));
            villageAsset.setAdultEducationCentre(Integer.parseInt(input[48]));
            villageAsset.setPublicLibrary(Integer.parseInt(input[49]));
            villageAsset.setRecreationalCentreOrSportsOrPlayground(Integer.parseInt(input[50]));
            villageAsset.setBanks(Integer.parseInt(input[51]));
            villageAsset.setAtm(Integer.parseInt(input[52]));
            villageAsset.setInternetCafe(Integer.parseInt(input[53]));
            villageAsset.setSpoOrPo(Integer.parseInt(input[54]));
            villageAsset.setcSC(Integer.parseInt(input[55]));
            agentService.storeVillageAsset(villageAsset);
        }
        return messages;
    }

    @Override
    public List<String> GovernmentSchemeDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            String phoneNumber = input[0];
            User user = userService.getUserByPhoneNum(phoneNumber);
            if (user == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }

            String schemeName = input[1];
            schemeName = StringUtils.replace(schemeName, "[", "").replace("]", "");
            List<String> schemeNameList = Arrays.stream(schemeName.split(",")).map(String::trim).collect(Collectors.toList());
            for (String governmentScheme : schemeNameList) {
                GovernmentSchemes governmentSchemes = governmentSchemesService.findGovernmentSchemeByName(governmentScheme);
                if (governmentSchemes == null) {
                    messages.add("schemeName " + input[1] + ", not found for User with mobile no " + input[0].trim() + "(line:" + line + ")");
                    continue;
                }
                UserSchemes userSchemes = new UserSchemes();
                userSchemes.setUserId(user.getId());
                userSchemes.setAddress(user.getAddressModel());
                userSchemes.setGovernmentSchemes(governmentSchemes);
                userService.saveUserScheme(userSchemes);
            }
        }
        return messages;
    }

    @Override
    public List<String> FinancialDetailCsv(MultipartFile file) throws IOException {
        // to check error line
        int line = 1;

        //Read Csv File
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        String[] input = csvReader.readNext();

        //Add those records which are not able to store
        List<String> messages = new ArrayList<>();

        while ((input = csvReader.readNext()) != null) {
            line++;

            if (StringUtils.isEmpty(input[0])) {
                continue;
            }

            FinancialDetails financialDetails = new FinancialDetails();

            String phoneNumber = input[0];
            User agent = userService.getUserByPhoneNum(phoneNumber);
            if (agent == null) {
                messages.add("User not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            int count = 0;
            for (FinancialDetails.AccountType accountType : FinancialDetails.AccountType.values()) {
                if (accountType.toString().equalsIgnoreCase(input[1])) {
                    financialDetails.setAccountType(accountType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("Account type not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            count = 0;
            for (FinancialDetails.UserType userType : FinancialDetails.UserType.values()) {
                if (userType.toString().equalsIgnoreCase(input[2])) {
                    financialDetails.setUserType(userType);
                    count++;
                }
            }
            if (count <= 0) {
                messages.add("User type not found with phone number: " + phoneNumber + "(line:" + line + ")");
                continue;
            }
            financialDetails.setBankName(input[3]);
            financialDetails.setAccountNumber(input[4]);
            financialDetails.setIfscCode(input[5]);
            financialDetails.setAadhaarCardNumber(input[6]);
            financialDetails.setCreatedByUserId(agent.getCreatedByUserId());
            financialDetails.setAccountHolderName(input[7]);
            financialDetails.setPrimaryAccount(Boolean.parseBoolean(input[8]));
            financialDetails.setUser(agent);
            userBankService.addFinancialDetails(financialDetails);
        }
        return messages;
    }
    
    @Override
	public List<String> getAllCategories(String language) {

		List<String> primaryCatNameList = new ArrayList<>();
		if(language.equals("English")) {
			List<PrimaryCategory> allCategoryList = primaryCategoryRepo.findAll();
			for (int i = 0; i < allCategoryList.size(); i++) {
				primaryCatNameList.add(allCategoryList.get(i).getPrimaryCategoryName());
			}
		}else if(language.equals("Hindi")) {
			List<PrimaryCategoryInHindi> allCategoryList = primaryCategoryInHindiRepo.findAll();
			for (int i = 0; i < allCategoryList.size(); i++) {
				primaryCatNameList.add(allCategoryList.get(i).getPrimaryCategoryName());
			}
		}
		
		return primaryCatNameList;
	}
    
    @Override
	public List<String> getSubCategoryListByPrimaryCategoryName(String primaryCategoryName,String state,String district,String language) {
		
    	List<String> resultList = new ArrayList<>();
    	List<String> finalResultList = new ArrayList<>();
		if(language.equals("English")) {
			Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
            AgroClimaticZone climaticZone = mongoTemplate.findOne(query,AgroClimaticZone.class);
            query = new Query(Criteria.where("reasonId").is(climaticZone.getReasonId()));
            List<String> cropNameList=mongoTemplate.findDistinct(query,"cropName",CropSeed.class,String.class);
            
			List<Category> subCatNameList = new ArrayList<>();
			PrimaryCategory allCategoryList = primaryCategoryRepo.findByName(primaryCategoryName);
			subCatNameList = allCategoryList.getCategoryList();
			for (int i = 0; i < subCatNameList.size(); i++) {
				resultList.add(subCatNameList.get(i).getCategoryName());
			}
			/*
			 * for(String str:cropNameList) { if(resultList.contains(str))
			 * finalResultList.add(str); }
			 */
		}else if(language.equals("Hindi")) {
			Query query = new Query(Criteria.where("state").is(state).and("district").is(district));
            AgroClimaticZoneInHindi climaticZoneInHindi = mongoTemplate.findOne(query,AgroClimaticZoneInHindi.class);
            query = new Query(Criteria.where("reasonId").is(climaticZoneInHindi.getReasonId()));
            List<String> cropNameList=mongoTemplate.findDistinct(query,"cropName",CropSeedInHindi.class,String.class);
            
			List<Category> subCatNameList = new ArrayList<>();
			PrimaryCategoryInHindi allCategoryList = primaryCategoryInHindiRepo.findByName(primaryCategoryName);
			subCatNameList = allCategoryList.getCategoryList();
			for (int i = 0; i < subCatNameList.size(); i++) {
				resultList.add(subCatNameList.get(i).getCategoryName());
			}
	/*		for(String str:cropNameList) {
				if(resultList.contains(str))
					finalResultList.add(str);
			}*/
		}
		
		return resultList;
	}
    
    @Override
	public List<String> getVarietyList(String primaryCategoryName, String subCategoryName,String language) {
    	List<String> resultStringList=new ArrayList<>();
    	if(language.equals("English")) {
    		List<Variety> varietyList=new ArrayList<>();
    		PrimaryCategory primaryCategory = primaryCategoryRepo.findByName(primaryCategoryName);
    		List<Category> categoryList= primaryCategory.getCategoryList();
    		
    		for(int i=0;i<categoryList.size();i++)
    		{
    			if(categoryList.get(i).getCategoryName().equalsIgnoreCase(subCategoryName))
    			{
    				Category category=categoryList.get(i);
    				varietyList=category.getVarietyList();
    			}
    		}
    		for(int i=0;i<varietyList.size();i++)
    		{
    			resultStringList.add(varietyList.get(i).getVarietyName());
    		}
    	}else if(language.equals("Hindi")) {
    		List<Variety> varietyList=new ArrayList<>();
    		PrimaryCategoryInHindi  primaryCategory = primaryCategoryInHindiRepo.findByName(primaryCategoryName);
    		List<Category> categoryList= primaryCategory.getCategoryList();
    		
    		for(int i=0;i<categoryList.size();i++)
    		{
    			if(categoryList.get(i).getCategoryName().equalsIgnoreCase(subCategoryName))
    			{
    				Category category=categoryList.get(i);
    				varietyList=category.getVarietyList();
    			}
    		}
    		for(int i=0;i<varietyList.size();i++)
    		{
    			resultStringList.add(varietyList.get(i).getVarietyName());
    		}
    	}
    	return resultStringList;
	}
}