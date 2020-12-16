package com.bharuwa.haritkranti.utils;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.requestModels.CropGroupReq;
import com.bharuwa.haritkranti.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author anuragdhunna
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private RoleService roleService;
    private FarmingService farmingService;
    private LandService landService;
    private LocationServices locationServices;
    private UserService userService;
    private EquipmentService equipmentService;
    private LoginService loginService;
    private CropService cropService;

    public DataLoader(RoleService roleService, FarmingService farmingService, LandService landService, LocationServices locationServices,
                      UserService userService, EquipmentService equipmentService, LoginService loginService, CropService cropService) {
        this.roleService = roleService;
        this.farmingService = farmingService;
        this.landService = landService;
        this.locationServices = locationServices;
        this.userService = userService;
        this.equipmentService = equipmentService;
        this.cropService = cropService;
        this.loginService = loginService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading data...");

        roleService.createDefaultRoles(new Role(Constants.ROLE_ADMIN, Constants.DESC_ROLE_ADMIN));
        roleService.createDefaultRoles(new Role(Constants.ROLE_USER, Constants.DESC_ROLE_USER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_AGENT, Constants.DESC_ROLE_AGENT));
        roleService.createDefaultRoles(new Role(Constants.ROLE_FAMILY_MEMBER, Constants.DESC_ROLE_FAMILY_MEMBER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_STATE_MANAGER, Constants.DESC_ROLE_STATE_MANAGER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_DISTRICT_MANAGER, Constants.DESC_ROLE_DISTRICT_MANAGER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_AGENT_MANAGER, Constants.DESC_ROLE_AGENT_MANAGER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_TEHSIL_MANAGER, Constants.DESC_ROLE_TEHSIL_MANAGER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_MARKET_USER, Constants.DESC_ROLE_MARKET_USER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_NATIONAL_MANAGER, Constants.DESC_ROLE_NATIONAL_MANAGER));
        roleService.createDefaultRoles(new Role(Constants.ROLE_ACCOUNTANT, Constants.DESC_ROLE_ACCOUNTANT));
        roleService.createDefaultRoles(new Role(Constants.ROLE_ADMIN_VIEW, Constants.DESC_ROLE_ADMIN_VIEW));

        // create admin account with all access
        User user = new User();
        user.setFirstName("Annadata");
        user.setLastName("Admin");
        user.setPrimaryPhone("1212121212");
        user.setPassword("god!@#");
        user.setUserCode("123");
        loginService.adminSignUp(user,false);

        // create admin account with view only access
        User adminView = new User();
        adminView.setFirstName("Annadata");
        adminView.setMiddleName("Admin");
        adminView.setLastName("View");
        adminView.setPrimaryPhone("1515151515");
        adminView.setPassword("admin!@#");
        loginService.adminSignUp(adminView,true);


        logger.info("Roles are added");

        // Load Drop Down values

        landService.createDefaultFieldSize(new FieldSize(FieldSize.FieldSizeType.Acre));
        landService.createDefaultFieldSize(new FieldSize(FieldSize.FieldSizeType.Hectare));

        farmingService.createDefaultFarmingType(new FarmingType(FarmingType.Type.Chemical, "Chemical Farming"));
        farmingService.createDefaultFarmingType(new FarmingType(FarmingType.Type.Organic, "Organic Farming"));
        farmingService.createDefaultFarmingType(new FarmingType(FarmingType.Type.INM_MIX, "INM/MIX Farming"));

        // Subsidy Data
//        userService.storeSubsidy(new Subsidy("Fertilizer Subsidy"));
//        userService.storeSubsidy(new Subsidy("Power Subsidy"));
//        userService.storeSubsidy(new Subsidy("Agricultural Equipment Subsidy"));
//        userService.storeSubsidy(new Subsidy("Irrigation Subsidy"));
//        userService.storeSubsidy(new Subsidy("Seed Subsidy"));
//        userService.storeSubsidy(new Subsidy("Export Subsidy"));
//        userService.storeSubsidy(new Subsidy("Credit Subsidy"));
//        userService.storeSubsidy(new Subsidy("Agricultural infrastructure Subsidy"));

//        Moveable Equipment Data
//        equipmentService.addEquipment(new Equipment("Tractor", Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Two-wheel tractor", Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Cultivator",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Rotavator",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Disk harrow",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Plough",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Power tiller",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Harrow",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Land Leveler",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Trowel (khurpi)",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Seed drill",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Planter",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Liquid manure/slurry spreader",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Knock shake Sprayer",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Diameter sorter",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Shape sorter",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Hand Sickle",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Harvester",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Thrasher",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Combine (grain) harvester",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Corn harvester",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Potato harvester",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Potato spinner/digger",Equipment.EquipmentType.Moveable));
//        equipmentService.addEquipment(new Equipment("Rice mill (Rice huller)",Equipment.EquipmentType.Moveable));

//        Immoveable Equipments Data
//        equipmentService.addEquipment(new Equipment("Grain dryer",Equipment.EquipmentType.Immoveable));
//        equipmentService.addEquipment(new Equipment("Irrigation engine",Equipment.EquipmentType.Immoveable));
//        equipmentService.addEquipment(new Equipment("Drip irrigation",Equipment.EquipmentType.Immoveable));
//        equipmentService.addEquipment(new Equipment("Sprinkler system",Equipment.EquipmentType.Immoveable));
//        equipmentService.addEquipment(new Equipment("Hydroponics",Equipment.EquipmentType.Immoveable));

//        logger.info("Adding Crop Group Data for Crops");
//
//        cropService.addCropGroup(new CropGroupReq("Cereal", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Millets", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Cash", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Pulses", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Vegetable", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Fodder", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Spices", CropGroup.Type.CROP));
//        cropService.addCropGroup(new CropGroupReq("Medicinal_And_Aromatic_Plant", CropGroup.Type.CROP));
//
//        logger.info("Adding Crop Group Data for Fruits");
//
//        cropService.addCropGroup(new CropGroupReq("Mango", CropGroup.Type.FRUIT));
//        CropGroup apple = cropService.addCropGroup(new CropGroupReq("Apple", CropGroup.Type.FRUIT));
//        CropGroup banana = cropService.addCropGroup(new CropGroupReq("Banana", CropGroup.Type.FRUIT));
//        CropGroup grape = cropService.addCropGroup(new CropGroupReq("Grape", CropGroup.Type.FRUIT));
//        CropGroup lemon = cropService.addCropGroup(new CropGroupReq("Lemon", CropGroup.Type.FRUIT));

//        logger.info("Adding Fruit Variety Sample Data");
//
//
//        State jammuAndKashmir = locationServices.findStateByName("Jammu And Kashmir");
//        State uttarakhand = locationServices.findStateByName("UTTARAKHAND");
//        State uttarPradesh = locationServices.findStateByName("UTTAR PRADESH");
//
//        Set<DosagePercentage> dosages = new LinkedHashSet<>();
//        DosagePercentage dosagePercentageOne = new DosagePercentage(DosagePercentage.TimeUnit.Year, 1, new BigDecimal(100), new BigDecimal(100), new BigDecimal(100), new BigDecimal(100));
//        DosagePercentage dosagePercentageTwo = new DosagePercentage(DosagePercentage.TimeUnit.Year, 2, new BigDecimal(0), new BigDecimal(100), new BigDecimal(100), new BigDecimal(100));
//        DosagePercentage dosagePercentageThree = new DosagePercentage(DosagePercentage.TimeUnit.Year, 3, new BigDecimal(0), new BigDecimal(200), new BigDecimal(100), new BigDecimal(100));
//
//        dosages.add(dosagePercentageOne);
//        dosages.add(dosagePercentageTwo);
//        dosages.add(dosagePercentageThree);
//
//        Set<FruitVarietyLocation> fruitVarietyLocations = new LinkedHashSet<>();
//        FruitVarietyLocation fruitVarietyLocation = new FruitVarietyLocation(uttarakhand, FruitVarietyLocation.SpacingUnit.Meter, new BigDecimal(2),
//                new BigDecimal(2.5), 800, Fertilizer.Unit.KG, new BigDecimal(30),
//                new BigDecimal(0.02), new BigDecimal(0.008), new BigDecimal(0.036), dosages);
//
//        fruitVarietyLocations.add(fruitVarietyLocation);
//
//        FruitVariety bananaVariety = new FruitVariety("Banana", banana, fruitVarietyLocations);
//
////        cropService.addFruitVariety(bananaVariety);
//
//        if (uttarPradesh != null) {
//
//            DosagePercentage dosagePercentageOneGrape = new DosagePercentage(DosagePercentage.TimeUnit.Year, 1, new BigDecimal(100), new BigDecimal(100), new BigDecimal(100), new BigDecimal(100));
//            DosagePercentage dosagePercentageTwoGrape = new DosagePercentage(DosagePercentage.TimeUnit.Year, 2, new BigDecimal(150), new BigDecimal(200), new BigDecimal(200), new BigDecimal(200));
//            DosagePercentage dosagePercentageThreeGrape = new DosagePercentage(DosagePercentage.TimeUnit.Year, 3, new BigDecimal(200), new BigDecimal(300), new BigDecimal(300), new BigDecimal(300));
//
//            dosages.clear();
//            dosages.add(dosagePercentageOneGrape);
//            dosages.add(dosagePercentageTwoGrape);
//            dosages.add(dosagePercentageThreeGrape);
//
//            fruitVarietyLocations.clear();
//            fruitVarietyLocation = new FruitVarietyLocation(uttarPradesh, FruitVarietyLocation.SpacingUnit.Meter, new BigDecimal(3),
//                    new BigDecimal(1.5), 889, Fertilizer.Unit.KG, new BigDecimal(20),
//                    new BigDecimal(0.07), new BigDecimal(0.035), new BigDecimal(0.07), dosages);
//
//            fruitVarietyLocations.add(fruitVarietyLocation);
//
//            FruitVariety appleVariety = new FruitVariety("Super Chief", apple, fruitVarietyLocations);
//
//            cropService.addFruitVariety(appleVariety);
//        }
    }
}
